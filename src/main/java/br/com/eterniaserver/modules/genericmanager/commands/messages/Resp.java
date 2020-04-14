package br.com.eterniaserver.modules.genericmanager.commands.messages;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Resp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tell")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayer(Vars.tell.get(player.getName()));
                    if (target != null && target.isOnline()) {
                        StringBuilder sb = new StringBuilder();
                        for (String arg : args) {
                            sb.append(arg).append(" ");
                        }
                        sb.substring(0, sb.length() - 1);
                        String s = sb.toString();
                        Vars.tell.put(target.getName(), player.getName());
                        Messages.PlayerMessage("tell.toplayer", player.getName(), target.getName(), s, player, false);
                        Messages.PlayerMessage("tell.fromplayer", target.getName(), player.getName(), s, target, false);
                    } else {
                        Messages.PlayerMessage("tell.rnaote", player);
                    }
                } else {
                    Messages.PlayerMessage("tell.ruse", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
