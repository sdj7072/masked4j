dependencies {
    api(project(":masked4j-core"))
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    // annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
}
