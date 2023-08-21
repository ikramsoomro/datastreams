plugins {
	java
	id("org.springframework.boot") version "2.7.14"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.lab"
version = "0.0.1-SNAPSHOT"



java {
	sourceCompatibility = JavaVersion.VERSION_17
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
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.apache.kafka:kafka-streams")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	testImplementation("junit:junit:4.13.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// Add Lombok dependency
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// SLF4J and Logback dependencies
	implementation("org.slf4j:slf4j-api:1.7.30")
	implementation("ch.qos.logback:logback-classic:1.2.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
