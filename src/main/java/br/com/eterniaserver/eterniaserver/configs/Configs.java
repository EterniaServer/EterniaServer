package br.com.eterniaserver.eterniaserver.configs;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Configs {

    private Configs() {
        throw new IllegalStateException("Utility class");
    }

    public static void reloadConfigs() {
        TABLE_CASH = EterniaServer.serverConfig.getString("sql.table-cash");
        TABLE_HOME = EterniaServer.serverConfig.getString("sql.table-home");
        TABLE_HOMES = EterniaServer.serverConfig.getString("sql.table-homes");
        TABLE_KITS = EterniaServer.serverConfig.getString("sql.table-kits");
        TABLE_NICK = EterniaServer.serverConfig.getString("sql.table-nick");
        TABLE_MONEY = EterniaServer.serverConfig.getString("sql.table-money");
        TABLE_MUTED = EterniaServer.serverConfig.getString("sql.table-muted");
        TABLE_PLAYER = EterniaServer.serverConfig.getString("sql.table-player");
        TABLE_REWARD = EterniaServer.serverConfig.getString("sql.table-rewards");
        TABLE_SHOP = EterniaServer.serverConfig.getString("sql.table-shop");
        TABLE_WARP = EterniaServer.serverConfig.getString("sql.table-warp");
        TABLE_XP = EterniaServer.serverConfig.getString("sql.table-xp");
    }

    public static String TABLE_CASH;
    public static String TABLE_HOME;
    public static String TABLE_HOMES;
    public static String TABLE_KITS;
    public static String TABLE_NICK;
    public static String TABLE_MONEY;
    public static String TABLE_MUTED;
    public static String TABLE_PLAYER;
    public static String TABLE_REWARD;
    public static String TABLE_SHOP;
    public static String TABLE_WARP;
    public static String TABLE_XP;

}
