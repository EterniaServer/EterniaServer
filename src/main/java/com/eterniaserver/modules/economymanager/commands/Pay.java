package com.eterniaserver.modules.economymanager.commands;

import com.eterniaserver.modules.economymanager.sql.Queries;
import com.eterniaserver.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (sender.hasPermission("eternia.pay")) {
                if (args.length == 2) {
                    double coins;
                    try {
                        coins = Double.parseDouble(args[1]);
                        if (coins > -1) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target != null && target.isOnline()) {
                                if (!(target.equals(player))) {
                                    if (Queries.getMoney(player.getName()) >= coins) {
                                        Queries.addMoney(target.getName(), coins);
                                        Queries.removeMoney(player.getName(), coins);
                                        MVar.playerReplaceMessage("eco.pay", coins, target.getName(), player);
                                        MVar.playerReplaceMessage("eco.pay-me", coins, player.getName(), target);
                                    } else {
                                        MVar.playerMessage("eco.pay-nomoney", player);
                                    }
                                } else {
                                    MVar.playerMessage("eco.auto-pay", player);
                                }
                            } else {
                                MVar.playerMessage("server.player-offline", player);
                            }
                        } else {
                            MVar.playerMessage("server.no-negative", player);
                        }
                    } catch (NumberFormatException e) {
                        MVar.playerMessage("server.no-number", player);
                    }
                } else {
                    MVar.playerMessage("eco.use", player);
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