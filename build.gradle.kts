import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
  `java-library`
  id("com.github.johnrengelman.shadow") version "7.0.0"
  id("io.papermc.paperweight.userdev") version "1.1.11"
  id("xyz.jpenilla.run-paper") version "1.0.4"
  id("net.minecrell.plugin-yml.bukkit") version "0.5.0"
}

group = "br.com.eterniaserver"
version = "3.0.0"
description = "Blablabla"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://raw.github.com/yurinogueira/EterniaLib/repository")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.minebench.de/")
    mavenLocal()
}

dependencies {
    paperDevBundle("1.17.1-R0.1-SNAPSHOT")
    implementation("org.bstats", "bstats-bukkit", "2.2.1")
    compileOnly("br.com.eterniaserver", "EterniaLib", "3.0.0-STABLE")
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")
    compileOnly("me.clip", "placeholderapi", "2.10.10")
    compileOnly("com.acrobot.chestshop", "chestshop", "3.10")
}

tasks {
  shadowJar {
    listOf(
            "org.bstats"
    ).forEach { relocate(it, "${rootProject.group}.lib.$it") }
  }

  build {
    dependsOn(reobfJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release.set(16)
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
  authors = listOf("Yuri Nogueira")
}
