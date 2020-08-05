package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.Constants;

public class Table {

    public Table() {

        if (EterniaLib.getMySQL()) {
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_MONEY, "(uuid varchar(36), balance double(22,4))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_KITS, "(name varchar(32), cooldown bigint(20))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_PLAYER, "(uuid varchar(36), time bigint(20), last bigint(20), hours int(11))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_MUTED, "(uuid varchar(36), time bigint(20))"), false);
        } else {
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_MONEY, "(uuid varchar(36), balance double(22))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_KITS, "(name varchar(32), cooldown integer)"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_PLAYER, "(uuid varchar(36), player_name varchar(16), time integer, last integer, hours int(11))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_MUTED, "(uuid varchar(36), time integer)"), false);
        }

        final String nameTime = "(name varchar(16), location varchar(64))";
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_XP, "(uuid varchar(36), xp int(11))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_WARP, nameTime), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_SHOP, nameTime), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_HOME, "(uuid varchar(36), homes varchar(1024))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_HOMES, "(name varchar(32), location varchar(64))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_REWARD, "(code varchar(16), lalalala varchar(16))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_NICK, "(uuid varchar(36), player_display varchar(16))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_CASH, "(uuid varchar(36), balance int(6))"), false);

    }

}
