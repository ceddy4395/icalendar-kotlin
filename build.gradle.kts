import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "dev.willekens"
version = "0.1.3"

val detektVersion = "1.23.3"

plugins {
    kotlin("jvm") version "1.8.20"
    id("maven-publish")
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
    `java-library`
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
}

repositories {
    mavenLocal()
    mavenCentral()
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

dependencies {
    detekt("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
}

dependencies {

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ceddy4395/icalendar-kotlin")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        publications {
            register<MavenPublication>("gpr") {
                from(components["java"])
            }
        }
    }
}


