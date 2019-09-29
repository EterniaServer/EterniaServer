package comandos.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class blocks implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            double carvoes = 0;
            double lapislazuli = 0;
            double redstone = 0;
            double barras_de_ferro = 0;
            double barras_de_ouro = 0;
            double diamantes = 0;
            double esmeralda = 0;
            Player player = (Player) sender;
            //Carvão
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null && i.getType() == Material.COAL) {
                    carvoes += i.getAmount();
                }
            }
            int blocos_de_cavao = (int) Math.floor(carvoes / 9);
            if (blocos_de_cavao != 0) {
                player.getInventory().removeItem(new ItemStack(Material.COAL, blocos_de_cavao * 9));
                player.getInventory().addItem(new ItemStack(Material.COAL_BLOCK, blocos_de_cavao));
            }
            //Lapis Lazuli
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null && i.getType() == Material.LAPIS_LAZULI) {
                    lapislazuli += i.getAmount();
                }
            }
            int blocos_lapis = (int) Math.floor(lapislazuli / 9);
            if (blocos_lapis != 0) {
                player.getInventory().removeItem(new ItemStack(Material.LAPIS_LAZULI, blocos_lapis * 9));
                player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, blocos_lapis));
            }
            //Redstone
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null && i.getType() == Material.REDSTONE) {
                    redstone += i.getAmount();
                }
            }
            int blocos_de_redstone = (int) Math.floor(redstone / 9);
            if (blocos_de_redstone != 0) {
                player.getInventory().removeItem(new ItemStack(Material.REDSTONE, blocos_de_redstone * 9));
                player.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, blocos_de_redstone));
            }
            //Ferro
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null && i.getType() == Material.IRON_INGOT) {
                    barras_de_ferro += i.getAmount();
                }
            }
            int blocos_de_ferro = (int) Math.floor(barras_de_ferro / 9);
            if (blocos_de_ferro != 0) {
                player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, blocos_de_ferro * 9));
                player.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, blocos_de_ferro));
            }
            //Ouro
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null && i.getType() == Material.GOLD_INGOT) {
                    barras_de_ouro += i.getAmount();
                }
            }
            int blocos_de_ouro = (int) Math.floor(barras_de_ouro / 9);
            if (blocos_de_ouro != 0) {
                player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, blocos_de_ouro * 9));
                player.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, blocos_de_ouro));
            }
            //Diamante
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null && i.getType() == Material.DIAMOND) {
                    diamantes += i.getAmount();
                }
            }
            int blocos_de_diamante = (int) Math.floor(diamantes / 9);
            if (blocos_de_diamante != 0) {
                player.getInventory().removeItem(new ItemStack(Material.DIAMOND, blocos_de_diamante * 9));
                player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, blocos_de_diamante));
            }
            //Esmeralda
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null && i.getType() == Material.EMERALD) {
                    esmeralda += i.getAmount();
                }
            }
            int blocos_de_esmeralda = (int) Math.floor(esmeralda / 9);
            if (blocos_de_esmeralda != 0) {
                player.getInventory().removeItem(new ItemStack(Material.EMERALD, blocos_de_esmeralda * 9));
                player.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, blocos_de_esmeralda));
            }
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + ":)" + ChatColor.DARK_GRAY + ".");
        }
        return true;
    }
}