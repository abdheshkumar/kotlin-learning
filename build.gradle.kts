import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "me.abdkumar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.arrow-kt:arrow-core:1.1.3")
    implementation("com.google.code.gson:gson:2.8.2")
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.23.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}

application {
    mainClass.set("MainKt")
}
