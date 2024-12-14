plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:5.0.1")
    testImplementation(kotlin("test"))
}