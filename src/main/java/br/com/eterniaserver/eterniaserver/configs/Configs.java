package br.com.eterniaserver.eterniaserver.configs;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Configs {

    private Configs() {
        throw new IllegalStateException("Utility class");
    }

    public static final String tableKits = EterniaServer.serverConfig.getString("sql.table-kits");
    public static final String tablePlayer = EterniaServer.serverConfig.getString("sql.table-player");
    public static final String tableReward = EterniaServer.serverConfig.getString("sql.table-rewards");
    public static final String tableLocations = EterniaServer.serverConfig.getString("sql.table-locations");
}
