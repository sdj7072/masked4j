plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":masked4j-spring-boot-starter"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
