package br.com.eterniaserver.eterniaserver.modules;

import java.io.File;

public class Constants {

    private Constants() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static final String DATA_LAYER_FOLDER_PATH = "plugins" + File.separator + "EterniaServer";
    public static final String CORE_CONFIG_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "core.yml";
    public static final String CORE_MESSAGES_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "messages.yml";
    public static final String CORE_COMMANDS_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "commands.yml";

    public static final String DATA_MODULE_FOLDER_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "modules";

    public static final String BED_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "bed";
    public static final String BED_CONFIG_FILE_PATH = BED_MODULE_FOLDER_PATH + File.separator + "bed.yml";
    public static final String BED_MESSAGES_FILE_PATH = BED_MODULE_FOLDER_PATH + File.separator + "messages.yml";

    public static final String CASH_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "cash";
    public static final String CASH_CONFIG_FILE_PATH = CASH_MODULE_FOLDER_PATH + File.separator + "cash.yml";
    public static final String CASH_MESSAGES_FILE_PATH = CASH_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String CASH_COMMANDS_FILE_PATH = CASH_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String CHAT_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "chat";
    public static final String CHAT_CONFIG_FILE_PATH = CHAT_MODULE_FOLDER_PATH + File.separator + "chat.yml";
    public static final String CHAT_MESSAGES_FILE_PATH = CHAT_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String CHAT_COMMANDS_FILE_PATH = CHAT_MODULE_FOLDER_PATH + File.separator + "commands.yml";
    public static final String CHAT_CHANNELS_FILE_PATH = CHAT_MODULE_FOLDER_PATH + File.separator + "channels.yml";

    public static final String ECONOMY_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "economy";
    public static final String ECONOMY_CONFIG_FILE_PATH = ECONOMY_MODULE_FOLDER_PATH + File.separator + "economy.yml";
    public static final String ECONOMY_MESSAGES_FILE_PATH = ECONOMY_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String ECONOMY_COMMANDS_FILE_PATH = ECONOMY_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String ELEVATOR_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "elevator";
    public static final String ELEVATOR_CONFIG_FILE_PATH = ELEVATOR_MODULE_FOLDER_PATH + File.separator + "elevator.yml";

    public static final String ENTITY_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "entity";
    public static final String ENTITY_CONFIG_FILE_PATH = ENTITY_MODULE_FOLDER_PATH + File.separator + "entity.yml";

    public static final String EXPERIENCE_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "experience";
    public static final String EXPERIENCE_CONFIG_FILE_PATH = EXPERIENCE_MODULE_FOLDER_PATH + File.separator + "config.yml";
    public static final String EXPERIENCE_MESSAGES_FILE_PATH = EXPERIENCE_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String EXPERIENCE_COMMANDS_FILE_PATH = EXPERIENCE_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String GLOW_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "glow";
    public static final String GLOW_CONFIG_FILE_PATH = GLOW_MODULE_FOLDER_PATH + File.separator + "config.yml";
    public static final String GLOW_MESSAGES_FILE_PATH = GLOW_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String GLOW_COMMANDS_FILE_PATH = GLOW_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String ITEM_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "item";
    public static final String ITEM_MESSAGES_FILE_PATH = ITEM_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String ITEM_COMMANDS_FILE_PATH = ITEM_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String KIT_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "kit";
    public static final String KIT_CONFIG_FILE_PATH = KIT_MODULE_FOLDER_PATH + File.separator + "config.yml";
    public static final String KIT_MESSAGES_FILE_PATH = KIT_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String KIT_COMMANDS_FILE_PATH = KIT_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String PAPI_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "papi";
    public static final String PAPI_CONFIG_FILE_PATH = PAPI_MODULE_FOLDER_PATH + File.separator + "papi.yml";

    public static final String REWARDS_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "rewards";
    public static final String REWARDS_CONFIG_FILE_PATH = REWARDS_MODULE_FOLDER_PATH + File.separator + "config.yml";
    public static final String REWARDS_MESSAGES_FILE_PATH = REWARDS_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String REWARDS_COMMANDS_FILE_PATH = REWARDS_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String SPAWNER_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "spawner";
    public static final String SPAWNER_CONFIG_FILE_PATH = SPAWNER_MODULE_FOLDER_PATH + File.separator + "config.yml";
    public static final String SPAWNER_MESSAGES_FILE_PATH = SPAWNER_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String SPAWNER_COMMANDS_FILE_PATH = SPAWNER_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String TELEPORT_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "teleport";
    public static final String TELEPORT_CONFIG_FILE_PATH = TELEPORT_MODULE_FOLDER_PATH + File.separator + "config.yml";
    public static final String TELEPORT_MESSAGES_FILE_PATH = TELEPORT_MODULE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String TELEPORT_COMMANDS_FILE_PATH = TELEPORT_MODULE_FOLDER_PATH + File.separator + "commands.yml";

    public static final String UTILITY_CLASS = "Utility class";

    public static final String TAG_FUNCTION = "eternia_function";
    public static final String TAG_INT_VALUE = "eternia_int_value";
    public static final String TAG_CASH_GUI_NAME = "cash_gui_name";
    public static final String TAG_CASH_ITEM_COST = "cash_item_cost";
    public static final String TAG_CASH_ITEM_MESSAGE = "cash_item_message";
    public static final String TAG_CASH_ITEM_COMMANDS = "cash_item_commands";
    public static final String TAG_CASH_ITEM_LORE = "cash_item_lore";
    public static final String TAG_SPAWNER = "eternia_spawner";
    public static final String TAG_WORLD = "eternia_wn";
    public static final String TAG_LOC_NAME = "eternia_lc";
    public static final String TAG_COORD_X = "eternia_x";
    public static final String TAG_COORD_Y = "eternia_y";
    public static final String TAG_COORD_Z = "eternia_z";
    public static final String TAG_COORD_YAW = "eternia_yaw";
    public static final String TAG_COORD_PITCH = "eternia_pitch";
    public static final String TAG_RUN_COMMAND = "eternia_cmd";
    public static final String TAG_RUN_IN_CONSOLE = "eternia_rcmd";
    public static final String TAG_USAGES = "eternia_usages";

}
