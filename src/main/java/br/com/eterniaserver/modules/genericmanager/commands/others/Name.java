package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
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
                    new PlayerMessage("other.del-nick", player);
                } else {
                    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0]));
                    new PlayerMessage("other.new-nick", player.getDisplayName(), player);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}