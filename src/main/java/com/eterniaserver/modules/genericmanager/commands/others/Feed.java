package com.eterniaserver.modules.genericmanager.commands.others;

import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Feed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.feed")) {
                if (args.length == 0) {
                    player.setFoodLevel(20);
                    new PlayerMessage("other.feed", player);
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.feed.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            target.setFoodLevel(20);
                            new PlayerMessage("other.feed-other", target.getName(), player);
                            new PlayerMessage("other.other-feed", player.getName(), target);
                        } else {
                            new PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        new PlayerMessage("server.no-perm", player);
                    }
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                target.setFoodLevel(20);
                new ConsoleMessage("other.feed-other", target.getName());
                new PlayerMessage("other.other-feed", "console", target);
            } else {
                new ConsoleMessage("server.player-offline");
            }
        }
        return true;
    }
}