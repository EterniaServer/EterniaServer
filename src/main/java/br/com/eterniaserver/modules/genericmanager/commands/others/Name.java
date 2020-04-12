package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Name implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.name")) {
                if (args.length == 0) {
                    player.setDisplayName(player.getName());
                    Messages.PlayerMessage("other.del-nick", player);
                } else {
                    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0]));
                    Messages.PlayerMessage("other.new-nick", player.getDisplayName(), player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}