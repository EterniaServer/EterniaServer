package br.com.eterniaserver.eterniaserver.modules.rewardsmanager;

import br.com.eterniaserver.eternialib.sql.Queries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.rewardsmanager.commands.RewardsSystem;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class RewardsManager {

    private final EterniaServer plugin;
    private final Messages messages;

    public RewardsManager(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();

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
        Queries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-rewards") + " (code, lalalala) VALUES('" + key + "', '" + grupo + "');");
    }

    public void deleteKey(final String key) {
        Queries.executeQuery("DELETE FROM " + plugin.serverConfig.getString("sql.table-rewards") + " WHERE code='" + key + "';");
    }

    public String existKey(final String key) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-rewards")+ " WHERE code='" + key + "';";
        return Queries.queryString(querie, "code", "lalalala");
    }

    public void giveReward(String group, Player player) {
        ConfigurationSection cs = plugin.rewardsConfig.getConfigurationSection("rewards." + group + ".commands");
        if (cs != null) {
            Random random = new Random();
            for (String percentage : cs.getKeys(true)) {
                if (random.nextDouble() <= Double.parseDouble(percentage)) {
                    for (String command : plugin.rewardsConfig.getStringList("rewards." + group + ".commands." + percentage)) {
                        String modifiedCommand;
                        if (plugin.hasPlaceholderAPI) {
                            modifiedCommand = messages.putPAPI(player, command);
                        } else {
                            modifiedCommand = command.replace("%player_name%", player.getName());
                        }
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                    }
                }
            }
        }
    }

}
