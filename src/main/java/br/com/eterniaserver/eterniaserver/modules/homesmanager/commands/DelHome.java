package br.com.eterniaserver.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.HomesManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHome implements CommandExecutor {

    private final HomesManager homesManager;
    private final Messages messages;

    public DelHome(HomesManager homesManager, Messages messages) {
        this.homesManager = homesManager;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.delhome")) {
                    if (homesManager.existHome(args[0].toLowerCase(), player.getName())) {
                        homesManager.delHome(args[0].toLowerCase(), player.getName());
                        messages.PlayerMessage("home.del", player);
                    } else {
                        messages.PlayerMessage("home.no", player);
                    }
                } else {
                    messages.PlayerMessage("server.no-perm", player);
                }
            } else {
                messages.PlayerMessage("home.use3", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }

}
