pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven("https://maven.fabricmc.net") {
                    name = "Fabric"
                }
            }
            filter {
                includeGroupAndSubgroups("net.fabricmc")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "KindDust"

includeBuild("build-logic")

include("kinddust-core")
include("kinddust-vm")
include("kinddust-cli")
include("kinddust-sdk")

include("kinddust-mc:kinddust-mc-common")
include("kinddust-mc:kinddust-mc-fabric")
include("kinddust-mc:kinddust-mc-neoforge")
