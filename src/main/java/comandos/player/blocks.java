package comandos.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class blocks implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            double coal = 0;
            double lapis_lazuli = 0;
            double redstone = 0;
            double iron_ingot = 0;
            double gold_ingot = 0;
            double diamond = 0;
            double emerald = 0;
            Player player = (Player) sender;
            //Carvão
            for (ItemStack i : player.getInventory().getContents())
            {
                if (i != null && i.getType() == Material.COAL)
                {
                    coal += i.getAmount();
                }
            }
            int coal_block = (int) Math.floor(coal / 9);
            if (coal_block != 0)
            {
                player.getInventory().removeItem(new ItemStack(Material.COAL, coal_block * 9));
                player.getInventory().addItem(new ItemStack(Material.COAL_BLOCK, coal_block));
            }
            //Lapis Lazuli
            for (ItemStack i : player.getInventory().getContents())
            {
                if (i != null && i.getType() == Material.LAPIS_LAZULI)
                {
                    lapis_lazuli += i.getAmount();
                }
            }
            int lapis_block = (int) Math.floor(lapis_lazuli / 9);
            if (lapis_block != 0)
            {
                player.getInventory().removeItem(new ItemStack(Material.LAPIS_LAZULI, lapis_block * 9));
                player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, lapis_block));
            }
            //Redstone
            for (ItemStack i : player.getInventory().getContents())
            {
                if (i != null && i.getType() == Material.REDSTONE)
                {
                    redstone += i.getAmount();
                }
            }
            int redstone_block = (int) Math.floor(redstone / 9);
            if (redstone_block != 0)
            {
                player.getInventory().removeItem(new ItemStack(Material.REDSTONE, redstone_block * 9));
                player.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, redstone_block));
            }
            //Ferro
            for (ItemStack i : player.getInventory().getContents())
            {
                if (i != null && i.getType() == Material.IRON_INGOT)
                {
                    iron_ingot += i.getAmount();
                }
            }
            int iron_block = (int) Math.floor(iron_ingot / 9);
            if (iron_block != 0)
            {
                player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, iron_block * 9));
                player.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, iron_block));
            }
            //Ouro
            for (ItemStack i : player.getInventory().getContents())
            {
                if (i != null && i.getType() == Material.GOLD_INGOT)
                {
                    gold_ingot += i.getAmount();
                }
            }
            int gold_block = (int) Math.floor(gold_ingot / 9);
            if (gold_block != 0)
            {
                player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, gold_block * 9));
                player.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, gold_block));
            }
            //Diamante
            for (ItemStack i : player.getInventory().getContents())
            {
                if (i != null && i.getType() == Material.DIAMOND)
                {
                    diamond += i.getAmount();
                }
            }
            int diamond_block = (int) Math.floor(diamond / 9);
            if (diamond_block != 0)
            {
                player.getInventory().removeItem(new ItemStack(Material.DIAMOND, diamond_block * 9));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, diamond_block));
            }
            //Esmeralda
            for (ItemStack i : player.getInventory().getContents())
            {
                if (i != null && i.getType() == Material.EMERALD)
                {
                    emerald += i.getAmount();
                }
            }
            int emerald_block = (int) Math.floor(emerald / 9);
            if (emerald_block != 0)
            {
                player.getInventory().removeItem(new ItemStack(Material.EMERALD, emerald_block * 9));
                player.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, emerald_block));
            }
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + ":)" + ChatColor.DARK_GRAY + ".");
        }
        return true;
    }
}