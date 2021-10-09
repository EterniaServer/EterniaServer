package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

final class Handlers implements Listener {

    private final EterniaServer plugin;

    public Handlers(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        final Block block = event.getBlockPlaced();
        if (block.getType() != Material.SPAWNER) {
            return;
        }

        final ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta == null) {
            return;
        }

        final String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
        final EntityType entity = EntityType.valueOf(entityName);
        final CreatureSpawner spawner = (CreatureSpawner) block.getState();

        spawner.setSpawnedType(entity);
        spawner.update();
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onBlockBreakEvent(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Material material = block.getType();
        if (material != Material.SPAWNER) {
            return;
        }

        final String worldName = block.getWorld().getName();
        if (isBlackListWorld(worldName)) {
            return;
        }

        final Player player = event.getPlayer();
        final String spawnerBreakPerm = plugin.getString(Strings.PERM_SPAWNERS_BREAK);
        if (!player.hasPermission(spawnerBreakPerm) && plugin.getBoolean(Booleans.BLOCK_BREAK_SPAWNERS)) {
            plugin.sendMessage(player, Messages.SPAWNER_WITHOUT_PERM);
            event.setCancelled(true);
        }

        if (player.hasPermission(spawnerBreakPerm)) {
            final ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission(plugin.getString(Strings.PERM_SPAWNERS_NO_SILK))) {
                giveSpawner(player, block);
                event.setExpToDrop(0);
                return;
            }

            event.setCancelled(true);
            plugin.sendMessage(player, Messages.SPAWNER_SILK_REQUESTED);
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