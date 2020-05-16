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
            if (!sender.hasPermission("eternia.afk")) {
                messages.sendMessage("server.no-perm", sender);
                return false;
            }

            if (vars.afk.contains(sender.getName())) {
                messages.BroadcastMessage("text.afkd", "%player_name%", sender.getName());
                vars.afk.remove(sender.getName());
            } else {
                vars.afk.add(sender.getName());
                messages.BroadcastMessage("text.afke", "%player_name%", sender.getName());
            }
        } else {
            messages.sendMessage("server.only-player", sender);
        }
        return true;

    }
}
