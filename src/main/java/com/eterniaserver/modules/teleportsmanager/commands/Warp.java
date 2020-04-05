package com.eterniaserver.modules.teleportsmanager.commands;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
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
                            new PlayerMessage("warps.warp", args[0], player);
                        } else {
                            new PlayerMessage("teleport.timing", CVar.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player, player.getLocation());
                            Vars.moved.put(player, false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                if (!Vars.moved.get(player)) {
                                    player.teleport(location);
                                    new PlayerMessage("warps.warp", args[0], player);
                                } else {
                                    new PlayerMessage("warps.move", player);
                                }
                            }, 20 * CVar.getInt("server.cooldown"));
                        }
                    } else {
                        new PlayerMessage("warps.noexist", args[0], player);
                    }
                } else {
                    new PlayerMessage("server.no-perm", player);
                }
            } else {
                new PlayerMessage("warps.use", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}
