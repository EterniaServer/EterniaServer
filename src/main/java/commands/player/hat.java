package commands.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class hat implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.comandos.player.hat"))
            {
                ItemStack capacete = player.getInventory().getHelmet();
                if (capacete != null)
                {
                    player.getWorld().dropItem(player.getLocation().add(0, 1, 0), capacete);
                    set_capacete(player);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                            "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Aproveite seu novo Caçapete" +
                            ChatColor.DARK_GRAY + ".");
                    return true;
                } else {
                    set_capacete(player);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                            "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Aproveite seu novo Caçapete" +
                            ChatColor.DARK_GRAY + ".");
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
    private void set_capacete(Player player)
    {
        player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
    }
}