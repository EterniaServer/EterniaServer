package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Checks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.concurrent.TimeUnit;

public class OnBedLeave implements Listener {

    private final Checks checks;
    private final Vars vars;

    public OnBedLeave(Checks checks, Vars vars) {
        this.checks = checks;
        this.vars = vars;
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - checks.getCooldown(playerName)) > 6) {
            vars.bed_cooldown.put(playerName, System.currentTimeMillis());
        }
    }

}
