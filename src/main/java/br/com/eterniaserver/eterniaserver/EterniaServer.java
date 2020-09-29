package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Files;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;
import br.com.eterniaserver.eterniaserver.events.BlockHandler;
import br.com.eterniaserver.eterniaserver.events.EntityHandler;
import br.com.eterniaserver.eterniaserver.events.PlayerHandler;
import br.com.eterniaserver.eterniaserver.events.ServerHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    public static final YamlConfiguration cmdConfig = new YamlConfiguration();
    public static final YamlConfiguration rewardsConfig = new YamlConfiguration();
    public static final YamlConfiguration chatConfig = new YamlConfiguration();
    public static final YamlConfiguration placeholderConfig = new YamlConfiguration();
    public static final YamlConfiguration groupConfig = new YamlConfiguration();
    public static final YamlConfiguration scheduleConfig = new YamlConfiguration();

    public static final Configs configs = new Configs();

    private final Files files = new Files(this);

    @Override
    public void onEnable() {

        files.loadDatabase();
        files.loadPlaceHolders();
        files.loadCommands();
        files.loadChat();
        files.loadRewards();
        files.loadSchedules();

        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new BlockHandler(), this);
        this.getServer().getPluginManager().registerEvents(new EntityHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
        this.getServer().getPluginManager().registerEvents(new ServerHandler(), this);

    }

}
