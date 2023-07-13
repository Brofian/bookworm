plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "de.fabianholzwarth"
version = "1.0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
}

tasks {
    getByName<Test>("test") {
        useJUnitPlatform()
    }
    runServer {
        minecraftVersion("1.20.1")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}