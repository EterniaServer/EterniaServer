package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderChest implements CommandExecutor {

    private final Messages messages;

    public EnderChest(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.enderchest") && args.length == 0) {
                player.openInventory(player.getEnderChest());
            } else if (player.hasPermission("eternia.enderchest.other") && args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    player.openInventory(target.getEnderChest());
                } else {
                    messages.sendMessage("server.player-offline", sender);
                }
            }else {
                messages.sendMessage("server.no-perm", sender);
            }
        } else {
            messages.sendMessage("server.only-player", sender);
        }
        return true;

    }
}
