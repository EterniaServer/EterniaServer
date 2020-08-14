package br.com.eterniaserver.eterniaserver.configs;

public interface Constants {

    String COMMANDS = "%command%";
    String MESSAGE = "%message%";
    String PLAYER = "%player_displayname%";
    String TARGET = "%target_displayname%";
    String PLAYER_DATA = "%player_register_data%";
    String PLAYER_LAST = "%player_last_login%";
    String VALUE = "%value%";
    String WARP = "%warp_name%";
    String WORLD = "%world_name%";
    String MODULE = "%module%";
    String AMOUNT = "%amount%";
    String TYPE = "%type%";
    String GROUP = "%group%";
    String KEY = "%key%";
    String MEM_USE = "%use_memory%";
    String MEM_MAX = "%max_memory%";
    String HOURS = "%hours%";
    String MINUTE = "%minutes%";
    String SECONDS = "%seconds%";
    String TPS = "%server_tps%";
    String TIME = "%time%";
    String KITS = "%kits%";
    String KIT_NAME = "%kit_name%";
    String COOLDOWN = "%cooldown%";
    String GM = "%gamemode%";
    String HOMES = "%homes%";
    String POSITION = "%position%";
    String NEW_NAME = "%new_name%";
    String ADVICE = "%advice%";
    String CHANNEL_NAME = "%channel_name%";
    String ERROR = "%error%";
    String NAME_STR = "name";
    String LOCATION_STR = "location";
    String PLAYER_NAME_STR = "player_name";
    String UUID_STR = "uuid";
    String PLAYER_DISPLAY_STR = "player_display";
    String BALANCE_STR = "balance";
    String CODE_STR = "code";
    String CODE_GROUP_STR = "lalalala";
    String TIME_STR = "time";
    String COOLDOWN_STR = "cooldown";
    String XP_STR = "xp";
    String HOMES_STR = "homes";
    String CLEAR_STR = "clear";
    String LAST_STR = "last";
    String HOURS_STR = "hours";

    static String getQueryCreateTable(final String table, final String values) {
        return "CREATE TABLE IF NOT EXISTS " + table + " " + values + ";";
    }

    static String getQuerySelectAll(final String table) {
        return "SELECT * FROM " + table + ";";
    }

    static String getQueryDelete(final String table, final String type, final String value) {
        return "DELETE FROM " + table + " WHERE " + type + "='" + value + "';";
    }

    static String getQueryUpdate(final String table, final String type, final Object value, final String type2, final Object value2) {
        return "UPDATE " + table + " SET " + type + "='" + value + "' WHERE " + type2 + "='" + value2 + "';";
    }

    static String getQueryInsert(final String table, final String type, final Object value, final String type2, final Object value2) {
        return "INSERT INTO " + table + " (" + type + ", " + type2 + ") VALUES ('" + value + "', '" + value2 + "');";
    }

    static String getQueryInsert(final String table, final String type, final Object value) {
        return "INSERT INTO " + table + " " + type + " VALUES " + value + ";";
    }

}
