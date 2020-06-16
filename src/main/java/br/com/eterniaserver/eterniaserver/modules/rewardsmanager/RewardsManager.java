package br.com.eterniaserver.eterniaserver.modules.rewardsmanager;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.rewardsmanager.commands.RewardsSystem;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class RewardsManager {

    private final EterniaServer plugin;

    public RewardsManager(EterniaServer plugin) {
        this.plugin = plugin;
        EFiles messages = plugin.getEFiles();

        if (plugin.serverConfig.getBoolean("modules.rewards")) {
            File rwConfig = new File(plugin.getDataFolder(), "rewards.yml");
            if (!rwConfig.exists()) plugin.saveResource("rewards.yml", false);

            plugin.rewardsConfig = new YamlConfiguration();

            try {
                plugin.rewardsConfig.load(rwConfig);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            plugin.getManager().registerCommand(new RewardsSystem(this, plugin));
            messages.sendConsole("modules.enable", "%module%", "Rewards");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Rewards");
        }
    }


    public void createKey(final String grupo, String key) {
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-rewards") + " (code, lalalala) VALUES('" + key + "', '" + grupo + "');");
    }

    public void deleteKey(final String key) {
        EQueries.executeQuery("DELETE FROM " + plugin.serverConfig.getString("sql.table-rewards") + " WHERE code='" + key + "';");
    }

    public String existKey(final String key) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-rewards")+ " WHERE code='" + key + "';";
        return EQueries.queryString(querie, "code", "lalalala");
    }

    public void giveReward(String group, Player player) {
        ConfigurationSection cs = plugin.rewardsConfig.getConfigurationSection("rewards." + group + ".commands");
        if (cs != null) {
            for (String percentage : cs.getKeys(true)) {
                if (Math.random() <= Double.parseDouble(percentage)) {
                    for (String command : plugin.rewardsConfig.getStringList("rewards." + group + ".commands." + percentage)) {
                        String modifiedCommand;
                        if (plugin.hasPlaceholderAPI) {
                            modifiedCommand = putPAPI(player, command);
                        } else {
                            modifiedCommand = command.replace("%player_name%", player.getName());
                        }
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                    }
                }
            }
        }
    }

    private String putPAPI(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

}
