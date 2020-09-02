package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import java.util.List;


public class PluginConfigs {

    private PluginConfigs() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TABLE_KITS = EterniaServer.serverConfig.getString("sql.table-kits");
    public static final String TABLE_PLAYER = EterniaServer.serverConfig.getString("sql.table-player");
    public static final String TABLE_REWARD = EterniaServer.serverConfig.getString("sql.table-rewards");
    public static final String TABLE_LOCATIONS = EterniaServer.serverConfig.getString("sql.table-locations");

    protected static final int BED_SPEED = EterniaServer.serverConfig.getInt("bed.speed");
    protected static final List<?> BED_BANNED_WORLD = EterniaServer.serverConfig.getList("bed.blacklisted-worlds");

    protected static final double BALANCE_START = EterniaServer.serverConfig.getDouble("money.start");
    protected static final String BALANCE_PLURAL_NAME = EterniaServer.serverConfig.getString("money.plural");
    protected static final String BALANCE_SINGULAR_NAME = EterniaServer.serverConfig.getString("money.singular");

}
