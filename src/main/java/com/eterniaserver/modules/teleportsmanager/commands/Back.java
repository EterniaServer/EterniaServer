package com.eterniaserver.modules.teleportsmanager.commands;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.modules.economymanager.sql.Queries;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
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
                if (Vars.back.containsKey(player)) {
                    double money = Queries.getMoney(player.getName());
                    double valor = CVar.getInt("money.back");
                    if (player.hasPermission("eternia.backfree") || !(Vars.economy)) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(Vars.back.get(player));
                            Vars.back.remove(player);
                            MVar.playerMessage("back.free", player);
                        } else {
                            MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                player.teleport(Vars.back.get(player));
                                Vars.back.remove(player);
                                MVar.playerMessage("back.free", player);
                            }, 20 * Vars.cooldown);
                        }
                    } else {
                        if (money >= valor) {
                            if (player.hasPermission("eternia.timing.bypass")) {
                                player.teleport(Vars.back.get(player));
                                Vars.back.remove(player);
                                Queries.removeMoney(player.getName(), valor);
                                MVar.playerReplaceMessage("back.nofree", valor, player);
                            } else {
                                MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                                {
                                    player.teleport(Vars.back.get(player));
                                    Vars.back.remove(player);
                                    Queries.removeMoney(player.getName(), valor);
                                    MVar.playerReplaceMessage("back.nofree", valor, player);
                                }, 20 * Vars.cooldown);
                            }
                        } else {
                            MVar.playerReplaceMessage("back.nomoney", valor, player);
                        }
                    }
                } else {
                    MVar.playerMessage("back.notp", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}