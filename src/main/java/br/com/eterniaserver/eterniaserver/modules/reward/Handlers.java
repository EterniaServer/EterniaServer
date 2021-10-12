package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Map;


final class Handlers implements Listener {

    private final EterniaServer plugin;

    public Handlers(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Material material = block.getType();
        final String materialName = material.name();

        final Map<Double, List<String>> blockChanceMap = plugin.getChanceMap(ChanceMaps.BLOCK_DROPS).get(materialName);
        if (blockChanceMap != null) {
            randomizeAndReward(event.getPlayer(), blockChanceMap);
            return;
        }

        final Map<Double, List<String>> farmChanceMap = plugin.getChanceMap(ChanceMaps.FARM_DROPS).get(materialName);
        if (farmChanceMap != null && isCropReady(block)) {
            randomizeAndReward(event.getPlayer(), farmChanceMap);
        }
    }

    private boolean isCropReady(Block block) {
        final BlockData blockData = block.getBlockData();
        if (blockData instanceof Ageable ageable) {
            return ageable.getAge() == ageable.getMaximumAge();
        }

        return false;
    }

    private void randomizeAndReward(Player player, Map<Double, List<String>> map) {
        final double randomNumber = Math.random();
        List<String> reward = null;
        for (Map.Entry<Double, List<String>> entry : map.entrySet()) {
            if (entry.getKey() > randomNumber) {
                reward = entry.getValue();
            }
        }

        if (reward == null) return;

        for (String command : reward) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), plugin.setPlaceholders(player, command));
        }
    }

}
