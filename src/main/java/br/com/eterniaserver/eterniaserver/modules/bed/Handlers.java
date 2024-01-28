package br.com.eterniaserver.eterniaserver.modules.bed;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.core.Entities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.concurrent.TimeUnit;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final DatabaseInterface databaseInterface;
    private final Services.SleepingService sleepingService;

    public Handlers(EterniaServer plugin, Services.SleepingService sleepingService) {
        this.plugin = plugin;
        this.databaseInterface = EterniaLib.getDatabase();
        this.sleepingService = sleepingService;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            Player player = event.getPlayer();
            Entities.PlayerProfile playerProfile = databaseInterface.get(Entities.PlayerProfile.class, player.getUniqueId());
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - sleepingService.getBedCooldown(player.getUniqueId())) > 6) {
                sleepingService.updateBedCooldown(player.getUniqueId());
                plugin.getServer().broadcast(plugin.getMiniMessage(Messages.NIGHT_PLAYER_SLEEPING, true, playerProfile.getPlayerName(), playerProfile.getPlayerDisplay()));
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - sleepingService.getBedCooldown(player.getUniqueId())) > 6) {
            sleepingService.updateBedCooldown(player.getUniqueId());
        }
    }

}
