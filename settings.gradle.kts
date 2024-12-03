dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":day_01")
include(":day_02")
include(":day_03")

include(":utils")

rootProject.name = "AdventOfCode2024"

