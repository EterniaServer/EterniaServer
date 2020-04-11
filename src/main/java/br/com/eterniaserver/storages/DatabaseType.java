package br.com.eterniaserver.storages;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.storages.sql.Connections;
import br.com.eterniaserver.storages.sql.Tables;

public class DatabaseType {

    public DatabaseType(EterniaServer plugin) {
        EterniaServer.connection = new Connections(plugin, EterniaServer.configs.getBoolean("sql.mysql"));
        new Tables();
    }

}