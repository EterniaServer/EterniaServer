package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.configuration.file.FileConfiguration;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class ChatConfiguration implements ReloadableConfiguration {

        @Override
        public FileConfiguration inFileConfiguration() {
            return null;
        }

        @Override
        public FileConfiguration outFileConfiguration() {
            return null;
        }

        @Override
        public String getFolderPath() {
            return Constants.CHAT_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CHAT_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return new String[0];
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return new CommandLocale[0];
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.WARNING_ADVICE;
        }

        @Override
        public void executeConfig() {

        }

        @Override
        public void executeCritical() {

        }
    }

}
