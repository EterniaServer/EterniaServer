package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.CustomKit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitsCfg implements ReloadableConfiguration {

    private final EterniaServer plugin;

    public KitsCfg(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public void executeConfig() {
        plugin.kitList.clear();
        FileConfiguration kits = YamlConfiguration.loadConfiguration(new File(Constants.KITS_FILE_PATH));
        FileConfiguration outKit = new YamlConfiguration();
        plugin.kitList.put("pa", new CustomKit(300, List.of("give %player_name% minecraft:golden_shovel 1"), List.of("$8[$aE$9S$8] $7Toma sua pá$8!".replace('$', (char) 0x00A7))));
        Map<String, CustomKit> tempKitList = new HashMap<>();
        ConfigurationSection configurationSection = kits.getConfigurationSection("kits");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                List<String> lista = kits.getStringList("kits." + key + ".text");
                plugin.putColorOnList(lista);
                tempKitList.put(key, new CustomKit(kits.getInt("kits." + key + ".delay"), kits.getStringList("kits." + key + ".command"), lista));
            }
        }
        if (tempKitList.isEmpty()) {
            tempKitList = new HashMap<>(plugin.kitList);
        }
        plugin.kitList.clear();
        tempKitList.forEach(plugin.kitList::put);
        tempKitList.forEach((k, v) -> {
            outKit.set("kits." + k + ".delay", v.getDelay());
            outKit.set("kits." + k + ".command", v.getCommands());
            outKit.set("kits." + k + ".text", v.getMessages());
        });
        outKit.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");
        try {
            outKit.save(Constants.KITS_FILE_PATH);
        } catch (IOException exception) {
            plugin.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }
    }

    @Override
    public void executeCritical() {

    }
}
