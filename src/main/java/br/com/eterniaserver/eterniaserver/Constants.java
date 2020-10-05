package br.com.eterniaserver.eterniaserver;

import java.io.File;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DATA_LAYER_FOLDER_PATH = "plugins" + File.separator + "EterniaServer";
    public static final String DATA_LOCALE_FOLDER_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "locales";
    public static final String CONFIG_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "config.yml";
    public static final String COMMANDS_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "commands.yml";
    public static final String KITS_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "kits.yml";
    public static final String REWARDS_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "rewards.yml";
    public static final String SCHEDULE_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "schedule.yml";
    public static final String CHAT_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "chat.yml";
    public static final String CASHGUI_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "cashgui.yml";
    public static final String BLOCKS_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "blocks.yml";
    public static final String MESSAGES_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String COMMANDS_LOCALE_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "commands.yml";

    public static String getQueryCreateTable(String table, String values) {
        return "CREATE TABLE IF NOT EXISTS " + table + " " + values + ";";
    }

    public static String getQuerySelectAll(String table) {
        return "SELECT * FROM " + table + ";";
    }

    public static String getQueryDelete(String table, String type, String value) {
        return "DELETE FROM " + table + " WHERE " + type + "='" + value + "';";
    }

    public static String getQueryUpdate(String table, String type, Object value, String type2, Object value2) {
        return "UPDATE " + table + " SET " + type + "='" + value + "' WHERE " + type2 + "='" + value2 + "';";
    }

    public static String getQueryInsert(String table, String type, Object value, String type2, Object value2) {
        return "INSERT INTO " + table + " (" + type + ", " + type2 + ") VALUES ('" + value + "', '" + value2 + "');";
    }

    public static String getQueryInsert(String table, String type, Object value) {
        return "INSERT INTO " + table + " " + type + " VALUES " + value + ";";
    }

}
