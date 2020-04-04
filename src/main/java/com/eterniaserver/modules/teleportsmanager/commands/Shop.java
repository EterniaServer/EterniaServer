package com.eterniaserver.modules.teleportsmanager.commands;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.modules.teleportsmanager.sql.QueriesP;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.Vars;
import com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Shop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                final Location location = QueriesW.getWarp("shop");
                if (player.hasPermission("eternia.warp.shop")) {
                    if (location != Vars.error) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(location);
                            MVar.playerReplaceMessage("warps.warp", "Loja", player);
                        } else {
                            MVar.playerReplaceMessage("teleport.timing", CVar.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player, player.getLocation());
                            Vars.moved.put(player, false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                if (!Vars.moved.get(player)) {
                                    player.teleport(location);
                                    MVar.playerReplaceMessage("warps.warp", "Loja", player);
                                } else {
                                    MVar.playerMessage("warps.move", player);
                                }
                            }, 20 * CVar.getInt("server.cooldown"));
                        }
                    } else {
                        MVar.playerReplaceMessage("warps.noexist", "shop", player);
                    }
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            } else if (args.length == 1) {
                final Location location = QueriesP.getShop(args[0].toLowerCase());
                if (player.hasPermission("eternia.shop.player")) {
                    if (location != Vars.error) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(location);
                            MVar.playerReplaceMessage("warps.shopp", args[0], player);
                        } else {
                            MVar.playerReplaceMessage("teleport.timing", CVar.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player, player.getLocation());
                            Vars.moved.put(player, false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                if (!Vars.moved.get(player)) {
                                    player.teleport(location);
                                    MVar.playerReplaceMessage("warps.shopp", args[0], player);
                                } else {
                                    MVar.playerMessage("warps.move", player);
                                }
                            }, 20 * CVar.getInt("server.cooldown"));
                        }
                    } else {
                        MVar.playerReplaceMessage("warps.shopno", "Loja", player);
                    }
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            } else {
                MVar.playerMessage("warps.shopuse", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}