package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;

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
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Objects;

public class OnPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Objects.requireNonNull(event.getTo()).distanceSquared(event.getFrom()) != 0) {
            Player player = event.getPlayer();
            if (Vars.playerposition.get(player.getName()) != player.getLocation()) {
                if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
                    Vars.afktime.put(player.getName(), System.currentTimeMillis());
                    if (Vars.afk.contains(player.getName())) {
                        Vars.afk.remove(player.getName());
                        Messages.BroadcastMessage("text.afkd", "%player_name%", player.getName());
                    }
                }
                if (EterniaServer.configs.getBoolean("modules.teleports")) {
                    Vars.moved.put(player.getName(), true);
                }
            }
            if (event.getTo().getY() > event.getTo().getY() && EterniaServer.configs.getBoolean("modules.elevator") && player.hasPermission("eternia.elevator")) {
                Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
                Material material = block.getType();
                List<String> stringList = EterniaServer.configs.getStringList("elevator.block");
                for (String value : stringList) {
                    if (value.equals(material.toString())) {
                        final int max = EterniaServer.configs.getInt("elevator.max");
                        final int min = EterniaServer.configs.getInt("elevator.min");
                        block = block.getRelative(BlockFace.UP, min);
                        int i;
                        for (i = max; i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.UP)) {
                            -- i;
                        }
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

}
