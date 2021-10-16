package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.core.queries.CreateTable;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eternialib.core.queries.Select;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConfigsCfg extends GenericCfg implements ReloadableConfiguration {

    private final EterniaServer plugin;

    public ConfigsCfg(final EterniaServer plugin,
                      final String[] strings,
                      final boolean[] booleans,
                      final int[] integers,
                      final double[] doubles) {
        super(plugin, strings, booleans, integers, doubles);
        this.plugin = plugin;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.WARNING_ADVICE;
    }

    @Override
    public void executeConfig() {
        final FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Constants.CONFIG_FILE_PATH));
        final FileConfiguration outFile = new YamlConfiguration();

        setBoolean(Booleans.MODULE_BED, file, outFile, "module.bed", true);
        setBoolean(Booleans.MODULE_BLOCK, file, outFile, "module.block-reward", true);
        setBoolean(Booleans.MODULE_CASH, file, outFile, "module.cash", true);
        setBoolean(Booleans.MODULE_CHAT, file, outFile, "module.chat", true);
        setBoolean(Booleans.MODULE_ENTITY, file, outFile, "module.entity", true);
        setBoolean(Booleans.MODULE_COMMANDS, file, outFile, "module.commands", true);
        setBoolean(Booleans.MODULE_ECONOMY, file, outFile, "module.economy", true);
        setBoolean(Booleans.MODULE_HOMES, file, outFile, "module.home", true);
        setBoolean(Booleans.MODULE_SHOP, file, outFile, "module.shop", true);
        setBoolean(Booleans.MODULE_KITS, file, outFile, "module.kits", true);
        setBoolean(Booleans.MODULE_GENERIC, file, outFile, "module.generic", true);
        setBoolean(Booleans.MODULE_TELEPORTS, file, outFile, "module.teleports", true);
        setBoolean(Booleans.MODULE_REWARDS, file, outFile, "module.rewards", true);
        setBoolean(Booleans.MODULE_SCHEDULE, file, outFile, "module.schedule", true);
        setBoolean(Booleans.ASYNC_CHECK, file, outFile, "server.async-check", true);
        setBoolean(Booleans.ITEMS_FUNCTIONS, file, outFile, "server.item-function", true);

        setString(Strings.TABLE_KITS, file, outFile, "sql.table-kits", "es_kits");
        setString(Strings.TABLE_PLAYER, file, outFile, "sql.table-player", "es_players");
        setString(Strings.TABLE_REWARD, file, outFile, "sql.table-rewards", "es_rewards");
        setString(Strings.TABLE_LOCATIONS, file, outFile, "sql.table-locations", "es_locations");
        setString(Strings.TABLE_TITLES, file, outFile, "sql.table-titles", "es_titles");
        setString(Strings.TABLE_SHOP_ADDON, file, outFile, "sql.table-chest-shop-addon", "es_cs_addon");

        setInteger(Integers.PLUGIN_TICKS, file, outFile, "server.checks", 1);
        setInteger(Integers.AFK_TIMER, file, outFile, "server.afk-timer", 900);
        setInteger(Integers.COOLDOWN, file, outFile, "server.cooldown", 4);
        setInteger(Integers.PVP_TIME, file, outFile, "server.pvp-time", 15);
        setInteger(Integers.NIGHT_SPEED, file, outFile, "bed.speed", 100);
        setInteger(Integers.COMMAND_CONFIRM_TIME, file, outFile, "command-confirm.time", 60);

        setList(Lists.BLACKLISTED_WORLDS_FLY, file, outFile, "server.blacklisted-fly-worlds", "world_evento");
        setList(Lists.BLACKLISTED_WORLDS_SLEEP, file, outFile, "bed.blacklisted-worlds", "world_evento");
        setList(Lists.BLACKLISTED_COMMANDS, file, outFile, "blocked-commands", "/op", "/deop", "/stop");
        setList(Lists.BLACKLISTED_WORLDS_BACK, file, outFile, "back.blacklisted-worlds", "world_evento", "world_pvp");
        setList(Lists.PROFILE_CUSTOM_MESSAGES, file, outFile, "profile.custom-messages");
        setList(Lists.TITLE_LIST, file, outFile, "titles", "Maníaco", "Farmer");

        saveFile(outFile, Constants.CONFIG_FILE_PATH);
    }

    @Override
    public void executeCritical() {
        CreateTable createTable;
        if (EterniaLib.getMySQL()) {
            createTable = new CreateTable(plugin.getString(Strings.TABLE_KITS));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "name VARCHAR(32)", "cooldown BIGINT(20)");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_PLAYER));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "uuid VARCHAR(36)", "player_name VARCHAR(16)",
                    "player_display VARCHAR(512)", "time BIGINT(20)", "last BIGINT(20)", "hours BIGINT(20)", "balance DOUBLE(20,4)",
                    "cash BIGINT(20)", "xp BIGINT(20)", "muted BIGINT(20)", "homes VARCHAR(1024)");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "name VARCHAR(64)", "world VARCHAR(32)",
                    "coord_x DOUBLE", "coord_y DOUBLE", "coord_z DOUBLE", "coord_yaw FLOAT", "coord_pitch FLOAT");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_TITLES));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "uuid VARCHAR(36)", "titles_array VARCHAR(4096)", "default_title VARCHAR(36)");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_REWARD));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "key_code VARCHAR(16)", "group_name VARCHAR(16)");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_SHOP_ADDON));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "shop_uuid VARCHAR(36)", "world VARCHAR(32)", "coord_x INT",
                    "coord_y INT", "coord_z INT");
        } else {
            createTable = new CreateTable(plugin.getString(Strings.TABLE_KITS));
            createTable.columns.set("name VARCHAR(32)", "cooldown INTEGER");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_PLAYER));
            createTable.columns.set("uuid VARCHAR(36)", "player_name VARCHAR(16)",
                    "player_display VARCHAR(512)", "time INTEGER", "last INTEGER", "hours INTEGER", "balance DOUBLE(22)",
                    "cash INTEGER", "xp INTEGER", "muted INTEGER", "homes VARCHAR(1024)");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            createTable.columns.set("name VARCHAR(64)", "world VARCHAR(32)", "coord_x DOUBLE", "coord_y DOUBLE",
                    "coord_z DOUBLE", "coord_yaw FLOAT", "coord_pitch FLOAT");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_TITLES));
            createTable.columns.set("uuid VARCHAR(36)", "titles_array VARCHAR(4096)", "default_title VARCHAR(36)");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_REWARD));
            createTable.columns.set("key_code VARCHAR(16)", "group_name VARCHAR(16)");
            SQL.execute(createTable);

            createTable = new CreateTable(plugin.getString(Strings.TABLE_SHOP_ADDON));
            createTable.columns.set("shop_uuid VARCHAR(36)", "world VARCHAR(32)", "coord_x INTEGER",
                    "coord_y INTEGER", "coord_z INTEGER");
        }

        SQL.execute(createTable);

        convertingDisplayNameSize();
        convertingOldTable(plugin.getString(Strings.TABLE_LOCATIONS));

        loadPlayers();
        loadKits();
        loadTitles();
    }

    private void loadPlayers() {
        final Set<String> playersName = new HashSet<>();
        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(plugin.getString(Strings.TABLE_PLAYER)).queryString()); ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()) {
                final String playerName = resultSet.getString("player_name");
                PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        resultSet.getString("player_display"),
                        resultSet.getLong("time"),
                        resultSet.getLong("last"),
                        resultSet.getLong("hours")
                );
                final UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
                    EterniaServer.getEconomyAPI().putInMoney(uuid, resultSet.getDouble("balance"));
                }
                getModules(playerProfile, resultSet);
                EterniaServer.getUserAPI().putProfile(uuid, playerProfile);
                if (playerName != null) {
                    playersName.add(playerName.toLowerCase());
                }
            }
        } catch (SQLException ignored) {
            plugin.logError("Erro ao carregar database", 3);
        }

        final List<String> shopList = plugin.getShopList();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            plugin.setError(new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666));
            try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    final String name = resultSet.getString("name");
                    final String worldName = resultSet.getString("world");
                    final double x = resultSet.getDouble("coord_x");
                    final double y = resultSet.getDouble("coord_y");
                    final double z = resultSet.getDouble("coord_z");
                    final float yaw = resultSet.getFloat("coord_yaw");
                    final float pitch = resultSet.getFloat("coord_pitch");

                    if (name == null || worldName == null) {
                        continue;
                    }

                    final World world = Bukkit.getWorld(worldName);

                    if (world == null) {
                        continue;
                    }

                    plugin.putLocation(name, new Location(world, x, y, z, yaw, pitch));
                    if (playersName.contains(name)) {
                        shopList.add(name);
                    }
                }
            } catch (SQLException ignored) {
                plugin.logError("Erro ao carregar database", 3);
            }
        });

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(plugin.getString(Strings.TABLE_SHOP_ADDON)).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    final String shopUUID = resultSet.getString("shop_uuid");
                    final String worldName = resultSet.getString("world");
                    final int x = resultSet.getInt("coord_x");
                    final int y = resultSet.getInt("coord_y");
                    final int z = resultSet.getInt("coord_z");

                    if (shopUUID == null || worldName == null) {
                        continue;
                    }

                    final World world = Bukkit.getWorld(worldName);

                    if (world == null) {
                        continue;
                    }

                    EterniaServer.getEconomyAPI().addSign(shopUUID, new Location(world, x, y, z));
                }
            } catch (SQLException ignored) {
                plugin.logError("Erro ao carregar database", 3);
            }
        });
        EterniaLib.report(plugin.getMessage(Messages.SERVER_DATA_LOADED, true, "Player Profiles", String.valueOf(EterniaServer.getUserAPI().getProfileMapSize())));
    }

    private void loadKits() {
        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(plugin.getString(Strings.TABLE_KITS)).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                EterniaServer.getUserAPI().putKitCooldown(resultSet.getString("name"), resultSet.getLong("cooldown"));
            }
        } catch (SQLException e) {
            plugin.logError("Erro ao pegar arquivos da database", 3);
        }
    }

    private void loadTitles() {
        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(plugin.getString(Strings.TABLE_TITLES)).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                PlayerProfile playerProfile = EterniaServer.getUserAPI().getProfile(UUID.fromString(resultSet.getString("uuid")));
                playerProfile.getTitles().addAll(Arrays.asList(resultSet.getString("titles_array").split(":")));
                playerProfile.setActiveTitle(resultSet.getString("default_title"));
            }
        } catch (SQLException exception) {
            plugin.logError("Erro ao carregar database", 3);
        }
    }

    private void getModules(PlayerProfile playerProfile, ResultSet resultSet) throws SQLException {
        if (plugin.getBoolean(Booleans.MODULE_CASH)) {
            playerProfile.setCash(resultSet.getInt("cash"));
        }
        if (plugin.getBoolean(Booleans.MODULE_HOMES)) {
            String result = resultSet.getString("homes");
            if (result != null) {
                for (String home : result.split(":")) {
                    playerProfile.getHomes().add(home);
                }
            }
        }
        if (plugin.getBoolean(Booleans.MODULE_CHAT)) {
            playerProfile.setMuted(resultSet.getLong("muted"));
        }
    }

    private void convertingDisplayNameSize() {
        try (Connection connection = SQL.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "ALTER TABLE ? MODIFY ? VARCHAR(512);")) {
            preparedStatement.setString(1, plugin.getString(Strings.TABLE_PLAYER));
            preparedStatement.setString(2, "player_display");
            preparedStatement.execute();
        } catch (SQLException ignored) { }
    }

    private void convertingOldTable(final String oldTable) {
        final List<Insert> insertList = new ArrayList<>();
        boolean converted;

        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(oldTable).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                final String[] split = resultSet.getString("location").split(":");
                final Insert insert = new Insert(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
                insert.columns.set("name", "world", "coord_x", "coord_y", "coord_z", "coord_yaw", "coord_pitch");
                insert.values.set(
                        resultSet.getString("name"),
                        split[0],
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]),
                        Double.parseDouble(split[3]),
                        Float.parseFloat(split[4]),
                        Float.parseFloat(split[5]));
                insertList.add(insert);
            }
            converted = true;
        } catch (SQLException ignored) {
            converted = false;
        }

        if (!converted) {
            return;
        }

        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS ?;")) {
            preparedStatement.setString(1, oldTable);
            preparedStatement.execute();
        } catch (SQLException ignored) {}

        for (final Insert insert : insertList) {
            SQL.execute(insert);
        }

        Bukkit.getConsoleSender().sendMessage(plugin.getMessage(Messages.SERVER_CONVERTING_DATABASE, true, "100"));
    }

}
