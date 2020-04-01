package com.eterniaserver.commands.messages;

import com.eterniaserver.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Donation implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.donation")) {
                MVar.playerMessage("text.donation", player);
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("doacao");
        }
        return true;
    }
}