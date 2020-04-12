package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.player.PlayerFlyState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.fly")) {
                if (args.length == 0) {
                    if (player.getWorld() == Bukkit.getWorld("evento")) {
                        if (player.hasPermission("eternia.fly.evento")) {
                            new PlayerFlyState(player);
                        } else {
                            Messages.PlayerMessage("server.no-perm", player);
                        }
                    } else {
                        new PlayerFlyState(player);
                    }
                    return true;
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.fly.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            new PlayerFlyState(target);
                        } else {
                            Messages.PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        Messages.PlayerMessage("server.no-perm", player);
                    }
                } else {
                    Messages.PlayerMessage("fly.use", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                new PlayerFlyState(target);
            } else {
                Messages.ConsoleMessage("server.player-offline");
            }
        }
        return true;
    }
}