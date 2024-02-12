package br.com.eterniaserver.eterniaserver.modules;

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

}
