plugins {
    kotlin("jvm")
    application
}

description = "Seminar 14: Dependency Injection Example"

application {
    mainClass.set("backend.tclass.reflection.ApplicationKt")
}

dependencies {
    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}
