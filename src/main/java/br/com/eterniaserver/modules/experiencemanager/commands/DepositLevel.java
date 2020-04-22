package br.com.eterniaserver.modules.experiencemanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.ExpAPI;
import br.com.eterniaserver.configs.methods.Checks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositLevel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.depositlvl")) {
                if (args.length == 1) {
                    int xp_atual = player.getLevel();
                    try {
                        int xpla = Integer.parseInt(args[0]);
                        if (xp_atual >= xpla) {
                            int xp = Checks.getXPForLevel(xpla);
                            int xpto = Checks.getXPForLevel(xp_atual);
                            ExpAPI.addExp(player.getName(), xp);
                            Messages.PlayerMessage("xp.deposit", "%amount%", xpla, player);
                            player.setLevel(0);
                            player.setExp(0);
                            player.giveExp(xpto - xp);
                        } else {
                            Messages.PlayerMessage("xp.noxp", player);
                        }
                    } catch (NumberFormatException e) {
                        Messages.PlayerMessage("server.no-number", player);
                    }
                } else {
                    Messages.PlayerMessage("xp.use", player);
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