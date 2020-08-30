package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Files;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;
import br.com.eterniaserver.eterniaserver.generics.EventAsyncPlayerChat;
import br.com.eterniaserver.eterniaserver.generics.EventEntityDamage;
import br.com.eterniaserver.eterniaserver.generics.EventEntityDamageByEntity;
import br.com.eterniaserver.eterniaserver.generics.EventEntityInventoryClick;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerBedEnter;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerBedLeave;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerBlockBreak;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerBlockPlace;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerCommandPreProcess;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerDeath;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerInteract;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerJoin;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerJump;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerLeave;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerMove;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerRespawn;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerSignChange;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerTeleport;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerToggleSneak;
import br.com.eterniaserver.eterniaserver.generics.EventServerListPing;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

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

    private final Files files = new Files(this);

    @Override
    public void onEnable() {

        files.loadConfigs();
        files.loadMessages();
        files.loadDatabase();
        files.loadPlaceHolders();

        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new EventPlayerBedEnter(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerBedLeave(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerJump(), this);
        this.getServer().getPluginManager().registerEvents(new EventEntityDamage(), this);
        this.getServer().getPluginManager().registerEvents(new EventEntityDamageByEntity(), this);
        this.getServer().getPluginManager().registerEvents(new EventEntityInventoryClick(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerBlockBreak(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new EventAsyncPlayerChat(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerCommandPreProcess(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerInteract(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerMove(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerRespawn(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerSignChange(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerToggleSneak(), this);
        this.getServer().getPluginManager().registerEvents(new EventServerListPing(), this);

    }

    public Files getFiles() {
        return files;
    }

}
