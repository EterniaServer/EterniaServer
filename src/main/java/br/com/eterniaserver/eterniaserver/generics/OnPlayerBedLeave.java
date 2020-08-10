package br.com.eterniaserver.eterniaserver.generics;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.concurrent.TimeUnit;

public class OnPlayerBedLeave implements Listener {

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - InternMethods.getCooldown(playerName)) > 6) {
            Vars.bedCooldown.put(playerName, System.currentTimeMillis());
        }
    }

}
