plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "masked4j"

include("masked4j-core")
include("masked4j-spring-boot-starter")
include("examples:spring-boot-sample")
include("masked4j-benchmark")
