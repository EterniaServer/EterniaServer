package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;

public class Table {

    public Table() {

        if (EterniaLib.getMySQL()) {
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableMoney, "(uuid varchar(36), balance double(22,4))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableKits, "(name varchar(32), cooldown bigint(20))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tablePlayer, "(uuid varchar(36), player_name varchar(16), time bigint(20), last bigint(20), hours bigint(20))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableMuted, "(uuid varchar(36), time bigint(20))"), false);
        } else {
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableMoney, "(uuid varchar(36), balance double(22))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableKits, "(name varchar(32), cooldown integer)"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tablePlayer, "(uuid varchar(36), player_name varchar(16), time integer, last integer, hours integer)"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableMuted, "(uuid varchar(36), time integer)"), false);
        }

        final String nameTime = "(name varchar(16), location varchar(64))";
        EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableXp, "(uuid varchar(36), xp int(11))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableWarp, nameTime), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableShop, nameTime), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableHome, "(uuid varchar(36), homes varchar(1024))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableHomes, "(name varchar(32), location varchar(64))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableReward, "(code varchar(16), lalalala varchar(16))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Configs.tableCash, "(uuid varchar(36), balance int(6))"), false);
    }

}
