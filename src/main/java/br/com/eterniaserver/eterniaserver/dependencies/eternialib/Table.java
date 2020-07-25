package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Table {

    public Table() {

        if (EterniaLib.getMySQL()) {
            final String playerNameTimeMySQL = "(player_name varchar(16), time bigint(20))";
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_MONEY, "(player_name varchar(32), balance double(22,4))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_KITS, "(name varchar(32), cooldown bigint(20))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_PLAYER, playerNameTimeMySQL), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_MUTED, playerNameTimeMySQL), false);
        } else {
            final String playerNameTimeSQLite = "(player_name varchar(16), time integer)";
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_MONEY, "(player_name varchar(32), balance double(22))"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_KITS, "(name varchar(32), cooldown integer)"), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_PLAYER, playerNameTimeSQLite), false);
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_MUTED, playerNameTimeSQLite), false);
        }
        final String nameTime = "(name varchar(16), location varchar(128))";
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_XP, "(player_name varchar(32), xp int(11))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_WARP, nameTime), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_SHOP, nameTime), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_HOME, "(player_name varchar(16), homes varchar(255))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_HOMES, "(name varchar(32), location varchar(128))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_REWARD, "(code varchar(16), lalalala varchar(16))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_NICK, "(player_name varchar(16), player_display varchar(16))"), false);
        EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_CASH, "(player_name varchar(16), balance int(6))"), false);
        
    }

}
