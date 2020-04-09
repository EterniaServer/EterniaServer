package br.com.eterniaserver.storages;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.storages.sql.Connections;
import br.com.eterniaserver.storages.sql.Tables;

public class StorageConfig {

    public StorageConfig(EterniaServer plugin) {
        EterniaServer.sqlcon = new Connections(plugin, CVar.getBool("sql.mysql"));
        new Tables();
    }

}