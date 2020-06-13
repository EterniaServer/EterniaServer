package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OnPlayerBlockBreak implements Listener {

    private final EterniaServer plugin;
    private final EFiles messages;

    private final String bString = "blocks.";

    public OnPlayerBlockBreak(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Material material = block.getType();
        if ((plugin.serverConfig.getBoolean("modules.spawners") && (material == Material.SPAWNER))
                && (!(plugin.serverConfig.getStringList("spawners.blacklisted-worlds").contains(player.getWorld().getName()))
                || player.hasPermission("eternia.spawners.bypass"))) {
            breakSpawner(event, player, block, material);
        } else {
            messages.sendMessage("spawner.others.blocked", player);
            event.setCancelled(true);
        }

        if (plugin.serverConfig.getBoolean("modules.block-reward") && (plugin.blockConfig.contains(bString + material.name().toUpperCase()))) {
            blockReward(event, material);
        }
    }

    private void breakSpawner(BlockBreakEvent event, Player player, Block block, Material material) {
        if (player.hasPermission("eternia.spawners.break")) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission("eternia.spawners.nosilk")) {
                if (Math.random() < plugin.serverConfig.getDouble("spawners.drop-chance")) {
                    dropSpawner(event, player, block, getSpawner(block, material));
                } else {
                    messages.sendMessage("spawner.others.failed", player);
                }
            } else {
                event.setCancelled(true);
                messages.sendMessage("spawner.others.need-silktouch", player);
            }
        } else {
            event.setCancelled(true);
            messages.sendMessage("server.no-perm", player);
        }
    }

    private ItemStack getSpawner(Block block, Material material) {
        final CreatureSpawner spawner = (CreatureSpawner) block.getState();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String mob = spawner.getSpawnedType().toString().replace("_", " ");
        String mobFormatted = mob.substring(0, 1).toUpperCase() + mob.substring(1).toLowerCase();
        return plugin.getChecks().getSpawner(meta, item, mobFormatted);
    }

    private void dropSpawner(BlockBreakEvent event, Player player, Block block, ItemStack item) {
        if (plugin.serverConfig.getBoolean("spawners.drop-in-inventory")) {
            if (player.getInventory().firstEmpty() == -1) {
                event.setCancelled(true);
                messages.sendMessage("spawner.others.inv-full", player);
            } else {
                player.getInventory().addItem(item);
                block.getDrops().clear();
                event.setExpToDrop(0);
            }
        } else {
            final Location loc = block.getLocation();
            loc.getWorld().dropItemNaturally(loc, item);
            event.setExpToDrop(0);
        }
    }

    private void blockReward(BlockBreakEvent event, Material material) {
        ConfigurationSection cs = plugin.blockConfig.getConfigurationSection(bString + material.name().toUpperCase());
        double randomNumber = Math.random();
        if (cs != null) {
            double lNumberAR = 1.1;
            for (String key : cs.getKeys(true)) {
                double current = Double.parseDouble(key);
                if (current < lNumberAR && current > randomNumber) lNumberAR = current;
            }
            if (lNumberAR <= 1) {
                for (String command : plugin.blockConfig.getStringList(bString + material.name().toUpperCase() + "." + lNumberAR)) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(event.getPlayer(), command));
                }
            }
        }
    }

}
