package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.ChatColor;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final Services.Spawner spawnerService;

    public Handlers(final EterniaServer plugin, Services.Spawner spawnerService) {
        this.plugin = plugin;
        this.spawnerService = spawnerService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        final Block block = event.getBlockPlaced();
        if (block.getType() != Material.SPAWNER) {
            return;
        }

        final ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta == null) return;

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

        if (material != Material.SPAWNER) return;

        final String worldName = block.getWorld().getName();

        if (isBlackListWorld(worldName)) return;

        final Player player = event.getPlayer();
        final String spawnerBreakPerm = plugin.getString(Strings.PERM_SPAWNERS_BREAK);

        if (!player.hasPermission(spawnerBreakPerm) && plugin.getBoolean(Booleans.BLOCK_BREAK_SPAWNERS)) {
            plugin.sendMiniMessages(player, Messages.SPAWNER_WITHOUT_PERM);
            event.setCancelled(true);
        }
        else if (player.hasPermission(spawnerBreakPerm)) {
            final ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission(plugin.getString(Strings.PERM_SPAWNERS_NO_SILK))) {
                giveSpawner(player, block);
                event.setExpToDrop(0);
            }
            else {
                event.setCancelled(true);
                plugin.sendMiniMessages(player, Messages.SPAWNER_SILK_REQUESTED);
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onEntityInventoryClick(InventoryClickEvent event) {
        final ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || plugin.getBoolean(Booleans.PREVENT_ANVIL)) return;

        if (event.getInventory().getType() == InventoryType.ANVIL && itemStack.getType() == Material.SPAWNER) {
            plugin.sendMiniMessages(event.getWhoClicked(), Messages.SPAWNER_CANT_CHANGE_NAME);
            event.setCancelled(true);
        }
    }

    private void giveSpawner(Player player, Block block) {
        double random = Math.random();
        if (random > plugin.getDouble(Doubles.DROP_CHANCE)) {
            plugin.sendMiniMessages(player, Messages.SPAWNER_DROP_FAILED);
            return;
        }

        if (!plugin.getBoolean(Booleans.INV_DROP)) {
            block.getWorld().dropItemNaturally(block.getLocation(), getSpawner(block));
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            block.getWorld().dropItemNaturally(block.getLocation(), getSpawner(block));
            plugin.sendMiniMessages(player, Messages.SPAWNER_INV_FULL);
            return;
        }

        player.getInventory().addItem(getSpawner(block));
        block.getDrops().clear();
    }

    private ItemStack getSpawner(Block block) {
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        ItemStack item = new ItemStack(block.getType());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(spawnerService.getSpawnerName(spawner.getSpawnedType()));
        item.setItemMeta(meta);
        return item;
    }

    private boolean isBlackListWorld(final String worldName) {
        return plugin.getStringList(Lists.BLACKLISTED_WORLDS_SPAWNERS).contains(worldName);
    }

}
