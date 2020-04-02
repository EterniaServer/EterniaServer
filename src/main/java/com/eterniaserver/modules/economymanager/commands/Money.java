package com.eterniaserver.modules.economymanager.commands;

import com.eterniaserver.modules.economymanager.sql.Queries;
import com.eterniaserver.configs.MVar;
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
                    double money = Queries.getMoney(player.getName());
                    MVar.playerReplaceMessage("eco.money", df2.format(money), player);
                }
            } else if (args.length == 1) {
                if (player.hasPermission("eternia.money.other")) {
                    double money = Queries.getMoney(args[0]);
                    MVar.playerReplaceMessage("eco.money-other", df2.format(money), player);
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            }
        } else {
            DecimalFormat df2 = new DecimalFormat(".##");
            if (args.length == 1) {
                double money = Queries.getMoney(args[0]);
                MVar.consoleReplaceMessage("eco.money-other", df2.format(money));
            }
        }
        return true;
    }
}