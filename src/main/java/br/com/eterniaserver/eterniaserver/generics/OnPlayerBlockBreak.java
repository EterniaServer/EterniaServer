package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnPlayerBlockBreak implements Listener {

    private final String blockConfig = "blocks.";
    private final EterniaServer plugin;
    private final EFiles messages;

    public OnPlayerBlockBreak(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Material material = block.getType();
        final String materialName = material.name().toUpperCase();
        if (EterniaServer.serverConfig.getBoolean("modules.spawners") && material == Material.SPAWNER &&
                !isBlackListWorld(player.getWorld().getName()) && (!player.hasPermission("eternia.spawners.bypass")) && player.hasPermission("eternia.spawners.break")) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission("eternia.spawners.nosilk")) {
                double random = Math.random();
                if (random < EterniaServer.serverConfig.getDouble("spawners.drop-chance")) {
                    if (EterniaServer.serverConfig.getBoolean("spawners.drop-in-inventory")) {
                        if (player.getInventory().firstEmpty() != -1) {
                            player.getInventory().addItem(getSpawner(block, material));
                            block.getDrops().clear();
                        } else {
                            event.setCancelled(true);
                            messages.sendMessage(Strings.M_SPAWNER_INVFULL, player);
                        }
                    } else {
                        final Location loc = block.getLocation();
                        loc.getWorld().dropItemNaturally(loc, getSpawner(block, material));
                    }
                    event.setExpToDrop(0);
                } else {
                    messages.sendMessage(Strings.M_SPAWNER_FAILED, player);
                }
            } else {
                event.setCancelled(true);
                messages.sendMessage(Strings.M_SPAWNER_SILK, player);
            }
        } else if (!player.hasPermission("eternia.spawners.break")) {
            messages.sendMessage(Strings.M_NO_PERM, player);
            event.setCancelled(true);
        } else {
            messages.sendMessage(Strings.M_SPAWNER_BLOCKED, player);
            event.setCancelled(true);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.block-reward") && EterniaServer.blockConfig.contains(blockConfig + materialName)) {
            winReward(materialName, player);
        }
    }

    private void winReward(final String materialName, final Player player) {
        double randomNumber = Math.random();
        double lowestNumberAboveRandom = 1.1;

        for (String key : EterniaServer.blockConfig.getConfigurationSection(blockConfig + materialName).getKeys(true)) {
            double current = Double.parseDouble(key);
            if (current < lowestNumberAboveRandom && current > randomNumber) {
                lowestNumberAboveRandom = current;
            }
        }
        if (lowestNumberAboveRandom <= 1) {
            for (String command : EterniaServer.blockConfig.getStringList(blockConfig + materialName + "." + lowestNumberAboveRandom)) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, command));
            }
        }
    }

    private ItemStack getSpawner(Block block, Material material) {
        final CreatureSpawner spawner = (CreatureSpawner) block.getState();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String mob = spawner.getSpawnedType().toString().replace("_", " ");
        String mobFormatted = mob.substring(0, 1).toUpperCase() + mob.substring(1).toLowerCase();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + EterniaServer.serverConfig.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
        }
        item.setItemMeta(meta);
        return item;
    }

    private boolean isBlackListWorld(final String worldName) {
        return EterniaServer.serverConfig.getStringList("spawners.blacklisted-worlds").contains(worldName);
    }

}
