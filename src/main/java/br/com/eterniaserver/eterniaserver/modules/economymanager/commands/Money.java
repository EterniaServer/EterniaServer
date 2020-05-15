package br.com.eterniaserver.eterniaserver.modules.economymanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import java.text.DecimalFormat;

public class Money implements CommandExecutor {

    private final Messages messages;
    private final br.com.eterniaserver.eterniaserver.API.Money moneyx;

    public Money(Messages messages, br.com.eterniaserver.eterniaserver.API.Money moneyx) {
        this.messages = messages;
        this.moneyx = moneyx;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            DecimalFormat df2 = new DecimalFormat(".##");
            if (args.length == 0) {
                if (player.hasPermission("eternia.money")) {
                    double money = moneyx.getMoney(player.getName());
                    messages.PlayerMessage("eco.money", "%money%", df2.format(money), player);
                }
            } else if (args.length == 1) {
                if (player.hasPermission("eternia.money.other")) {
                    double money = moneyx.getMoney(args[0]);
                    messages.PlayerMessage("eco.money-other", "%money%", df2.format(money), player);
                } else {
                    messages.PlayerMessage("server.no-perm", player);
                }
            }
        } else {
            DecimalFormat df2 = new DecimalFormat(".##");
            if (args.length == 1) {
                double money = moneyx.getMoney(args[0]);
                messages.sendConsole("eco.money-other", "%money%", df2.format(money));
            }
        }
        return true;
    }
}