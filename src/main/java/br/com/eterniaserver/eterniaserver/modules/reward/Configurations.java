package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.CommandsCfg;
import br.com.eterniaserver.eterniaserver.api.interfaces.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.objects.CommandI18n;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Configurations {

    static class CommandsLocales implements CommandsCfg {

        private final FileConfiguration inFileConfiguration;
        private final FileConfiguration outFileConfiguration;

        private final CommandI18n[] commandsLocalesArray = new CommandI18n[Enums.Commands.values().length];

        protected CommandsLocales() {
            this.inFileConfiguration = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFileConfiguration = new YamlConfiguration();

            commandsLocalesArray[Enums.Commands.USE_KEY.ordinal()] = new CommandI18n(
                    Enums.Commands.USE_KEY.name(),
                    "usekey|usarchave",
                    " <chave>",
                    " Utilize uma chave de Reward",
                    "eternia.usekey"
            );
            commandsLocalesArray[Enums.Commands.GEN_KEY.ordinal()] = new CommandI18n(
                    Enums.Commands.GEN_KEY.name(),
                    "genkey|gerarchave",
                    " <reward>",
                    " Gere uma chave para um Reward",
                    "eternia.genkey"
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
            return Constants.REWARDS_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.REWARDS_COMMAND_FILE_PATH;
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

            final List<Map<String, Map<Double, List<String>>>> chanceMap = plugin.chanceMaps();

            // Maps
            chanceMap.set(ChanceMaps.REWARDS.ordinal(), getChanceMap("rewards", "vip"));
            chanceMap.set(ChanceMaps.FARM_DROPS.ordinal(), getChanceMap("farms", "CARROTS"));
            chanceMap.set(ChanceMaps.BLOCK_DROPS.ordinal(), getChanceMap("blocks", "STONE"));

            // Maps
            chanceMap.get(ChanceMaps.REWARDS.ordinal()).forEach((k, v) -> v.forEach((l, b) -> outFile.set("rewards." + k + "." + String.format("%.10f", l).replace('.', ','), b)));
            chanceMap.get(ChanceMaps.FARM_DROPS.ordinal()).forEach((k, v) -> v.forEach((l, b) -> outFile.set("farm." + k + "." + String.format("%.10f", l).replace('.', ','), b)));
            chanceMap.get(ChanceMaps.BLOCK_DROPS.ordinal()).forEach((k, v) -> v.forEach((l, b) -> outFile.set("blocks." + k + "." + String.format("%.10f", l).replace('.', ','), b)));

            saveConfiguration(true);
        }

        private Map<String, Map<Double, List<String>>> getChanceMap(String path, String defaultEntry) {
            final Map<String, Map<Double, List<String>>> rewardsMap = new HashMap<>();
            final ConfigurationSection rewards = inFile.getConfigurationSection(path);

            if (rewards != null) {
                for (final String key : rewards.getKeys(false)) {
                    final Map<Double, List<String>> keyChanceMap = new HashMap<>();
                    final ConfigurationSection section = rewards.getConfigurationSection(path + "." + key);

                    if (section == null) continue;

                    for (String chance : section.getKeys(false)) {
                        keyChanceMap.put(Double.parseDouble(chance.replace(',', '.')), rewards.getStringList(path + "." + key + "." + chance));
                    }
                    rewardsMap.put(key, keyChanceMap);
                }
            }
            if (rewardsMap.isEmpty()) {
                rewardsMap.put(defaultEntry, Map.of(1.0, List.of("give yurinogueira stone 1")));
            }

            return rewardsMap;
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

            addMessage(Messages.REWARD_INVALID_KEY,
                    "A chave <color:#00aaaa>{0}<color:#aaaaaa> é inválida<color:#555555>.",
                    "0: chave"
            );
            addMessage(Messages.REWARD_CREATED,
                    "Reward criado com sucesso<color:#555555>, <color:#aaaaaa>chave<color:#555555>: <color:#00aaaa>{0}<color:#555555>.",
                    "0: chave"
            );
            addMessage(Messages.REWARD_INVALID_KEY,
                    "Não foi encontrado nenhum reward com o nome de <color:#00aaaa>{0}<color:#555555>.",
                    "0: reward"
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
            return Constants.REWARDS_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.REWARDS_MESSAGE_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return messages;
        }
    }

}
