package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.simplifications;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Thor implements CommandExecutor {

    private final Messages messages;

    public Thor(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.thor")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        target.getWorld().strikeLightning(target.getLocation());
                        messages.PlayerMessage("simp.thor-other", "%target_name%", target.getName(), player);
                        messages.PlayerMessage("simp.other-thor", "%target_name%", player.getName(), target);
                    } else {
                        messages.PlayerMessage("server.player-offline", player);
                    }
                } else {
                    player.getWorld().strikeLightning(player.getTargetBlock(null, 100).getLocation());
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }
}
