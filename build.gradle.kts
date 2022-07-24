buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("app.cash.licensee:licensee-gradle-plugin:1.5.0")
    }
}

apply(plugin = "app.cash.licensee")

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("maven-publish")
    id("com.diffplug.spotless")
}

val vertxVersion: String by project
val kotlinVersion: String by project
val projectVersion: String by project
val jacksonVersion: String by project
val slf4jVersion: String by project
val kotlinxDatetime: String by project
val kotlinxSerializationJson: String by project

group = "plus.sourceplus"
version = project.properties["protocolVersion"] as String? ?: projectVersion

repositories {
    mavenCentral()
}

configure<app.cash.licensee.LicenseeExtension> {
    allowDependency("org.jetbrains.kotlinx", "kotlinx-datetime-js", kotlinxDatetime) {
        because("Apache-2.0")
    }
    allowDependency("org.jetbrains.kotlinx", "kotlinx-serialization-core-js", kotlinxSerializationJson) {
        because("Apache-2.0")
    }
    allowDependency("org.jetbrains.kotlinx", "kotlinx-serialization-json-js", kotlinxSerializationJson) {
        because("Apache-2.0")
    }
    allow("Apache-2.0")
    allow("MIT")
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sourceplusplus/protocol")
            credentials {
                username = System.getenv("GH_PUBLISH_USERNAME")?.toString()
                password = System.getenv("GH_PUBLISH_TOKEN")?.toString()
            }
        }
    }
}

dependencies {
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("io.vertx:vertx-core:$vertxVersion")
    implementation("io.vertx:vertx-codegen:$vertxVersion")
    implementation("io.vertx:vertx-tcp-eventbus-bridge:$vertxVersion")
    implementation("io.vertx:vertx-service-proxy:$vertxVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    testImplementation("junit:junit:4.13.2")

    annotationProcessor("io.vertx:vertx-codegen:$vertxVersion:processor")
    kapt(findProject("codegen") ?: project(":protocol:codegen"))
}

tasks.withType<JavaCompile> {
    options.release.set(8)
    sourceCompatibility = "1.8"
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

spotless {
    kotlin {
        target("**/*.kt")
        licenseHeaderFile(file("LICENSE-HEADER.txt"))
    }
}

kapt {
    annotationProcessor("spp.protocol.codegen.ProtocolCodeGenProcessor")
}
