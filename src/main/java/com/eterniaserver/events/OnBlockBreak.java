package com.eterniaserver.events;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.spawner.SpawnerBreak;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (EterniaServer.getBlocks().contains("Blocks." + event.getBlock().getType())) {
            ConfigurationSection cs = EterniaServer.getBlocks().getConfigurationSection("Blocks." + event.getBlock().getType());
            double randomNumber = new Random().nextDouble();
            assert cs != null;
            List<String> mainList = new ArrayList<>(cs.getKeys(true));
            double lowestNumberAboveRandom = 1.1;
            for (int i = 1; i < cs.getKeys(true).size(); i++) {
                double current = Double.parseDouble(mainList.get(i).replace("[", "").replace("]", ""));
                if (current < lowestNumberAboveRandom && current > randomNumber) {
                    lowestNumberAboveRandom = current;
                }
            }
            if (lowestNumberAboveRandom <= 1) {
                List<String> stringList = EterniaServer.getBlocks().getStringList("Blocks." + event.getBlock().getType() + "." + "[" + lowestNumberAboveRandom + "]");
                for (String command : stringList) {
                    String modifiedCommand = command.replace("%PLAYER%", event.getPlayer().getPlayerListName());
                    EterniaServer.getMain().getServer().dispatchCommand(EterniaServer.getMain().getServer().getConsoleSender(), modifiedCommand);
                }
            }
        }
        if (event.getBlock().getType() == Material.SPAWNER) {
            Block block = event.getBlock();
            Material material = block.getType();
            Player player = event.getPlayer();
            if (EterniaServer.getMain().getConfig().getStringList("spawners.blacklisted-worlds").contains(player.getWorld().getName()) && (!player.hasPermission("eternia.spawners.bypass"))) {
                MVar.playerMessage("spawners.block", player);
                event.setCancelled(true);
                return;
            }
            if (player.hasPermission("eternia.spawners.break")) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission("eternia.spawners.nosilk")) {
                    if (itemInHand.getItemMeta() == null) {
                        SpawnerBreak.spawner(block, material, player, event);
                    } else if (itemInHand.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED) <= 5) {
                        SpawnerBreak.spawner(block, material, player, event);
                    } else {
                        event.setCancelled(true);
                        MVar.playerMessage("spawners.no-pick", player);
                    }
                } else {
                    event.setCancelled(true);
                    MVar.playerMessage("spawners.no-silktouch", player);
                }
            } else {
                event.setCancelled(true);
                MVar.playerMessage("server.no-perm", player);
            }
        }
    }
}