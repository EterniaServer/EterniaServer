package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.Configs;
import br.com.eterniaserver.eterniaserver.generics.PluginConstants;

public class Table {

    public Table() {

        if (EterniaLib.getMySQL()) {
            EQueries.executeQuery(PluginConstants.getQueryCreateTable(Configs.instance.tableKits,
                    PluginConstants.PRIMARY_KEY +
                            "name VARCHAR(32), " +
                            "cooldown BIGINT(20))"), false);
            EQueries.executeQuery(PluginConstants.getQueryCreateTable(Configs.instance.tablePlayer,
                    PluginConstants.PRIMARY_KEY +
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
            EQueries.executeQuery(PluginConstants.getQueryCreateTable(Configs.instance.tableLocations,
                    PluginConstants.PRIMARY_KEY +
                            "name VARCHAR(32), " +
                            "location VARCHAR(64))"), false);
            EQueries.executeQuery(PluginConstants.getQueryCreateTable(Configs.instance.tableRewards,
                    PluginConstants.PRIMARY_KEY +
                            "code VARCHAR(16), " +
                            "group_name VARCHAR(16))"), false);
        } else {
            EQueries.executeQuery(PluginConstants.getQueryCreateTable(Configs.instance.tableKits,
                    "(name VARCHAR(32), " +
                            "cooldown INTEGER)"), false);
            EQueries.executeQuery(PluginConstants.getQueryCreateTable(Configs.instance.tablePlayer,
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
            EQueries.executeQuery(PluginConstants.getQueryCreateTable(Configs.instance.tableLocations,
                    "(name VARCHAR(32), " +
                            "location VARCHAR(64))"), false);
            EQueries.executeQuery(PluginConstants.getQueryCreateTable(Configs.instance.tableRewards,
                    "(code VARCHAR(16), " +
                            "group_name VARCHAR(16))"), false);
        }
    }

}
