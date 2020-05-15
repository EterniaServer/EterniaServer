package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.simplifications;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sun implements CommandExecutor {

    private final Messages messages;

    public Sun(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.sun")) {
                player.getWorld().setStorm(false);
                messages.PlayerMessage("simp.weather", player);
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }
}
