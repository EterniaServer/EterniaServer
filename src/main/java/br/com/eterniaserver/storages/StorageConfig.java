package br.com.eterniaserver.storages;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.CVar;

public class StorageConfig {

    public StorageConfig(EterniaServer plugin) {
        EterniaServer.sqlcon = new Connections(plugin, CVar.getBool("sql.mysql"));
        new Tables();
    }

}