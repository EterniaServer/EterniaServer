package br.com.eterniaserver.eterniaserver.modules.experiencemanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.API.Exp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckLevel implements CommandExecutor {

    private final Messages messages;
    private final Exp expx;

    public CheckLevel(Messages messages, Exp expx) {
        this.messages = messages;
        this.expx = expx;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.checklvl")) {
                int lvl = player.getLevel();
                float xp = player.getExp();
                player.setLevel(0);
                player.setExp(0);
                player.giveExp(expx.getExp(player.getName()));
                messages.PlayerMessage("xp.getxp", "%amount%", player.getLevel(), player);
                player.setLevel(lvl);
                player.setExp(xp);
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }
}