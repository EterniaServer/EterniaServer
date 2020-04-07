package br.com.eterniaserver.modules.genericmanager.commands.simplifications;

import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
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
                        new PlayerMessage("simp.thor-other", target.getName(), player);
                        new PlayerMessage("simp.other-thor", player.getName(), target);
                    } else {
                        new PlayerMessage("server.player-offline", player);
                    }
                } else {
                    player.getWorld().strikeLightning(player.getTargetBlock(null, 100).getLocation());
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
