package br.com.eterniaserver.storages;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.storages.sql.Connections;
import br.com.eterniaserver.storages.sql.Table;

public class DatabaseType {

    public DatabaseType(EterniaServer plugin, Messages messages) {
        plugin.connections = new Connections(plugin, plugin.serverConfig.getBoolean("sql.mysql"), messages);
        new Table(plugin);
    }

}