package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportToPlayer implements CommandExecutor {

    private final Messages messages;
    private final Vars vars;

    public TeleportToPlayer(Messages messages, Vars vars) {
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (vars.teleports.containsKey(player)) {
                    messages.PlayerMessage("server.telep", player);
                } else {
                    if (args.length == 1) {
                        try {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target != null && target.isOnline()) {
                                if (target != player) {
                                    if (!vars.tpa_requests.containsKey(target.getName())) {
                                        vars.tpa_requests.remove(target.getName());
                                        vars.tpa_requests.put(target.getName(), player.getName());
                                        messages.PlayerMessage("teleport.receiver", "%target_name%", player.getName(), target);
                                        messages.PlayerMessage("teleport.send", "%target_name%", target.getName(), player);
                                    } else {
                                        messages.PlayerMessage("warps.jadeu", player);
                                    }
                                } else {
                                    messages.PlayerMessage("teleport.auto", player);
                                }
                            } else {
                                messages.PlayerMessage("server.player-offline", player);
                            }
                        } catch (Exception e) {
                            messages.PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        messages.PlayerMessage("teleport.use", player);
                    }
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }

}