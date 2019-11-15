package commands.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class donation implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Olá, você pode fazer uma doação via transferência" +
                    " bancária ou boleto, basta falar com yurinogueira no Discord" + ChatColor.DARK_GRAY + ".");
            return true;
        }
        return false;
    }
}