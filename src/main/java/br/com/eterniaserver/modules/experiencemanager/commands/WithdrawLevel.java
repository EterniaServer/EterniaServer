package br.com.eterniaserver.modules.experiencemanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.Exp;
import br.com.eterniaserver.configs.methods.Checks;
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
                if (args.length == 1) {
                    try {
                        int xpla = Checks.getXPForLevel(Integer.parseInt(args[0]));
                        if (Exp.getExp(player.getName()) >= xpla) {
                            Exp.removeExp(player.getName(), xpla);
                            player.giveExp(xpla);
                            Messages.PlayerMessage("xp.withdraw", "%level%", player.getLevel(), player);
                        } else {
                            Messages.PlayerMessage("xp.noxp", player);
                        }
                    } catch (NumberFormatException e) {
                        Messages.PlayerMessage("server.no-number", player);
                    }
                } else {
                    Messages.PlayerMessage("xp.use2", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}