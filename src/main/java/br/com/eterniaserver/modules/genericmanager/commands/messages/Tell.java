package br.com.eterniaserver.modules.genericmanager.commands.messages;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tell implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tell")) {
                if (args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        StringBuilder sb = new StringBuilder();
                        for (String arg : args) {
                            if (!arg.equals(args[0])) {
                                sb.append(arg).append(" ");
                            }
                        }
                        sb.substring(0, sb.length() - 1);
                        String s = sb.toString();
                        Vars.tell.put(target.getName(), player.getName());
                        Messages.PlayerMessage("tell.toplayer", "%player_name%", player.getName(), "%target_name%", target.getName(), "%message%", s, player, false);
                        Messages.PlayerMessage("tell.fromplayer", "%player_name%", target.getName(), "%target_name%", player.getName(), "%message%", s, target, false);
                    }
                } else {
                    Messages.PlayerMessage("tell.use", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
