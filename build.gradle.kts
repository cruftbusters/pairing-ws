import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.21"
  application
}

group = "com.painkiller"
val major = 0
val minor = 0

repositories {
  jcenter()
  mavenCentral()
  maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
  maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

dependencies {
  implementation("io.ktor:ktor-html-builder:1.4.0")
  implementation("io.ktor:ktor-jackson:1.4.0")
  implementation("io.ktor:ktor-server-netty:1.4.0")
  implementation("org.slf4j:slf4j-simple:+")
  testImplementation("io.ktor:ktor-client-apache:1.4.0")
  testImplementation("io.ktor:ktor-client-core:1.4.0")
  testImplementation("io.ktor:ktor-client-jackson:1.4.0")
  testImplementation("org.hamcrest:hamcrest-junit:+")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:+")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testImplementation(kotlin("test-junit5"))
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "11"
}

tasks.named("processResources") {
  doFirst {
    ProcessBuilder("git rev-list --count HEAD".split(" "))
      .start()
      .run {
        val patch = inputStream.bufferedReader().readText().trim()
        file("src/main/resources/version.properties").writeText("version=$major.$minor.$patch")
      }
  }
}
