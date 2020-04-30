package br.com.eterniaserver;

import br.com.eterniaserver.API.Exp;
import br.com.eterniaserver.API.Money;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.dependencies.papi.PAPI;
import br.com.eterniaserver.events.*;
import br.com.eterniaserver.modules.*;
import br.com.eterniaserver.modules.bedmanager.BedManager;
import br.com.eterniaserver.modules.chatmanager.ChatManager;
import br.com.eterniaserver.modules.chatmanager.chats.Local;
import br.com.eterniaserver.modules.chatmanager.chats.Staff;
import br.com.eterniaserver.modules.chatmanager.events.ChatEvent;
import br.com.eterniaserver.modules.economymanager.EconomyManager;
import br.com.eterniaserver.modules.experiencemanager.ExperienceManager;
import br.com.eterniaserver.modules.genericmanager.GenericManager;
import br.com.eterniaserver.modules.genericmanager.Reload;
import br.com.eterniaserver.modules.homesmanager.HomesManager;
import br.com.eterniaserver.modules.kitsmanager.KitsManager;
import br.com.eterniaserver.modules.playerchecksmanager.PlayerChecksManager;
import br.com.eterniaserver.modules.spawnermanager.SpawnersManager;
import br.com.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.player.PlayerFlyState;
import br.com.eterniaserver.player.PlayerManager;
import br.com.eterniaserver.storages.Configs;
import br.com.eterniaserver.storages.MessagesConfig;
import br.com.eterniaserver.storages.DatabaseType;
import br.com.eterniaserver.dependencies.vault.VaultHook;
import br.com.eterniaserver.dependencies.vault.VaultUnHook;

import br.com.eterniaserver.storages.sql.Connections;
import io.papermc.lib.PaperLib;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    private final Vars vars = new Vars();

    public boolean hasPlaceholderAPI = true;
    private TeleportsManager teleportsManager;

    private final Strings strings = new Strings(this);
    private final Messages messages = new Messages(strings);
    private final Checks checks = new Checks(this, vars);
    private final PlayerManager playerManager = new PlayerManager(this, vars);
    private final Local local = new Local(this, messages, strings, vars);
    private final Staff staff = new Staff(this, strings);
    private final ChatEvent chatEvent = new ChatEvent(this, local, staff, vars);
    private final Reload reload = new Reload(this, messages);
    private final PlayerFlyState playerFlyState = new PlayerFlyState(messages);
    private final Exp exp = new Exp(this, vars);
    private final Money money = new Money(this, playerManager, vars);

    public Connections connections;

    public FileConfiguration blockConfig;
    public FileConfiguration serverConfig;
    public FileConfiguration cmdConfig;
    public FileConfiguration kitConfig;
    public FileConfiguration chatConfig;
    public FileConfiguration msgConfig;
    public FileConfiguration placeholderConfig;
    public FileConfiguration groupConfig;

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
        teleportsManager();
        playerChecksManager();
        spawnersManager();

        placeholderAPIHook();
        vaultHook();

        this.getServer().getPluginManager().registerEvents(new OnBlockBreak(this, messages), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockPlace(this, messages), this);
        this.getServer().getPluginManager().registerEvents(new OnDamage(this, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnInventoryClick(this, messages, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerChat(this, chatEvent, checks, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerCommandPreProcessEvent(this, messages, strings), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeath(this, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteract(this, messages, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(this, messages, checks, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(this, playerManager, messages, checks, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMove(this, messages, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(this, vars), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerToggleSneakEvent(this), this);

    }

    @Override
    public void onDisable() {

        vaultUnHook();

    }

    private void vaultUnHook() {
        new VaultUnHook(this);
    }

    private void vaultHook() {
        new VaultHook(this, messages, playerManager, money, vars);
    }

    private void placeholderAPIHook() {
        new PAPI(this, messages, vars);
    }

    private void spawnersManager() {
        new SpawnersManager(this, messages, strings);
    }

    private void playerChecksManager() {
        new PlayerChecksManager(this, messages, strings, teleportsManager, vars);
    }

    private void teleportsManager() {
        teleportsManager = new TeleportsManager(this, messages, strings, vars, money);
    }

    private void kitsManager() {
        new KitsManager(this, messages, playerManager, strings, vars);
    }

    private void homesManager() {
        new HomesManager(this, messages, strings, vars);
    }

    private void genericManager() {
        new GenericManager(this, messages, strings, playerFlyState, reload, vars);
    }

    private void experienceManager() {
        new ExperienceManager(this, messages, checks, exp);
    }

    private void elevatorManager() {
        new ElevatorManager(this, messages);
    }

    private void economyManager() {
        new EconomyManager(this, messages, money);
    }

    private void commandsManager() {
        new CommandsManager(this, messages);
    }

    private void chatManager() {
        new ChatManager(this, messages, strings, vars);
    }

    private void blockRewardManager(){
        new BlockRewardManager(this, messages);
    }

    private void bedManager() {
        new BedManager(this, messages, checks, vars);
    }

    private void databaseType() {
        new DatabaseType(this, messages);
    }

    private void saveDefaultMessages() {
        new MessagesConfig(this);
    }

    private void saveDefaultConfigs() {
        new Configs(this);
    }

}