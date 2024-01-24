import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

group = "br.com.eterniaserver"
version = "4.0.0"
description = "Blablabla"

plugins {
    `java-library`
    id("io.freefair.lombok") version "6.6.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.minebench.de/")
    mavenLocal()
}

dependencies {
    implementation("org.bstats", "bstats-bukkit", "3.0.0")
    compileOnly("io.papermc.paper", "paper-api", "1.20.4-R0.1-SNAPSHOT")
    compileOnly("br.com.eterniaserver", "eternialib", "4.0.2-BETA")
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")
    compileOnly("me.clip", "placeholderapi", "2.11.1")
    compileOnly("com.acrobot.chestshop", "chestshop", "3.10")
}

tasks {
    shadowJar {
        listOf(
                "org.bstats",
        ).forEach {
            relocate(it, "${rootProject.group}.lib.$it")
        }
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("${project.version}")
    }

    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = "br.com.eterniaserver.eterniaserver.EterniaServer"
    apiVersion = "1.16"
    website = "www.eterniaserver.com.br"
    depend = listOf("EterniaLib", "PlaceholderAPI")
    softDepend = listOf("Vault", "ChestShop")
    authors = listOf("Yuri Nogueira")
}
