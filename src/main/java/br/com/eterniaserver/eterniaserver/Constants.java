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
    public static final String CONSTANTS_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "constants.yml";

    protected void sendMessage(CommandSender sender, Messages messagesId, String... args) {
        sendMessage(sender, messagesId, true, args);
    }

    protected static void sendMessage(CommandSender sender, Messages messagesId, boolean prefix, String... args) {
        sender.sendMessage(EterniaServer.getMessage(messagesId, prefix, args));
    }

    protected static String getMessage(Messages messagesId, boolean prefix, String[] messages, String... args) {
        String message = messages[messagesId.ordinal()];

        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i]);
        }

        if (prefix) {
            return EterniaServer.getString(Strings.SERVER_PREFIX) + message;
        }

        return message;
    }

}
