package com.eterniaserver.commands.teleports;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.api.WarpAPI;
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
                    if (WarpAPI.existWarp(args[0])) {
                        WarpAPI.delWarp(args[0]);
                        MVar.playerMessage("warps.delwarp", player);
                    } else {
                        MVar.playerReplaceMessage("warps.noexist", args[0], player);
                    }
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            } else {
                MVar.playerMessage("warps.deluse", player);
            }
        } else {
            MVar.consoleMessage("only-player");
        }
        return true;
    }
}
