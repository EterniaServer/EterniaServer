package com.eterniaserver.modules.teleportsmanager.commands;

import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.delwarp")) {
                    if (QueriesW.existWarp(args[0])) {
                        QueriesW.delWarp(args[0]);
                        new PlayerMessage("warps.delwarp", player);
                    } else {
                        new PlayerMessage("warps.noexist", args[0], player);
                    }
                } else {
                    new PlayerMessage("server.no-perm", player);
                }
            } else {
                new PlayerMessage("warps.deluse", player);
            }
        } else {
            new ConsoleMessage("only-player");
        }
        return true;
    }
}
