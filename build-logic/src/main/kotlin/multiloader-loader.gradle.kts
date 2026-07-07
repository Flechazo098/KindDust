import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar

plugins {
    id("multiloader-common")
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
val commonProjectPath = ":kinddust-mc:kinddust-mc-common"

val commonJava by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}
val commonResources by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
    compileOnly(project(commonProjectPath)) {
        attributes {
            attribute(loaderAttribute, "common")
        }
    }
    commonJava(project(path = commonProjectPath, configuration = "commonJava"))
    commonResources(project(path = commonProjectPath, configuration = "commonResources"))
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(commonJava)
    source(commonJava)
}

tasks.processResources {
    dependsOn(commonResources)
    from(commonResources)
}

tasks.named<Javadoc>("javadoc") {
    dependsOn(commonJava)
    source(commonJava)
}

tasks.named<Jar>("sourcesJar") {
    dependsOn(commonJava)
    from(commonJava)
    dependsOn(commonResources)
    from(commonResources)
}
