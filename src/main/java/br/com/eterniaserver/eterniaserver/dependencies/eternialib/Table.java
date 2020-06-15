package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;

public class Table {

    public Table(EterniaServer plugin) {

        if (plugin.serverConfig.getBoolean("sql.mysql")) {
            EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-money") + " (player_name varchar(32), balance double(22,4));");
        } else {
            EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-money") + " (player_name varchar(32), balance double(22));");
        }

        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-xp") + " (player_name varchar(32), xp int(11));");
        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-warp") + " (name varchar(16), location varchar(128));");
        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-shop") + " (name varchar(16), location varchar(128));");
        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-home") + " (player_name varchar(16), homes varchar(255));");
        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-homes") + " (name varchar(32), location varchar(128));");
        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-kits") + " (name varchar(32), cooldown varchar(16));");
        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-player") + " (player_name varchar(16), time varchar(16));");
        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-muted") + " (player_name varchar(16), time varchar(16));");
        EQueries.executeQuery(Strings.tableCreate + plugin.serverConfig.getString("sql.table-rewards") + " (code varchar(16), lalalala varchar(16));");

    }

}
