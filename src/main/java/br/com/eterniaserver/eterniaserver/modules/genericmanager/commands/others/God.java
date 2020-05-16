package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class God implements CommandExecutor {

    private final Messages messages;
    private final Vars vars;

    public God(Messages messages, Vars vars) {
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 && sender.hasPermission("eternia.god")) {
            if (vars.god.contains(sender.getName())) {
                messages.sendMessage("simp.godd", sender);
                vars.god.remove(sender.getName());
            } else {
                messages.sendMessage("simp.gode", sender);
                vars.god.add(sender.getName());
            }
        } else if (args.length == 1 && sender.hasPermission("eternia.god.other")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                if (vars.god.contains(target.getName())) {
                    messages.sendMessage("simp.godd", target);
                    vars.god.remove(target.getName());
                } else {
                    messages.sendMessage("simp.gode", target);
                    vars.god.add(target.getName());
                }
            } else {
                messages.sendMessage("server.player-offline", sender);
            }
        } else {
            messages.sendMessage("server.no-perm", sender);
        }
        return true;

    }
}
