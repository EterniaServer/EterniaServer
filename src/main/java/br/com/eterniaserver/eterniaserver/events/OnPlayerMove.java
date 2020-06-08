package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

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

public class OnPlayerMove implements Listener {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;

    public OnPlayerMove(EterniaServer plugin, Messages messages, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.vars = vars;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;

        if (event.getTo().distanceSquared(event.getFrom()) != 0) {
            final Player player = event.getPlayer();
            final String playerName = player.getName();
            if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
                vars.afktime.put(playerName, System.currentTimeMillis());
                if (vars.afk.contains(playerName)) {
                    vars.afk.remove(playerName);
                    messages.broadcastMessage("generic.afk.disabled", "%player_name%", playerName);
                }
            }
            if (event.getTo().getY() > event.getFrom().getY() && plugin.serverConfig.getBoolean("modules.elevator") && player.hasPermission("eternia.elevator")) {
                Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
                Material material = block.getType();
                for (String value : plugin.serverConfig.getStringList("elevator.block")) {
                    if (value.equals(material.toString())) {
                        final int max = plugin.serverConfig.getInt("elevator.max");
                        final int min = plugin.serverConfig.getInt("elevator.min");
                        block = block.getRelative(BlockFace.UP, min);

                        int i;
                        for (i = max; i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.UP)) -- i;

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
