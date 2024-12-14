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
include(":day_04")
include(":day_05")
include(":day_06")
include(":day_07")
include(":day_08")
include(":day_09")
include(":day_10")
include(":day_11")
include(":day_12")
include(":day_13")
include(":day_14")
include(":day_15")
include(":day_16")
include(":day_17")
include(":day_18")
include(":day_19")
include(":day_20")
include(":day_21")
include(":day_22")
include(":day_23")
include(":day_24")
include(":day_25")

include(":utils")

rootProject.name = "AdventOfCode2024"
