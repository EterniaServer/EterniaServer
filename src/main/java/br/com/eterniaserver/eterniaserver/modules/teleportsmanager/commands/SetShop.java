package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetShop implements CommandExecutor {

    private final TeleportsManager teleportsManager;
    private final Messages messages;

    public SetShop(TeleportsManager teleportsManager, Messages messages) {
        this.teleportsManager = teleportsManager;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setshop")) {
                teleportsManager.setShop(player.getLocation(), player.getName().toLowerCase());
                messages.PlayerMessage("warps.shopd", player);
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
