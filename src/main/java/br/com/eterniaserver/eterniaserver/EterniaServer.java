package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.dependencies.papi.*;
import br.com.eterniaserver.eterniaserver.generics.*;
import br.com.eterniaserver.eterniaserver.generics.InternMethods;
import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Files;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;

import co.aikar.commands.PaperCommandManager;

import io.papermc.lib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;

public class EterniaServer extends JavaPlugin {

    public boolean chatMuted = false;
    public boolean hasPlaceholderAPI = true;

    public final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);
    public final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private PaperCommandManager manager;
    private EFiles eFiles;

    private final InternMethods internMethods = new InternMethods(this);
    private final PlaceHolders placeHolders = new PlaceHolders(this);
    private final EconomyManager money = new EconomyManager(this);
    private final ExperienceManager exp = new ExperienceManager(this);

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

        loadManagers();

        placeholderAPIHook();
        vaultHook();

        this.getServer().getPluginManager().registerEvents(new OnPlayerJump(this), this);
        this.getServer().getPluginManager().registerEvents(new OnAsyncPlayerPreLogin(this), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityDamage(this), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityInventoryClick(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerBlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerBlockPlace(this), this);
        this.getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerCommandPreProcess(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeath(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteract(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMove(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerSignChange(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerToggleSneak(this), this);
        this.getServer().getPluginManager().registerEvents(new OnServerListPing(this), this);

    }

    private void vaultHook() {
        new VaultHook(this);
    }

    private void placeholderAPIHook() {
        new PAPI(this);
    }

    private void loadManagers() {
        new Managers(this);
    }

    public InternMethods getInternMethods() {
        return internMethods;
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

    public PlaceHolders getPlaceHolders() {
        return placeHolders;
    }

}
