package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarp implements CommandExecutor {

    private final TeleportsManager teleportsManager;
    private final Messages messages;

    public SetWarp(TeleportsManager teleportsManager, Messages messages) {
        this.teleportsManager = teleportsManager;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.setwarp")) {
                    teleportsManager.setWarp(player.getLocation(), args[0].toLowerCase());
                    messages.PlayerMessage("warps.createwarp", "%warp_name%", args[0], player);
                } else {
                    messages.PlayerMessage("server.no-perm", player);
                }
            } else {
                messages.PlayerMessage("warps.use2", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
