package br.com.eterniaserver.eterniaserver.modules.placeholderapi;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;


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
    public void loadCommandsCompletions() {}

    @Override
    public void loadConditions() {}

    @Override
    public void loadListeners() {}

    @Override
    public void loadSchedules() {}

    @Override
    public void loadCommands() {}


}
