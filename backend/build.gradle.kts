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

    // Dependência para compatibilidade do jackson
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")

    // Dependências para logging
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.0")
    testImplementation("ch.qos.logback:logback-classic:1.5.5")

    // Dependências para validação de dados
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")

    // Dependências para Ebean (manipulação de banco de dados)
    implementation("io.ebean:ebean:15.5.2")
    runtimeOnly("io.ebean:ebean-agent:15.5.2")
    implementation("io.ebean:ebean-api:15.5.2")
    implementation("io.ebean:ebean-core:15.5.2")
    implementation("io.ebean:ebean-querybean:15.5.2")
    implementation("io.ebean:ebean-ddl-generator:15.5.2")
    implementation("io.ebean:ebean-platform-postgres:15.5.2")

    // Criptografia
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("com.nimbusds:nimbus-jose-jwt:9.41.2")

    //JWT
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt:0.12.6")

    // Dependências para reduzir verbosidade com Lombok
    compileOnly("org.projectlombok:lombok:1.18.34")
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // Dependências para testes
    testImplementation("com.h2database:h2:2.3.232")
    testImplementation("io.ebean:ebean-test:15.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
    testImplementation("org.mockito:mockito-junit-jupiter:5.13.0")
}

tasks.test {
    useJUnitPlatform()
}
