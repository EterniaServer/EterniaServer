package br.com.eterniaserver.eterniaserver.api;

import br.com.eterniaserver.eterniaserver.enums.Messages;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public interface FileCfg {

    FileConfiguration inFileConfiguration();

    FileConfiguration outFileConfiguration();

    String getFolderPath();

    String getFilePath();

    String[] messages();

    default void addMessage(Messages messagesEnum, String text, String notes) {
        messages()[messagesEnum.ordinal()] = inFileConfiguration().getString(getPath(messagesEnum, false), text);

        outFileConfiguration().set(getPath(messagesEnum, false), messages()[messagesEnum.ordinal()]);
        outFileConfiguration().set(getPath(messagesEnum, true), notes);
    }

    private String getPath(Messages messagesEnum, boolean isHelp) {
        if (isHelp) {
            return "messages." + messagesEnum.name() + ".notes";
        }

        return "messages." + messagesEnum.name() + ".text";
    }

    default void saveConfiguration(final boolean inFolder) {
        if (inFolder && new File(getFolderPath()).mkdir()) {
            Bukkit.getLogger().log(Level.FINE, "Folder path " + getFolderPath() + "created.");
        }

        try {
            outFileConfiguration().save(getFilePath());
        } catch (IOException exception) {
            Bukkit.getLogger().log(Level.WARNING, "Creation failed to file " + getFilePath());
        }
    }

}
