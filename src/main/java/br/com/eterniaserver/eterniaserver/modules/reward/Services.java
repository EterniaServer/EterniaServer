package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.CreateTable;
import br.com.eterniaserver.eternialib.core.queries.Delete;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eternialib.core.queries.Select;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


final class Services {

    static class Rewards {

        private final EterniaServer plugin;

        private final List<Map<String, Map<Double, List<String>>>> chanceMap;
        private final Map<String, String> rewards = new HashMap<>();

        protected Rewards(final EterniaServer plugin) {
            this.plugin = plugin;
            this.chanceMap = plugin.chanceMaps();

            final String[] MYSQL_FIELDS = { "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "key_code VARCHAR(16)", "group_name VARCHAR(16)" };
            final String[] SQLITE_FIELDS = { "key_code VARCHAR(16)", "group_name VARCHAR(16)" };

            final CreateTable createTable = new CreateTable(plugin.getString(Strings.TABLE_REWARD));
            createTable.columns.set(EterniaLib.getMySQL() ? MYSQL_FIELDS : SQLITE_FIELDS);
            SQL.execute(createTable);

            try (Connection connection = SQL.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(new Select(plugin.getString(Strings.TABLE_REWARD)).queryString());
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    rewards.put(resultSet.getString("key_code"), resultSet.getString("group_name"));
                }
            } catch (SQLException ignored) {
                Bukkit.getLogger().log(Level.SEVERE, "Can't load rewards database table");
            }
        }

        protected boolean addReward(String key, String reward) {
            if (!chanceMap.get(ChanceMaps.REWARDS.ordinal()).containsKey(reward)) {
                return false;
            }

            rewards.put(key, reward);

            final Insert insert = new Insert(plugin.getString(Strings.TABLE_REWARD));
            insert.columns.set("key_code", "group_name");
            insert.values.set(key, reward);
            SQL.executeAsync(insert);
            return true;
        }

        protected void removeReward(String key) {
            rewards.remove(key);

            final Delete delete = new Delete(plugin.getString(Strings.TABLE_REWARD));
            delete.where.set("key_code", key);
            SQL.executeAsync(delete);
        }

        protected String getReward(String key) {
            return rewards.get(key);
        }

        protected Map<Double, List<String>> getRewards(String group) {
            return chanceMap.get(ChanceMaps.REWARDS.ordinal()).get(group);
        }
    }

}
