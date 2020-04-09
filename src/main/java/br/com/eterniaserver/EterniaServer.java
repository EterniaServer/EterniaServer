package br.com.eterniaserver;

import br.com.eterniaserver.events.*;
import br.com.eterniaserver.modules.*;
import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.storages.Configs;
import br.com.eterniaserver.storages.MessagesConfig;
import br.com.eterniaserver.storages.sql.Connections;
import br.com.eterniaserver.storages.StorageConfig;
import br.com.eterniaserver.vault.VaultHook;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    public static FileConfiguration configs;
    public static FileConfiguration blocks;
    public static FileConfiguration messages;

    public static Connections sqlcon;

    @Override
    public void onEnable() {
        saveDefaultConfigs();
        saveDefaultMessage();

        setDefaultStorage();

        teleportsManager();
        spawnersManager();
        antiNetherTrapManager();
        economyManager();
        experienceManager();
        genericManager();
        blockRewardManager();
        bedManager();
        vaultHook();

        this.getServer().getPluginManager().registerEvents(new OnBlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new OnChat(), this);
        this.getServer().getPluginManager().registerEvents(new OnDamage(), this);
        this.getServer().getPluginManager().registerEvents(new OnExpDrop(), this);
        this.getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);

    }

    @Override
    public void onDisable() {
        if (CVar.getBool("modules.economy")) {
            sqlcon.Close();
        }
    }

    private void vaultHook() {
        new VaultHook(this);
    }

    private void bedManager() {
        new BedManager(this);
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
        new StorageConfig(this);
    }

    private void saveDefaultMessage() {
        new MessagesConfig(this);
    }

    private void saveDefaultConfigs() {
        new Configs(this);
    }

    public static FileConfiguration getBlocks() {
        return blocks;
    }

    public static FileConfiguration getMessages() {
        return messages;
    }

    public static FileConfiguration getConfigs() {
        return configs;
    }

}