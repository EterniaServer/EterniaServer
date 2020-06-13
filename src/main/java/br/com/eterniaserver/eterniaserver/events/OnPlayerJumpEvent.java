package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
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

public class OnPlayerJumpEvent implements Listener {

    private final EterniaServer plugin;

    final int max;
    final int min ;

    public OnPlayerJumpEvent(EterniaServer plugin) {
        this.plugin = plugin;
        max = plugin.serverConfig.getInt("elevator.max");
        min = plugin.serverConfig.getInt("elevator.min");
    }

    @EventHandler
    public void onPlayerJumpEvent(PlayerJumpEvent event) {
        final Player player = event.getPlayer();
        if (plugin.serverConfig.getBoolean("modules.elevator") && player.hasPermission("eternia.elevator")) {
            Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            for (String value : plugin.serverConfig.getStringList("elevator.block")) {
                if (value.equals(material.toString())) {
                    block = block.getRelative(BlockFace.UP, min);

                    int i;
                    for (i = max; i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.UP)) --i;

                    if (i > 0) {
                        Location location = player.getLocation();
                        location.setY((location.getY() + (double) max + 3.0D - (double) i) - 1);
                        PaperLib.teleportAsync(player, location);
                        player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
                    }
                    break;
                }
            }
        }
    }

}
