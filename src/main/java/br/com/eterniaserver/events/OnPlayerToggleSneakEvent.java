package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.List;

public class OnPlayerToggleSneakEvent implements Listener {

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("eternia.elevator") && EterniaServer.configs.getBoolean("modules.elevator") && !player.isSneaking()) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            List<String> stringList = EterniaServer.configs.getStringList("elevator.block");
            for (String value : stringList) {
                if (value.equals(material.toString())) {
                    final int max = EterniaServer.configs.getInt("elevator.max");
                    final int min = EterniaServer.configs.getInt("elevator.min");
                    block = block.getRelative(BlockFace.DOWN, min);
                    int i;
                    for (i = max; i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.DOWN)) {
                        --i;
                    }
                    if (i > 0) {
                        Location location = player.getLocation();
                        location.setY((location.getY() - (double) max - 3.0D + (double) i) + 1);
                        PaperLib.teleportAsync(player, location);
                    }
                    break;
                }
            }
        }
    }

}
