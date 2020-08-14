package br.com.eterniaserver.eterniaserver.configs;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String COMMANDS = "%command%";
    public static final String MESSAGE = "%message%";
    public static final String PLAYER = "%player_displayname%";
    public static final String TARGET = "%target_displayname%";
    public static final String PLAYER_DATA = "%player_register_data%";
    public static final String PLAYER_LAST = "%player_last_login%";
    public static final String VALUE = "%value%";
    public static final String WARP = "%warp_name%";
    public static final String WORLD = "%world_name%";
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
    public static final String TPS = "%server_tps%";
    public static final String TIME = "%time%";
    public static final String KITS = "%kits%";
    public static final String KIT_NAME = "%kit_name%";
    public static final String COOLDOWN = "%cooldown%";
    public static final String GM = "%gamemode%";
    public static final String HOMES = "%homes%";
    public static final String POSITION = "%position%";
    public static final String NEW_NAME = "%new_name%";
    public static final String ADVICE = "%advice%";
    public static final String CHANNEL_NAME = "%channel_name%";
    public static final String ERROR = "%error%";
    public static final String NAME_STR = "name";
    public static final String LOCATION_STR = "location";
    public static final String PLAYER_NAME_STR = "player_name";
    public static final String UUID_STR = "uuid";
    public static final String PLAYER_DISPLAY_STR = "player_display";
    public static final String BALANCE_STR = "balance";
    public static final String CODE_STR = "code";
    public static final String CODE_GROUP_STR = "lalalala";
    public static final String TIME_STR = "time";
    public static final String COOLDOWN_STR = "cooldown";
    public static final String XP_STR = "xp";
    public static final String HOMES_STR = "homes";
    public static final String CLEAR_STR = "clear";
    public static final String LAST_STR = "last";
    public static final String HOURS_STR = "hours";

    public static String getQueryCreateTable(final String table, final String values) {
        return "CREATE TABLE IF NOT EXISTS " + table + " " + values + ";";
    }

    public static String getQuerySelectAll(final String table) {
        return "SELECT * FROM " + table + ";";
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

    public static String getQueryInsert(final String table, final String type, final Object value) {
        return "INSERT INTO " + table + " " + type + " VALUES " + value + ";";
    }

}
