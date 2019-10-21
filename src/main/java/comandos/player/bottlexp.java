package comandos.player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bottlexp implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            int XP_real = 0;
            int xp1 = player.getLevel();
            if(xp1 >= 1 && xp1 <= 16)
            {
                XP_real = (int)(Math.pow(xp1, 2) + 6 * xp1);
            }
            else if(xp1 >= 17 && xp1 <= 31)
            {
                XP_real = (int)( 2.5 * Math.pow(xp1, 2) - 40.5 * xp1 + 360);
            }
            else if(xp1 >= 32)
            {
                XP_real = (int)(4.5 * Math.pow(xp1, 2) - 162.5 * xp1 + 2220);
            }
            XP_real = XP_real / 10;
            if (XP_real >= 10)
            {
                player.getWorld().dropItem(player.getLocation().add(0, 1, 0), new ItemStack(Material.EXPERIENCE_BOTTLE, XP_real));
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Toma ai suas garrafas" + ChatColor.DARK_GRAY + "!");
                player.setLevel(0);
                return true;
            }
            else
            {
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem a quantidade miníma de XP" + ChatColor.DARK_GRAY + ".");
                return true;
            }
        }
        return false;
    }
}