package br.com.eterniaserver.modules.genericmanager.commands.replaces;

import br.com.eterniaserver.configs.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Speed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.speed")) {
                if (args.length == 1) {
                    try {
                        int speed = Integer.parseInt(args[0]);
                        if (speed > 0 && speed < 11) {
                            player.setFlySpeed((float) speed / 10);
                            player.setWalkSpeed((float) speed / 10);
                        } else {
                            Messages.PlayerMessage("other.speed-no", player);
                        }
                    } catch (NumberFormatException e) {
                        Messages.PlayerMessage("server.no-number", player);
                    }
                } else {
                    Messages.PlayerMessage("other.speed-use", player);
                }
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
