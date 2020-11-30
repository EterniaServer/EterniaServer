package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eternialib.NBTItem;
import br.com.eterniaserver.eternialib.NBTTileEntity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIServer;

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
import java.util.concurrent.atomic.AtomicReference;

public class BlockHandler implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission(EterniaServer.getString(Strings.PERM_SIGN_COLOR))) {
            for (byte i = 0; i < 4; i++) {
                event.setLine(i, APIServer.getColor(event.getLine(i)));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_SPAWNERS)) return;

        final Block block = event.getBlockPlaced();
        if (block.getType() == Material.SPAWNER) {
            final ItemMeta meta = event.getItemInHand().getItemMeta();
            if (meta != null) {
                String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
                EntityType entity = EntityType.valueOf(entityName);
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(entity);
                spawner.update();
                NBTTileEntity tileEntity = new NBTTileEntity(block.getState());
                tileEntity.setString("ms_mob", tileEntity.toString());
            }
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        if (!EterniaServer.getBoolean(Booleans.MODULE_SPAWNERS) && !EterniaServer.getBoolean(Booleans.MODULE_BLOCK)) return;

        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Material material = block.getType();
        final String materialName = material.name().toUpperCase();
        final String worldName = player.getWorld().getName();

        if (EterniaServer.getBoolean(Booleans.MODULE_SPAWNERS) && material == Material.SPAWNER && !isBlackListWorld(worldName) && player.hasPermission(EterniaServer.getString(Strings.PERM_SPAWNERS_BREAK))) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission(EterniaServer.getString(Strings.PERM_SPAWNERS_NO_SILK))) {
                giveSpawner(player, material, block);
                event.setExpToDrop(0);
            } else {
                event.setCancelled(true);
                EterniaServer.sendMessage(player, Messages.SPAWNER_SILK_REQUESTED);
            }
        } else if (!player.hasPermission(EterniaServer.getString(Strings.PERM_SPAWNERS_BREAK)) && material == Material.SPAWNER) {
            EterniaServer.sendMessage(player, Messages.SERVER_NO_PERM);
            event.setCancelled(true);
        } else if (isBlackListWorld(worldName) && material == Material.SPAWNER) {
            EterniaServer.sendMessage(player, Messages.SPAWNER_WORLD_BLOCKED);
            event.setCancelled(true);
        }

        if (!EterniaServer.getBoolean(Booleans.MODULE_BLOCK)) return;

        if (EterniaServer.getChanceMap(ChanceMaps.BLOCK_DROPS).containsKey(materialName)) {
            randomizeAndReward(player, EterniaServer.getChanceMap(ChanceMaps.BLOCK_DROPS).get(materialName));
        }

        if (EterniaServer.getChanceMap(ChanceMaps.FARM_DROPS).containsKey(materialName)) {
            winFarmReward(block, player);
        }

    }

    private void winFarmReward(Block block, Player player) {
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Ageable) {
            Ageable ageable = (Ageable) blockData;
            if (ageable.getAge() == ageable.getMaximumAge()) {
                randomizeAndReward(player, EterniaServer.getChanceMap(ChanceMaps.FARM_DROPS).get(block.getType().name().toUpperCase()));
            }
        }
    }

    private void randomizeAndReward(Player player, Map<Double, List<String>> map) {
        double randomNumber = Math.random();
        AtomicReference<List<String>> reward = new AtomicReference<>();
        map.forEach((k, v) -> {
            if (k > randomNumber) {
                reward.set(v);
            }
        });
        if (reward.get() == null) return;
        for (String command : reward.get()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), APIServer.setPlaceholders(player, command));
        }
    }

    private void giveSpawner(Player player, Material material, Block block) {
        double random = Math.random();
        if (random < EterniaServer.getDouble(Doubles.DROP_CHANCE)) {
            if (EterniaServer.getBoolean(Booleans.INV_DROP)) {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(getSpawner(block, material));
                    block.getDrops().clear();
                } else {
                    EterniaServer.sendMessage(player, Messages.SPAWNER_INV_FULL);
                    final Location loc = block.getLocation();
                    loc.getWorld().dropItemNaturally(loc, getSpawner(block, material));
                }
            } else {
                final Location loc = block.getLocation();
                loc.getWorld().dropItemNaturally(loc, getSpawner(block, material));
            }
        } else {
            EterniaServer.sendMessage(player, Messages.SPAWNER_DROP_FAILED);
        }
    }

    private ItemStack getSpawner(Block block, Material material) {
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        EntityType entityType = spawner.getSpawnedType();

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String mobFormatted = spawner.getSpawnedType().toString();
        meta.setDisplayName("§8[" + EterniaServer.getString(Strings.SPAWNERS_COLORS) + mobFormatted + " §7Spawner§8]");
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("ms_mob", entityType.name());

        return nbtItem.getItem();
    }

    private boolean isBlackListWorld(final String worldName) {
        return EterniaServer.getStringList(Lists.BLACKLISTED_WORLDS_SPAWNERS).contains(worldName);
    }

}
