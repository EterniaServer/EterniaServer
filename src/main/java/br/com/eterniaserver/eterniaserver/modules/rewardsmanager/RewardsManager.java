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
import java.util.Random;

public class RewardsManager {

    private final EterniaServer plugin;
    private final String tableName = "sql.table-rewards";

    public final Random random = new Random();
    public final byte[] bytes = new byte[20];

    public RewardsManager(EterniaServer plugin) {
        this.plugin = plugin;
        EFiles messages = plugin.getEFiles();

        if (plugin.serverConfig.getBoolean("modules.rewards")) {
            File rwConfig = new File(plugin.getDataFolder(), "rewards.yml");
            if (!rwConfig.exists()) plugin.saveResource("rewards.yml", false);

            plugin.rewardsConfig = new YamlConfiguration();

            try {
                plugin.rewardsConfig.load(rwConfig);
            } catch (IOException | InvalidConfigurationException ignored) { }
            plugin.getManager().registerCommand(new RewardsSystem(this, plugin));
            messages.sendConsole("modules.enable", "%module%", "Rewards");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Rewards");
        }
    }


    public void createKey(final String grupo, String key) {
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString(tableName) + " (code, lalalala) VALUES('" + key + "', '" + grupo + "');");
    }

    public void deleteKey(final String key) {
        EQueries.executeQuery("DELETE FROM " + plugin.serverConfig.getString(tableName) + " WHERE code='" + key + "';");
    }

    public String existKey(final String key) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString(tableName)+ " WHERE code='" + key + "';";
        return EQueries.queryString(querie, "code", "lalalala");
    }

    public void giveReward(String group, Player player) {
        ConfigurationSection cs = plugin.rewardsConfig.getConfigurationSection("rewards." + group + ".commands");
        for (String percentage : cs.getKeys(true)) {
            random.nextBytes(bytes);
            if (random.nextDouble() <= Double.parseDouble(percentage)) {
                for (String command : plugin.rewardsConfig.getStringList("rewards." + group + ".commands." + percentage)) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, command));
                }
            }
        }
    }

}
