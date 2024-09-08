import java.util.Calendar

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("app.cash.licensee:licensee-gradle-plugin:1.7.0")
    }
}

apply(plugin = "app.cash.licensee")

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("maven-publish")
    id("com.diffplug.spotless")
    id("io.gitlab.arturbosch.detekt")
}

val vertxVersion: String by project
val kotlinVersion: String by project
val projectVersion: String by project
val jacksonVersion: String by project
val jupiterVersion: String by project

group = "plus.sourceplus"
version = project.properties["protocolVersion"] as String? ?: projectVersion

repositories {
    mavenCentral()
}

configure<app.cash.licensee.LicenseeExtension> {
    allow("Apache-2.0")
    allow("MIT")
}

val sourcesJar = tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(project.the<SourceSetContainer>()["main"].allSource)
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

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group.toString()
                artifactId = "protocol"
                version = project.version.toString()

                from(components["kotlin"])

                // Ship the sources jar
                artifact(sourcesJar)
            }
        }
    }
}

dependencies {
    implementation("io.vertx:vertx-core:$vertxVersion")
    implementation("io.vertx:vertx-codegen:$vertxVersion")
    implementation("io.vertx:vertx-tcp-eventbus-bridge:$vertxVersion")
    implementation("io.vertx:vertx-service-proxy:$vertxVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    testImplementation("io.vertx:vertx-junit5:$vertxVersion")
    testImplementation("io.vertx:vertx-core:$vertxVersion")

    kapt("io.vertx:vertx-codegen:$vertxVersion:processor")
    compileOnly("io.vertx:vertx-codegen:$vertxVersion")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
}

tasks.withType<JavaCompile> {
    options.release.set(8)
    sourceCompatibility = "1.8"
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=all")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config.setFrom(arrayOf(File(project.rootDir, "detekt.yml")))
}

spotless {
    kotlin {
        target("**/*.kt")

        val startYear = 2022
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val copyrightYears = if (startYear == currentYear) {
            "$startYear"
        } else {
            "$startYear-$currentYear"
        }

        val protocolProject = findProject(":protocol") ?: rootProject
        val licenseHeader = Regex("( . Copyright [\\S\\s]+)")
            .find(File(protocolProject.projectDir, "LICENSE").readText())!!
            .value.lines().joinToString("\n") {
                if (it.trim().isEmpty()) {
                    " *"
                } else {
                    " * " + it.trim()
                }
            }
        val formattedLicenseHeader = buildString {
            append("/*\n")
            append(
                licenseHeader.replace(
                    "Copyright [yyyy] [name of copyright owner]",
                    "Source++, the continuous feedback platform for developers.\n" +
                            " * Copyright (C) $copyrightYears CodeBrig, Inc."
                ).replace(
                    "http://www.apache.org/licenses/LICENSE-2.0",
                    "    http://www.apache.org/licenses/LICENSE-2.0"
                )
            )
            append("/")
        }
        licenseHeader(formattedLicenseHeader)
    }
}
