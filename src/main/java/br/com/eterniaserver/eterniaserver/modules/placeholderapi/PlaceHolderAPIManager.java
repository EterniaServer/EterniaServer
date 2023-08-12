package br.com.eterniaserver.eterniaserver.modules.placeholderapi;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;

import java.util.logging.Level;


public class PlaceHolderAPIManager implements Module {

    private final EterniaServer plugin;

    public PlaceHolderAPIManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.PlaceHolderConfiguration papiConfig = new Configurations.PlaceHolderConfiguration(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "papi", papiConfig);

        papiConfig.executeConfig();
        papiConfig.executeCritical();
        papiConfig.saveConfiguration(true);

        new Services.Placeholders(plugin).register();
    }

    @Override
    public void loadCommandsCompletions() {
        plugin.getLogger().log(Level.INFO, "PlaceHolderAPI module: no commands completions");
    }

    @Override
    public void loadConditions() {
        plugin.getLogger().log(Level.INFO, "PlaceHolderAPI module: no conditions");
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


}
