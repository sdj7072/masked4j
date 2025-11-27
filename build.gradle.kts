import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension

plugins {
    id("org.springframework.boot") version "3.3.0" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false

    id("com.vanniktech.maven.publish") version "0.30.0" apply false
    id("com.diffplug.spotless") version "6.25.0" apply false
}

allprojects {
    group = "io.github.sdj7072"
    val releaseVersion = System.getenv("RELEASE_VERSION")
    version = if (!releaseVersion.isNullOrEmpty()) releaseVersion.removePrefix("v") else "1.1.0"

    repositories {
        mavenCentral()
    }
}



subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "jacoco")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            target("**/*.java")
            googleJavaFormat() // Use default 2 spaces
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    tasks.withType<JacocoReport> {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
        finalizedBy(tasks.named("jacocoTestReport"))
    }



    configure<JavaPluginExtension> {
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
        val ossrhUsername = System.getenv("OSSRH_USERNAME")
        val ossrhPassword = System.getenv("OSSRH_PASSWORD")
        if (ossrhUsername != null) {
            ext["mavenCentralUsername"] = ossrhUsername
        }
        if (ossrhPassword != null) {
            ext["mavenCentralPassword"] = ossrhPassword
        }

        apply(plugin = "com.vanniktech.maven.publish")

        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
            signAllPublications()
            
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

        configure<SigningExtension> {
            val signingKey = System.getenv("GPG_SIGNING_KEY")
            val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
            if (!signingKey.isNullOrEmpty() && !signingPassword.isNullOrEmpty()) {
                useInMemoryPgpKeys(signingKey, signingPassword)
            }
        }
    }
}

tasks.register("installGitHooks") {
    doLast {
        val hooksDir = file(".git/hooks")
        hooksDir.mkdirs()
        val preCommit = file(".git/hooks/pre-commit")
        preCommit.writeText("""
            #!/bin/sh
            echo "Running spotlessApply..."
            ./gradlew spotlessApply

            STAGED_FILES=${'$'}(git diff --cached --name-only)
            UNSTAGED_FILES=${'$'}(git diff --name-only)

            CONFLICT=0
            for file in ${'$'}STAGED_FILES; do
                if echo "${'$'}UNSTAGED_FILES" | grep -q "^${'$'}file${'$'}"; then
                    CONFLICT=1
                    break
                fi
            done

            if [ ${'$'}CONFLICT -eq 1 ]; then
                echo "❌ Spotless formatted some staged files."
                echo "Please review the changes, stage them (git add), and commit again."
                exit 1
            fi
            exit 0
        """.trimIndent())
        preCommit.setExecutable(true)
        println("Git pre-commit hook installed successfully.")
    }
}
