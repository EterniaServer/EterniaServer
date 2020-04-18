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

    public static void BroadcastMessage(String message, Object valor) {
        message = putPrefix(message);
        Bukkit.broadcastMessage(message.replace("%s", String.valueOf(valor)));
    }

    public static void BroadcastMessage(String message, Object valor, Object valor2) {
        message = putPrefix(message);
        message = message.replace("%s", String.valueOf(valor));
        Bukkit.broadcastMessage(message.replace("%b", String.valueOf(valor2)));
    }

    public static void BroadcastMessage(String message, Object valor, Object valor2, Object valor3) {
        message = putPrefix(message);
        message = message.replace("%s", String.valueOf(valor));
        message = message.replace("%b", String.valueOf(valor2));
        Bukkit.broadcastMessage(message.replace("%v", String.valueOf(valor3)));
    }

    public static void ConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(putPrefix(message));
    }

    public static void ConsoleMessage(String message, Object valor) {
        message = putPrefix(message);
        Bukkit.getConsoleSender().sendMessage(message.replace("%s", String.valueOf(valor)));
    }

    public static void ConsoleMessage(String message, Object valor, Object valor2) {
        message = putPrefix(message);
        message = message.replace("%s", String.valueOf(valor));
        Bukkit.getConsoleSender().sendMessage(message.replace("%b", String.valueOf(valor2)));
    }

    public static void ConsoleMessage(String message, Object valor, Object valor2, Object valor3) {
        message = putPrefix(message);
        message = message.replace("%s", String.valueOf(valor));
        message = message.replace("%b", String.valueOf(valor2));
        Bukkit.getConsoleSender().sendMessage(message.replace("%v", String.valueOf(valor3)));
    }

    public static void PlayerMessage(String message, Player player) {
        player.sendMessage(putPrefix(message));
    }

    public static void PlayerMessage(String message, Object valor, Player player) {
        message = putPrefix(message);
        player.sendMessage(message.replace("%s", String.valueOf(valor)));
    }

    public static void PlayerMessage(String message, Object valor, Object valor2, Player player) {
        message = putPrefix(message);
        message = message.replace("%s", String.valueOf(valor));
        player.sendMessage(message.replace("%b", String.valueOf(valor2)));
    }

    public static void PlayerMessage(String message, Object valor, Object valor2, Object valor3, Player player) {
        message = putPrefix(message);
        message = message.replace("%s", String.valueOf(valor));
        message = message.replace("%b", String.valueOf(valor2));
        player.sendMessage(message.replace("%v", String.valueOf(valor3)));
    }

    public static void PlayerMessage(String message, Object valor, Object valor2, Object valor3, Player player, boolean console) {
        message = Strings.getMessage(message);
        message = message.replace("%s", String.valueOf(valor));
        message = message.replace("%b", String.valueOf(valor2));
        player.sendMessage(message.replace("%v", String.valueOf(valor3)));
        if (console) {
            Bukkit.getConsoleSender().sendMessage(message.replace("%v", String.valueOf(valor3)));
        }
    }

    private static java.lang.String putPrefix(String message) {
        return Strings.getMessage("server.prefix") + Strings.getMessage(message);
    }

}
