package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.sql.Queries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Table {

    public Table(EterniaServer plugin) {

        if (plugin.serverConfig.getBoolean("sql.mysql")) {
            Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-money") + " (player_name varchar(32), balance double(22,4));");
        } else {
            Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-money") + " (player_name varchar(32), balance double(22));");
        }

        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-xp") + " (player_name varchar(32), xp int(11));");
        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-warp") + " (name varchar(16), location varchar(128));");
        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-shop") + " (name varchar(16), location varchar(128));");
        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-home") + " (player_name varchar(16), homes varchar(255));");
        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-homes") + " (name varchar(32), location varchar(128));");
        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-kits") + " (name varchar(32), cooldown varchar(16));");
        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-player") + " (player_name varchar(16), time varchar(16));");
        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-muted") + " (player_name varchar(16), time varchar(16));");
        Queries.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-rewards") + " (code varchar(16), lalalala varchar(16));");

    }

}
