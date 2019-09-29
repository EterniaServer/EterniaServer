package comandos.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class advice implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender != null) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.comandos.staff.aviso")) {
                if (args.length >= 1) {
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    sb.substring(0, sb.length() - 1);
                    String s = sb.toString();
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                            "S" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + s + ChatColor.DARK_GRAY + "!");
                    return true;
                } else {
                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                            "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Use: " + ChatColor.GOLD + "/comandos.staff.aviso " +
                            ChatColor.DARK_AQUA + "<mensagem>" + ChatColor.DARK_GRAY + ".");
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem permissão para isso" +
                        ChatColor.DARK_GRAY + ".");
                return true;
            }
        }
        return false;
    }
}