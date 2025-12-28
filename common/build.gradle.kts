plugins {
    kotlin("jvm") version "2.2.21"
    `maven-publish`
}

group = "com.tony.mywallet"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["kotlin"])
        }
    }
}
