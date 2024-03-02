package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;

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
        Configurations.SpawnerMessagesConfiguration messages = new Configurations.SpawnerMessagesConfiguration(plugin.messages());
        Configurations.SpawnerCommandsConfiguration commands = new Configurations.SpawnerCommandsConfiguration();

        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "spawner", true, configuration);
        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "spawner_messages", true, messages);
        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "spawner_commands", true, commands);

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
    public void loadConditions() {}

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin, spawnerService), plugin);
    }

    @Override
    public void loadSchedules() {}

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Give(plugin, spawnerService));
    }

}
