package br.com.eterniaserver.modules.chatmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Nickname implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.nickname")) {
                if (args.length == 2) {
                    if (player.hasPermission("eternia.nickname.other")) {

                    }
                } else if (args.length == 1) {
                    if (args[0].equals("clear")) {
                        player.setDisplayName(player.getName());
                        
                    }
                } else {

                }
            }
        }
        return true;
    }

}