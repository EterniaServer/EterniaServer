package com.eterniaserver.modules.teleportsmanager.commands;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.warp." + args[0].toLowerCase())) {
                    final Location location = QueriesW.getWarp(args[0].toLowerCase());
                    if (location != Vars.error) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(location);
                            MVar.playerReplaceMessage("warps.warp", args[0], player);
                        } else {
                            MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                player.teleport(location);
                                MVar.playerReplaceMessage("warps.warp", args[0], player);
                            }, 20 * Vars.cooldown);
                        }
                    } else {
                        MVar.playerReplaceMessage("warps.noexist", args[0], player);
                    }
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            } else {
                MVar.playerMessage("warps.use", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}
