package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;

public class TableCfg {

    public TableCfg() {

        if (EterniaLib.getMySQL()) {
            EQueries.executeQuery(Constants.getQueryCreateTable(EterniaServer.getString(ConfigStrings.TABLE_KITS),
                    "(id INT AUTO_INCREMENT NOT NULL PRIMARY KEY, " +
                            "name VARCHAR(32), " +
                            "cooldown BIGINT(20))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(EterniaServer.getString(ConfigStrings.TABLE_PLAYER),
                    "(id INT AUTO_INCREMENT NOT NULL PRIMARY KEY, " +
                            "uuid VARCHAR(36), " +
                            "player_name VARCHAR(16), " +
                            "player_display VARCHAR(16), " +
                            "time BIGINT(20), " +
                            "last BIGINT(20), " +
                            "hours BIGINT(20), " +
                            "balance DOUBLE(20,4), " +
                            "cash BIGINT(20), " +
                            "xp BIGINT(20), " +
                            "muted BIGINT(20), " +
                            "homes VARCHAR(1024))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(EterniaServer.getString(ConfigStrings.TABLE_LOCATIONS),
                    "(id INT AUTO_INCREMENT NOT NULL PRIMARY KEY, " +
                            "name VARCHAR(32), " +
                            "location VARCHAR(64))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(EterniaServer.getString(ConfigStrings.TABLE_REWARD),
                    "(id INT AUTO_INCREMENT NOT NULL PRIMARY KEY, " +
                            "code VARCHAR(16), " +
                            "group_name VARCHAR(16))"), false);
        } else {
            EQueries.executeQuery(Constants.getQueryCreateTable(EterniaServer.getString(ConfigStrings.TABLE_KITS),
                    "(name VARCHAR(32), " +
                            "cooldown INTEGER)"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(EterniaServer.getString(ConfigStrings.TABLE_PLAYER),
                    "(uuid VARCHAR(36), " +
                            "player_name VARCHAR(16), " +
                            "player_display VARCHAR(16), " +
                            "time INTEGER, " +
                            "last INTEGER, " +
                            "hours INTEGER, " +
                            "balance DOUBLE(22), " +
                            "cash INTEGER, " +
                            "xp INTEGER, " +
                            "muted INTEGER, " +
                            "homes VARCHAR(1024))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(EterniaServer.getString(ConfigStrings.TABLE_LOCATIONS),
                    "(name VARCHAR(32), " +
                            "location VARCHAR(64))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(EterniaServer.getString(ConfigStrings.TABLE_REWARD),
                    "(code VARCHAR(16), " +
                            "group_name VARCHAR(16))"), false);
        }
    }

}
