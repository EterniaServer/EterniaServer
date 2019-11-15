package commands.player;
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
            int xp_real = 0;
            int xp_atual = player.getLevel();
            if(xp_atual >= 1 && xp_atual <= 16)
            {
                xp_real = (int)(Math.pow(xp_atual, 2) + 6 * xp_atual);
            }
            else if(xp_atual >= 17 && xp_atual <= 31)
            {
                xp_real = (int)( 2.5 * Math.pow(xp_atual, 2) - 40.5 * xp_atual + 360);
            }
            else if(xp_atual >= 32)
            {
                xp_real = (int)(4.5 * Math.pow(xp_atual, 2) - 162.5 * xp_atual + 2220);
            }
            xp_real = xp_real / 10;
            if (xp_real >= 10)
            {
                player.getWorld().dropItem(player.getLocation().add(0, 1, 0), new ItemStack(Material.EXPERIENCE_BOTTLE, xp_real));
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