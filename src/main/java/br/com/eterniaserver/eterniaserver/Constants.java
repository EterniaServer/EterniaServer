package br.com.eterniaserver.eterniaserver;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String MESSAGE = "%message%";
    public static final String PLAYER = "%player_displayname%";
    public static final String TARGET = "%target_displayname%";
    public static final String PLAYER_DATA = "%player_register_data%";
    public static final String VALUE = "%value%";
    public static final String WARP = "%warp_name%";
    public static final String MODULE = "%module%";
    public static final String AMOUNT = "%amount%";
    public static final String TYPE = "%type%";
    public static final String GROUP = "%group%";
    public static final String KEY = "%key%";
    public static final String MEM_USE = "%use_memory%";
    public static final String MEM_MAX = "%max_memory%";
    public static final String HOURS = "%hours%";
    public static final String MINUTE = "%minutes%";
    public static final String SECONDS = "%seconds%";

    public static final String TABLE_MONEY = EterniaServer.serverConfig.getString("sql.table-money");
    public static final String TABLE_PLAYER = EterniaServer.serverConfig.getString("sql.table-player");
    public static final String TABLE_REWARD = EterniaServer.serverConfig.getString("sql.table-rewards");
    public static final String TABLE_SHOP = EterniaServer.serverConfig.getString("sql.table-shop");
    public static final String TABLE_WARP = EterniaServer.serverConfig.getString("sql.table-warp");

    public static String getQuerySelectAll(final String table) {
        return "SELECT * FROM " + table + ";";
    }

    public static String getQuerySelect(final String table, final String type, final String value) {
        return "SELECT * FROM " + table + " WHERE " + type + "='" + value + "';";
    }

    public static String getQueryDelete(final String table, final String type, final String value) {
        return "DELETE FROM " + table + " WHERE " + type + "='" + value + "';";
    }

    public static String getQueryUpdate(final String table, final String type, final Object value, final String type2, final Object value2) {
        return "UPDATE " + table + " SET " + type + "='" + value + "' WHERE " + type2 + "='" + value2 + "';";
    }

    public static String getQueryInsert(final String table, final String type, final Object value, final String type2, final Object value2) {
        return "INSERT INTO " + table + " (" + type + ", " + type2 + ") VALUES ('" + value + "', '" + value2 + "');";
    }

}
