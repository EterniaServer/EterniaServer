package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final Services.Spawner spawnerService;

    public Handlers(final EterniaServer plugin, Services.Spawner spawnerService) {
        this.plugin = plugin;
        this.spawnerService = spawnerService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getItem() != null
                && event.getClickedBlock().getType() == Material.SPAWNER
                && !event.getPlayer().hasPermission(plugin.getString(Strings.PERM_SPAWNERS_CHANGE))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        if (block.getType() != Material.SPAWNER) {
            return;
        }

        ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta == null) {
            return;
        }

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        if (!dataContainer.has(plugin.getKey(ItemsKeys.TAG_SPAWNER), PersistentDataType.STRING)) {
            return;
        }

        String entityName = dataContainer.get(plugin.getKey(ItemsKeys.TAG_SPAWNER), PersistentDataType.STRING);
        EntityType entity = EntityType.valueOf(entityName);
        CreatureSpawner spawner = (CreatureSpawner) block.getState();

        spawner.setSpawnedType(entity);
        spawner.update();
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onBlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();

        if (material != Material.SPAWNER) {
            return;
        }

        String worldName = block.getWorld().getName();

        if (isBlackListWorld(worldName)) {
            return;
        }

        Player player = event.getPlayer();
        String spawnerBreakPerm = plugin.getString(Strings.PERM_SPAWNERS_BREAK);

        if (!player.hasPermission(spawnerBreakPerm) && plugin.getBoolean(Booleans.BLOCK_BREAK_SPAWNERS)) {
            EterniaLib.getChatCommons().sendMessage(player, Messages.SPAWNER_WITHOUT_PERM);
            event.setCancelled(true);
        }
        else if (player.hasPermission(spawnerBreakPerm)) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission(plugin.getString(Strings.PERM_SPAWNERS_NO_SILK))) {
                giveSpawner(player, block);
                event.setExpToDrop(0);
            }
            else {
                event.setCancelled(true);
                EterniaLib.getChatCommons().sendMessage(player, Messages.SPAWNER_SILK_REQUESTED);
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onEntityInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || plugin.getBoolean(Booleans.PREVENT_ANVIL)) {
            return;
        }

        if (event.getInventory().getType() == InventoryType.ANVIL && itemStack.getType() == Material.SPAWNER) {
            EterniaLib.getChatCommons().sendMessage(event.getWhoClicked(), Messages.SPAWNER_CANT_CHANGE_NAME);
            event.setCancelled(true);
        }
    }

    private void giveSpawner(Player player, Block block) {
        double random = Math.random();
        if (random > plugin.getDouble(Doubles.DROP_CHANCE)) {
            EterniaLib.getChatCommons().sendMessage(player, Messages.SPAWNER_DROP_FAILED);
            return;
        }

        if (!plugin.getBoolean(Booleans.INV_DROP)) {
            block.getWorld().dropItemNaturally(block.getLocation(), spawnerService.getSpawner(block));
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            block.getWorld().dropItemNaturally(block.getLocation(), spawnerService.getSpawner(block));
            EterniaLib.getChatCommons().sendMessage(player, Messages.SPAWNER_INV_FULL);
            return;
        }

        player.getInventory().addItem(spawnerService.getSpawner(block));
        block.getDrops().clear();
    }

    private boolean isBlackListWorld(final String worldName) {
        return plugin.getStringList(Lists.BLACKLISTED_WORLDS_SPAWNERS).contains(worldName);
    }

}
