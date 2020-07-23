package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Table {

    public Table() {

        if (EterniaLib.getMySQL()) {
            EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-money") + " (player_name varchar(32), balance double(22,4));", false);
            EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-kits") + " (name varchar(32), cooldown bigint(20));", false);
            EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-player") + " (player_name varchar(16), time bigint(20));", false);
            EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-muted") + " (player_name varchar(16), time bigint(20));", false);
        } else {
            EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-kits") + " (name varchar(32), cooldown integer);", false);
            EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-player") + " (player_name varchar(16), time integer);", false);
            EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-muted") + " (player_name varchar(16), time integer);", false);
            EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-money") + " (player_name varchar(32), balance double(22));", false);
        }

        EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-xp") + " (player_name varchar(32), xp int(11));", false);
        EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-warp") + " (name varchar(16), location varchar(128));", false);
        EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-shop") + " (name varchar(16), location varchar(128));", false);
        EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-home") + " (player_name varchar(16), homes varchar(255));", false);
        EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-homes") + " (name varchar(32), location varchar(128));", false);
        EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-rewards") + " (code varchar(16), lalalala varchar(16));", false);
        EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-nick") + " (player_name varchar(16), player_display varchar(16));", false);
        EQueries.executeQuery("CREATE TABLE IF NOT EXISTS " + EterniaServer.serverConfig.getString("sql.table-cash") + " (player_name varchar(16), balance int(6));", false);
        
    }

}
