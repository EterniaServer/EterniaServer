package br.com.eterniaserver.eterniaserver.api;

import br.com.eterniaserver.eternialib.CommandManager;

public interface Module {

    void loadConfigurations();

    void loadCommandsCompletions();

    void loadListeners();

    void loadSchedules();

    void loadCommands();

    void reloadConfigurations();

    default <T extends Enum<T>> void loadCommandsLocales(CommandsCfg commandsLocales, Class<T> enumCommands) {
        for (final T command : enumCommands.getEnumConstants()) {
            CommandManager.getCommandReplacements().addReplacements(
                    command.name(), commandsLocales.getName(command.ordinal()),
                    command.name() + "_DESCRIPTION", commandsLocales.getDescription(command.ordinal()),
                    command.name() + "_SYNTAX", commandsLocales.getSyntax(command.ordinal()),
                    command.name() + "_PERM", commandsLocales.getPerm(command.ordinal())
            );
        }
    }

}
