package commands.player;

import center.vars;
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
            if (player.hasPermission("eternia.goldenshovel"))
            {
                int cooldownTime = 300;
                if (cooldowns.containsKey(sender.getName()))
                {
                    long secondsLeft = ((cooldowns.get(sender.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0)
                    {
                        player.sendMessage(vars.replaceObject("pa-falta-tempo", secondsLeft));
                        return true;
                    }
                }
                cooldowns.put(sender.getName(), System.currentTimeMillis());
                PlayerInventory inventory = player.getInventory();
                inventory.addItem(new ItemStack(Material.GOLDEN_SHOVEL));
                player.sendMessage(vars.getString("pa-sucesso"));
                return true;
            }
            else
            {
                player.sendMessage(vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}