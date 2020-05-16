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

        if (sender instanceof Player && args.length == 0 && sender.hasPermission("eternia.feed")) {
            ((Player) sender).setFoodLevel(20);
            messages.sendMessage("other.feed", sender);
        } else if (args.length == 1 && sender.hasPermission("eternia.feed.other")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                target.setFoodLevel(20);
                messages.sendMessage("other.feed-other", "%target_name%", target.getName(), sender);
                messages.sendMessage("other.other-feed", "%target_name%", sender.getName(), target);
            } else {
                messages.sendMessage("server.player-offline", sender);
            }
        } else {
            messages.sendMessage("server.no-perm", sender);
        }
        return true;

    }
}