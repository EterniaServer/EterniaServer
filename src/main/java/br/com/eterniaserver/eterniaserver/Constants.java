package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import org.bukkit.command.CommandSender;

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
    public static final String REWARDS_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "rewards.yml";
    public static final String SCHEDULE_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "schedule.yml";
    public static final String CHAT_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "chat.yml";
    public static final String CASHGUI_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "cashgui.yml";
    public static final String BLOCKS_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "blocks.yml";
    public static final String MESSAGES_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String COMMANDS_LOCALE_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "commands.yml";
    public static final String CONSTANTS_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "constants.yml";

    public static final String NBT_FUNCTION = "eternia_function";

    public static final String NBT_INT_VALUE = "eternia_int_value";
    public static final String NBT_WORLD = "eternia_wn";
    public static final String NBT_LOC_NAME = "eternia_lc";
    public static final String NBT_COORD_X = "eternia_x";
    public static final String NBT_COORD_Y = "eternia_y";
    public static final String NBT_COORD_Z = "eternia_z";
    public static final String NBT_COORD_YAW = "eternia_yaw";
    public static final String NBT_COORD_PITCH = "eternia_pitch";
    public static final String NBT_RUN_COMMAND = "eternia_cmd";
    public static final String NBT_RUN_IN_CONSOLE = "eternia_rcmd";
    public static final String NBT_USAGES = "eternia_usages";

    public static final String NEW = "_new";

    protected void sendMessage(CommandSender sender, Messages messagesId, String... args) {
        sendMessage(sender, messagesId, true, args);
    }

    protected static void sendMessage(CommandSender sender, Messages messagesId, boolean prefix, String... args) {
        sender.sendMessage(getMessage(messagesId, prefix, args));
    }

    protected static String getMessage(Messages messagesId, boolean prefix, String... args) {
        String message = EterniaServer.messages[messagesId.ordinal()];

        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i]);
        }

        if (prefix) {
            return EterniaServer.getString(Strings.SERVER_PREFIX) + message;
        }

        return message;
    }

}
