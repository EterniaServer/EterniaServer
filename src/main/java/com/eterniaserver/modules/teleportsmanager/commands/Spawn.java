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

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            final Location location = QueriesW.getWarp("spawn");
            if (location != Vars.error) {
                if (args.length == 0) {
                    if (player.hasPermission("eternia.spawn")) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(location);
                            MVar.playerReplaceMessage("warps.warp", "Spawn", player);
                        } else {
                            MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                player.teleport(location);
                                MVar.playerReplaceMessage("warps.warp", "Spawn", player);
                            }, 20 * Vars.cooldown);
                        }
                    } else {
                        MVar.playerMessage("sem-permissao", player);
                    }
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.spawn.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            target.teleport(location);
                            MVar.playerReplaceMessage("warps.warp", "Spawn", player);
                            MVar.playerReplaceMessage("warps.spawn-other", target.getName(), player);
                        } else {
                            MVar.playerMessage("server.player-offline", player);
                        }
                    } else {
                        MVar.playerMessage("server.no-perm", player);
                    }
                } else {
                    MVar.playerMessage("warps.spawn-use", player);
                }
            } else {
                MVar.playerReplaceMessage("warps.spawnno", "Spawn", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}