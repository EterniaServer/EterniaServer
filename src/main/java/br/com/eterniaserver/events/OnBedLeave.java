package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.methods.Checks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.concurrent.TimeUnit;

public class OnBedLeave implements Listener {

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Checks.getCooldown(player.getName())) > 6) {
            Vars.bed_cooldown.put(player.getName(), System.currentTimeMillis());
            Messages.BroadcastMessage("bed.player-leave", "%player_name%", player.getName());
        }
    }

}
