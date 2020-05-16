package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.player.PlayerFlyState;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {

    private final Messages messages;
    private final PlayerFlyState playerFlyState;

    public Fly(Messages messages, PlayerFlyState playerFlyState) {
        this.messages = messages;
        this.playerFlyState = playerFlyState;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && args.length == 0 && sender.hasPermission("eternia.fly")) {
            Player player = (Player) sender;
            if (player.getWorld() == Bukkit.getWorld("evento") && !player.hasPermission("eternia.fly.evento")) {
                messages.sendMessage("server.no-perm", sender);
            } else {
                playerFlyState.changeFlyState(player);
            }
        } else if (args.length == 1 && sender.hasPermission("eternia.fly.other")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) playerFlyState.changeFlyState(target);
            else messages.sendMessage("server.player-offline", sender);
        } else if (args.length >= 2) {
            messages.sendMessage("fly.use", sender);
        } else {
            messages.sendMessage("server.no-perm", sender);
        }
        return true;

    }
}