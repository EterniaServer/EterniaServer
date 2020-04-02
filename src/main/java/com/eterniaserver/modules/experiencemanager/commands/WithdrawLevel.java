package com.eterniaserver.modules.experiencemanager.commands;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.modules.experiencemanager.sql.Queries;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawLevel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.withdrawlvl")) {
                MVar.playerReplaceMessage("xp.withdraw", player.getLevel(), player);
                player.giveExp(Queries.takeExp(player.getName()));
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}