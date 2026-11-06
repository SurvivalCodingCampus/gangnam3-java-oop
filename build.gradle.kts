// 어떤 언어, 기능으로 빌드 할지
plugins {
    java
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.serialization") version "2.4.21"
}

group = "com.survivalcoding"
version = "1.0-SNAPSHOT"

// 라이브러리를 어디서 받아 올지
repositories {
    mavenCentral()
}

// 어떤 라이브 러리를 쓸지
dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //직렬화
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    // 코루틴
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    //Http 통신
    implementation("io.ktor:ktor-client-core:3.6.0")
    //코르틴 최적화
    implementation("io.ktor:ktor-client-cio:3.6.0")

    testImplementation("io.ktor:ktor-client-mock:3.6.0")
}

// 테스트를 어떻게 돌릴지
tasks.test {
    useJUnitPlatform()
}