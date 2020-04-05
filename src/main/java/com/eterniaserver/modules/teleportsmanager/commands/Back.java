package com.eterniaserver.modules.teleportsmanager.commands;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import com.eterniaserver.modules.economymanager.sql.Queries;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.back")) {
                if (Vars.back.containsKey(player.getName())) {
                    double money = Queries.getMoney(player.getName());
                    double valor = CVar.getInt("money.back");
                    if (player.hasPermission("eternia.backfree") || !(CVar.getBool("modules.economy"))) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(Vars.back.get(player.getName()));
                            Vars.back.remove(player.getName());
                            new PlayerMessage("back.free", player);
                        } else {
                            new PlayerMessage("teleport.timing", CVar.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player, player.getLocation());
                            Vars.moved.put(player, false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                if (!Vars.moved.get(player)) {
                                    player.teleport(Vars.back.get(player.getName()));
                                    Vars.back.remove(player.getName());
                                    new PlayerMessage("back.free", player);
                                } else {
                                    new PlayerMessage("warps.move", player);
                                }
                            }, 20 * CVar.getInt("server.cooldown"));
                        }
                    } else {
                        if (money >= valor) {
                            if (player.hasPermission("eternia.timing.bypass")) {
                                player.teleport(Vars.back.get(player.getName()));
                                Vars.back.remove(player.getName());
                                Queries.removeMoney(player.getName(), valor);
                                new PlayerMessage("back.nofree", valor, player);
                            } else {
                                new PlayerMessage("teleport.timing", CVar.getInt("server.cooldown"), player);
                                Vars.moved.put(player, false);
                                Vars.playerposition.put(player, player.getLocation());
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                                {
                                    if (!Vars.moved.get(player)) {
                                        player.teleport(Vars.back.get(player.getName()));
                                        Vars.back.remove(player.getName());
                                        Queries.removeMoney(player.getName(), valor);
                                        new PlayerMessage("back.nofree", valor, player);
                                    } else {
                                        new PlayerMessage("warps.move", player);
                                    }
                                }, 20 * CVar.getInt("server.cooldown"));
                            }
                        } else {
                            new PlayerMessage("back.nomoney", valor, player);
                        }
                    }
                } else {
                    new PlayerMessage("back.notp", player);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}