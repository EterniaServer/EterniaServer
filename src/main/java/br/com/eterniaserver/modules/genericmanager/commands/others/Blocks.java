package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Messages;
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
                double coal = 0, lapiz = 0, redstone = 0, iron = 0, gold = 0, diamond = 0, esmeralda = 0;
                for (ItemStack i : player.getInventory().getContents()) {
                    if (i.getType() == Material.COAL) {
                        coal += i.getAmount();
                    }
                    if (i.getType() == Material.LAPIS_LAZULI) {
                        lapiz += i.getAmount();
                    }
                    if (i.getType() == Material.REDSTONE) {
                        redstone += i.getAmount();
                    }
                    if (i.getType() == Material.IRON_INGOT) {
                        iron += i.getAmount();
                    }
                    if (i.getType() == Material.GOLD_INGOT) {
                        gold += i.getAmount();
                    }
                    if (i.getType() == Material.DIAMOND) {
                        diamond += i.getAmount();
                    }
                    if (i.getType() == Material.EMERALD) {
                        esmeralda += i.getAmount();
                    }
                }
                int itens = (int) Math.floor(coal / 9);
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.COAL, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.COAL_BLOCK, itens));
                }
                itens = (int) Math.floor(lapiz / 9);
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.LAPIS_LAZULI, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, itens));
                }
                itens = (int) Math.floor(redstone / 9);
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.REDSTONE, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, itens));
                }
                itens = (int) Math.floor(iron / 9);
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, itens));
                }
                itens = (int) Math.floor(gold / 9);
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, itens));
                }
                itens = (int) Math.floor(diamond / 9);
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.DIAMOND, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, itens));
                }
                itens = (int) Math.floor(esmeralda / 9);
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.EMERALD, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, itens));
                }
                Messages.PlayerMessage("other.done", player);
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
