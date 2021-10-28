plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("maven-publish")
}

val vertxVersion: String by project
val kotlinVersion: String by project
val protocolVersion: String by project

group = "spp.protocol"
version = protocolVersion

repositories {
    mavenCentral()
}

configure<PublishingExtension> {
    repositories {
        maven("file://${System.getenv("HOME")}/.m2/repository")
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
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
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
                implementation("io.vertx:vertx-core:$vertxVersion")
                implementation("io.vertx:vertx-codegen:$vertxVersion")
                implementation(files(".ext/vertx-service-discovery-4.0.3-SNAPSHOT.jar"))
                implementation(files(".ext/vertx-service-proxy-4.0.2.jar"))
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.13.0")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava:2.13.0")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
                implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.0")
                implementation("org.jooq:jooq:3.15.4")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("io.vertx:vertx-core:$vertxVersion")
                implementation("com.google.guava:guava:31.0.1-jre")
                implementation("junit:junit:4.13.2")
                implementation("com.fasterxml.jackson.core:jackson-core:2.13.0")
                implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.13.0")
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava:2.13.0")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
                implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
            }
        }
    }
}

dependencies {
    "kapt"("io.vertx:vertx-codegen:$vertxVersion:processor")
}

tasks.register<Copy>("setupJsonMappers") {
    from(file("$projectDir/src/jvmMain/resources/META-INF/vertx/json-mappers.properties"))
    into(file("$buildDir/tmp/kapt3/src/main/resources/META-INF/vertx"))
}
tasks.getByName("compileKotlinJvm").dependsOn("setupJsonMappers")

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
