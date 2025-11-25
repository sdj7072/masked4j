plugins {
    `java-library`
    `maven-publish`
}

group = "io.github.masked4j"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Jackson for JSON integration
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    
    // Spring Boot Support (Optional)
    compileOnly("org.springframework.boot:spring-boot-autoconfigure:3.3.0")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:2.15.2")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.0")
    testImplementation("org.springframework.boot:spring-boot-starter-web:3.3.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Masked4J")
                description.set("A lightweight, flexible Java library for masking sensitive data.")
                url.set("https://github.com/sdj7072/masked4j")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("sdj7072")
                        name.set("sdj7072")
                        email.set("sdj7072@gmail.com") // Assuming email or leaving generic if unknown, but ID is known.
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/sdj7072/masked4j.git")
                    developerConnection.set("scm:git:ssh://github.com/sdj7072/masked4j.git")
                    url.set("https://github.com/sdj7072/masked4j")
                }
            }
        }
    }
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}


