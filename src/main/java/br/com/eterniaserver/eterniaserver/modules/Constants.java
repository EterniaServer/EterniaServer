package br.com.eterniaserver.eterniaserver.modules;

import java.io.File;

public class Constants {

    private Constants() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static final String DATA_LAYER_FOLDER_PATH = "plugins" + File.separator + "EterniaServer";
    public static final String CORE_CONFIG_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "core.yml";
    public static final String DATA_MODULE_FOLDER_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "modules";

    public static final String SPAWNER_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "spawner";
    public static final String SPAWNER_CONFIG_FILE_PATH = SPAWNER_MODULE_FOLDER_PATH + File.separator + "spawner.yml";

    public static final String EXPERIENCE_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "experience";
    public static final String EXPERIENCE_CONFIG_FILE_PATH = EXPERIENCE_MODULE_FOLDER_PATH + File.separator + "experience.yml";

    public static final String ELEVATOR_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "elevator";
    public static final String ELEVATOR_CONFIG_FILE_PATH = ELEVATOR_MODULE_FOLDER_PATH + File.separator + "elevator.yml";

    public static final String REWARDS_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "rewards";
    public static final String REWARDS_CONFIG_FILE_PATH = REWARDS_MODULE_FOLDER_PATH + File.separator + "rewards.yml";

    public static final String GLOW_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "glow";
    public static final String GLOW_CONFIG_FILE_PATH = GLOW_MODULE_FOLDER_PATH + File.separator + "glow.yml";

    public static final String PAPI_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "papi";
    public static final String PAPI_CONFIG_FILE_PATH = PAPI_MODULE_FOLDER_PATH + File.separator + "papi.yml";

    public static final String CASH_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "cash";
    public static final String CASH_CONFIG_FILE_PATH = CASH_MODULE_FOLDER_PATH + File.separator + "cash.yml";

    public static final String ENTITY_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "entity";
    public static final String ENTITY_CONFIG_FILE_PATH = ENTITY_MODULE_FOLDER_PATH + File.separator + "entity.yml";

    public static final String ECONOMY_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "economy";
    public static final String ECONOMY_CONFIG_FILE_PATH = ECONOMY_MODULE_FOLDER_PATH + File.separator + "economy.yml";


    public static final String UTILITY_CLASS = "Utility class";

    public static final String TAG_FUNCTION = "eternia_function";
    public static final String TAG_INT_VALUE = "eternia_int_value";
    public static final String TAG_CASH_GUI_NAME = "cash_gui_name";
    public static final String TAG_CASH_ITEM_COST = "cash_item_cost";
    public static final String TAG_CASH_ITEM_MESSAGE = "cash_item_message";
    public static final String TAG_CASH_ITEM_COMMANDS = "cash_item_commands";
    public static final String TAG_CASH_ITEM_LORE = "cash_item_lore";
    public static final String TAG_SPAWNER = "eternia_spawner";

}
