package br.com.eterniaserver.modules.economymanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.Money;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Eco implements CommandExecutor {

    private final Messages messages;
    private final Money moneyx;

    public Eco(Messages messages, Money moneyx) {
        this.messages = messages;
        this.moneyx = moneyx;
    }

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
                                        moneyx.setMoney(target.getName(), money);
                                        messages.PlayerMessage("eco.eco-set", "%money%", money, "%target_name%", target.getName(), player);
                                        messages.PlayerMessage("eco.eco-rset", "%money%", money, "%target_name%", player.getName(), target);
                                        break;
                                    case "remove":
                                        moneyx.removeMoney(target.getName(), money);
                                        messages.PlayerMessage("eco.eco-remove", "%money%" ,money, "%target_name%", target.getName(), player);
                                        messages.PlayerMessage("eco.eco-rremove", "%money%", money, "%target_name%", player.getName(), target);
                                        break;
                                    case "give":
                                        moneyx.addMoney(target.getName(), money);
                                        messages.PlayerMessage("eco.eco-give", "%money%", money, "%target_name%", target.getName(), player);
                                        messages.PlayerMessage("eco.eco-receive", "%money%", money, "%target_name%", player.getName(), target);
                                        break;
                                    default:
                                        messages.PlayerMessage("eco.eco", player);
                                        break;
                                }
                            } else {
                                messages.PlayerMessage("server.no-negative", player);
                            }
                        } catch (Exception e) {
                            messages.PlayerMessage("server.no-number", player);
                        }
                    } else {
                        messages.PlayerMessage("server.player-offline", player);
                    }
                } else {
                    messages.PlayerMessage("eco.eco", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
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
                                        moneyx.setMoney(target.getName(), money);
                                        messages.ConsoleMessage("eco.eco-set", "%money%", money, "%target_name%", target.getName());
                                        messages.PlayerMessage("eco.eco-rset", "%money%", money, "%target_name%", "console", target);
                                    case "remove":
                                        moneyx.removeMoney(target.getName(), money);
                                        messages.ConsoleMessage("eco.eco-remove", "%money%", money, "%target_name%", target.getName());
                                        messages.PlayerMessage("eco.eco-rremove", "%money%", money, "%target_name%", "console", target);
                                    case "give":
                                        moneyx.addMoney(target.getName(), money);
                                        messages.ConsoleMessage("eco.eco-give", "%money%", money, "%target_name%", target.getName());
                                        messages.PlayerMessage("eco.eco-receive", "%money%", money, "%target_name%", "console", target);
                                    default:
                                        messages.ConsoleMessage("eco.eco");
                                }
                            } else {
                                messages.ConsoleMessage("server.no-negative");
                            }
                        } catch (NumberFormatException e) {
                            messages.ConsoleMessage("server.no-number");
                        }
                    } else {
                        messages.ConsoleMessage("server.player-offline");
                    }
                } catch (Exception e) {
                    messages.ConsoleMessage("server.player-offline");
                }
            } else {
                messages.ConsoleMessage("eco.eco");
            }
        }
        return true;
    }

}