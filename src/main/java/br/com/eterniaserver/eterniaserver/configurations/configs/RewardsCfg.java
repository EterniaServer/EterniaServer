package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardsCfg implements ReloadableConfiguration {

    private final EterniaServer plugin;

    public RewardsCfg(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public void executeConfig() {
        FileConfiguration rewardsConfig = YamlConfiguration.loadConfiguration(new File(Constants.REWARDS_FILE_PATH));
        FileConfiguration outRewards = new YamlConfiguration();

        Map<Double, List<String>> tempRewardsMapP = new HashMap<>();
        tempRewardsMapP.put(1.0, List.of("lp user %player_name% parent removetemp cliente", "lp user %player_name% parent addtemp cliente 30d", "broadcast &3%player_name% &7virou um cliente pelos próximos &330 &7dias&8."));
        tempRewardsMapP.put(0.2, List.of("crates givekey %player_name% farmraros"));

        Map<String, Map<Double, List<String>>> rewardsMap = new HashMap<>();

        rewardsMap.put("cliente", tempRewardsMapP);

        Map<String, Map<Double, List<String>>> tempRewardsMap = new HashMap<>();

        ConfigurationSection configurationSection = rewardsConfig.getConfigurationSection("rewards");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                Map<Double, List<String>> tempChanceMap = new HashMap<>();
                ConfigurationSection section = rewardsConfig.getConfigurationSection("rewards." + key);
                if (section != null) {
                    for (String chance : section.getKeys(false)) {
                        tempChanceMap.put(Double.parseDouble(chance.replace(',', '.')), rewardsConfig.getStringList("rewards." + key + "." + chance));
                    }
                }
                tempRewardsMap.put(key, tempChanceMap);
            }
        }

        if (tempRewardsMap.isEmpty()) {
            tempRewardsMap = new HashMap<>(rewardsMap);
        }

        rewardsMap.clear();
        tempRewardsMap.forEach(rewardsMap::put);

        rewardsMap.forEach((k, v) -> v.forEach((l, b) -> outRewards.set("rewards." + k + "." + String.format("%.10f", l).replace('.', ','), b)));
        outRewards.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        plugin.chanceMaps.set(ChanceMaps.REWARDS.ordinal(), rewardsMap);

        try {
            outRewards.save(Constants.REWARDS_FILE_PATH);
        } catch (IOException exception) {
            plugin.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }
    }

    @Override
    public void executeCritical() {

    }
}
