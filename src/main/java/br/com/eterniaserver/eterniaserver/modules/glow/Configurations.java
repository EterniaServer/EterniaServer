package br.com.eterniaserver.eterniaserver.modules.glow;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.CommandsCfg;
import br.com.eterniaserver.eterniaserver.api.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.objects.CommandI18n;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


final class Configurations {

    static class CommandsLocales implements CommandsCfg {

        private final FileConfiguration inFileConfiguration;
        private final FileConfiguration outFileConfiguration;

        private final CommandI18n[] commandsLocalesArray = new CommandI18n[Enums.Commands.values().length];

        protected CommandsLocales() {
            this.inFileConfiguration = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFileConfiguration = new YamlConfiguration();

            commandsLocalesArray[Enums.Commands.GLOW.ordinal()] = new CommandI18n(
                    Enums.Commands.GLOW.name(),
                    "glow|brilho",
                    "",
                    " Brilhe como uma brabuleta",
                    "eternia.glow"
            );
            commandsLocalesArray[Enums.Commands.GLOW_COLOR.ordinal()] = new CommandI18n(
                    Enums.Commands.GLOW_COLOR.name(),
                    "color|cor",
                    " <cor>",
                    " Escolha a cor da sua brabuleta",
                    "eternia.glow"
            );

            syncToFile();
            saveConfiguration(true);
        }

        @Override
        public CommandI18n[] getCommandsLocalesArray() {
            return commandsLocalesArray;
        }

        @Override
        public FileConfiguration inFileConfiguration() {
            return inFileConfiguration;
        }

        @Override
        public FileConfiguration outFileConfiguration() {
            return outFileConfiguration;
        }

        @Override
        public String getFolderPath() {
            return Constants.SPAWNER_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.SPAWNER_COMMAND_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }
    }

    static class Configs implements FileCfg {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        protected Configs(final Services.Glow servicesGlow) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            // Color constants
            servicesGlow.setColor(Enums.Color.AQUA, inFile.getString("constant.aqua", "Celeste"));
            servicesGlow.setColor(Enums.Color.BLACK, inFile.getString("constant.black", "Preto"));
            servicesGlow.setColor(Enums.Color.BLUE, inFile.getString("constant.blue", "Azul"));
            servicesGlow.setColor(Enums.Color.DARK_AQUA, inFile.getString("constant.dark-aqua", "Ciano"));
            servicesGlow.setColor(Enums.Color.DARK_BLUE, inFile.getString("constant.dark-blue", "Azul Escuro"));
            servicesGlow.setColor(Enums.Color.DARK_GRAY, inFile.getString("constant.dark-gray", "Cinza Escuro"));
            servicesGlow.setColor(Enums.Color.DARK_GREEN, inFile.getString("constant.dark-green", "Verde Escuro"));
            servicesGlow.setColor(Enums.Color.DARK_PURPLE, inFile.getString("constant.dark-purple", "Roxo"));
            servicesGlow.setColor(Enums.Color.DARK_RED, inFile.getString("constant.dark-red", "Vermelho Escuro"));
            servicesGlow.setColor(Enums.Color.GOLD, inFile.getString("constant.gold", "Laranja"));
            servicesGlow.setColor(Enums.Color.GRAY, inFile.getString("constant.gray", "Cinza"));
            servicesGlow.setColor(Enums.Color.GREEN, inFile.getString("constant.green", "Verde"));
            servicesGlow.setColor(Enums.Color.LIGHT_PURPLE, inFile.getString("constant.light-purple", "Rosa"));
            servicesGlow.setColor(Enums.Color.RED, inFile.getString("constant.red", "Vermelho"));
            servicesGlow.setColor(Enums.Color.WHITE, inFile.getString("constant.white", "Branco"));
            servicesGlow.setColor(Enums.Color.YELLOW, inFile.getString("constant.yellow", "Amarelo"));

            // Color constants
            outFile.set("constant.aqua", servicesGlow.getColor(Enums.Color.AQUA));
            outFile.set("constant.black", servicesGlow.getColor(Enums.Color.BLACK));
            outFile.set("constant.blue", servicesGlow.getColor(Enums.Color.BLUE));
            outFile.set("constant.dark-aqua", servicesGlow.getColor(Enums.Color.DARK_AQUA));
            outFile.set("constant.dark-blue", servicesGlow.getColor(Enums.Color.DARK_BLUE));
            outFile.set("constant.dark-gray", servicesGlow.getColor(Enums.Color.DARK_GRAY));
            outFile.set("constant.dark-green", servicesGlow.getColor(Enums.Color.DARK_GREEN));
            outFile.set("constant.dark-purple", servicesGlow.getColor(Enums.Color.DARK_PURPLE));
            outFile.set("constant.dark-red", servicesGlow.getColor(Enums.Color.DARK_RED));
            outFile.set("constant.gold", servicesGlow.getColor(Enums.Color.GOLD));
            outFile.set("constant.gray", servicesGlow.getColor(Enums.Color.GRAY));
            outFile.set("constant.green", servicesGlow.getColor(Enums.Color.GREEN));
            outFile.set("constant.light-purple", servicesGlow.getColor(Enums.Color.LIGHT_PURPLE));
            outFile.set("constant.red", servicesGlow.getColor(Enums.Color.RED));
            outFile.set("constant.white", servicesGlow.getColor(Enums.Color.WHITE));
            outFile.set("constant.yellow", servicesGlow.getColor(Enums.Color.YELLOW));

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
            return Constants.SPAWNER_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.SPAWNER_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }

    }

    static class Locales implements FileCfg {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final String[] messages;

        protected Locales(final EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            this.messages = plugin.messages();

            addMessage(Messages.GLOW_ENABLED,
                    "Brilho de brabuleta ativado<color:#555555>!",
                    ""
            );
            addMessage(Messages.GLOW_DISABLED,
                    "Brilho de brabuleta desativado<color:#555555>.",
                    ""
            );
            addMessage(Messages.GLOW_COLOR_CHANGED,
                    "Cor do brilhinho alterada para <color:#00aaaa>{0}<color:#555555>.",
                    "0: nome da cor"
            );
            addMessage(Messages.GLOW_INVALID_COLOR,
                    "Cor inválida<color:#555555>.",
                    ""
            );

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
            return Constants.SPAWNER_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.SPAWNER_MESSAGE_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return messages;
        }
    }

}
