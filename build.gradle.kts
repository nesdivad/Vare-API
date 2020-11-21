val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposedVersion = "0.26.2"
val h2Version = "1.4.200"
val hikariCpVersion = "3.4.5"
val assertjVersion = "3.16.1"
val restAssuredVersion = "4.3.1"
val junitVersion = "5.6.2"

plugins {
    application
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    id("org.flywaydb.flyway") version "5.2.4"
}

group = "h577870"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
}
/*
Trenger jeg alle disse? Det finner vi ut av en annen dag...
 */
dependencies {
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("com.zaxxer:HikariCP:$hikariCpVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("org.flywaydb:flyway-core:5.2.4")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.springframework.security:spring-security-crypto:5.4.1")

    compile("org.postgresql:postgresql:42.2.2")
    compile("org.jetbrains.exposed", "exposed-core", "0.24.1")
    compile("org.jetbrains.exposed", "exposed-dao", "0.24.1")
    compile("org.jetbrains.exposed", "exposed-jdbc", "0.24.1")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")

}
flyway {
    url = System.getenv("DB_URL")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_PASSWORD")
    baselineOnMigrate=true
    locations = arrayOf("filesystem:resources/db/migrations")
}
