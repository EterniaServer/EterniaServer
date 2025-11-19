object Constants {
    const val PROJECT_VERSION = "4.3.1"

    const val JAVA_VERSION = "21"
    const val JACOCO_VERSION = "0.8.12"

    const val PAPER_VERSION = "1.21.8-R0.1-SNAPSHOT"
    const val ETERNIALIB_VERSION = "4.5.9"
    const val VAULT_API_VERSION = "2.15"
    const val JUPITER_VERSION = "5.11.4"
    const val MOCKITO_VERSION = "5.16.1"
    const val PAPI_VERSION = "2.11.6"
    const val DISCORDSRV_VERSION = "1.29.0"
}

plugins {
    id("java")
    id("maven-publish")
    id("jacoco")
    id("org.sonarqube") version("6.3.1.5724")
    id("io.freefair.lombok") version("9.1.0")
    id("com.gradleup.shadow") version("9.2.2")
}

jacoco {
    toolVersion = Constants.JACOCO_VERSION
}

sonar  {
    properties {
        property("sonar.projectKey", "EterniaServer_EterniaServer")
        property("sonar.projectVersion", "${project.version}")
        property("sonar.organization", "eterniaserver")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.test.inclusions", "**/*Test.java,**/Test*.java")
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.exclusions", "**/test/**,**/*Test.java,**/Test*.java")
        property("sonar.java.source", Constants.JAVA_VERSION)
    }
}

group = "br.com.eterniaserver"
version = Constants.PROJECT_VERSION

repositories {
    mavenCentral()
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "papi-repo"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "scarsz"
        url = uri("https://nexus.scarsz.me/content/groups/public/")
    }
    maven {
        name = "codemc"
        url = uri("https://repo.codemc.io/repository/creatorfromhell/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    mavenLocal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(Constants.JAVA_VERSION))
    }
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", Constants.PAPER_VERSION)
    compileOnly("com.github.EterniaServer", "EterniaLib", Constants.ETERNIALIB_VERSION)
    compileOnly("net.milkbowl.vault", "VaultUnlockedAPI", Constants.VAULT_API_VERSION)
    compileOnly("me.clip", "placeholderapi", Constants.PAPI_VERSION)
    compileOnly("com.discordsrv", "discordsrv", Constants.DISCORDSRV_VERSION)
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher")
    testImplementation("io.papermc.paper", "paper-api", Constants.PAPER_VERSION)
    testImplementation("com.github.EterniaServer", "EterniaLib", Constants.ETERNIALIB_VERSION)
    testImplementation("net.milkbowl.vault", "VaultUnlockedAPI", Constants.VAULT_API_VERSION)
    testImplementation(platform("org.junit:junit-bom:${Constants.JUPITER_VERSION}"))
    testImplementation("org.junit.jupiter", "junit-jupiter")
    testImplementation("org.mockito", "mockito-core", Constants.MOCKITO_VERSION)
    testImplementation("org.mockito", "mockito-junit-jupiter", Constants.MOCKITO_VERSION)
}

tasks.shadowJar {
    archiveBaseName.set(project.name)
    archiveClassifier.set("")
    archiveVersion.set("${project.version}")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.test {
    val mockitoJar = configurations.testRuntimeClasspath
        .get()
        .filter { it.name.contains("mockito-core") }
        .firstOrNull()

    jvmArgs = listOf("-javaagent:$mockitoJar")

    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
        csv.required = true
    }
}

tasks.named("sonar") {
    dependsOn(tasks.jacocoTestReport)
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand(mapOf("version" to version))
        filteringCharset = "UTF-8"
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

afterEvaluate {
    tasks.named("generateMetadataFileForGprPublication") {
        dependsOn(tasks.named("jar"))
        dependsOn(tasks.named("shadowJar"))
    }
}

publishing {
    repositories {
        maven {
            name = "br.com.eterniaserver"
            url = uri("https://maven.pkg.github.com/eterniaserver/eterniaserver")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["shadow"])
            artifact(sourcesJar.get())
        }
    }
}
