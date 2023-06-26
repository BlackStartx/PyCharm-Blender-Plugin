plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.14.1"
}

group = "org.blackstartx"
version = "2023.1.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(group = "junit", name = "junit", version = "4.13.2")
    implementation(group = "org.json", name = "json", version = "20230227")
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    plugins.set(listOf("python"))
    version.set("PY-2023.1")
}
tasks {
    patchPluginXml {}
}