package br.com.eterniaserver.modules.experiencemanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.ExpAPI;
import br.com.eterniaserver.configs.methods.Checks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckLevel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.checklevel")) {
                int lvl = player.getLevel();
                float xp = player.getExp();
                player.setLevel(0);
                player.setExp(0);
                player.giveExp(ExpAPI.getExp(player.getName()));
                Messages.PlayerMessage("xp.getxp", "%amount%", player.getLevel(), player);
                player.setLevel(lvl);
                player.setExp(xp);
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}