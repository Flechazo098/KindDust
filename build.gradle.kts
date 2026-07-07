plugins {
    id("net.fabricmc.fabric-loom") version "1.16.3" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
    id("com.gradleup.shadow") version "9.3.0" apply false
}

allprojects {
    group = providers.gradleProperty("group").get()
    version = providers.gradleProperty("version").get()
    description = providers.gradleProperty("mod_description").get()

    repositories {
        mavenCentral()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net")
    }
}

subprojects {
    pluginManager.withPlugin("java") {
        extensions.configure<JavaPluginExtension>("java") {
            toolchain.languageVersion = JavaLanguageVersion.of(
                providers.gradleProperty("java_version").get().toInt()
            )
            withSourcesJar()
            withJavadocJar()
        }

        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
        }

        tasks.withType<Javadoc>().configureEach {
            (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
        }
    }
}
