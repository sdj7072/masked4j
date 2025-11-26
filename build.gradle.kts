import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension

plugins {
    id("org.springframework.boot") version "3.3.0" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false

}

allprojects {
    group = "io.github.sdj7072"
    val releaseVersion = System.getenv("RELEASE_VERSION")
    version = if (!releaseVersion.isNullOrEmpty()) releaseVersion.removePrefix("v") else "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}



subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.spring.dependency-management")

    if (project.name == "masked4j-core" || project.name == "masked4j-spring-boot-starter") {
        apply(plugin = "maven-publish")
        apply(plugin = "signing")
    }

    configure<JavaPluginExtension> {
        withSourcesJar()
        withJavadocJar()
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }

    }

    tasks.withType<JavaCompile> {
        options.release.set(17)
        options.encoding = "UTF-8"
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
    
    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.0")
        }
    }
    
    dependencies {
        "testImplementation"(platform("org.junit:junit-bom:5.10.0"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
        "testImplementation"("org.assertj:assertj-core:3.24.2")
    }
    
    if (project.name == "masked4j-core" || project.name == "masked4j-spring-boot-starter") {
        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("maven") {
                    from(components["java"])
                    version = project.version.toString()
                    pom {
                        name.set(project.name)
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
                                email.set("sdj7072@gmail.com")
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
            repositories {
                maven {
                    name = "CentralPortal"
                    url = uri("https://central.sonatype.com/api/v1/publisher")
                    credentials {
                        username = System.getenv("OSSRH_USERNAME")
                        password = System.getenv("OSSRH_PASSWORD")
                    }
                }
            }
        }

        configure<SigningExtension> {
            val signingKey = System.getenv("GPG_SIGNING_KEY")
            val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
            if (signingKey != null && signingPassword != null) {
                useInMemoryPgpKeys(signingKey, signingPassword)
                sign(extensions.getByType<PublishingExtension>().publications["maven"])
            }
        }
    }
}
