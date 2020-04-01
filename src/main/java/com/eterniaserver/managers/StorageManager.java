package com.eterniaserver.managers;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.storage.sqlsetup.Queries;
import com.eterniaserver.storage.sqlsetup.Tables;

public class StorageManager {

    public StorageManager(boolean mysql) {
        String host = EterniaServer.getMain().getConfig().getString("sql.host");
        String database = EterniaServer.getMain().getConfig().getString("sql.database");
        String username = EterniaServer.getMain().getConfig().getString("sql.user");
        String password = EterniaServer.getMain().getConfig().getString("sql.password");
        if (mysql) {
            EterniaServer.sqlcon = new Queries(host, database, username, password, true);
        } else {
            EterniaServer.sqlcon = new Queries(host, database, username, password, false);
        }
        new Tables();
    }

}