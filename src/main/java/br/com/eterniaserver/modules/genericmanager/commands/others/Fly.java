package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.player.PlayerFlyState;
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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.fly")) {
                if (args.length == 0) {
                    if (player.getWorld() == Bukkit.getWorld("evento")) {
                        if (player.hasPermission("eternia.fly.evento")) {
                            playerFlyState.changeFlyState(player);
                        } else {
                            messages.PlayerMessage("server.no-perm", player);
                        }
                    } else {
                        playerFlyState.changeFlyState(player);
                    }
                    return true;
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.fly.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            playerFlyState.changeFlyState(target);
                        } else {
                            messages.PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        messages.PlayerMessage("server.no-perm", player);
                    }
                } else {
                    messages.PlayerMessage("fly.use", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                playerFlyState.changeFlyState(target);
            } else {
                messages.ConsoleMessage("server.player-offline");
            }
        }
        return true;
    }
}