package comandos.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class doar implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Olá, você pode fazer uma doação via transferência" +
                    " bancária, basta falar com yurinogueira no Discord, ou " + ChatColor.DARK_AQUA +
                    " https://pag.ae/7V9tW9fur" + ChatColor.DARK_GRAY + ".");
            return true;
        }
        return false;
    }
}