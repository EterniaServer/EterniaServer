package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
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

            final boolean[] booleans = plugin.booleans();

            // Booleans
            booleans[Booleans.MODULE_SPAWNERS.ordinal()] = inFile.getBoolean("modules.spawners", true);
            // Booleans
            outFile.set("modules.spawners", booleans[Booleans.MODULE_SPAWNERS.ordinal()]);

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
            return Constants.DATA_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CORE_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }

    }

}
