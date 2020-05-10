package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenInventory implements CommandExecutor {

    private final Messages messages;

    public OpenInventory(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1 && player.hasPermission("eternia.openinv")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    player.openInventory(target.getInventory());
                } else {
                    messages.PlayerMessage("server.player-offline", player);
                }
            } else if (args.length == 0 && player.hasPermission("eternia.openinv")) {
                messages.PlayerMessage("simp.inv", player);
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
