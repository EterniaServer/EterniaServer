package br.com.eterniaserver.eterniaserver.api.interfaces;

import br.com.eterniaserver.eterniaserver.objects.CommandI18n;


public interface CommandsCfg extends FileCfg {

    CommandI18n[] getCommandsLocalesArray();

    default String getName(final int id) {
        return getCommandsLocalesArray()[id].name();
    }

    default void syncToFile() {
        for (final CommandI18n command : getCommandsLocalesArray()) {
            command.name(inFileConfiguration().getString(command.key() + ".name", command.name()));
            command.syntax(inFileConfiguration().getString(command.key() + ".syntax", command.syntax()));
            command.description(inFileConfiguration().getString(command.key() + ".description", command.description()));
            command.perm(inFileConfiguration().getString(command.key() + ".perm", command.perm()));

            outFileConfiguration().set(command.key() + ".name", command.name());
            outFileConfiguration().set(command.key() + ".syntax", command.syntax());
            outFileConfiguration().set(command.key() + ".description", command.description());
            outFileConfiguration().set(command.key() + ".perm", command.perm());
        }
    }

    default String getSyntax(final int id) {
        return getCommandsLocalesArray()[id].syntax();
    }

    default String getDescription(final int id) {
        return getCommandsLocalesArray()[id].description();
    }

    default String getPerm(final int id) {
        return getCommandsLocalesArray()[id].perm();
    }

}
