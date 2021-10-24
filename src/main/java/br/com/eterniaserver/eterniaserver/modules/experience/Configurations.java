package br.com.eterniaserver.eterniaserver.modules.experience;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.CommandsCfg;
import br.com.eterniaserver.eterniaserver.api.interfaces.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.objects.CommandI18n;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

final class Configurations {

    static class CommandsLocales implements CommandsCfg {

        private final FileConfiguration inFileConfiguration;
        private final FileConfiguration outFileConfiguration;

        private final CommandI18n[] commandsLocalesArray = new CommandI18n[Enums.Commands.values().length];

        protected CommandsLocales() {
            this.inFileConfiguration = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFileConfiguration = new YamlConfiguration();

            commandsLocalesArray[Enums.Commands.EXPERIENCE.ordinal()] = new CommandI18n(
                    Enums.Commands.EXPERIENCE.name(),
                    "xp",
                    " <página>",
                    " Ajuda para o sistema de Experiência",
                    "eternia.xp.user"
            );
            commandsLocalesArray[Enums.Commands.EXPERIENCE_SET.ordinal()] = new CommandI18n(
                    Enums.Commands.EXPERIENCE_SET.name(),
                    "set|definir",
                    " <jogador> <quantia> <xp | level>",
                    " Define o nível de experiência de um jogador",
                    "eternia.xp.admin"
            );
            commandsLocalesArray[Enums.Commands.EXPERIENCE_TAKE.ordinal()] = new CommandI18n(
                    Enums.Commands.EXPERIENCE_TAKE.name(),
                    "take|retirar",
                    " <jogador> <quantia> <xp | level>",
                    " Retira uma quantidade de níveis de experiência de um jogador",
                    "eternia.xp.admin"
            );
            commandsLocalesArray[Enums.Commands.EXPERIENCE_GIVE.ordinal()] = new CommandI18n(
                    Enums.Commands.EXPERIENCE_GIVE.name(),
                    "give|dar",
                    " <jogador> <quantia> <xp | level>",
                    " Dá uma quantidade de níveis de experiência para um jogador",
                    "eternia.xp.admin"
            );
            commandsLocalesArray[Enums.Commands.EXPERIENCE_CHECK.ordinal()] = new CommandI18n(
                    Enums.Commands.EXPERIENCE_CHECK.name(),
                    "check|verificar",
                    "",
                    " Verifica a quantia de níveis guardados",
                    "eternia.xp.user"
            );
            commandsLocalesArray[Enums.Commands.EXPERIENCE_BOTTLE.ordinal()] = new CommandI18n(
                    Enums.Commands.EXPERIENCE_BOTTLE.name(),
                    "bottle|garrafinhas",
                    " <quantia>",
                    " Enganharra sua experiência atual",
                    "eternia.xp.user"
            );
            commandsLocalesArray[Enums.Commands.EXPERIENCE_WITHDRAW.ordinal()] = new CommandI18n(
                    Enums.Commands.EXPERIENCE_WITHDRAW.name(),
                    "withdraw|sacar",
                    " <quantia>",
                    " Saca uma quantia de níveis guardados",
                    "eternia.xp.user"
            );
            commandsLocalesArray[Enums.Commands.EXPERIENCE_DEPOSIT.ordinal()] = new CommandI18n(
                    Enums.Commands.EXPERIENCE_DEPOSIT.name(),
                    "deposit|depositar",
                    " <quantia>",
                    " Deposita uma quantia de níveis",
                    "eternia.xp.user"
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
            return Constants.EXPERIENCE_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.EXPERIENCE_COMMAND_FILE_PATH;
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

            final String[] strings = plugin.strings();
            final List<List<String>> stringLists = plugin.stringLists();

            // Strings
            strings[Strings.MINI_MESSAGES_BOTTLE_EXP_NAME.ordinal()] = inFile.getString("mini-messages.bottle-name", "<color:#aaaaaa>Garrafa com <color:#00aaaa>Experiência");
            strings[Strings.EXP_XP_LABEL.ordinal()] = inFile.getString("label.xp", "xp");
            strings[Strings.EXP_LEVEL_LABEL.ordinal()] = inFile.getString("label.level", "níveis");
            // Lists
            final List<String> list = inFile.getStringList("mini-messages.bottle-lore");
            stringLists.set(Lists.MINI_MESSAGES_BOTTLE_EXP_LORE.ordinal(), list.isEmpty() ? List.of("<color:#aaaaaa>Quantia<color:#555555>: <color:#00aaaa>%amount%") : list);

            // Strings
            outFile.set("mini-messages.bottle-name", strings[Strings.MINI_MESSAGES_BOTTLE_EXP_NAME.ordinal()]);
            outFile.set("label.xp", strings[Strings.EXP_XP_LABEL.ordinal()]);
            outFile.set("label.level", strings[Strings.EXP_LEVEL_LABEL.ordinal()]);
            // Lists
            outFile.set("mini-messages.bottle-lore", plugin.stringLists.get(Lists.MINI_MESSAGES_BOTTLE_EXP_LORE.ordinal()));

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
            return Constants.EXPERIENCE_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.EXPERIENCE_CONFIG_FILE_PATH;
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

            addMessage(Messages.EXP_INVALID_CHOICE,
                    "Argumento inválido<color:#555555>, <color:#aaaaaa>o tipo deve ser <color:#00aaaa>xp <color:#aaaaaa>ou <color:#00aaaa>level<color:#555555>.",
                    ""
            );
            addMessage(Messages.EXP_SET_FROM,
                    "Você definiu para <color:#00aaaa>{0}<color:#aaaaaa> a quantia de <color:#00aaaa>{3}<color:#aaaaaa> de <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador; 3: exp ou nível"
            );
            addMessage(Messages.EXP_SETED,
                    "A sua quantia de <color:#00aaaa>{3}<color:#aaaaaa> foi definida para <color:#00aaaa>{0}<color:#aaaaaa> por <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de exp; 1: nome de quem alterou; 2: apelido de quem alterou; 3: exp ou nível"
            );
            addMessage(Messages.EXP_REMOVE_FROM,
                    "Você removeu <color:#00aaaa>{0} {3}<color:#aaaaaa> de <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador; 3: exp ou nível"
            );
            addMessage(Messages.EXP_REMOVED,
                    "Foi retirado <color:#00aaaa>{0}<color:#aaaaaa> de sua quantia de <color:#00aaaa>{3}<color:#aaaaaa> por <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de exp; 1: nome de quem removeu; 2: apelido de quem alterou; 3: exp ou nível"
            );
            addMessage(Messages.EXP_GIVE_FROM,
                    "Você deu <color:#00aaaa>{0}<color:#aaaaaa> de <color:#00aaaa>{3}<color:#aaaaaa> para <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador; 3: exp ou nível"
            );
            addMessage(Messages.EXP_GIVED,
                    "Foi recebeu <color:#00aaaa>{0} {3}<color:#aaaaaa> por <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de exp; 1: nome de quem removeu; 2: apelido de quem alterou; 3: exp ou nível"
            );
            addMessage(Messages.EXP_BALANCE,
                    "Você possui <color:#00aaaa>{0}<color:#aaaaaa> níveis de experiência guardados<color:#555555>.",
                    "0: quantia de exp"
            );
            addMessage(Messages.EXP_BOTTLED,
                    "Tome sua garrafinha<color:#555555>!",
                    ""
            );
            addMessage(Messages.EXP_INSUFFICIENT,
                    "Você não possui tudo isso de experiência<color:#555555>.",
                    ""
            );
            addMessage(Messages.EXP_WITHDRAW,
                    "Você sacou <color:#00aaaa>{0}<color:#aaaaaa> níveis de experiência<color:#555555>.",
                    "0: quantia de níveis"
            );
            addMessage(Messages.EXP_DEPOSIT,
                    "Você depositou <color:#00aaaa>{0}<color:#aaaaaa> níveis de experiência<color:#555555>.",
                    "0: quantia de níveis"
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
            return Constants.EXPERIENCE_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.EXPERIENCE_MESSAGE_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return messages;
        }

    }

}
