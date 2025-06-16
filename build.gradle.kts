plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.5.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.rest-assured:rest-assured:5.5.0")

    implementation("com.h2database:h2:2.3.230")
    implementation("org.hibernate.orm:hibernate-core:6.6.0.Final")

    // Logger
    implementation("org.slf4j:slf4j-simple:2.0.16")
}

application {
    mainClass.set("org.example.Main")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.example.Main"
    }
}

tasks.withType<JavaExec> {
    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}
