package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAll implements CommandExecutor {

    private final Messages messages;

    public TeleportAll(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpall")) {
                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (other != player) {
                        other.teleport(player);
                    }
                }
                messages.PlayerMessage("teleport.all", player);
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}