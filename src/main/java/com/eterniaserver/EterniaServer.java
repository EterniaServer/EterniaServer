package com.eterniaserver;

import com.eterniaserver.configs.Vars;
import com.eterniaserver.events.*;
import com.eterniaserver.modules.*;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
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
        economyManager();
        experienceManager();
        genericManager();
        blockRewardManager();

        testPaper();

        this.getServer().getPluginManager().registerEvents(new OnChat(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new OnExpDrop(), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new OnDamage(), this);
        this.getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
    }

    @Override
    public void onDisable() {
        if (Vars.economy)
        {
            sqlcon.Close();
        }
    }

    private void testPaper() {
        boolean isPapermc = false;
        try {
            isPapermc = Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData") != null;
        } catch (ClassNotFoundException e) {
            MVar.consoleMessage("server.gotopaper");
        }

        if (isPapermc) {
            MVar.consoleMessage("server.usepaper");
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
        new StorageConfig(CVar.getBool("sql.mysql"));
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