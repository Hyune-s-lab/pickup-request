dependencies {
    implementation(project(":common"))
    implementation(project(":domain:pickup"))
    implementation(project(":infrastructure:mysql"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    val testcontainersVersion: String by project
    implementation("org.testcontainers:testcontainers:${testcontainersVersion}")
    implementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    implementation("org.testcontainers:mysql:${testcontainersVersion}")
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
