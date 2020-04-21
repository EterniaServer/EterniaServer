package br.com.eterniaserver.modules.experiencemanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.ExpAPI;
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
                Messages.PlayerMessage("xp.withdraw", player.getLevel(), player);
                player.giveExp(ExpAPI.takeExp(player.getName()));
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}