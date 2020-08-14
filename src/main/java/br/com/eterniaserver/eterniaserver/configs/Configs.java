package br.com.eterniaserver.eterniaserver.configs;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Configs {

    private Configs() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TABLE_CASH = EterniaServer.serverConfig.getString("sql.table-cash");
    public static final String TABLE_HOME = EterniaServer.serverConfig.getString("sql.table-home");
    public static final String TABLE_HOMES = EterniaServer.serverConfig.getString("sql.table-homes");
    public static final String TABLE_KITS = EterniaServer.serverConfig.getString("sql.table-kits");
    public static final String TABLE_NICK = EterniaServer.serverConfig.getString("sql.table-nick");
    public static final String TABLE_MONEY = EterniaServer.serverConfig.getString("sql.table-money");
    public static final String TABLE_MUTED = EterniaServer.serverConfig.getString("sql.table-muted");
    public static final String TABLE_PLAYER = EterniaServer.serverConfig.getString("sql.table-player");
    public static final String TABLE_REWARD = EterniaServer.serverConfig.getString("sql.table-rewards");
    public static final String TABLE_SHOP = EterniaServer.serverConfig.getString("sql.table-shop");
    public static final String TABLE_WARP = EterniaServer.serverConfig.getString("sql.table-warp");
    public static final String TABLE_XP = EterniaServer.serverConfig.getString("sql.table-xp");

}
