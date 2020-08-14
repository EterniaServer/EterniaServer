package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.concurrent.TimeUnit;

public class OnPlayerBedEnter implements Listener {

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled()) return;

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            final Player player = event.getPlayer();
            final String playerName = player.getName();
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - InternMethods.getCooldown(playerName)) > 6) {
                Vars.bedCooldown.put(playerName, System.currentTimeMillis());
                Bukkit.getConsoleSender().sendMessage(Strings.MSG_PLAYER_SKIP.replace(Constants.PLAYER, player.getDisplayName()));
            }
        }
    }

}
