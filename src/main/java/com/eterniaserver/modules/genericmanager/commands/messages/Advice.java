package com.eterniaserver.modules.genericmanager.commands.messages;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.methods.BroadcastMessage;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Advice implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.advice")) {
                if (args.length >= 1) {
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    sb.substring(0, sb.length() - 1);
                    String s = sb.toString();
                    new BroadcastMessage("text.global-advice", MVar.getColor(s));
                } else {
                    new PlayerMessage("text.advice", player);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length >= 1) {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                sb.substring(0, sb.length() - 1);
                String s = sb.toString();
                new BroadcastMessage("text.global-advice", MVar.getColor(s));
            } else {
                new ConsoleMessage("text.advice");
            }
        }
        return true;
    }
}