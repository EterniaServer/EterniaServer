package com.eterniaserver.commands.messages;

import com.eterniaserver.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Colors implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.colors")) {
                MVar.playerMessage("text.color", player);
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("cores");
        }
        return true;
    }
}