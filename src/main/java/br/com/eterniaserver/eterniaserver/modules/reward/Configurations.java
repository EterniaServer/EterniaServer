package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
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

    static class RewardConfiguration implements ReloadableConfiguration {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final CommandLocale[] commandsLocalesArray;

        private final String[] messages;

        private final String[] strings;
        private final List<Map<String, Map<Double, List<String>>>> chanceMap;


        protected RewardConfiguration(EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.commandsLocalesArray = new CommandLocale[Enums.Commands.values().length];
            this.messages = plugin.messages();
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
            // Locales
            addMessage(Messages.REWARD_INVALID_KEY,
                    "A chave <color:#00aaaa>{0}<color:#aaaaaa> é inválida<color:#555555>.",
                    "chave"
            );
            addMessage(Messages.REWARD_CREATED,
                    "Reward criado com sucesso<color:#555555>, <color:#aaaaaa>chave<color:#555555>: <color:#00aaaa>{0}<color:#555555>.",
                    "chave"
            );
            addMessage(Messages.REWARD_INVALID_KEY,
                    "Não foi encontrado nenhum reward com o nome de <color:#00aaaa>{0}<color:#555555>.",
                    "reward"
            );

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

}
