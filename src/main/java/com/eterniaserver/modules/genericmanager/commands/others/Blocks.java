package com.eterniaserver.modules.genericmanager.commands.others;

import com.eterniaserver.configs.MVar;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Blocks implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.blocks")) {
                compact(Material.COAL, Material.COAL_BLOCK, player);
                compact(Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, player);
                compact(Material.REDSTONE, Material.REDSTONE_BLOCK, player);
                compact(Material.IRON_INGOT, Material.IRON_BLOCK, player);
                compact(Material.GOLD_INGOT, Material.GOLD_BLOCK, player);
                compact(Material.DIAMOND, Material.DIAMOND_BLOCK, player);
                compact(Material.EMERALD, Material.EMERALD_BLOCK, player);
                MVar.playerMessage("other.done", player);
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }

    private void compact(Material material, Material block, Player player) {
        double item = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i.getType() == material) {
                item += i.getAmount();
            }
        }
        int itens = (int) Math.floor(item / 9);
        if (itens != 0) {
            player.getInventory().removeItem(new ItemStack(material, itens * 9));
            player.getInventory().addItem(new ItemStack(block, itens));
        }
    }

}
