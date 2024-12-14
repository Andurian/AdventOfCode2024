plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(project(":utils"))
    implementation("com.github.ajalt.clikt:clikt:5.0.1")

    testImplementation(kotlin("test"))
}

application {
    mainClass = "meow.andurian.aoc2024.day_16.Day16Kt"
}
