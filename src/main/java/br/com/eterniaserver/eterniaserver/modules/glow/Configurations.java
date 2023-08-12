package br.com.eterniaserver.eterniaserver.modules.glow;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class GlowConfiguration implements ReloadableConfiguration {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final CommandLocale[] commandsLocalesArray;

        private final String[] messages;
        private final Services.Glow servicesGlow;

        protected GlowConfiguration(EterniaServer plugin, Services.Glow servicesGlow) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.commandsLocalesArray = new CommandLocale[Enums.Commands.values().length];
            this.messages = plugin.messages();
            this.servicesGlow = servicesGlow;
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
            return Constants.GLOW_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.GLOW_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return messages;
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return commandsLocalesArray;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            // Locale
            addMessage(Messages.GLOW_ENABLED,
                    "Brilho de brabuleta ativado<color:#555555>!"
            );
            addMessage(Messages.GLOW_DISABLED,
                    "Brilho de brabuleta desativado<color:#555555>."
            );
            addMessage(Messages.GLOW_COLOR_CHANGED,
                    "Cor do brilhinho alterada para <color:#00aaaa>{0}<color:#555555>.",
                    "nome da cor"
            );
            addMessage(Messages.GLOW_INVALID_COLOR,
                    "Cor inválida<color:#555555>."
            );

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
        }

        @Override
        public void executeCritical() {
            addCommandLocale(Enums.Commands.GLOW, new CommandLocale(
                    "glow|brilho",
                    "",
                    " Brilhe como uma brabuleta",
                    "eternia.glow",
                    null
            ));
            addCommandLocale(Enums.Commands.GLOW_COLOR, new CommandLocale(
                    "color|cor",
                    " <cor>",
                    " Escolha a cor da sua brabuleta",
                    "eternia.glow",
                    null
            ));
        }
    }

}
