package com.eterniaserver.spawner;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnerBreak {
    public static void spawner(Block block, Material material, Player player, BlockBreakEvent event) {
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String mob = spawner.getSpawnedType().toString().replace("_", " ");
        String mobFormatted = mob.substring(0, 1).toUpperCase() + mob.substring(1).toLowerCase();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + CVar.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
        List<String> newLore = new ArrayList<>();
        EterniaServer.getMain().getConfig().getStringList("spawners.lore");
        if (CVar.getBool("spawners.enable-lore")) {
            for (String line : EterniaServer.getMain().getConfig().getStringList("spawners.lore")) {
                newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
            }
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);
        if (CVar.getDouble("spawners.drop-chance") != 1) {
            double random = Math.random();
            if (random >= CVar.getDouble("spawners.drop-chance")) {
                MVar.playerMessage("spawners.no-drop", player);
                return;
            }
        }
        if (CVar.getBool("spawners.drop-in-inventory")) {
            if (player.getInventory().firstEmpty() == -1) {
                event.setCancelled(true);
                MVar.playerMessage("spawners.invfull", player);
                return;
            }
            player.getInventory().addItem(item);
            block.getDrops().clear();
            return;
        }
        Location loc = block.getLocation();
        Objects.requireNonNull(loc.getWorld()).dropItemNaturally(loc, item);
    }
}
