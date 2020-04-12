package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.configs.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAll implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.spectator")) {
                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (other != player) {
                        other.teleport(player);
                    }
                }
                Messages.PlayerMessage("teleport.all", player);
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}