package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.*;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class SpawnerMessagesConfiguration implements ReloadableConfiguration {

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        protected SpawnerMessagesConfiguration(EterniaServer plugin) {
            this.plugin = plugin;
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
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
            return Constants.SPAWNER_MESSAGES_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return plugin.messages();
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return new CommandLocale[0];
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            addMessage(Messages.SPAWNER_INV_FULL,
                    "O inventário do jogador está cheio<color:#555555>."
            );
            addMessage(Messages.SPAWNER_RECEIVED,
                    "Você recebeu <color:#00aaaa>{3}<color:#aaaaaa> spawner de <color:#00aaaa>{0}<color:#aaaaaa> por <color:#00aaaa>{2}<color:#555555>.",
                    "mob",
                    "nome do jogador",
                    "apelido do jogador",
                    "quantia"
            );
            addMessage(Messages.SPAWNER_SENT,
                    "Você enviou <color:#00aaaa>{3}<color:#aaaaaa> spawner de <color:#00aaaa>{0}<color:#aaaaaa> para <color:#00aaaa>{2}<color:#555555>.",
                    "mob",
                    "nome do jogador",
                    "apelido do jogador",
                    "quantia"
            );
            addMessage(Messages.SPAWNER_SEND_TYPES,
                    "Os tipos de spawners válidos são<color:#555555>: <color:#00aaaa>{0}<color:#555555>.",
                    "tipos"
            );
            addMessage(Messages.SPAWNER_CANT_CHANGE_NAME,
                    "Você não pode renomear spawners<color:#555555>."
            );
            addMessage(Messages.SPAWNER_WORLD_BLOCKED,
                    "Você não pode quebrar spawners nesse mundo<color:#555555>."
            );
            addMessage(Messages.SPAWNER_DROP_FAILED,
                    "Não foi dessa vez<color:#555555>."
            );
            addMessage(Messages.SPAWNER_SILK_REQUESTED,
                    "Você precisa de uma picareta com toque suave para isso<color:#555555>."
            );
            addMessage(Messages.SPAWNER_WITHOUT_PERM,
                    "Você não possui permissão para isso<color:#555555>."
            );
        }

        @Override
        public void executeCritical() { }
    }

    static class SpawnerCommandsConfiguration implements ReloadableConfiguration {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final CommandLocale[] commandsLocalesArray;

        protected SpawnerCommandsConfiguration() {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.commandsLocalesArray = new CommandLocale[Enums.Commands.values().length];
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
            return Constants.SPAWNER_COMMANDS_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return new String[0];
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return commandsLocalesArray;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.BLOCKED;
        }

        @Override
        public void executeConfig() { }

        @Override
        public void executeCritical() {
            addCommandLocale(Enums.Commands.SPAWNER_GIVE, new CommandLocale(
                    "spawnergive|darspawner",
                    " <mob> <quantia> <jogador>",
                    " Dá uma quantia de spawners para um jogador",
                    "eternia.spawnergive",
                    null
            ));
        }
    }

    static class SpawnerConfiguration implements ReloadableConfiguration {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final boolean[] booleans;
        private final double[] doubles;
        private final String[] strings;

        private final List<List<String>> stringLists;

        protected SpawnerConfiguration(EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            this.booleans = plugin.booleans();
            this.doubles = plugin.doubles();
            this.strings = plugin.strings();
            this.stringLists = plugin.stringLists();

            NamespacedKey[] namespacedKeys = plugin.namespacedKeys();

            namespacedKeys[ItemsKeys.TAG_SPAWNER.ordinal()] = new NamespacedKey(
                    plugin, Constants.TAG_SPAWNER
            );
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
            return new String[0];
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return new CommandLocale[0];
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            // Booleans
            booleans[Booleans.INV_DROP.ordinal()] = inFile.getBoolean("configs.drop-in-inventory", true);
            booleans[Booleans.BLOCK_BREAK_SPAWNERS.ordinal()] = inFile.getBoolean("configs.block-break-if-no-perm", true);
            booleans[Booleans.PREVENT_ANVIL.ordinal()] = inFile.getBoolean("configs.prevent-anvil", true);
            // Doubles
            doubles[Doubles.DROP_CHANCE.ordinal()] = inFile.getDouble("configs.drop-chance", 1.0);
            // Strings
            strings[Strings.MINI_MESSAGES_ENTITIES_DIVIDER.ordinal()] = inFile.getString("mini-messages.entities-divider", "<color:#555555>, <color:#00aaaa>");
            strings[Strings.MINI_MESSAGES_SPAWNERS_FORMAT.ordinal()] = inFile.getString("mini-messages.spawners-format", "<color:#555555>[<color:#926CEB>%spawner_name% <color:#aaaaaa>Spawner<color:#555555>]");
            strings[Strings.PERM_SPAWNERS_BREAK.ordinal()] = inFile.getString("perm.spawner-break.silk", "eternia.spawners.break");
            strings[Strings.PERM_SPAWNERS_NO_SILK.ordinal()] = inFile.getString("perm.spawner-break.no-silk", "eternia.spawners.nosilk");
            strings[Strings.PERM_SPAWNERS_CHANGE.ordinal()] = inFile.getString("perm.spawner-change", "eternia.change-spawner");
            // Lists
            List<String> list = inFile.getStringList("configs.blacklisted-worlds");
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
            outFile.set("mini-messages.spawners-format", strings[Strings.MINI_MESSAGES_SPAWNERS_FORMAT.ordinal()]);
            // Lists
            outFile.set("configs.blacklisted-worlds", stringLists.get(Lists.BLACKLISTED_WORLDS_SPAWNERS.ordinal()));
        }

        @Override
        public void executeCritical() { }
    }

}
