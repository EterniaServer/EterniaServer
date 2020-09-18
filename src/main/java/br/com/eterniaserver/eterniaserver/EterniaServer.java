package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Files;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;
import br.com.eterniaserver.eterniaserver.generics.EventHandlerBlock;
import br.com.eterniaserver.eterniaserver.generics.EventHandlerEntity;
import br.com.eterniaserver.eterniaserver.generics.EventHandlerPlayer;
import br.com.eterniaserver.eterniaserver.generics.EventHandlerServer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    public static final YamlConfiguration serverConfig = new YamlConfiguration();
    public static final YamlConfiguration msgConfig = new YamlConfiguration();
    public static final YamlConfiguration cmdConfig = new YamlConfiguration();
    public static final YamlConfiguration rewardsConfig = new YamlConfiguration();
    public static final YamlConfiguration blockConfig = new YamlConfiguration();
    public static final YamlConfiguration kitConfig = new YamlConfiguration();
    public static final YamlConfiguration chatConfig = new YamlConfiguration();
    public static final YamlConfiguration placeholderConfig = new YamlConfiguration();
    public static final YamlConfiguration groupConfig = new YamlConfiguration();
    public static final YamlConfiguration cashConfig = new YamlConfiguration();
    public static final YamlConfiguration scheduleConfig = new YamlConfiguration();

    private final Files files = new Files(this);

    @Override
    public void onEnable() {

        files.loadConfigs();
        files.loadMessages();
        files.loadDatabase();
        files.loadPlaceHolders();

        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new EventHandlerBlock(), this);
        this.getServer().getPluginManager().registerEvents(new EventHandlerEntity(), this);
        this.getServer().getPluginManager().registerEvents(new EventHandlerPlayer(), this);
        this.getServer().getPluginManager().registerEvents(new EventHandlerServer(), this);

    }

    public Files getFiles() {
        return files;
    }

}
