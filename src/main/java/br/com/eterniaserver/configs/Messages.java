package br.com.eterniaserver.configs;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Messages {

    public static String putPAPI(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    public static void BroadcastMessage(String message) {
        Bukkit.broadcastMessage(putPrefix(message));
    }

    public static void BroadcastMessage(String message, String from, Object to) {
        message = putPrefix(message);
        Bukkit.broadcastMessage(message.replace(from, String.valueOf(to)));
    }

    public static void BroadcastMessage(String message, String from, Object to, String from_2,Object to_2) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        Bukkit.broadcastMessage(message.replace(from_2, String.valueOf(to_2)));
    }

    public static void BroadcastMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        message = message.replace(from_2, String.valueOf(to_2));
        Bukkit.broadcastMessage(message.replace(from_3, String.valueOf(to_3)));
    }

    public static void ConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(putPrefix(message));
    }

    public static void ConsoleMessage(String message, String from, Object to) {
        message = putPrefix(message);
        Bukkit.getConsoleSender().sendMessage(message.replace(from, String.valueOf(to)));
    }

    public static void ConsoleMessage(String message, String from, Object to, String from_2, Object to_2) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        Bukkit.getConsoleSender().sendMessage(message.replace(from_2, String.valueOf(to_2)));
    }

    public static void ConsoleMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        message = message.replace(from_2, String.valueOf(to_2));
        Bukkit.getConsoleSender().sendMessage(message.replace(from_3, String.valueOf(to_3)));
    }

    public static void PlayerMessage(String message, Player player) {
        player.sendMessage(putPrefix(message));
    }

    public static void PlayerMessage(String message, String from, Object to, Player player) {
        message = putPrefix(message);
        player.sendMessage(message.replace(from, String.valueOf(to)));
    }

    public static void PlayerMessage(String message, String from, Object to, String from_2, Object to_2, Player player) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        player.sendMessage(message.replace(from_2, String.valueOf(to_2)));
    }

    public static void PlayerMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3, Player player) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        message = message.replace(from_2, String.valueOf(to_2));
        player.sendMessage(message.replace(from_3, String.valueOf(to_3)));
    }

    public static void PlayerMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3, Player player, boolean console) {
        message = Strings.getMessage(message);
        message = message.replace(from, String.valueOf(to));
        message = message.replace(from_2, String.valueOf(to_2));
        message = message.replace(from_3, String.valueOf(to_3));
        player.sendMessage(message);
        if (console) {
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    private static java.lang.String putPrefix(String message) {
        return Strings.getMessage("server.prefix") + Strings.getMessage(message);
    }

}
