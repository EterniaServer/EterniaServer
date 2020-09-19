package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.CashGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilCashGui {

    public CashGui get() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Loja de &aCash&8."));
        itemStack.setItemMeta(itemMeta);

        Map<Integer, ItemStack> menuGui = new HashMap<>();
        Map<Integer, ItemStack> permGui = new HashMap<>();
        Map<Integer, ItemStack> pacoGui = new HashMap<>();
        Map<Integer, ItemStack> tagsGui = new HashMap<>();
        Map<Integer, ItemStack> spawGui = new HashMap<>();

        int size = EterniaServer.cashConfig.getInt("menu.size");
        for (int i = 0; i < size; i++) {
            if (!EterniaServer.cashConfig.contains("menu." + i)) {
                menuGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("menu." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("menu." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("menu." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            menuGui.put(i, item);
        }

        for (int i = 0; i < EterniaServer.cashConfig.getInt("guis.perm.size"); i++) {
            if (!EterniaServer.cashConfig.contains("guis.perm." + i)) {
                permGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("guis.perm." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("guis.perm." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("guis.perm." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            permGui.put(i, item);
        }

        for (int i = 0; i < EterniaServer.cashConfig.getInt("guis.pacotes.size"); i++) {
            if (!EterniaServer.cashConfig.contains("guis.pacotes." + i)) {
                pacoGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("guis.pacotes." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("guis.pacotes." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("guis.pacotes." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            pacoGui.put(i, item);
        }

        for (int i = 0; i < EterniaServer.cashConfig.getInt("guis.tags.size"); i++) {
            if (!EterniaServer.cashConfig.contains("guis.tags." + i)) {
                tagsGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("guis.tags." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("guis.tags." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("guis.tags." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            tagsGui.put(i, item);
        }

        for (int i = 0; i < EterniaServer.cashConfig.getInt("guis.spawners.size"); i++) {
            if (!EterniaServer.cashConfig.contains("guis.spawners." + i)) {
                spawGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("guis.spawners." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("guis.spawners." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("guis.spawners." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            spawGui.put(i, item);
        }

        return new CashGui(menuGui, permGui, pacoGui, tagsGui, spawGui);
    }

}
