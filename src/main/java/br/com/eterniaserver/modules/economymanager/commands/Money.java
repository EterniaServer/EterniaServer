package br.com.eterniaserver.modules.economymanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.economymanager.sql.MoneyAPI;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import java.text.DecimalFormat;

public class Money implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            DecimalFormat df2 = new DecimalFormat(".##");
            if (args.length == 0) {
                if (player.hasPermission("eternia.money")) {
                    double money = MoneyAPI.getMoney(player.getName());
                    Messages.PlayerMessage("eco.money", df2.format(money), player);
                }
            } else if (args.length == 1) {
                if (player.hasPermission("eternia.money.other")) {
                    double money = MoneyAPI.getMoney(args[0]);
                    Messages.PlayerMessage("eco.money-other", df2.format(money), player);
                } else {
                    Messages.PlayerMessage("server.no-perm", player);
                }
            }
        } else {
            DecimalFormat df2 = new DecimalFormat(".##");
            if (args.length == 1) {
                double money = MoneyAPI.getMoney(args[0]);
                Messages.ConsoleMessage("eco.money-other", df2.format(money));
            }
        }
        return true;
    }
}