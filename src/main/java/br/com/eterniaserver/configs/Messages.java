package br.com.eterniaserver.configs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Messages {

    public static void BroadcastMessage(java.lang.String message) {
        Bukkit.broadcastMessage(putPrefix(message));
    }

    public static void BroadcastMessage(java.lang.String message, Object valor) {
        message = putPrefix(message);
        Bukkit.broadcastMessage(message.replace("%s", java.lang.String.valueOf(valor)));
    }

    public static void BroadcastMessage(java.lang.String message, Object valor, Object valor2) {
        message = putPrefix(message);
        message = message.replace("%s", java.lang.String.valueOf(valor));
        Bukkit.broadcastMessage(message.replace("%b", java.lang.String.valueOf(valor2)));
    }

    public static void BroadcastMessage(java.lang.String message, Object valor, Object valor2, Object valor3) {
        message = putPrefix(message);
        message = message.replace("%s", java.lang.String.valueOf(valor));
        message = message.replace("%b", java.lang.String.valueOf(valor2));
        Bukkit.broadcastMessage(message.replace("%v", java.lang.String.valueOf(valor3)));
    }

    public static void ConsoleMessage(java.lang.String message) {
        Bukkit.getConsoleSender().sendMessage(putPrefix(message));
    }

    public static void ConsoleMessage(java.lang.String message, Object valor) {
        message = putPrefix(message);
        Bukkit.getConsoleSender().sendMessage(message.replace("%s", java.lang.String.valueOf(valor)));
    }

    public static void ConsoleMessage(java.lang.String message, Object valor, Object valor2) {
        message = putPrefix(message);
        message = message.replace("%s", java.lang.String.valueOf(valor));
        Bukkit.getConsoleSender().sendMessage(message.replace("%b", java.lang.String.valueOf(valor2)));
    }

    public static void ConsoleMessage(java.lang.String message, Object valor, Object valor2, Object valor3) {
        message = putPrefix(message);
        message = message.replace("%s", java.lang.String.valueOf(valor));
        message = message.replace("%b", java.lang.String.valueOf(valor2));
        Bukkit.getConsoleSender().sendMessage(message.replace("%v", java.lang.String.valueOf(valor3)));
    }

    public static void PlayerMessage(java.lang.String message, Player player) {
        player.sendMessage(putPrefix(message));
    }

    public static void PlayerMessage(java.lang.String message, Object valor, Player player) {
        message = putPrefix(message);
        player.sendMessage(message.replace("%s", java.lang.String.valueOf(valor)));
    }

    public static void PlayerMessage(java.lang.String message, Object valor, Object valor2, Player player) {
        message = putPrefix(message);
        message = message.replace("%s", java.lang.String.valueOf(valor));
        player.sendMessage(message.replace("%b", java.lang.String.valueOf(valor2)));
    }

    public static void PlayerMessage(java.lang.String message, Object valor, Object valor2, Object valor3, Player player) {
        message = putPrefix(message);
        message = message.replace("%s", java.lang.String.valueOf(valor));
        message = message.replace("%b", java.lang.String.valueOf(valor2));
        player.sendMessage(putPrefix(message).replace("%v", java.lang.String.valueOf(valor3)));
    }

    private static java.lang.String putPrefix(java.lang.String message) {
        return Strings.getMessage("server.prefix") + Strings.getMessage(message);
    }

}
