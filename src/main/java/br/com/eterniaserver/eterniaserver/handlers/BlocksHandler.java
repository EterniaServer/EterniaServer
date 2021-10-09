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

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        if (!plugin.getBoolean(Booleans.MODULE_SPAWNERS) && !plugin.getBoolean(Booleans.MODULE_BLOCK)) return;

        final Block block = event.getBlock();
        final Material material = block.getType();
        final String materialName = material.name().toUpperCase();

        if (plugin.getBoolean(Booleans.MODULE_BLOCK) && plugin.getChanceMap(ChanceMaps.BLOCK_DROPS).containsKey(materialName)) {
            randomizeAndReward(event.getPlayer(), plugin.getChanceMap(ChanceMaps.BLOCK_DROPS).get(materialName));
            return;
        }

        if (plugin.getBoolean(Booleans.MODULE_BLOCK) && plugin.getChanceMap(ChanceMaps.FARM_DROPS).containsKey(materialName)) {
            winFarmReward(event.getBlock(), event.getPlayer());
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

}
