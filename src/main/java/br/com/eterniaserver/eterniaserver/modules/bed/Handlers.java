package br.com.eterniaserver.eterniaserver.modules.bed;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.bed.Services.SleepingService;

import net.kyori.adventure.text.Component;

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
    private final SleepingService sleepingService;

    public Handlers(EterniaServer plugin, SleepingService sleepingService) {
        this.plugin = plugin;
        this.databaseInterface = EterniaLib.getDatabase();
        this.sleepingService = sleepingService;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            Player player = event.getPlayer();
            PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

            if (checkBedCooldown(player)) {
                sleepingService.updateBedCooldown(player.getUniqueId());
                Component message = plugin.getMiniMessage(
                        Messages.NIGHT_PLAYER_SLEEPING,
                        true,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
                plugin.getServer().broadcast(message);
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        if (checkBedCooldown(player)) {
            sleepingService.updateBedCooldown(player.getUniqueId());
        }
    }

    private boolean checkBedCooldown(Player player) {
        long time = System.currentTimeMillis() - sleepingService.getBedCooldown(player.getUniqueId());
        return TimeUnit.MILLISECONDS.toSeconds(time) > 6;
    }

}
