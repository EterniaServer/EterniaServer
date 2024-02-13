package br.com.eterniaserver.eterniaserver.modules.elevator;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;


final class Handlers implements Listener {

    private final EterniaServer plugin;

    public Handlers(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(plugin.getString(Strings.PERM_ELEVATOR))) {
            return;
        }

        Block baseBlock = event.getTo().getBlock().getRelative(BlockFace.DOWN);
        Material baseMaterial = baseBlock.getType();
        if (!plugin.elevatorMaterials().contains(baseMaterial)) {
            return;
        }

        int minSpace = plugin.getInteger(Integers.ELEVATOR_MIN);
        int maxSpace = plugin.getInteger(Integers.ELEVATOR_MAX);
        Block block = baseBlock.getRelative(BlockFace.UP, minSpace);
        for (int i = minSpace + 1; i < maxSpace; i++) {
            if (block.getType() == baseMaterial) {
                Location location = player.getLocation();
                location.setY(location.getY() + i - 0.5D);
                player.teleportAsync(location);
                player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
                break;
            }
            block = block.getRelative(BlockFace.UP);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(plugin.getString(Strings.PERM_ELEVATOR)) || player.isSneaking()) {
            return;
        }

        Block baseBlock = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        Material baseMaterial = baseBlock.getType();
        if (!plugin.elevatorMaterials().contains(baseMaterial)) {
            return;
        }

        int minSpace = plugin.getInteger(Integers.ELEVATOR_MIN);
        int maxSpace = plugin.getInteger(Integers.ELEVATOR_MAX);
        Block block = baseBlock.getRelative(BlockFace.DOWN, minSpace);
        for (int i = minSpace + 1; i < maxSpace; i++) {
            if (block.getType() == baseMaterial) {
                Location location = player.getLocation();
                location.setY(location.getY() - i + 1.15D);
                player.teleportAsync(location);
                player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.D));
                break;
            }
            block = block.getRelative(BlockFace.DOWN);
        }
    }


}
