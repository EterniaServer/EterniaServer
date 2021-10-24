package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;

import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpawnerManager implements Module {

    private final EterniaServer plugin;

    private Services.Spawner spawnerService;

    public SpawnerManager(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        new Configurations.Configs(plugin);
        new Configurations.Locales(plugin);

        loadCommandsLocales(new Configurations.CommandsLocales(), Enums.Commands.class);

        this.spawnerService = new Services.Spawner(plugin);
    }

    @Override
    public void loadCommandsCompletions() {
        CommandManager.getCommandCompletions().registerStaticCompletion(
                "valid_entities",
                Stream.of(Enums.Entities.values()).map(Enum::name).collect(Collectors.toList())
        );
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin, spawnerService), plugin);
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "Spawner module: no schedules");
    }

    @Override
    public void loadCommands() {
        CommandManager.registerCommand(new Commands.Give(plugin, spawnerService));
    }

    @Override
    public void reloadConfigurations() {
        new Configurations.Configs(plugin);
        new Configurations.Locales(plugin);
    }

}
