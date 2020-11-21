package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.CreateTable;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;

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

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_LOCATIONS));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "name VARCHAR(32)", "location VARCHAR(64)");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_REWARD));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "code VARCHAR(16)", "group_name VARCHAR(16)");
            SQL.execute(createTable);
        } else {
            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_KITS));
            createTable.columns.set("name VARCHAR(32)", "cooldown INTEGER");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_PLAYER));
            createTable.columns.set("uuid VARCHAR(36)", "player_name VARCHAR(16)",
                    "player_display VARCHAR(16)", "time INTEGER", "last INTEGER", "hours INTEGER", "balance DOUBLE(22)",
                    "cash INTEGER", "xp INTEGER", "muted INTEGER", "homes VARCHAR(1024)");
            SQL.execute(createTable);

            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_LOCATIONS));
            createTable.columns.set("name VARCHAR(32)", "location VARCHAR(64)");
            SQL.execute(createTable);


            createTable = new CreateTable(EterniaServer.getString(Strings.TABLE_REWARD));
            createTable.columns.set("code VARCHAR(16)", "group_name VARCHAR(16)");
            SQL.execute(createTable);
        }
    }

}
