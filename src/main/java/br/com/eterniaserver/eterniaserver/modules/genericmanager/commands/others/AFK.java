package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFK implements CommandExecutor {

    private final Messages messages;
    private final Vars vars;

    public AFK(Messages messages, Vars vars) {
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.afk")) {
                if (vars.afk.contains(player.getName())) {
                    messages.BroadcastMessage("text.afkd", "%player_name%", player.getName());
                    vars.afk.remove(player.getName());
                } else {
                    vars.afk.add(player.getName());
                    messages.BroadcastMessage("text.afke", "%player_name%", player.getName());
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
