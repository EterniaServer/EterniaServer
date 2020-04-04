package com.eterniaserver.storage;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;

public class StorageConfig {

    public StorageConfig(EterniaServer plugin) {
        String host = plugin.getConfig().getString("sql.host");
        String database = plugin.getConfig().getString("sql.database");
        String username = plugin.getConfig().getString("sql.user");
        String password = plugin.getConfig().getString("sql.password");
        if (CVar.getBool("sql.mysql")) {
            EterniaServer.sqlcon = new Connections(plugin, host, database, username, password, true);
        } else {
            EterniaServer.sqlcon = new Connections(plugin, host, database, username, password, false);
        }
        new Tables();
    }

}