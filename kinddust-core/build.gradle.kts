plugins {
    `java-library`
    id("com.gradleup.shadow")
}

dependencies {
    api(project(":kinddust-sdk"))
}

tasks.shadowJar {
    val sdkJar = project(":kinddust-sdk").tasks.named<Jar>("jar")
    dependsOn(sdkJar)
    from(zipTree(sdkJar.flatMap { it.archiveFile }))
    manifest {
        attributes("Automatic-Module-Name" to "com.flechazo.kinddust.core")
    }
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

configurations.named("shadowRuntimeElements") {
    outgoing.artifacts.clear()
    outgoing.artifact(tasks.shadowJar)
}

val embeddedShadowElements by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.SHADOWED))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, providers.gradleProperty("java_version").get().toInt())
    }
    outgoing.artifact(tasks.shadowJar)
}
