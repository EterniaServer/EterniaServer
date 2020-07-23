package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.concurrent.TimeUnit;

public class OnPlayerBedEnter implements Listener {

    private final EterniaServer plugin;

    public OnPlayerBedEnter(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled()) return;

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            final Player player = event.getPlayer();
            final String playerName = player.getName();
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - plugin.getInternMethods().getCooldown(playerName)) > 6) {
                Vars.bedCooldown.put(playerName, System.currentTimeMillis());
                plugin.getEFiles().broadcastMessage("bed.player-s", Constants.PLAYER, player.getDisplayName());
            }
        }
    }

}
