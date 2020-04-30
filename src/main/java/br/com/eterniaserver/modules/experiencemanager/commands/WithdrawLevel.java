package br.com.eterniaserver.modules.experiencemanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.Exp;
import br.com.eterniaserver.configs.methods.Checks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawLevel implements CommandExecutor {

    private final Checks checks;
    private final Messages messages;
    private final Exp expx;

    public WithdrawLevel(Checks checks, Messages messages, Exp expx) {
        this.checks = checks;
        this.messages = messages;
        this.expx = expx;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.withdrawlvl")) {
                if (args.length == 1) {
                    try {
                        int xpla = checks.getXPForLevel(Integer.parseInt(args[0]));
                        if (expx.getExp(player.getName()) >= xpla) {
                            expx.removeExp(player.getName(), xpla);
                            player.giveExp(xpla);
                            messages.PlayerMessage("xp.withdraw", "%level%", player.getLevel(), player);
                        } else {
                            messages.PlayerMessage("xp.noxp", player);
                        }
                    } catch (NumberFormatException e) {
                        messages.PlayerMessage("server.no-number", player);
                    }
                } else {
                    messages.PlayerMessage("xp.use2", player);
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