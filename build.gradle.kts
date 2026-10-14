plugins {
    java
    kotlin("jvm") version "2.4.20"
}

group = "com.survivalcoding"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
}

tasks.test {
    useJUnitPlatform()
}