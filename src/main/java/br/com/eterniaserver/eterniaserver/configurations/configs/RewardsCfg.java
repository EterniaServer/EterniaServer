package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.Constants;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardsCfg {

    public final Map<String, Map<Double, List<String>>> rewardsMap = new HashMap<>();

    public RewardsCfg() {

        FileConfiguration rewardsConfig = YamlConfiguration.loadConfiguration(new File(Constants.REWARDS_FILE_PATH));
        FileConfiguration outRewards = new YamlConfiguration();

        Map<Double, List<String>> tempRewardsMapP = new HashMap<>();
        tempRewardsMapP.put(1.0, List.of("lp user %player_name% parent removetemp cliente", "lp user %player_name% parent addtemp cliente 30d", "broadcast &3%player_name% &7virou um cliente pelos próximos &330 &7dias&8."));
        tempRewardsMapP.put(0.2, List.of("crates givekey %player_name% farmraros"));
        this.rewardsMap.put("cliente", tempRewardsMapP);

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
            tempRewardsMap = new HashMap<>(this.rewardsMap);
        }

        this.rewardsMap.clear();
        tempRewardsMap.forEach(this.rewardsMap::put);

        this.rewardsMap.forEach((k, v) -> v.forEach((l, b) -> outRewards.set("rewards." + k + "." + String.format("%.10f", l).replace('.', ','), b)));
        outRewards.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        try {
            outRewards.save(Constants.REWARDS_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }

    }

}
