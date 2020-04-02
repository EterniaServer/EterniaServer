package com.eterniaserver.modules.teleportsmanager.commands;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.setwarp")) {
                    QueriesW.setWarp(player.getLocation(), args[0].toLowerCase());
                    MVar.playerReplaceMessage("warps.createwarp", args[0], player);
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            } else {
                MVar.playerMessage("warps.use2", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}
