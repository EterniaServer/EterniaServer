package commands.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import java.util.HashMap;

public class goldenshovel implements CommandExecutor
{
    private final HashMap<String, Long> cooldowns = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            int cooldownTime = 300;
            if(cooldowns.containsKey(sender.getName()))
            {
                long secondsLeft = ((cooldowns.get(sender.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
                if(secondsLeft>0)
                {
                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                            "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você deve esperar " + ChatColor.DARK_AQUA
                            + secondsLeft + ChatColor.GRAY + " segundos, para poder usar esse comando novamente" + ChatColor.DARK_GRAY + ".");
                    return true;
                }
            }
            cooldowns.put(sender.getName(), System.currentTimeMillis());
            PlayerInventory inventory = player.getInventory();
            inventory.addItem(new ItemStack(Material.GOLDEN_SHOVEL));
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Toma sua pá" + ChatColor.DARK_GRAY + "!");
            return true;
        }
        return false;
    }
}