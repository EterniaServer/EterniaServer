package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;

import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpawnerManager implements Module {

    private final EterniaServer plugin;

    private Services.Spawner spawnerService;

    public SpawnerManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.SpawnerConfiguration configuration = new Configurations.SpawnerConfiguration(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "spawner", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);

        loadCommandsLocale(configuration, Enums.Commands.class);

        this.spawnerService = new Services.Spawner(plugin);
    }

    @Override
    public void loadCommandsCompletions() {
        EterniaLib.getCmdManager().getCommandCompletions().registerStaticCompletion(
                "valid_entities",
                Stream.of(Enums.Entities.values()).map(Enum::name).collect(Collectors.toList())
        );
    }

    @Override
    public void loadConditions() {
        plugin.getLogger().log(Level.INFO, "spawner module: no conditions");
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin, spawnerService), plugin);
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "spawner module: no schedules");
    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Give(plugin, spawnerService));
    }

}
