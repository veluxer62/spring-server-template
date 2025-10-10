plugins {
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"

    val kotlinVersion = "2.2.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version "2.2.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "spring-project-template"
val basePackage: String by project

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val kotestVersion = "6.0.3"
val mockkVersion = "1.14.5"
val testContainersPostgresVersion = "1.21.1"
val hibernateUtilsVersion = "3.11.0"
val kotlinLoggingVersion = "3.0.5"
val springDocVersion = "2.8.13"

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Hibernate
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:$hibernateUtilsVersion")
    kapt("org.hibernate.orm:hibernate-jpamodelgen")

    // flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Logging
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")

    // Springdoc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    runtimeOnly("org.postgresql:postgresql")

    // Spring
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
        exclude(group = "com.vaadin.external.google", module = "android-json")
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions-spring:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions-now:$kotestVersion")

    // Mockk
    testImplementation("io.mockk:mockk:$mockkVersion")

    // TestContainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql:$testContainersPostgresVersion")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("kotest.framework.config.fqn", "$basePackage.KotestConfiguration")
}

ktlint {
    version.set("1.7.1")

    filter {
        exclude("**/generated/**")
    }
}
