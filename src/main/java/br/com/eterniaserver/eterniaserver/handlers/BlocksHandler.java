package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class BlocksHandler implements Listener {

    private final EterniaServer plugin;

    public BlocksHandler(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission(plugin.getString(Strings.PERM_SIGN_COLOR))) {
            for (byte i = 0; i < 4; i++) {
                event.setLine(i, plugin.getColor(event.getLine(i)));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getBoolean(Booleans.MODULE_SPAWNERS)) return;

        final Block block = event.getBlockPlaced();
        if (block.getType() == Material.SPAWNER) {
            final ItemMeta meta = event.getItemInHand().getItemMeta();
            if (meta != null) {
                String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
                EntityType entity = EntityType.valueOf(entityName);
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(entity);
                spawner.update();
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        if (!plugin.getBoolean(Booleans.MODULE_SPAWNERS) && !plugin.getBoolean(Booleans.MODULE_BLOCK)) return;

        final Block block = event.getBlock();
        final Material material = block.getType();
        final String materialName = material.name().toUpperCase();
        final String worldName = block.getWorld().getName();

        if (plugin.getBoolean(Booleans.MODULE_SPAWNERS) && material == Material.SPAWNER && !isBlackListWorld(worldName)) {
            getSpawner(event.getPlayer(), event, block);
            return;
        }

        if (plugin.getBoolean(Booleans.MODULE_BLOCK) && plugin.getChanceMap(ChanceMaps.BLOCK_DROPS).containsKey(materialName)) {
            randomizeAndReward(event.getPlayer(), plugin.getChanceMap(ChanceMaps.BLOCK_DROPS).get(materialName));
            return;
        }

        if (plugin.getBoolean(Booleans.MODULE_BLOCK) && plugin.getChanceMap(ChanceMaps.FARM_DROPS).containsKey(materialName)) {
            winFarmReward(event.getBlock(), event.getPlayer());
        }

    }

    private void getSpawner(Player player, BlockBreakEvent event, Block block) {
        if (player.hasPermission(plugin.getString(Strings.PERM_SPAWNERS_BREAK))) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission(plugin.getString(Strings.PERM_SPAWNERS_NO_SILK))) {
                giveSpawner(player, block);
                event.setExpToDrop(0);
                return;
            }

            event.setCancelled(true);
            plugin.sendMessage(player, Messages.SPAWNER_SILK_REQUESTED);
            return;
        }

        if (plugin.getBoolean(Booleans.BLOCK_BREAK_SPAWNERS)) {
            plugin.sendMessage(player, Messages.SPAWNER_WITHOUT_PERM);
            event.setCancelled(true);
        }
    }

    private void winFarmReward(Block block, Player player) {
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Ageable) {
            Ageable ageable = (Ageable) blockData;
            if (ageable.getAge() == ageable.getMaximumAge()) {
                randomizeAndReward(player, plugin.getChanceMap(ChanceMaps.FARM_DROPS).get(block.getType().name().toUpperCase()));
            }
        }
    }

    private void randomizeAndReward(Player player, Map<Double, List<String>> map) {
        double randomNumber = Math.random();
        List<String> reward = null;
        for (Map.Entry<Double, List<String>> entry : map.entrySet()) {
            if (entry.getKey() > randomNumber) {
                reward = entry.getValue();
            }
        }
        if (reward == null) {
            return;
        }
        for (String command : reward) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), plugin.setPlaceholders(player, command));
        }
    }

    private void giveSpawner(Player player, Block block) {
        double random = Math.random();
        if (random < plugin.getDouble(Doubles.DROP_CHANCE)) {
            if (plugin.getBoolean(Booleans.INV_DROP)) {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(getSpawner(block));
                    block.getDrops().clear();
                } else {
                    plugin.sendMessage(player, Messages.SPAWNER_INV_FULL);
                    final Location loc = block.getLocation();
                    loc.getWorld().dropItemNaturally(loc, getSpawner(block));
                }
            } else {
                final Location loc = block.getLocation();
                loc.getWorld().dropItemNaturally(loc, getSpawner(block));
            }
        } else {
            plugin.sendMessage(player, Messages.SPAWNER_DROP_FAILED);
        }
    }

    private ItemStack getSpawner(Block block) {
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        ItemStack item = new ItemStack(block.getType());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(plugin.getSpawnerName(spawner.getSpawnedType()));
        item.setItemMeta(meta);
        return item;
    }

    private boolean isBlackListWorld(final String worldName) {
        return plugin.getStringList(Lists.BLACKLISTED_WORLDS_SPAWNERS).contains(worldName);
    }

}
