package br.com.eterniaserver.modules.economymanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.Money;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class Pay implements CommandExecutor {

    private final Messages messages;
    private final Money moneyx;

    public Pay(Messages messages, Money moneyx) {
        this.messages = messages;
        this.moneyx = moneyx;
    }

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
                                    if (moneyx.getMoney(player.getName()) >= coins) {
                                        moneyx.addMoney(target.getName(), coins);
                                        moneyx.removeMoney(player.getName(), coins);
                                        messages.PlayerMessage("eco.pay", "%amount%", coins, "%target_name%", target.getName(), player);
                                        messages.PlayerMessage("eco.pay-me", "%amount%", coins, "%target_name%", player.getName(), target);
                                    } else {
                                        messages.PlayerMessage("eco.pay-nomoney", player);
                                    }
                                } else {
                                    messages.PlayerMessage("eco.auto-pay", player);
                                }
                            } else {
                                messages.PlayerMessage("server.player-offline", player);
                            }
                        } else {
                            messages.PlayerMessage("server.no-negative", player);
                        }
                    } catch (NumberFormatException e) {
                        messages.PlayerMessage("server.no-number", player);
                    }
                } else {
                    messages.PlayerMessage("eco.use", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}