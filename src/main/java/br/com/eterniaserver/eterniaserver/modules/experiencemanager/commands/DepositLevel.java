package br.com.eterniaserver.eterniaserver.modules.experiencemanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.API.Exp;
import br.com.eterniaserver.eterniaserver.configs.methods.Checks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositLevel implements CommandExecutor {

    private final Checks checks;
    private final Messages messages;
    private final Exp expx;

    public DepositLevel(Checks checks, Messages messages, Exp expx) {
        this.checks = checks;
        this.messages = messages;
        this.expx = expx;
    }

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
                            int xp = checks.getXPForLevel(xpla);
                            int xpto = checks.getXPForLevel(xp_atual);
                            expx.addExp(player.getName(), xp);
                            messages.PlayerMessage("xp.deposit", "%amount%", xpla, player);
                            player.setLevel(0);
                            player.setExp(0);
                            player.giveExp(xpto - xp);
                        } else {
                            messages.PlayerMessage("xp.noxp", player);
                        }
                    } catch (NumberFormatException e) {
                        messages.PlayerMessage("server.no-number", player);
                    }
                } else {
                    messages.PlayerMessage("xp.use", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }
}