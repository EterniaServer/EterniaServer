package br.com.eterniaserver.eterniaserver.modules.bed;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Integers;

public class BedManager implements Module {

    private final Services.SleepingService sleepingService = new Services.SleepingService();
    private final EterniaServer plugin;

    public BedManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.BedConfiguration configuration = new Configurations.BedConfiguration(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "bed", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);
    }

    @Override
    public void loadCommandsCompletions() {
    }

    @Override
    public void loadConditions() {
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin, sleepingService), plugin);
    }

    @Override
    public void loadSchedules() {
        new Schedules
                .CheckWorld(plugin, sleepingService)
                .runTaskTimer(plugin, 0, 20L * plugin.getInteger(Integers.BED_CHECK_TIME));
    }

    @Override
    public void loadCommands() {
    }
}
