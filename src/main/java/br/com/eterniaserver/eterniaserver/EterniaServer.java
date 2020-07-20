package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.dependencies.papi.*;
import br.com.eterniaserver.eterniaserver.generics.*;
import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Files;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;

import co.aikar.commands.PaperCommandManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class EterniaServer extends JavaPlugin {

    private boolean chatMuted = false;

    public final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);
    public final DecimalFormat df2 = new DecimalFormat(".##");
    public final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final InternMethods internMethods = new InternMethods();
    private final PlaceHolders placeHolders = new PlaceHolders();

    private PaperCommandManager manager;
    private EFiles messages;
    private Files files;

    public static final FileConfiguration serverConfig = new YamlConfiguration();
    public static final FileConfiguration msgConfig = new YamlConfiguration();
    public static final FileConfiguration cmdConfig = new YamlConfiguration();
    public static final FileConfiguration rewardsConfig = new YamlConfiguration();
    public static final FileConfiguration blockConfig = new YamlConfiguration();
    public static final FileConfiguration kitConfig = new YamlConfiguration();
    public static final FileConfiguration chatConfig = new YamlConfiguration();
    public static final FileConfiguration placeholderConfig = new YamlConfiguration();
    public static final FileConfiguration groupConfig = new YamlConfiguration();
    public static final FileConfiguration cashConfig = new YamlConfiguration();

    @Override
    public void onEnable() {

        manager = new PaperCommandManager(this);
        files = new Files(this);

        files.loadConfigs();
        files.loadMessages();
        files.loadDatabase();

        messages = new EFiles(msgConfig);

        loadManagers();
        placeholderAPIHook();
        vaultHook();

        this.getServer().getPluginManager().registerEvents(new OnPlayerJump(), this);
        this.getServer().getPluginManager().registerEvents(new OnAsyncPlayerPreLogin(this), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityDamage(), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityInventoryClick(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerBlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerCommandPreProcess(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteract(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMove(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerSignChange(this), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerToggleSneak(), this);
        this.getServer().getPluginManager().registerEvents(new OnServerListPing(), this);

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
        return messages;
    }

    public PaperCommandManager getManager() {
        return manager;
    }

    public PlaceHolders getPlaceHolders() {
        return placeHolders;
    }

    public boolean isChatMuted() {
        return chatMuted;
    }

    public void setChatMuted(boolean chatMuted) {
        this.chatMuted = chatMuted;
    }

    public Files getFiles() {
        return files;
    }

}
