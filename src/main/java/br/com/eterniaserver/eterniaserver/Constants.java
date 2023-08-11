package br.com.eterniaserver.eterniaserver;

import java.io.File;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DATA_LAYER_FOLDER_PATH = "plugins" + File.separator + "EterniaServer";
    public static final String DATA_LOCALE_FOLDER_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "locales";
    public static final String CONFIG_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "config.yml";
    public static final String ENTITY_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "entity.yml";
    public static final String COMMANDS_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "commands.yml";
    public static final String KITS_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "kits.yml";
    public static final String SCHEDULE_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "schedule.yml";
    public static final String CHAT_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "chat.yml";
    public static final String ECONOMY_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "economy.yml";
    public static final String MESSAGES_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String COMMANDS_LOCALE_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "commands.yml";
    public static final String CONSTANTS_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "constants.yml";

    public static final String NEW = "_new";

}
