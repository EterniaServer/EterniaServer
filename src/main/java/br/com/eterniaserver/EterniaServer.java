package br.com.eterniaserver;

import br.com.eterniaserver.dependencies.papi.PAPI;
import br.com.eterniaserver.events.*;
import br.com.eterniaserver.modules.*;
import br.com.eterniaserver.modules.bedmanager.BedManager;
import br.com.eterniaserver.modules.chatmanager.ChatManager;
import br.com.eterniaserver.modules.economymanager.EconomyManager;
import br.com.eterniaserver.modules.experiencemanager.ExperienceManager;
import br.com.eterniaserver.modules.genericmanager.GenericManager;
import br.com.eterniaserver.modules.homesmanager.HomesManager;
import br.com.eterniaserver.modules.kitsmanager.KitsManager;
import br.com.eterniaserver.modules.playerchecksmanager.PlayerChecksManager;
import br.com.eterniaserver.modules.spawnermanager.SpawnersManager;
import br.com.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.storages.Configs;
import br.com.eterniaserver.storages.MessagesConfig;
import br.com.eterniaserver.storages.sql.Connections;
import br.com.eterniaserver.storages.DatabaseType;
import br.com.eterniaserver.dependencies.vault.VaultHook;
import br.com.eterniaserver.dependencies.vault.VaultUnHook;

import io.papermc.lib.PaperLib;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    public static Connections connection;

    public static FileConfiguration blocks;
    public static FileConfiguration configs;
    public static FileConfiguration commands;
    public static FileConfiguration kits;
    public static FileConfiguration chat;
    public static FileConfiguration messages;
    public static FileConfiguration cph;
    public static FileConfiguration groups;

    @Override
    public void onEnable() {

        PaperLib.suggestPaper(this);

        saveDefaultConfigs();
        saveDefaultMessages();

        databaseType();

        bedManager();
        blockRewardManager();
        chatManager();
        commandsManager();
        economyManager();
        elevatorManager();
        experienceManager();
        genericManager();
        homesManager();
        kitsManager();
        playerChecksManager();
        spawnersManager();
        teleportsManager();

        placeholderAPIHook();
        vaultHook();

        this.getServer().getPluginManager().registerEvents(new OnBlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new OnDamage(), this);
        this.getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerChat(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerCommandPreProcessEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteract(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerToggleSneakEvent(), this);

    }

    @Override
    public void onDisable() {

        vaultUnHook();

    }

    private void vaultUnHook() {
        new VaultUnHook();
    }

    private void vaultHook() {
        new VaultHook(this);
    }

    private void placeholderAPIHook() {
        new PAPI();
    }

    private void spawnersManager() {
        new SpawnersManager(this);
    }

    private void teleportsManager() {
        new TeleportsManager(this);
    }

    private void playerChecksManager() {
        new PlayerChecksManager(this);
    }

    private void kitsManager() {
        new KitsManager(this);
    }

    private void homesManager() {
        new HomesManager(this);
    }

    private void genericManager() {
        new GenericManager(this);
    }

    private void experienceManager() {
        new ExperienceManager(this);
    }

    private void elevatorManager() {
        new ElevatorManager();
    }

    private void economyManager() {
        new EconomyManager(this);
    }

    private void commandsManager() {
        new CommandsManager(this);
    }

    private void chatManager() {
        new ChatManager(this);
    }

    private void blockRewardManager(){
        new BlockRewardManager(this);
    }

    private void bedManager() {
        new BedManager(this);
    }

    private void databaseType() {
        new DatabaseType(this);
    }

    private void saveDefaultMessages() {
        new MessagesConfig(this);
    }

    private void saveDefaultConfigs() {
        new Configs(this);
    }

}