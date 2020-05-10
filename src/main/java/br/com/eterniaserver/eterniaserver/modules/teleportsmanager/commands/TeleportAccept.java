package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.player.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAccept implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;

    public TeleportAccept(EterniaServer plugin, Messages messages, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (vars.tpa_requests.containsKey(player.getName())) {
                    Player target = Bukkit.getPlayer(vars.tpa_requests.get(player.getName()));
                    if (target != null) {
                        messages.PlayerMessage("teleport.accept", "%target_name%", player.getName(), target);
                        vars.teleports.put(target, new PlayerTeleport(target, player.getLocation(), "teleport.tpto", plugin));
                    }
                    vars.tpa_requests.remove(player.getName());
                } else {
                    messages.PlayerMessage("teleport.noask", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
