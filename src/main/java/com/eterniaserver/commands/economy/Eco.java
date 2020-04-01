package com.eterniaserver.commands.economy;

import com.eterniaserver.api.MoneyAPI;
import com.eterniaserver.configs.MVar;
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
                                        MoneyAPI.setMoney(target.getName(), money);
                                        MVar.playerReplaceMessage("eco.eco-set", money, target.getName(), player);
                                        MVar.playerReplaceMessage("eco.eco-rset", money, player.getName(), target);
                                        break;
                                    case "remove":
                                        MoneyAPI.removeMoney(target.getName(), money);
                                        MVar.playerReplaceMessage("eco.eco-remove", money, target.getName(), player);
                                        MVar.playerReplaceMessage("eco.eco-rremove", money, player.getName(), target);
                                        break;
                                    case "give":
                                        MoneyAPI.addMoney(target.getName(), money);
                                        MVar.playerReplaceMessage("eco.eco-give", money, target.getName(), player);
                                        MVar.playerReplaceMessage("eco.eco-receive", money, player.getName(), target);
                                        break;
                                    default:
                                        MVar.playerMessage("eco.eco", player);
                                        break;
                                }
                            } else {
                                MVar.playerMessage("server.no-negative", player);
                            }
                        } catch (Exception e) {
                            MVar.playerMessage("server.no-number", player);
                        }
                    } else {
                        MVar.playerMessage("server.player-offline", player);
                    }
                } else {
                    MVar.playerMessage("eco.eco", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
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
                                        MoneyAPI.setMoney(target.getName(), money);
                                        MVar.consoleReplaceMessage("eco.eco-set", money, target.getName());
                                        MVar.playerReplaceMessage("eco.eco-rset", money, "console", target);
                                    case "remove":
                                        MoneyAPI.removeMoney(target.getName(), money);
                                        MVar.consoleReplaceMessage("eco.eco-remove", money, target.getName());
                                        MVar.playerReplaceMessage("eco.eco-rremove", money, "console", target);
                                    case "give":
                                        MoneyAPI.addMoney(target.getName(), money);
                                        MVar.consoleReplaceMessage("eco.eco-give", money, target.getName());
                                        MVar.playerReplaceMessage("eco.eco-receive", money, "console", target);
                                    default:
                                        MVar.consoleMessage("eco.eco");
                                }
                            } else {
                                MVar.consoleMessage("server.no-negative");
                            }
                        } catch (NumberFormatException e) {
                            MVar.consoleMessage("server.no-number");
                        }
                    } else {
                        MVar.consoleMessage("server.player-offline");
                    }
                } catch (Exception e) {
                    MVar.consoleMessage("server.player-offline");
                }
            } else {
                MVar.consoleMessage("eco.eco");
            }
        }
        return true;
    }

}