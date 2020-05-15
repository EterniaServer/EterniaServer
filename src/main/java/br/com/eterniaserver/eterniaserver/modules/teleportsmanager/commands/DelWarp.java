package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarp implements CommandExecutor {

    private final TeleportsManager teleportsManager;
    private final Messages messages;

    public DelWarp(TeleportsManager teleportsManager, Messages messages) {
        this.teleportsManager = teleportsManager;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.delwarp")) {
                    if (teleportsManager.existWarp(args[0])) {
                        teleportsManager.delWarp(args[0]);
                        messages.PlayerMessage("warps.delwarp", player);
                    } else {
                        messages.PlayerMessage("warps.noexist", "%warp_name%", args[0], player);
                    }
                } else {
                    messages.PlayerMessage("server.no-perm", player);
                }
            } else {
                messages.PlayerMessage("warps.deluse", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }
}
