package br.com.eterniaserver;

import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.events.*;
import br.com.eterniaserver.modules.*;
import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.storages.MessagesConfig;
import br.com.eterniaserver.storages.Connections;
import br.com.eterniaserver.storages.StorageConfig;
import br.com.eterniaserver.vault.VaultHook;
import org.bukkit.Bukkit;
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

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            final VaultHook vaultHook = new VaultHook();
            vaultHook.hook();
        } else {
            new ConsoleMessage("server.no-vault");
            this.getPluginLoader().disablePlugin(this);
        }

        setDefaultStorage();

        teleportsManager();
        spawnersManager();
        antiNetherTrapManager();
        economyManager();
        experienceManager();
        genericManager();
        blockRewardManager();
        bedManager();

        this.getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
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