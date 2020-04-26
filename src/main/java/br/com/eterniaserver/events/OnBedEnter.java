package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.Checks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.concurrent.TimeUnit;

public class OnBedEnter implements Listener {

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            Player player = event.getPlayer();
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Checks.getCooldown(player.getName())) > 6) {
                Vars.bed_cooldown.put(player.getName(), System.currentTimeMillis());
                Messages.BroadcastMessage("bed.player-s", "%player_name%", player.getName());
            }
        }
    }

}
