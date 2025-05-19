package br.com.eterniaserver.eterniaserver.modules.placeholderapi;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;


public class PlaceHolderAPIManager implements Module {

    private final EterniaServer plugin;

    public PlaceHolderAPIManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "papi",
                true,
                new Configurations.PlaceHolderConfiguration(plugin)
        );

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
