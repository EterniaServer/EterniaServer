package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Table;
import br.com.eterniaserver.eterniaserver.dependencies.papi.PlaceHolders;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;
import br.com.eterniaserver.eterniaserver.events.BlockHandler;
import br.com.eterniaserver.eterniaserver.events.EntityHandler;
import br.com.eterniaserver.eterniaserver.events.PlayerHandler;
import br.com.eterniaserver.eterniaserver.events.ServerHandler;

import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    public static final Configs configs = new Configs();

    @Override
    public void onEnable() {

        new Table();
        new PlaceHolders().register();
        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new BlockHandler(), this);
        this.getServer().getPluginManager().registerEvents(new EntityHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
        this.getServer().getPluginManager().registerEvents(new ServerHandler(), this);

    }

}
