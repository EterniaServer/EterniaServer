import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

group = "br.com.eterniaserver"
version = "3.0.0"
description = "Blablabla"

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.0"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://raw.github.com/EterniaServer/EterniaLib/repository")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.minebench.de/")
    mavenLocal()
}

dependencies {
    implementation("org.bstats", "bstats-bukkit", "2.2.1")
    implementation("net.kyori", "adventure-text-minimessage", "4.2.0-SNAPSHOT") {
        exclude("net.kyori", "adventure-api")
    }
    compileOnly("io.papermc.paper", "paper-api", "1.18.1-R0.1-SNAPSHOT")
    compileOnly("br.com.eterniaserver", "EterniaLib", "3.0.0-STABLE")
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")
    compileOnly("me.clip", "placeholderapi", "2.10.10")
    compileOnly("com.acrobot.chestshop", "chestshop", "3.10")
}

tasks {
    shadowJar {
        listOf(
                "org.bstats",
                "net.kyori.adventure.text.minimessage",
        ).forEach {
            relocate(it, "${rootProject.group}.lib.$it")
        }
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
    apiVersion = "1.17"
    website = "www.eterniaserver.com.br"
    depend = listOf("EterniaLib", "PlaceholderAPI")
    softDepend = listOf("Vault", "ChestShop")
    authors = listOf("Yuri Nogueira")
}
