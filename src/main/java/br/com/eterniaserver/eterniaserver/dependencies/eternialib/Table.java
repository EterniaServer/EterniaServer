package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;

public class Table {

    public Table() {

        if (EterniaLib.getMySQL()) {
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.TABLE_KITS,
                    Constants.PRIMARY_KEY +
                            "name VARCHAR(32), " +
                            "cooldown BIGINT(20))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.TABLE_PLAYER,
                    Constants.PRIMARY_KEY +
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
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.TABLE_LOCATIONS,
                    Constants.PRIMARY_KEY +
                            "name VARCHAR(32), " +
                            "location VARCHAR(64))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.TABLE_REWARD,
                    Constants.PRIMARY_KEY +
                            "code VARCHAR(16), " +
                            "group_name VARCHAR(16))"), false);
        } else {
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.TABLE_KITS,
                    "(name VARCHAR(32), " +
                            "cooldown INTEGER)"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.TABLE_PLAYER,
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
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.TABLE_LOCATIONS,
                    "(name VARCHAR(32), " +
                            "location VARCHAR(64))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.TABLE_REWARD,
                    "(code VARCHAR(16), " +
                            "group_name VARCHAR(16))"), false);
        }
    }

}
