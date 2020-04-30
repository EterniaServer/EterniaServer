package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import io.papermc.lib.PaperLib;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class OnPlayerToggleSneakEvent implements Listener {

    private final EterniaServer plugin;

    public OnPlayerToggleSneakEvent(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (player.hasPermission("eternia.elevator") && plugin.serverConfig.getBoolean("modules.elevator") && !player.isSneaking()) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            for (String value : plugin.serverConfig.getStringList("elevator.block")) {
                if (value.equals(material.toString())) {
                    final int max = plugin.serverConfig.getInt("elevator.max");
                    final int min = plugin.serverConfig.getInt("elevator.min");
                    block = block.getRelative(BlockFace.DOWN, min);
                    int i;
                    for (i = max; i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.DOWN)) {
                        --i;
                    }
                    if (i > 0) {
                        Location location = player.getLocation();
                        location.setY((location.getY() - (double) max - 3.0D + (double) i) + 1);
                        PaperLib.teleportAsync(player, location);
                        player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.D));
                    }
                    break;
                }
            }
        }
    }

}
