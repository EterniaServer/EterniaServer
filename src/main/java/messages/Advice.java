package messages;

import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Advice implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.advice")) {
                if (args.length >= 1) {
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    sb.substring(0, sb.length() - 1);
                    String s = sb.toString();
                    Vars.broadcastReplaceMessage("aviso-global", s);
                } else {
                    Vars.playerMessage("aviso", player);
                }
            } else {
                Vars.playerMessage("sem-permissao", player);
            }
        } else {
            if (args.length >= 1) {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                sb.substring(0, sb.length() - 1);
                String s = sb.toString();
                Vars.broadcastReplaceMessage("aviso-global", s);
            } else {
                Vars.consoleMessage("aviso");
            }
        }
        return true;
    }
}