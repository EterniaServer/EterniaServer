package com.eterniaserver.storage;

import com.eterniaserver.EterniaServer;

public class StorageConfig {

    public StorageConfig(boolean mysql) {
        String host = EterniaServer.getMain().getConfig().getString("sql.host");
        String database = EterniaServer.getMain().getConfig().getString("sql.database");
        String username = EterniaServer.getMain().getConfig().getString("sql.user");
        String password = EterniaServer.getMain().getConfig().getString("sql.password");
        if (mysql) {
            EterniaServer.sqlcon = new Connections(host, database, username, password, true);
        } else {
            EterniaServer.sqlcon = new Connections(host, database, username, password, false);
        }
        new Tables();
    }

}