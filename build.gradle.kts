import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.20.0"
}

group = "com.p10"
version = "0.0.1-SNAPSHOT"
description = "Project P10 - Stats backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webclient")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("tools.jackson.module:jackson-module-kotlin")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webclient-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sourceSets {
    main {
        kotlin {
            srcDir("$rootDir/build/generated/src/main/kotlin")
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

val openApis =
    mapOf(
        "p10-stats" to
            mapOf(
                "path" to "p10-stats.yaml",
                "package" to "com.p10.stats.generated",
            ),
        "pointsCalculator" to
            mapOf(
                "path" to "points-calculator.yaml",
                "package" to "com.p10.points.generated",
            ),
    )

tasks {
    openApis.forEach { (openApiName, properties) ->
        register<GenerateTask>("generate-$openApiName") {
            generatorName.set("kotlin-spring")
            inputSpec.set("$rootDir/src/main/resources/api/${properties["path"]}")
            apiPackage.set("${properties["package"]}.api")
            modelPackage.set(properties["package"])
            outputDir.set("$rootDir/build/generated")
            configOptions.putAll(
                mapOf(
                    "gradleBuildFile" to "false",
                    "useSpringBoot3" to "true",
                    "documentationProvider" to "none",
                    "interfaceOnly" to "true",
                ),
            )
            generateApiTests.set(false)
        }
    }

    register("openApis") {
        dependsOn(openApis.keys.map { "generate-$it" })
    }

    compileKotlin {
        dependsOn("openApis")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
