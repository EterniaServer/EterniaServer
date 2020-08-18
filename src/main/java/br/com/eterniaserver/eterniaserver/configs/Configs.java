package br.com.eterniaserver.eterniaserver.configs;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Configs {

    private Configs() {
        throw new IllegalStateException("Utility class");
    }

    public static final String tableCash = EterniaServer.serverConfig.getString("sql.table-cash");
    public static final String tableHome = EterniaServer.serverConfig.getString("sql.table-home");
    public static final String tableHomes = EterniaServer.serverConfig.getString("sql.table-homes");
    public static final String tableKits = EterniaServer.serverConfig.getString("sql.table-kits");
    public static final String tableNick = EterniaServer.serverConfig.getString("sql.table-nick");
    public static final String tableMoney = EterniaServer.serverConfig.getString("sql.table-money");
    public static final String tableMuted = EterniaServer.serverConfig.getString("sql.table-muted");
    public static final String tablePlayer = EterniaServer.serverConfig.getString("sql.table-player");
    public static final String tableReward = EterniaServer.serverConfig.getString("sql.table-rewards");
    public static final String tableShop = EterniaServer.serverConfig.getString("sql.table-shop");
    public static final String tableWarp = EterniaServer.serverConfig.getString("sql.table-warp");
    public static final String tableXp = EterniaServer.serverConfig.getString("sql.table-xp");
}
