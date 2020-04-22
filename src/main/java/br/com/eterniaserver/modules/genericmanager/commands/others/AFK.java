package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFK implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.afk")) {
                if (Vars.afk.contains(player.getName())) {
                    Messages.BroadcastMessage("text.afkd", "%player_name%", player.getName());
                    Vars.afk.remove(player.getName());
                } else {
                    Vars.afk.add(player.getName());
                    Messages.BroadcastMessage("text.afke", "%player_name%", player.getName());
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
