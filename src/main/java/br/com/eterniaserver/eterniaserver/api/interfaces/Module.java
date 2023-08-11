package br.com.eterniaserver.eterniaserver.api.interfaces;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;

public interface Module {

    void loadConfigurations();

    void loadCommandsCompletions();

    void loadConditions();

    void loadListeners();

    void loadSchedules();

    void loadCommands();

    default <T extends Enum<T>> void loadCommandsLocale(ReloadableConfiguration config, Class<T> enumCommands) {
        for (T command : enumCommands.getEnumConstants()) {
            CommandLocale commandLocale = config.commandsLocale()[command.ordinal()];
            EterniaLib.getCmdManager().getCommandReplacements().addReplacements(
                    command.name().toLowerCase(), commandLocale.name(),
                    command.name().toLowerCase() + "_DESCRIPTION", commandLocale.description(),
                    command.name().toLowerCase() + "_PERM", commandLocale.perm(),
                    command.name().toLowerCase() + "_SYNTAX", commandLocale.syntax(),
                    command.name().toLowerCase() + "_ALIASES", commandLocale.aliases()
            );
        }
    }

    default <T extends Enum<T>> void loadCommandsLocales(CommandsCfg commandsLocales, Class<T> enumCommands) {
        for (final T command : enumCommands.getEnumConstants()) {
            EterniaLib.getCmdManager().getCommandReplacements().addReplacements(
                    command.name(), commandsLocales.getName(command.ordinal()),
                    command.name() + "_DESCRIPTION", commandsLocales.getDescription(command.ordinal()),
                    command.name() + "_SYNTAX", commandsLocales.getSyntax(command.ordinal()),
                    command.name() + "_PERM", commandsLocales.getPerm(command.ordinal())
            );
        }
    }

}
