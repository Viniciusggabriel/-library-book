plugins {
    id("idea")
    id("java")
    id("io.ebean") version "15.5.2"
}

group = "com.library"
version = "1.0-SNAPSHOT"

ebean {
    debugLevel = 1
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Variáveis de ambiente
    implementation("io.github.cdimascio:dotenv-java:3.0.0")

    // Driver PostgreSQL
    implementation("org.postgresql:postgresql:42.7.3")

    // Dependências para servidor HTTP Jetty
    implementation("org.eclipse.jetty:jetty-server:12.0.13")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:12.0.13")

    // Dependências para logging
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.0")
    testImplementation("ch.qos.logback:logback-classic:1.5.5")

    // Dependências para a manipulação do banco de dados Ebean
    implementation("io.ebean:ebean:15.5.2")
    runtimeOnly("io.ebean:ebean-agent:15.5.2")
    implementation("io.ebean:ebean-api:15.5.2")
    implementation("io.ebean:ebean-core:15.5.2")
    implementation("io.ebean:ebean-querybean:15.5.2")
    implementation("io.ebean:ebean-ddl-generator:15.5.2")

    // Dependências para reduzir verbosidade
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // Dependências para testes
    testImplementation("io.ebean:ebean-test:15.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
    testImplementation("org.mockito:mockito-junit-jupiter:5.13.0")
}

tasks.test {
    useJUnitPlatform()
}
