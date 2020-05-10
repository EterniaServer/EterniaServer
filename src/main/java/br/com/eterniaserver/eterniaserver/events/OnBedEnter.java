package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.configs.methods.Checks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.concurrent.TimeUnit;

public class OnBedEnter implements Listener {

    private final Messages messages;
    private final Checks checks;
    private final Vars vars;

    public OnBedEnter(Messages messages, Checks checks, Vars vars) {
        this.messages = messages;
        this.checks = checks;
        this.vars = vars;
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            Player player = event.getPlayer();
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - checks.getCooldown(player.getName())) > 6) {
                vars.bed_cooldown.put(player.getName(), System.currentTimeMillis());
                messages.BroadcastMessage("bed.player-s", "%player_name%", player.getName());
            }
        }
    }

}
