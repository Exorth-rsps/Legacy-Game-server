pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
}
rootProject.name = "Alter"

plugins {
    id("de.fayard.refreshVersions") version("0.51.0")
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":util")
include(":net")
include(":game-plugins")
include(":game-api")
include(":game-server")
include(":Logger")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            files("../gradle/libs.versions.toml")
        }
    }
}
include("Logger")
