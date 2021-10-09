package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.Module;

import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpawnerManager implements Module {

    private final EterniaServer plugin;

    public SpawnerManager(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        new Configurations.Configs(plugin);
        new Configurations.Locales(plugin);
    }

    @Override
    public void loadCommandsLocales() {
        Configurations.CommandsLocales commandsLocales = new Configurations.CommandsLocales();

        for (final Enums.Commands command : Enums.Commands.values()) {
            CommandManager.getCommandReplacements().addReplacements(
                    command.name(), commandsLocales.getName(command.ordinal()),
                    command.name() + "_DESCRIPTION", commandsLocales.getDescription(command.ordinal()),
                    command.name() + "_SYNTAX", commandsLocales.getSyntax(command.ordinal()),
                    command.name() + "_PERM", commandsLocales.getPerm(command.ordinal())
            );
        }
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
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin), plugin);
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "Spawner module: no schedules");
    }

    @Override
    public void loadCommands() {
        CommandManager.registerCommand(new Commands.Give(plugin));
    }

    @Override
    public void reloadConfigurations() {

    }

}
