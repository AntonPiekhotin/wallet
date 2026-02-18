plugins {
	kotlin("jvm") version "2.2.21"
	`maven-publish`
}

group = "com.tony.mywallet"
version = "1.0"

repositories {
	mavenCentral()
	mavenLocal()
}

kotlin {
	jvmToolchain(21)
}

dependencies {
	implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
	implementation("com.tony.mywallet:common:1.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["kotlin"])
		}
	}
}

