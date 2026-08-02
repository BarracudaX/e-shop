plugins {
    id ("java")
    id ("org.springframework.boot") version ("4.1.0")
    id ("io.spring.dependency-management") version ("1.1.7")
    kotlin("jvm") version "2.4.10"
    kotlin("plugin.lombok") version "2.4.10"
}

group = (")com.example(")
version = (")0.0.1-SNAPSHOT(")
description = (")e-shop(")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
}
kotlin {
    jvmToolchain(26)
}

repositories {
    mavenCentral()
}

dependencies {
    //spring
    implementation ("org.springframework.boot:spring-boot-starter-liquibase")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    implementation ("org.springframework.boot:spring-boot-starter-webmvc")
    implementation ("org.springframework.modulith:spring-modulith-starter-core")
    implementation ("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation ("org.springframework.boot:spring-boot-starter-jackson")

    //spring development only
    developmentOnly ("org.springframework.boot:spring-boot-devtools")
    developmentOnly ("org.springframework.boot:spring-boot-docker-compose")

    //other
    runtimeOnly ("org.postgresql:postgresql")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    implementation("org.aspectj:aspectjtools:1.9.25.1")
    runtimeOnly("org.aspectj:aspectjweaver:1.9.25.1")


    //testing
    testImplementation ("org.springframework.boot:spring-boot-starter-webflux") //only for testing with WebTestClient
    testImplementation ("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation ("org.springframework.boot:spring-boot-starter-liquibase-test")
    testImplementation ("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation ("org.springframework.boot:spring-boot-testcontainers")
    testImplementation ("org.springframework.modulith:spring-modulith-starter-test")
    testImplementation ("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation ("org.testcontainers:testcontainers-postgresql")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly ("org.springframework.boot:spring-boot-starter-data-jdbc")

    //lombok
    annotationProcessor ("org.projectlombok:lombok")
    testCompileOnly ("org.projectlombok:lombok")
    testAnnotationProcessor ("org.projectlombok:lombok")
    compileOnly ("org.projectlombok:lombok")

    //kotlin
    testImplementation(kotlin("test"))

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:2.1.0")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
