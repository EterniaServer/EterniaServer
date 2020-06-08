package br.com.eterniaserver.eterniaserver.storages.database;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Table {

    public Table(EterniaServer plugin) {

        if (plugin.serverConfig.getBoolean("sql.mysql")) {
            plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-money") + " (player_name varchar(32), balance double(22,4));");
        } else {
            plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-money") + " (player_name varchar(32), balance double(22));");
        }

        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-xp") + " (player_name varchar(32), xp int(11));");
        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-warp") + " (name varchar(16), location varchar(128));");
        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-shop") + " (name varchar(16), location varchar(128));");
        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-home") + " (player_name varchar(16), homes varchar(255));");
        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-homes") + " (name varchar(32), location varchar(128));");
        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-kits") + " (name varchar(32), cooldown varchar(16));");
        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-player") + " (player_name varchar(16), time varchar(16));");
        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-muted") + " (player_name varchar(16), time varchar(16));");
        plugin.executeQuery("CREATE TABLE IF NOT EXISTS " + plugin.serverConfig.getString("sql.table-rewards") + " (code varchar(16), lalalala varchar(16));");

    }

}
