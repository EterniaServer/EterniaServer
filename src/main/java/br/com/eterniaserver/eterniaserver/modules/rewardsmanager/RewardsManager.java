package br.com.eterniaserver.eterniaserver.modules.rewardsmanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.rewardsmanager.commands.RewardsSystem;

import co.aikar.commands.PaperCommandManager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class RewardsManager {

    private final EterniaServer plugin;
    private final Messages messages;

    public RewardsManager(EterniaServer plugin, Messages messages, PaperCommandManager manager) {
        this.plugin = plugin;
        this.messages = messages;
        if (plugin.serverConfig.getBoolean("modules.rewards")) {
            File rwConfig = new File(plugin.getDataFolder(), "rewards.yml");
            if (!rwConfig.exists()) plugin.saveResource("rewards.yml", false);

            plugin.rewardsConfig = new YamlConfiguration();

            try {
                plugin.rewardsConfig.load(rwConfig);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            manager.registerCommand(new RewardsSystem(this, messages, plugin));
            messages.sendConsole("modules.enable", "%module%", "Rewards");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Rewards");
        }
    }


    public void createKey(final String grupo, String key) {
        final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-rewards") + " (code, lalalala) VALUES('" + key + "', '" + grupo + "');";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement createKey = connection.prepareStatement(querie);
            createKey.execute();
            createKey.close();
        }, true);
    }

    public void deleteKey(final String key) {
        final String querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-rewards") + " WHERE code='" + key + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement delKey = connection.prepareStatement(querie);
            delKey.execute();
            delKey.close();
        }, true);
    }

    public String existKey(final String key) {
        AtomicReference<String> grupo = new AtomicReference<>("no");
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-rewards")+ " WHERE code='" + key + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement getKey = connection.prepareStatement(querie);
            ResultSet resultSet = getKey.executeQuery();
            if (resultSet.next() && resultSet.getString("code") != null) {
                grupo.set(resultSet.getString("lalalala"));
            }
            resultSet.close();
            getKey.close();
        });
        return grupo.get();
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
