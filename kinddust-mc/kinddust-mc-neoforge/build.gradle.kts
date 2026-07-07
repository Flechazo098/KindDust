import org.gradle.api.attributes.Attribute
import org.slf4j.event.Level

plugins {
    id("multiloader-loader")
    id("net.neoforged.moddev")
}

val modId = providers.gradleProperty("mod_id").get()
val neoforge_version: String by project
val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

dependencies {
    implementation(project(":kinddust-vm"))
    "jarJar"(project(path = ":kinddust-vm", configuration = "embeddedShadowElements"))
}

neoForge {
    version = neoforge_version

    val accessTransformer = project(":kinddust-mc:kinddust-mc-common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (accessTransformer.exists()) {
        accessTransformers.from(accessTransformer.absolutePath)
    }

    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            ideName = "NeoForge ${name.replaceFirstChar { it.uppercase() }} (${project.path})"
            logLevel = Level.DEBUG
        }
        register("client") {
            client()
            gameDirectory = layout.projectDirectory.dir("runs/client")
        }
        register("data") {
            clientData()
            gameDirectory = layout.projectDirectory.dir("runs/data")
            programArguments.addAll(
                "--mod",
                modId,
                "--all",
                "--output",
                file("src/generated/resources/").absolutePath,
                "--existing",
                file("src/main/resources/").absolutePath,
            )
        }
        register("server") {
            server()
            gameDirectory = layout.projectDirectory.dir("runs/server")
            programArgument("--nogui")
        }
    }

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "neoforge")
        }
    }
}

sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName, getTaskName(null, "jarJar")).forEach { variant ->
        configurations.matching { it.name == variant }.configureEach {
            attributes {
                attribute(loaderAttribute, "neoforge")
            }
        }
    }
}
