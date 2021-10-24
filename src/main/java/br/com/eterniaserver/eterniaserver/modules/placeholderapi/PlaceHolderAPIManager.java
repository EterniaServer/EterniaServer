package br.com.eterniaserver.eterniaserver.modules.placeholderapi;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;

import java.util.logging.Level;


public class PlaceHolderAPIManager implements Module {

    private final EterniaServer plugin;

    public PlaceHolderAPIManager(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        new Configurations.Configs(plugin);

        new Services.Placeholders(plugin).register();
    }

    @Override
    public void loadCommandsCompletions() {
        plugin.getLogger().log(Level.INFO, "PlaceHolderAPI module: no commands completions");
    }

    @Override
    public void loadListeners() {
        plugin.getLogger().log(Level.INFO, "PlaceHolderAPI module: no listeners");
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "PlaceHolderAPI module: no schedules");
    }

    @Override
    public void loadCommands() {
        plugin.getLogger().log(Level.INFO, "PlaceHolderAPI module: no commands");
    }

    @Override
    public void reloadConfigurations() {
        new Configurations.Configs(plugin);
    }

}
