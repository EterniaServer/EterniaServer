package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.CreateTable;
import br.com.eterniaserver.eternialib.core.queries.Select;
import br.com.eterniaserver.eterniaserver.api.interfaces.LocationManager;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

final class CraftLocationManager implements LocationManager {

    private final Map<UUID, PlayerTeleport> teleportsMap = new HashMap<>();
    private final Map<String, Location> locationsMap = new HashMap<>();

    CraftLocationManager(final EterniaServer plugin) {
        final String[] MYSQL_FIELDS = {
                "id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "name VARCHAR(64)", "world VARCHAR(32)",
                "coord_x DOUBLE", "coord_y DOUBLE", "coord_z DOUBLE", "coord_yaw FLOAT", "coord_pitch FLOAT"
        };
        final String[] SQLITE_FIELDS = {
                "name VARCHAR(64)", "world VARCHAR(32)", "coord_x DOUBLE", "coord_y DOUBLE",
                "coord_z DOUBLE", "coord_yaw FLOAT", "coord_pitch FLOAT"
        };

        final CreateTable createTable = new CreateTable(plugin.getString(Strings.TABLE_SERVER_LOCATIONS));
        createTable.columns.set(EterniaLib.getMySQL() ? MYSQL_FIELDS : SQLITE_FIELDS);
        SQL.execute(createTable);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            try (Connection connection = SQL.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(new Select(plugin.getString(Strings.TABLE_SERVER_LOCATIONS)).queryString());
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    final String name = resultSet.getString("name");
                    final String worldName = resultSet.getString("world");
                    final double x = resultSet.getDouble("coord_x");
                    final double y = resultSet.getDouble("coord_y");
                    final double z = resultSet.getDouble("coord_z");
                    final float yaw = resultSet.getFloat("coord_yaw");
                    final float pitch = resultSet.getFloat("coord_pitch");

                    if (name == null || worldName == null) continue;

                    final World world = Bukkit.getWorld(worldName);

                    if (world == null) continue;

                    locationsMap.put(name, new Location(world, x, y, z, yaw, pitch));
                }
            } catch (SQLException ignored) {
                plugin.getLogger().log(Level.SEVERE, "Can't load player database table");
            }
        });
    }

    @Override
    public Location getLocation(String name) {
        return locationsMap.get(name);
    }

    @Override
    public void putLocation(String name, Location location) {
        locationsMap.put(name, location);
    }

    @Override
    public void removeLocation(String name) {
        locationsMap.remove(name);
    }

    @Override
    public PlayerTeleport getTeleport(UUID uuid) {
        return teleportsMap.get(uuid);
    }

    @Override
    public void putTeleport(UUID uuid, PlayerTeleport playerTeleport) {
        teleportsMap.put(uuid, playerTeleport);
    }

    @Override
    public void removeTeleport(UUID uuid) {
        teleportsMap.remove(uuid);
    }
}
