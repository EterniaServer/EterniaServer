package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.dependencies.papi.*;
import br.com.eterniaserver.eterniaserver.events.*;
import br.com.eterniaserver.eterniaserver.modules.Managers;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.chats.*;
import br.com.eterniaserver.eterniaserver.modules.economymanager.EconomyManager;
import br.com.eterniaserver.eterniaserver.modules.experiencemanager.ExperienceManager;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.HomesManager;
import br.com.eterniaserver.eterniaserver.modules.kitsmanager.KitsManager;
import br.com.eterniaserver.eterniaserver.modules.rewardsmanager.RewardsManager;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.eterniaserver.objects.Checks;
import br.com.eterniaserver.eterniaserver.objects.PlayerManager;
import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Files;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;

import br.com.eterniaserver.eterniaserver.objects.Vars;
import co.aikar.commands.PaperCommandManager;

import io.papermc.lib.PaperLib;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;

public class EterniaServer extends JavaPlugin {

    public boolean chatMuted = false;
    public boolean hasPlaceholderAPI = true;

    public final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private PaperCommandManager manager;
    private EFiles eFiles;

    private final Vars vars = new Vars();
    private final Checks checks = new Checks(this);
    private final PlaceHolders placeHolders = new PlaceHolders(this);
    private final PlayerManager playerManager = new PlayerManager(this);
    private final EconomyManager money = new EconomyManager(this);
    private final ExperienceManager exp = new ExperienceManager(this);

    public Local local;
    public Staff staff;
    public TeleportsManager teleportsManager;
    public Files files;

    public FileConfiguration serverConfig, msgConfig, cmdConfig, rewardsConfig;
    public FileConfiguration blockConfig, kitConfig, chatConfig, placeholderConfig, groupConfig;

    @Override
    public void onEnable() {

        PaperLib.suggestPaper(this);

        manager = new PaperCommandManager(this);
        files = new Files(this);

        files.loadConfigs();
        files.loadMessages();
        files.loadDatabase();

        eFiles = new EFiles(msgConfig);

        local = new Local(this);
        staff = new Staff(this);

        loadManagers();
        homesManager();
        kitsManager();
        rewardsManager();
        loadTeleportsManager();

        placeholderAPIHook();
        vaultHook();

        this.getServer().getPluginManager().registerEvents(new OnEntityDamage(this), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityInventoryClick(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerBlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerBlockPlace(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerChat(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerCommandPreProcess(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeath(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteract(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMove(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerSignChange(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerToggleSneak(this), this);

    }

    private void vaultHook() {
        new VaultHook(this);
    }

    public void rewardsManager() {
        new RewardsManager(this);
    }

    private void placeholderAPIHook() {
        new PAPI(this);
    }

    private void kitsManager() {
        new KitsManager(this);
    }

    private void homesManager() {
        new HomesManager(this);
    }

    private void loadTeleportsManager() {
        teleportsManager = new TeleportsManager(this);
    }

    private void loadManagers() {
        new Managers(this);
    }

    public Checks getChecks() {
        return checks;
    }

    public EFiles getEFiles() {
        return eFiles;
    }

    public ExperienceManager getExp() {
        return exp;
    }

    public PaperCommandManager getManager() {
        return manager;
    }

    public EconomyManager getMoney() {
        return money;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PlaceHolders getPlaceHolders() {
        return placeHolders;
    }

    public Vars getVars() {
        return vars;
    }

}
