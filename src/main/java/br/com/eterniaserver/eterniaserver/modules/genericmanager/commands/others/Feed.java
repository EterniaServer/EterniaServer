package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Feed implements CommandExecutor {

    private final Messages messages;

    public Feed(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.feed")) {
                if (args.length == 0) {
                    player.setFoodLevel(20);
                    messages.PlayerMessage("other.feed", player);
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.feed.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            target.setFoodLevel(20);
                            messages.PlayerMessage("other.feed-other", "%target_name%", target.getName(), player);
                            messages.PlayerMessage("other.other-feed", "%target_name%", player.getName(), target);
                        } else {
                            messages.PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        messages.PlayerMessage("server.no-perm", player);
                    }
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                target.setFoodLevel(20);
                messages.sendConsole("other.feed-other", "%target_name%", target.getName());
                messages.PlayerMessage("other.other-feed", "%target_name%", "console", target);
            } else {
                messages.sendConsole("server.player-offline");
            }
        }
        return true;
    }
}