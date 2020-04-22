package br.com.eterniaserver.modules.genericmanager.commands.simplifications;

import br.com.eterniaserver.configs.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Thor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.thor")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        target.getWorld().strikeLightning(target.getLocation());
                        Messages.PlayerMessage("simp.thor-other", "%target_name%", target.getName(), player);
                        Messages.PlayerMessage("simp.other-thor", "%target_name%", player.getName(), target);
                    } else {
                        Messages.PlayerMessage("server.player-offline", player);
                    }
                } else {
                    player.getWorld().strikeLightning(player.getTargetBlock(null, 100).getLocation());
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
