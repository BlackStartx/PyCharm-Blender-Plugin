plugins {
    id("org.jetbrains.intellij.platform") version "2.0.1"
}

group = "org.blackstartx"
val tVer: String = "2024.2"
val code = "isCommunity = true"
val path = "src\\main\\java\\plugin_settings\\PluginSettings.java"
val type = if (File(path).readText().contains(code)) "Community" else "Professional"
version = tVer

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation(group = "org.json", name = "json", version = "20240303")
    intellijPlatform {
        pycharmProfessional("2024.2")
        bundledPlugin("PythonCore")
        bundledPlugin("Pythonid")
        instrumentationTools()
    }
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellijPlatform {
    pluginConfiguration {
        id = "org.blackstartx.blend-charm"
        name = "Blend-Charm"
        version = tVer
        changeNotes = file("changelog.md").readText()

        productDescriptor {
        }
        ideaVersion {
        }
        vendor {
        }
    }
}

tasks {
    buildPlugin {
        archiveFileName.set("${rootProject.name}.${version}.${type}.Edition.zip")
    }

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {}
}