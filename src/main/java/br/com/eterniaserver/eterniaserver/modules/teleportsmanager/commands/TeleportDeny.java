package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportDeny implements CommandExecutor {

    private final Messages messages;
    private final Vars vars;

    public TeleportDeny(Messages messages, Vars vars) {
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (vars.tpa_requests.containsKey(player.getName())) {
                    messages.PlayerMessage("teleport.auto-deny", "%target_name%", vars.tpa_requests.get(player.getName()), player);
                    Player target = Bukkit.getPlayer(vars.tpa_requests.get(player.getName()));
                    vars.tpa_requests.remove(player.getName());
                    if (target != null && target.isOnline()) {
                        messages.PlayerMessage("teleport.deny", target);
                    }
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
