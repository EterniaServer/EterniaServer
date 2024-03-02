package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eternialib.chat.MessageMap;
import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.configuration.interfaces.CmdConfiguration;
import br.com.eterniaserver.eternialib.configuration.interfaces.MsgConfiguration;
import br.com.eterniaserver.eternialib.configuration.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class RewardMessagesConfiguration implements MsgConfiguration<Messages> {

        private final FileConfiguration inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        private final FileConfiguration outFile = new YamlConfiguration();

        private final MessageMap<Messages, String> messageMap;

        public RewardMessagesConfiguration(MessageMap<Messages, String> messageMap) {
            this.messageMap = messageMap;
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
            return Constants.REWARDS_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.REWARDS_MESSAGES_FILE_PATH;
        }

        @Override
        public MessageMap<Messages, String> messages() {
            return messageMap;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            addMessage(Messages.REWARD_INVALID_KEY,
                    "A chave #00aaaa{0}#aaaaaa é inválida#555555.",
                    "chave"
            );
            addMessage(Messages.REWARD_CREATED,
                    "Reward criado com sucesso#555555, #aaaaaachave#555555: #00aaaa{0}#555555.",
                    "chave"
            );
            addMessage(Messages.REWARD_INVALID_KEY,
                    "Não foi encontrado nenhum reward com o nome de #00aaaa{0}#555555.",
                    "reward"
            );
        }

        @Override
        public void executeCritical() { }
    }

    static class RewardCommandsConfiguration implements CmdConfiguration<Enums.Commands> {

        private final FileConfiguration inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        private final FileConfiguration outFile = new YamlConfiguration();

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
            return Constants.REWARDS_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.REWARDS_COMMANDS_FILE_PATH;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.BLOCKED;
        }

        @Override
        public void executeConfig() { }

        @Override
        public void executeCritical() {
            addCommandLocale(Enums.Commands.USE_KEY, new CommandLocale(
                    "usekey|usarchave",
                    " <chave>",
                    " Utilize uma chave de Reward",
                    "eternia.usekey",
                    null
            ));
            addCommandLocale(Enums.Commands.GEN_KEY, new CommandLocale(
                    "genkey|gerarchave",
                    " <reward>",
                    " Gere uma chave para um Reward",
                    "eternia.genkey",
                    null
            ));
        }
    }

    static class RewardConfiguration implements ReloadableConfiguration {

        private final FileConfiguration inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        private final FileConfiguration outFile = new YamlConfiguration();

        private final String[] strings;
        private final List<Map<String, Map<Double, List<String>>>> chanceMap;

        protected RewardConfiguration(EterniaServer plugin) {
            this.strings = plugin.strings();
            this.chanceMap = plugin.chanceMaps();
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
            return Constants.REWARDS_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.REWARDS_CONFIG_FILE_PATH;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            // Strings
            strings[Strings.REWARD_TABLE_NAME.ordinal()] = inFile.getString("table-name.reward", "e_revision");

            // Maps
            chanceMap.set(ChanceMaps.REWARDS.ordinal(), getChanceMap("rewards", "vip"));
            chanceMap.set(ChanceMaps.FARM_DROPS.ordinal(), getChanceMap("farms", "CARROTS"));
            chanceMap.set(ChanceMaps.BLOCK_DROPS.ordinal(), getChanceMap("blocks", "STONE"));

            // Strings
            outFile.set("table-name.reward", strings[Strings.REWARD_TABLE_NAME.ordinal()]);

            // Maps
            chanceMap.get(ChanceMaps.REWARDS.ordinal()).forEach((k, v) -> v.forEach((l, b) -> outFile.set("rewards." + k + "." + String.format("%.10f", l).replace('.', ','), b)));
            chanceMap.get(ChanceMaps.FARM_DROPS.ordinal()).forEach((k, v) -> v.forEach((l, b) -> outFile.set("farms." + k + "." + String.format("%.10f", l).replace('.', ','), b)));
            chanceMap.get(ChanceMaps.BLOCK_DROPS.ordinal()).forEach((k, v) -> v.forEach((l, b) -> outFile.set("blocks." + k + "." + String.format("%.10f", l).replace('.', ','), b)));
        }

        private Map<String, Map<Double, List<String>>> getChanceMap(String path, String defaultEntry) {
            Map<String, Map<Double, List<String>>> rewardsMap = new HashMap<>();
            ConfigurationSection rewards = inFile.getConfigurationSection(path);

            if (rewards != null) {
                for (String key : rewards.getKeys(false)) {
                    Map<Double, List<String>> keyChanceMap = new HashMap<>();
                    ConfigurationSection section = inFile.getConfigurationSection(path + "." + key);

                    if (section == null) {
                        continue;
                    }

                    for (String chance : section.getKeys(false)) {
                        keyChanceMap.put(Double.parseDouble(chance.replace(',', '.')), inFile.getStringList(path + "." + key + "." + chance));
                    }
                    rewardsMap.put(key, keyChanceMap);
                }
            }
            if (rewardsMap.isEmpty()) {
                rewardsMap.put(defaultEntry, Map.of(1.0, List.of("give %player_name% stone 1")));
            }

            return rewardsMap;
        }

        @Override
        public void executeCritical() { }
    }

}
