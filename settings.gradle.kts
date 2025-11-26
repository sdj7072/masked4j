plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "masked4j"

include("masked4j-core")
include("masked4j-spring-boot-starter")
include("examples:spring-boot-sample")
