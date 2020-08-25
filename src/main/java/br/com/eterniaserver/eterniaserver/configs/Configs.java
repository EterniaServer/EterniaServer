package br.com.eterniaserver.eterniaserver.configs;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import java.util.List;


public class Configs {

    private Configs() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TABLE_KITS = EterniaServer.serverConfig.getString("sql.table-kits");
    public static final String TABLE_PLAYER = EterniaServer.serverConfig.getString("sql.table-player");
    public static final String TABLE_REWARD = EterniaServer.serverConfig.getString("sql.table-rewards");
    public static final String TABLE_LOCATIONS = EterniaServer.serverConfig.getString("sql.table-locations");

    public static final int BED_SPEED = EterniaServer.serverConfig.getInt("bed.speed");
    public static final List<?> BED_BANNED_WORLD = EterniaServer.serverConfig.getList("bed.blacklisted-worlds");

    public static final double BALANCE_START = EterniaServer.serverConfig.getDouble("money.start");
    public static final String BALANCE_PLURAL_NAME = EterniaServer.serverConfig.getString("money.plural");
    public static final String BALANCE_SINGULAR_NAME = EterniaServer.serverConfig.getString("money.singular");

}
