package br.com.eterniaserver.eterniaserver.configs;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {

    private final Strings strings;

    public Messages(Strings strings) {
        this.strings = strings;
    }

    public String putPAPI(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    public void broadcastMessage(String message) {
        broadcastMessage(message, null, null, null, null, null, null);
    }

    public void broadcastMessage(String message, String from, Object to) {
        broadcastMessage(message, from, to, null, null, null, null);
    }

    public void broadcastMessage(String message, String from, Object to, String from_2, Object to_2) {
        broadcastMessage(message, from, to, from_2, to_2, null, null);
    }

    public void broadcastMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3) {
        Bukkit.broadcastMessage(formatWithPrefix(message, from, to, from_2, to_2, from_3, to_3));
    }

    public void sendConsole(String message) {
        sendConsole(message, null, null, null, null, null, null);
    }

    public void sendConsole(String message, String from, Object to) {
        sendConsole(message, from, to, null, null, null, null);
    }

    public void sendConsole(String message, String from, Object to, String from_2, Object to_2) {
        sendConsole(message, from, to, from_2, to_2, null, null);
    }

    public void sendConsole(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3) {
        Bukkit.getConsoleSender().sendMessage(formatWithPrefix(message, from, to, from_2, to_2, from_3, to_3));
    }

    public void sendMessage(String message, CommandSender sender) {
        sendMessage(message, null, null, null, null, null, null, sender);
    }

    public void sendMessage(String message, String from, Object to, CommandSender sender) {
        sendMessage(message, from, to, null, null, null, null, sender);
    }

    public void sendMessage(String message, String from, Object to, String from_2, Object to_2, CommandSender sender) {
        sendMessage(message, from, to, from_2, to_2, null, null, sender);
    }

    public void sendMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3, CommandSender sender) {
        sender.sendMessage(formatWithPrefix(message, from, to, from_2, to_2, from_3, to_3));
    }

    private String formatWithPrefix(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3) {
        message = putPrefix(message);
        if (from != null) message = message.replace(from, String.valueOf(to));
        if (from_2 != null) message = message.replace(from_2, String.valueOf(to_2));
        if (from_3 != null) message = message.replace(from_3, String.valueOf(to_3));
        return message;
    }

    private String putPrefix(String message) {
        return strings.getMessage("server.prefix") + strings.getMessage(message);
    }

}
