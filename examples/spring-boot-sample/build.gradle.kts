plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":masked4j-spring-boot-starter"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
