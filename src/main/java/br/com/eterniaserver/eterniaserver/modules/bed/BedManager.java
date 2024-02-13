package br.com.eterniaserver.eterniaserver.modules.bed;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.modules.bed.Services.SleepingService;
import br.com.eterniaserver.eterniaserver.modules.bed.Configurations.BedMessages;
import br.com.eterniaserver.eterniaserver.modules.bed.Configurations.BedConfiguration;

public class BedManager implements Module {

    private final SleepingService sleepingService = new SleepingService();
    private final EterniaServer plugin;

    public BedManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        BedMessages messages = new BedMessages(plugin);
        BedConfiguration configuration = new BedConfiguration(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "bed_messages", messages);
        EterniaLib.registerConfiguration("eterniaserver", "bed", configuration);

        messages.executeConfig();
        configuration.executeConfig();

        configuration.executeCritical();

        messages.saveConfiguration(true);
        configuration.saveConfiguration(true);
    }

    @Override
    public void loadCommandsCompletions() { }

    @Override
    public void loadConditions() { }

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
    public void loadCommands() { }

}
