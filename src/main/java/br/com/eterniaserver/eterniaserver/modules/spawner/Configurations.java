package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.CommandsCfg;
import br.com.eterniaserver.eterniaserver.api.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
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

            commandsLocalesArray[Enums.Commands.SPAWNER_GIVE.ordinal()] = new CommandI18n(
                    Enums.Commands.SPAWNER_GIVE.name(),
                    "spawnergive|darspawner",
                    " <mob> <quantia> <jogador>",
                    " Dá uma quantia de spawners para um jogador",
                    "eternia.spawnergive"
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

        protected Configs(final EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            final boolean[] booleans = plugin.booleans();
            final double[] doubles = plugin.doubles();
            final String[] strings = plugin.strings();
            final List<List<String>> stringLists = plugin.stringLists();

            // Booleans
            booleans[Booleans.INV_DROP.ordinal()] = inFile.getBoolean("configs.drop-in-inventory", true);
            booleans[Booleans.BLOCK_BREAK_SPAWNERS.ordinal()] = inFile.getBoolean("configs.block-break-if-no-perm", true);
            booleans[Booleans.PREVENT_ANVIL.ordinal()] = inFile.getBoolean("configs.prevent-anvil", true);
            // Doubles
            doubles[Doubles.DROP_CHANCE.ordinal()] = inFile.getDouble("configs.drop-chance", 1.0);
            // Strings
            strings[Strings.MINI_MESSAGES_ENTITIES_DIVIDER.ordinal()] = inFile.getString("mini-messages.entities-divider", "<color:#555555>, <color:#00aaaa>");
            strings[Strings.PERM_SPAWNERS_BREAK.ordinal()] = inFile.getString("perm.spawner-break.silk", "eternia.spawners.break");
            strings[Strings.PERM_SPAWNERS_NO_SILK.ordinal()] = inFile.getString("perm.spawner-break.no-silk", "eternia.spawners.nosilk");
            strings[Strings.PERM_SPAWNERS_CHANGE.ordinal()] = inFile.getString("perm.spawner-change", "eternia.change-spawner");
            // Lists
            final List<String> list = inFile.getStringList("configs.blacklisted-worlds");
            stringLists.set(Lists.BLACKLISTED_WORLDS_SPAWNERS.ordinal(), list.isEmpty() ? List.of("world_evento") : list);

            // Booleans
            outFile.set("configs.drop-in-inventory", booleans[Booleans.INV_DROP.ordinal()]);
            outFile.set("configs.block-break-if-no-perm", booleans[Booleans.BLOCK_BREAK_SPAWNERS.ordinal()]);
            outFile.set("configs.prevent-anvil", booleans[Booleans.PREVENT_ANVIL.ordinal()]);
            // Doubles
            outFile.set("configs.drop-chance", doubles[Doubles.DROP_CHANCE.ordinal()]);
            // Strings
            outFile.set("mini-messages.entities-divider", strings[Strings.MINI_MESSAGES_ENTITIES_DIVIDER.ordinal()]);
            outFile.set("perm.spawner-break.silk", strings[Strings.PERM_SPAWNERS_BREAK.ordinal()]);
            outFile.set("perm.spawner-break.no-silk", strings[Strings.PERM_SPAWNERS_NO_SILK.ordinal()]);
            outFile.set("perm.spawner-change", strings[Strings.PERM_SPAWNERS_CHANGE.ordinal()]);
            // Lists
            outFile.set("configs.blacklisted-worlds", plugin.stringLists.get(Lists.BLACKLISTED_WORLDS_SPAWNERS.ordinal()));

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

            addMessage(Messages.SPAWNER_INV_FULL,
                    "O inventário do jogador está cheio<color:#555555>.",
                    ""
            );
            addMessage(Messages.SPAWNER_RECEIVED,
                    "Você recebeu <color:#00aaaa>{3}<color:#aaaaaa> spawner de <color:#00aaaa>{0}<color:#aaaaaa> por <color:#00aaaa>{2}<color:#555555>.",
                    "0: mob; 1: nome do jogador; 2: apelido do jogador; 3: quantia"
            );
            addMessage(Messages.SPAWNER_SENT,
                    "Você enviou <color:#00aaaa>{3}<color:#aaaaaa> spawner de <color:#00aaaa>{0}<color:#aaaaaa> para <color:#00aaaa>{2}<color:#555555>.",
                    "0: mob; 1: nome do jogador; 2: apelido do jogador; 3: quantia"
            );
            addMessage(Messages.SPAWNER_SEND_TYPES,
                    "Os tipos de spawners válidos são<color:#555555>: <color:#00aaaa>{0}<color:#555555>.",
                    "0: tipos"
            );
            addMessage(Messages.SPAWNER_CANT_CHANGE_NAME,
                    "Você não pode renomear spawners<color:#555555>.",
                    ""
            );
            addMessage(Messages.SPAWNER_WORLD_BLOCKED,
                    "Você não pode quebrar spawners nesse mundo<color:#555555>.",
                    ""
            );
            addMessage(Messages.SPAWNER_DROP_FAILED,
                    "Não foi dessa vez<color:#555555>.",
                    ""
            );
            addMessage(Messages.SPAWNER_SILK_REQUESTED,
                    "Você precisa de uma picareta com toque suave para isso<color:#555555>.",
                    ""
            );
            addMessage(Messages.SPAWNER_WITHOUT_PERM,
                    "Você não possui permissão para isso<color:#555555>.",
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
