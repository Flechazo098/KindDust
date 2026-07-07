import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar

plugins {
    `java-library`
    `maven-publish`
}

val javaVersion = providers.gradleProperty("java_version").get().toInt()
val minecraftVersion = providers.gradleProperty("minecraft_version").get()
val minecraftVersionRange = providers.gradleProperty("minecraft_version_range").get()
val fabricVersion = providers.gradleProperty("fabric_version").get()
val fabricLoaderVersion = providers.gradleProperty("fabric_loader_version").get()
val modName = providers.gradleProperty("mod_name").get()
val modAuthor = providers.gradleProperty("mod_author").get()
val modId = providers.gradleProperty("mod_id").get()
val modLicense = providers.gradleProperty("mod_license").get()
val neoforgeVersion = providers.gradleProperty("neoforge_version").get()
val neoforgeLoaderVersionRange = providers.gradleProperty("neoforge_loader_version_range").get()

base {
    archivesName = "$modId-${project.name}-$minecraftVersion"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_$modName" }
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to modName,
                "Specification-Vendor" to modAuthor,
                "Specification-Version" to archiveVersion.get(),
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to modAuthor,
                "Built-On-Minecraft" to minecraftVersion,
            )
        )
    }
}

tasks.processResources {
    val expandProps = mapOf(
        "version" to project.version,
        "group" to project.group,
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "fabric_version" to fabricVersion,
        "fabric_loader_version" to fabricLoaderVersion,
        "mod_name" to modName,
        "mod_author" to modAuthor,
        "mod_id" to modId,
        "license" to modLicense,
        "description" to project.description,
        "neoforge_version" to neoforgeVersion,
        "neoforge_loader_version_range" to neoforgeLoaderVersionRange,
        "credits" to "",
        "java_version" to javaVersion,
    )
    val jsonExpandProps = expandProps.mapValues { (_, value) ->
        if (value is String) value.replace("\n", "\\n") else value
    }

    inputs.properties(expandProps)

    filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
        expand(expandProps)
    }
    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "*.mixins.json")) {
        expand(jsonExpandProps)
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri(System.getenv("local_maven_url") ?: rootProject.layout.buildDirectory.dir("repo").get().asFile)
        }
    }
}
