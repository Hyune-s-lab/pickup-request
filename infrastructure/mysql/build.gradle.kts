apply {
    plugin("org.jetbrains.kotlin.plugin.jpa")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain:pickup"))

    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    val testcontainersVersion: String by project
    implementation("org.testcontainers:testcontainers:${testcontainersVersion}")
    implementation("org.testcontainers:junit-jupiter:${testcontainersVersion}")
    implementation("org.testcontainers:mysql:${testcontainersVersion}")

    val p6spyVersion: String by project
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:${p6spyVersion}")

    val querydslVersion: String by project
    implementation("com.querydsl:querydsl-jpa:${querydslVersion}:jakarta")
    kapt("com.querydsl:querydsl-apt:${querydslVersion}:jakarta")
}
