package br.com.eterniaserver.eterniaserver.api.interfaces;

import br.com.eterniaserver.eternialib.EterniaLib;

public interface Module {

    void loadConfigurations();

    void loadCommandsCompletions();

    void loadConditions();

    void loadListeners();

    void loadSchedules();

    void loadCommands();

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
