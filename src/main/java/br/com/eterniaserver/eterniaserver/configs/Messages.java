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

    public void BroadcastMessage(String message) {
        Bukkit.broadcastMessage(putPrefix(message));
    }

    public void BroadcastMessage(String message, String from, Object to) {
        message = putPrefix(message);
        Bukkit.broadcastMessage(message.replace(from, String.valueOf(to)));
    }

    public void BroadcastMessage(String message, String from, Object to, String from_2,Object to_2) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        Bukkit.broadcastMessage(message.replace(from_2, String.valueOf(to_2)));
    }

    public void BroadcastMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        message = message.replace(from_2, String.valueOf(to_2));
        Bukkit.broadcastMessage(message.replace(from_3, String.valueOf(to_3)));
    }

    public void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(putPrefix(message));
    }

    public void sendConsole(String message, String from, Object to) {
        message = putPrefix(message);
        Bukkit.getConsoleSender().sendMessage(message.replace(from, String.valueOf(to)));
    }

    public void sendConsole(String message, String from, Object to, String from_2, Object to_2) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        Bukkit.getConsoleSender().sendMessage(message.replace(from_2, String.valueOf(to_2)));
    }

    public void sendConsole(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        message = message.replace(from_2, String.valueOf(to_2));
        Bukkit.getConsoleSender().sendMessage(message.replace(from_3, String.valueOf(to_3)));
    }

    public void PlayerMessage(String message, Player player) {
        player.sendMessage(putPrefix(message));
    }

    public void PlayerMessage(String message, String from, Object to, Player player) {
        message = putPrefix(message);
        player.sendMessage(message.replace(from, String.valueOf(to)));
    }

    public void PlayerMessage(String message, String from, Object to, String from_2, Object to_2, Player player) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        player.sendMessage(message.replace(from_2, String.valueOf(to_2)));
    }

    public void PlayerMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3, Player player) {
        message = putPrefix(message);
        message = message.replace(from, String.valueOf(to));
        message = message.replace(from_2, String.valueOf(to_2));
        player.sendMessage(message.replace(from_3, String.valueOf(to_3)));
    }

    public void PlayerMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3, Player player, boolean console) {
        message = strings.getMessage(message);
        message = message.replace(from, String.valueOf(to));
        message = message.replace(from_2, String.valueOf(to_2));
        message = message.replace(from_3, String.valueOf(to_3));
        player.sendMessage(message);
        if (console) {
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    public void sendMessage(String message, CommandSender sender) {
        sender.sendMessage(putPrefix(message));
    }

    public void sendMessage(String message, String from, Object to, CommandSender sender) {
        sender.sendMessage(putPrefix(message)
                .replace(from, String.valueOf(to)));
    }

    public void sendMessage(String message, String from, Object to, String from_2, Object to_2, CommandSender sender) {
        sender.sendMessage(putPrefix(message)
                .replace(from, String.valueOf(to))
                .replace(from_2, String.valueOf(to_2)));
    }

    public void sendMessage(String message, String from, Object to, String from_2, Object to_2, String from_3, Object to_3, CommandSender sender) {
        sender.sendMessage(putPrefix(message)
                .replace(from, String.valueOf(to))
                .replace(from_2, String.valueOf(to_2))
                .replace(from_3, String.valueOf(to_3)));
    }

    private String putPrefix(String message) {
        return strings.getMessage("server.prefix") + strings.getMessage(message);
    }

}
