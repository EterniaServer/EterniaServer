package commands.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class colors implements CommandExecutor
{

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.BLACK + "&0" + ChatColor.DARK_BLUE +
                    "&1" + ChatColor.DARK_GREEN + "&2" + ChatColor.DARK_AQUA + "&3" + ChatColor.DARK_RED + "&4" +
                    ChatColor.DARK_PURPLE + "&5" + ChatColor.GOLD + "&6" + ChatColor.GRAY + "&7" +
                    ChatColor.DARK_GRAY + "&8" + ChatColor.BLUE + "&9" + ChatColor.GREEN + "&a" +
                    ChatColor.AQUA + "&b" + ChatColor.RED + "&c" + ChatColor.LIGHT_PURPLE + "&d" +
                    ChatColor.YELLOW + "&e" + ChatColor.WHITE + "&f" + ChatColor.DARK_GRAY + ".");
            return true;
        }
        return false;
    }
}
