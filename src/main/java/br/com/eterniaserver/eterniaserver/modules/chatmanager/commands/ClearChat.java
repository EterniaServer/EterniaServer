package br.com.eterniaserver.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChat implements CommandExecutor {

    private final Messages messages;

    public ClearChat(Messages messages) {
        this.messages = messages;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("eternia.chat.clear")) {
                for (int i = 0; i < 150; i++) Bukkit.broadcastMessage("");
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            for (int i = 0; i < 150; i++) Bukkit.broadcastMessage("");
        }
        return true;
    }
}
