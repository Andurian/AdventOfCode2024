plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(project(":utils"))
    testImplementation(kotlin("test"))
}

application {
    mainClass = "meow.andurian.aoc2024.day_05.Day05Kt"
}
