package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.CreateTable;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eternialib.sql.queries.Select;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableCfg {

    public TableCfg() {

        CreateTable createTable;
        if (EterniaLib.getMySQL()) {
            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_KITS));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "name VARCHAR(32)", "cooldown BIGINT(20)");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_PLAYER));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "uuid VARCHAR(36)", "player_name VARCHAR(16)",
                    "player_display VARCHAR(16)", "time BIGINT(20)", "last BIGINT(20)", "hours BIGINT(20)", "balance DOUBLE(20,4)",
                    "cash BIGINT(20)", "xp BIGINT(20)", "muted BIGINT(20)", "homes VARCHAR(1024)");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "name VARCHAR(64)", "world VARCHAR(32)",
                    "coord_x DOUBLE", "coord_y DOUBLE", "coord_z DOUBLE", "coord_yaw FLOAT", "coord_pitch FLOAT");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_REWARD));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "key_code VARCHAR(16)", "group_name VARCHAR(16)");
        } else {
            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_KITS));
            createTable.columns.set("name VARCHAR(32)", "cooldown INTEGER");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_PLAYER));
            createTable.columns.set("uuid VARCHAR(36)", "player_name VARCHAR(16)",
                    "player_display VARCHAR(16)", "time INTEGER", "last INTEGER", "hours INTEGER", "balance DOUBLE(22)",
                    "cash INTEGER", "xp INTEGER", "muted INTEGER", "homes VARCHAR(1024)");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            createTable.columns.set("name VARCHAR(64)", "world VARCHAR(32)", "coord_x DOUBLE", "coord_y DOUBLE",
                    "coord_z DOUBLE", "coord_yaw FLOAT", "coord_pitch FLOAT");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_REWARD));
            createTable.columns.set("key_code VARCHAR(16)", "group_name VARCHAR(16)");
        }

        SQL.execute(createTable);

        convertingOldTable(EterniaServer.getString(Strings.TABLE_LOCATIONS));
    }

    private void convertingOldTable(final String oldTable) {
        final List<Insert> insertList = new ArrayList<>();
        boolean converted;

        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(oldTable).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                final String[] split = resultSet.getString("location").split(":");
                final Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
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

        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS " + oldTable + ";")) {
            preparedStatement.execute();
        } catch (SQLException ignored) {}

        for (final Insert insert : insertList) {
            SQL.execute(insert);
        }

        Bukkit.getConsoleSender().sendMessage(EterniaServer.getMessage(Messages.SERVER_CONVERTING_DATABASE, true, "100"));
    }

}
