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
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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

kotlin {
    jvm {
        withJava()
    }
    js {
        browser { }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetime")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJson")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
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
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.vertx:vertx-core:$vertxVersion")
                implementation("com.google.guava:guava:31.1-jre")
                implementation("junit:junit:4.13.2")
                implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
                implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava:$jacksonVersion")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
                implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
            }
        }
    }
}

dependencies {
    val kapt by configurations
    kapt("io.vertx:vertx-codegen:$vertxVersion:processor")
    kapt(findProject("codegen") ?: project(":protocol:codegen"))
}

configure<org.jetbrains.kotlin.noarg.gradle.NoArgExtension> {
    annotation("kotlinx.serialization.Serializable")
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
