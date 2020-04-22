package br.com.eterniaserver.modules.economymanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.Money;
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
                                        Money.setMoney(target.getName(), money);
                                        Messages.PlayerMessage("eco.eco-set", "%money%", money, "%target_name", target.getName(), player);
                                        Messages.PlayerMessage("eco.eco-rset", "%money%", money, "%target_name%", player.getName(), target);
                                        break;
                                    case "remove":
                                        Money.removeMoney(target.getName(), money);
                                        Messages.PlayerMessage("eco.eco-remove", "%money%" ,money, "%target_name%", target.getName(), player);
                                        Messages.PlayerMessage("eco.eco-rremove", "%money%", money, "%target_name%", player.getName(), target);
                                        break;
                                    case "give":
                                        Money.addMoney(target.getName(), money);
                                        Messages.PlayerMessage("eco.eco-give", "%money%", money, "%target_name%", target.getName(), player);
                                        Messages.PlayerMessage("eco.eco-receive", "%money%", money, "%target_name%", player.getName(), target);
                                        break;
                                    default:
                                        Messages.PlayerMessage("eco.eco", player);
                                        break;
                                }
                            } else {
                                Messages.PlayerMessage("server.no-negative", player);
                            }
                        } catch (Exception e) {
                            Messages.PlayerMessage("server.no-number", player);
                        }
                    } else {
                        Messages.PlayerMessage("server.player-offline", player);
                    }
                } else {
                    Messages.PlayerMessage("eco.eco", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
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
                                        Money.setMoney(target.getName(), money);
                                        Messages.ConsoleMessage("eco.eco-set", "%money%", money, "%target_name%", target.getName());
                                        Messages.PlayerMessage("eco.eco-rset", "%money%", money, "%target_name%", "console", target);
                                    case "remove":
                                        Money.removeMoney(target.getName(), money);
                                        Messages.ConsoleMessage("eco.eco-remove", "%money%", money, "%target_name%", target.getName());
                                        Messages.PlayerMessage("eco.eco-rremove", "%money%", money, "%target_name%", "console", target);
                                    case "give":
                                        Money.addMoney(target.getName(), money);
                                        Messages.ConsoleMessage("eco.eco-give", "%money%", money, "%target_name%", target.getName());
                                        Messages.PlayerMessage("eco.eco-receive", "%money%", money, "%target_name%", "console", target);
                                    default:
                                        Messages.ConsoleMessage("eco.eco");
                                }
                            } else {
                                Messages.ConsoleMessage("server.no-negative");
                            }
                        } catch (NumberFormatException e) {
                            Messages.ConsoleMessage("server.no-number");
                        }
                    } else {
                        Messages.ConsoleMessage("server.player-offline");
                    }
                } catch (Exception e) {
                    Messages.ConsoleMessage("server.player-offline");
                }
            } else {
                Messages.ConsoleMessage("eco.eco");
            }
        }
        return true;
    }

}