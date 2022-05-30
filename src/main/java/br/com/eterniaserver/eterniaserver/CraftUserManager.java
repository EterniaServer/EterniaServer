package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.CreateTable;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eternialib.core.queries.Select;
import br.com.eterniaserver.eterniaserver.api.interfaces.UserManager;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;


final class CraftUserManager implements UserManager {

    private final EterniaServer plugin;

    private final Set<String> playerNameList = new HashSet<>();
    private final Map<UUID, PlayerProfile> playerProfileMap = new HashMap<>();

    CraftUserManager(final EterniaServer plugin) {
        final String[] MYSQL_FIELDS = {
                "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "uuid VARCHAR(36)", "player_name VARCHAR(16)",
                "player_display VARCHAR(512)", "time BIGINT(20)", "last BIGINT(20)", "hours BIGINT(20)",
                "balance DOUBLE(20,4)", "cash BIGINT(20)", "xp BIGINT(20)", "muted BIGINT(20)", "homes VARCHAR(1024)"
        };
        final String[] SQLITE_FIELDS = {
                "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "uuid VARCHAR(36)", "player_name VARCHAR(16)",
                "player_display VARCHAR(512)", "time INTEGER", "last INTEGER", "hours INTEGER",
                "balance DOUBLE(22)", "cash INTEGER", "xp INTEGER", "muted INTEGER", "homes VARCHAR(1024)"
        };

        final CreateTable createTable = new CreateTable(plugin.getString(Strings.TABLE_PLAYER_PROFILES));
        createTable.columns.set(EterniaLib.getMySQL() ? MYSQL_FIELDS : SQLITE_FIELDS);
        SQL.execute(createTable);

        try (Connection connection = SQL.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(new Select(plugin.getString(Strings.TABLE_PLAYER_PROFILES)).queryString());
             ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()) {
                final UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                final PlayerProfile playerProfile = new PlayerProfile(
                        uuid,
                        resultSet.getString("player_name"),
                        resultSet.getString("player_display"),
                        resultSet.getLong("time"),
                        resultSet.getLong("last"),
                        resultSet.getLong("hours")
                );

                // Money Module
                playerProfile.setMoney(resultSet.getDouble("balance"));
                // Cash Module
                playerProfile.setCash(resultSet.getInt("cash"));
                // Experience Module
                playerProfile.setExp(resultSet.getInt("xp"));
                // Home Module
                playerProfile.setHomes(resultSet.getString("homes"));
                // Chat Module
                playerProfile.setMuted(resultSet.getLong("muted"));

                playerNameList.add(playerProfile.getName());
                playerProfileMap.put(uuid, playerProfile);
            }
        } catch (SQLException ignored) {
            plugin.getLogger().log(Level.SEVERE, "Can't load player database table");
        }

        this.plugin = plugin;
    }

    @Override
    public PlayerProfile create(UUID uuid, String playerName) {
        final long time = System.currentTimeMillis();

        final PlayerProfile playerProfile = new PlayerProfile(uuid, playerName, playerName, time, time, 0);
        final Insert insert = new Insert(plugin.getString(Strings.TABLE_PLAYER_PROFILES));

        insert.columns.set("uuid", "player_name", "time", "last");
        insert.values.set(uuid.toString(), playerName, time, time);
        SQL.executeAsync(insert);

        playerProfileMap.put(uuid, playerProfile);

        return playerProfile;
    }

    @Override
    public PlayerProfile get(UUID uuid) {
        return playerProfileMap.get(uuid);
    }

    @Override
    public Set<String> getPlayersNames() {
        return playerNameList;
    }

}
