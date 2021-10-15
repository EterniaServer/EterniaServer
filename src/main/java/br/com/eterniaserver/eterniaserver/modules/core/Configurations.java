package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.CommandsCfg;
import br.com.eterniaserver.eterniaserver.api.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
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
            final String[] strings = plugin.strings();

            // Booleans
            booleans[Booleans.MODULE_SPAWNERS.ordinal()] = inFile.getBoolean("modules.spawners", true);
            booleans[Booleans.MODULE_EXPERIENCE.ordinal()] = inFile.getBoolean("modules.experience", true);
            booleans[Booleans.MODULE_ELEVATOR.ordinal()] = inFile.getBoolean("modules.elevator", true);
            booleans[Booleans.MODULE_REWARDS.ordinal()] = inFile.getBoolean("modules.rewards", true);
            booleans[Booleans.MODULE_GLOW.ordinal()] = inFile.getBoolean("modules.glow", true);
            // Strings
            strings[Strings.MINI_MESSAGES_SERVER_SERVER_LIST.ordinal()] = inFile.getString("mini-messages.motd", "             <color:#69CEDB>⛏ <gradient:#111111:#112222>❱───❰</gradient> <gradient:#6FE657:#6892F2>EterniaServer</gradient> <gradient:#112222:#111111>❱───❰</gradient> <color:#69CEDB>⛏\n                      <gradient:#926CEB:#6892F2>MOUNTAIN UPDATE</gradient>");

            // Booleans
            outFile.set("modules.spawners", booleans[Booleans.MODULE_SPAWNERS.ordinal()]);
            outFile.set("modules.experience", booleans[Booleans.MODULE_EXPERIENCE.ordinal()]);
            outFile.set("modules.elevator", booleans[Booleans.MODULE_ELEVATOR.ordinal()]);
            outFile.set("modules.rewards", booleans[Booleans.MODULE_REWARDS.ordinal()]);
            outFile.set("modules.glow", booleans[Booleans.MODULE_GLOW.ordinal()]);
            // Strings
            outFile.set("mini-messages.motd", strings[Strings.MINI_MESSAGES_SERVER_SERVER_LIST.ordinal()]);

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
