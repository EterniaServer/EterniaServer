package br.com.eterniaserver.eterniaserver.modules.placeholderapi;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

final class Configurations {
    static class Configs implements FileCfg {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        protected Configs(final EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            final String[] strings = plugin.strings();

            // Strings
            strings[Strings.DATA_FORMAT.ordinal()] = inFile.getString("format.data-time", "dd/MM/yyyy HH:mm");
            strings[Strings.INVALID_PLACEHOLDER.ordinal()] = inFile.getString("placeholders.invalid", "PLACEHOLDER NÃO EXISTENTE");
            strings[Strings.BALANCE_TOP_TAG.ordinal()] = inFile.getString("placeholders.balance-top", "§8[§2Magnata§8]");
            strings[Strings.AFK_PLACEHOLDER.ordinal()] = inFile.getString("placeholders.afk", "§9 AFK");
            strings[Strings.GOD_PLACEHOLDER.ordinal()] = inFile.getString("placeholders.god", "§9 GOD");

            // Strings
            outFile.set("format.data-time", strings[Strings.DATA_FORMAT.ordinal()]);
            outFile.set("placeholders.invalid", strings[Strings.INVALID_PLACEHOLDER.ordinal()]);
            outFile.set("placeholders.balance-top", strings[Strings.BALANCE_TOP_TAG.ordinal()]);
            outFile.set("placeholders.afk", strings[Strings.AFK_PLACEHOLDER.ordinal()]);
            outFile.set("placeholders.god", strings[Strings.GOD_PLACEHOLDER.ordinal()]);

            saveConfiguration(true);
        }

        @Override
        public FileConfiguration inFileConfiguration() {
            return inFile;
        }

        @Override
        public FileConfiguration outFileConfiguration() {
            return outFile;
        }

        @Override
        public String getFolderPath() {
            return Constants.PAPI_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.PAPI_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }

    }
}
