package br.com.eterniaserver.modules.economymanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.economymanager.sql.Queries;
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
                                        Messages.PlayerMessage("eco.pay", coins, target.getName(), player);
                                        Messages.PlayerMessage("eco.pay-me", coins, player.getName(), target);
                                    } else {
                                        Messages.PlayerMessage("eco.pay-nomoney", player);
                                    }
                                } else {
                                    Messages.PlayerMessage("eco.auto-pay", player);
                                }
                            } else {
                                Messages.PlayerMessage("server.player-offline", player);
                            }
                        } else {
                            Messages.PlayerMessage("server.no-negative", player);
                        }
                    } catch (NumberFormatException e) {
                        Messages.PlayerMessage("server.no-number", player);
                    }
                } else {
                    Messages.PlayerMessage("eco.use", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}