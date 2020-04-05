package com.eterniaserver.modules.economymanager.commands;

import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import com.eterniaserver.modules.economymanager.sql.Queries;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Eco implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.eco")) {
                if (args.length == 3) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        String fun = args[1];
                        try {
                            double money = Double.parseDouble(args[2]);
                            if (money > 0) {
                                switch (fun) {
                                    case "set":
                                        Queries.setMoney(target.getName(), money);
                                        new PlayerMessage("eco.eco-set", money, target.getName(), player);
                                        new PlayerMessage("eco.eco-rset", money, player.getName(), target);
                                        break;
                                    case "remove":
                                        Queries.removeMoney(target.getName(), money);
                                        new PlayerMessage("eco.eco-remove", money, target.getName(), player);
                                        new PlayerMessage("eco.eco-rremove", money, player.getName(), target);
                                        break;
                                    case "give":
                                        Queries.addMoney(target.getName(), money);
                                        new PlayerMessage("eco.eco-give", money, target.getName(), player);
                                        new PlayerMessage("eco.eco-receive", money, player.getName(), target);
                                        break;
                                    default:
                                        new PlayerMessage("eco.eco", player);
                                        break;
                                }
                            } else {
                                new PlayerMessage("server.no-negative", player);
                            }
                        } catch (Exception e) {
                            new PlayerMessage("server.no-number", player);
                        }
                    } else {
                        new PlayerMessage("server.player-offline", player);
                    }
                } else {
                    new PlayerMessage("eco.eco", player);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 3) {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    assert target != null;
                    if (target.isOnline()) {
                        String fun = args[1];
                        try {
                            double money = Double.parseDouble(args[2]);
                            if (money > 0) {
                                switch (fun) {
                                    case "set":
                                        Queries.setMoney(target.getName(), money);
                                        new ConsoleMessage("eco.eco-set", money, target.getName());
                                        new PlayerMessage("eco.eco-rset", money, "console", target);
                                    case "remove":
                                        Queries.removeMoney(target.getName(), money);
                                        new ConsoleMessage("eco.eco-remove", money, target.getName());
                                        new PlayerMessage("eco.eco-rremove", money, "console", target);
                                    case "give":
                                        Queries.addMoney(target.getName(), money);
                                        new ConsoleMessage("eco.eco-give", money, target.getName());
                                        new PlayerMessage("eco.eco-receive", money, "console", target);
                                    default:
                                        new ConsoleMessage("eco.eco");
                                }
                            } else {
                                new ConsoleMessage("server.no-negative");
                            }
                        } catch (NumberFormatException e) {
                            new ConsoleMessage("server.no-number");
                        }
                    } else {
                        new ConsoleMessage("server.player-offline");
                    }
                } catch (Exception e) {
                    new ConsoleMessage("server.player-offline");
                }
            } else {
                new ConsoleMessage("eco.eco");
            }
        }
        return true;
    }

}