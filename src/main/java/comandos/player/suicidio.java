package comandos.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class suicidio implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1){
            if (sender instanceof Player) {
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String s = sb.toString();
                Player player = (Player) sender;
                player.setHealth(0);
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + s + ChatColor.GRAY + "disse " +
                        ChatColor.DARK_AQUA + player.getName() + ChatColor.GRAY + " antes de tirar a própria vida" +
                        ChatColor.DARK_GRAY + ".");
                return true;
            }
        } else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.setHealth(0);
                return true;
            }
        }
        return false;
    }
}