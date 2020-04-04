package com.eterniaserver;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.events.*;
import com.eterniaserver.modules.*;
import com.eterniaserver.storage.MessagesConfig;
import com.eterniaserver.storage.Connections;
import com.eterniaserver.storage.StorageConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    public static FileConfiguration messagesConfig;
    public static FileConfiguration blocks;
    public static Connections sqlcon;
    private static EterniaServer plugin;

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        saveDefaultMessage();

        setDefaultStorage();

        teleportsManager();
        spawnersManager();
        antiNetherTrapManager();
        afkManager();
        economyManager();
        experienceManager();
        genericManager();
        blockRewardManager();

        this.getServer().getPluginManager().registerEvents(new OnChat(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new OnExpDrop(), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new OnDamage(), this);
        this.getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);

        if (CVar.getBool("server.async-check")) {
            new OnPlayerMove().runTaskTimerAsynchronously(this, 20L, 20);
        } else {
            new OnPlayerMove().runTaskTimer(this, 20L, 20);
        }
    }

    @Override
    public void onDisable() {
        if (CVar.getBool("modules.economy")) {
            sqlcon.Close();
        }
    }

    private void blockRewardManager(){
        new BlockRewardManager(this);
    }

    private void genericManager() {
        new GenericManager(this);
    }

    private void experienceManager() {
        new ExperienceManager(this);
    }

    private void economyManager() {
        new EconomyManager(this);
    }

    private void afkManager() {
        new AFKManager(this);
    }

    private void antiNetherTrapManager() {
        new AntiNetherTrapManager(this);
    }

    private void spawnersManager() {
        new SpawnersManager(this);
    }

    private void teleportsManager() {
        new TeleportsManager(this);
    }

    private void setDefaultStorage() {
        new StorageConfig(this);
    }

    private void saveDefaultMessage() {
        new MessagesConfig(this);
    }

    public static EterniaServer getMain() {
        return plugin;
    }

    public static FileConfiguration getBlocks() {
        return blocks;
    }

    public static FileConfiguration getMessages() {
        return messagesConfig;
    }

}