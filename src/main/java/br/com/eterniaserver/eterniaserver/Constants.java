package br.com.eterniaserver.eterniaserver;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String MESSAGE = "%message%";
    public static final String PLAYER = "%player_displayname%";
    public static final String TARGET = "%target_displayname%";
    public static final String VALUE = "%value%";
    public static final String WARP = "%warp_name%";
    public static final String MODULE = "%module%";
    public static final String AMOUNT = "%amount%";
    public static final String TYPE = "%type%";
    public static final String TABLE = "%table%";

    public static final String TABLE_SHOP = EterniaServer.serverConfig.getString("sql.table-shop");
    public static final String TABLE_WARP = EterniaServer.serverConfig.getString("sql.table-warp");

    public static String getQuerySelect(final String table, final String type, final String value) {
        return "SELECT * FROM " + table + " WHERE " + type + "='" + value + "';";
    }

    public static String getQueryDelete(final String table, final String type, final String value) {
        return "DELETE FROM " + table + " WHERE " + type + "='" + value + "';";
    }

    public static String getQueryUpdate(final String table, final String type, final String value, final String type2, final String value2) {
        return "UPDATE " + table + " SET " + type + "='" + value + "' WHERE " + type2 + "='" + value2 + "';";
    }

    public static String getQueryInsert(final String table, final String type, final String value, final String type2, final String value2) {
        return "INSERT INTO " + table + " (" + type + ", " + type2 + ") VALUES ('" + value + "', '" + value2 + "');";
    }

}
