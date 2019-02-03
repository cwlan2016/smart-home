import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.20"
}

group = "com.thgeorgiou"
version = "0.1.0"

repositories {
    mavenCentral()
    jcenter()
    maven {
        setUrl("https://repo.eclipse.org/content/repositories/paho-releases/")
    }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))

    // Spark for HTTP API
    implementation("com.sparkjava:spark-core:2.7.2")

    // Logging
    implementation("org.slf4j:slf4j-simple:1.7.21")

    // MQTT Client
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0")

    // JSON
    implementation("com.beust:klaxon:5.0.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "com.thgeorgiou.smart_home_server.MainKt"
}