package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.CommandsCfg;
import br.com.eterniaserver.eterniaserver.api.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
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

            commandsLocalesArray[Enums.Commands.GAMEMODE.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE.name(),
                    "gamemode|gm",
                    " <página>",
                    " Ajuda para o sistema de Gamemode",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.GAMEMODE_SURVIVAL.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE_SURVIVAL.name(),
                    "survival|s|0",
                    " (opcional) <jogador>",
                    " Define o modo de jogo seu, ou de outro jogador, para Sobrevivência",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.GAMEMODE_CREATIVE.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE_CREATIVE.name(),
                    "creative|c|1",
                    " (opcional) <jogador>",
                    " Define o modo de jogo seu, ou de outro jogador, para Criativo",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.GAMEMODE_ADVENTURE.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE_ADVENTURE.name(),
                    "adventure|a|2",
                    " (opcional) <jogador>",
                    " Define o modo de jogo seu, ou de outro jogador, para Aventura",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.GAMEMODE_SPECTATOR.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE_SPECTATOR.name(),
                    "spectator|spec|3",
                    " (opcional) <jogador>",
                    " Define o modo de jogo seu, ou de outro jogador, para Espectador",
                    "eternia.gamemode"
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
            return Constants.DATA_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CORE_COMMAND_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }
    }

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

    static class Locales implements FileCfg {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final String[] messages;

        protected Locales(final EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            this.messages = plugin.messages();

            addMessage(Messages.GAMEMODE_SETED,
                    "Seu modo de jogo foi definido para <color:#00aaaa>{0}<color:#555555>.",
                    "0: modo de jogo"
            );
            addMessage(Messages.GAMEMODE_SET_FROM,
                    "O modo de jogo de <color:#00aaaa>{2}<color:#aaaaaa> foi definido para <color:#00aaaa>{0}<color:#555555>.",
                    "0: modo de jogo; 1: nome do jogador; 2: apelido do jogador"
            );
            addMessage(Messages.GAMEMODE_NOT_BY_CONSOLE,
                    "Você precisa informar o nome de um jogador online<color:#555555>.",
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
