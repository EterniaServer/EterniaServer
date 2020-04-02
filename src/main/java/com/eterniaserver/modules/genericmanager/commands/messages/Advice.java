package com.eterniaserver.modules.genericmanager.commands.messages;

import com.eterniaserver.configs.MVar;
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
                    MVar.broadcastReplaceMessage("text.global-advice", s);
                } else {
                    MVar.playerMessage("text.advice", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            if (args.length >= 1) {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                sb.substring(0, sb.length() - 1);
                String s = sb.toString();
                MVar.broadcastReplaceMessage("text.global-advice", s);
            } else {
                MVar.consoleMessage("text.advice");
            }
        }
        return true;
    }
}