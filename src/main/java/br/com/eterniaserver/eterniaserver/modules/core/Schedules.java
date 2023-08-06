package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Services.Afk;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


final class Schedules {

    private Schedules() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class MainTick extends BukkitRunnable {

        private final EterniaServer plugin;
        private final DatabaseInterface database;
        private final Afk afkServices;

        private final int ticks;

        public MainTick(final EterniaServer plugin, final Services.Afk afkServices) {
            this.plugin = plugin;
            this.afkServices = afkServices;
            this.ticks = plugin.getInteger(Integers.PLUGIN_TICKS);
            this.database = EterniaLib.getDatabase();
        }

        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerProfile playerProfile = database.get(PlayerProfile.class, player.getUniqueId());

                checkTeleports(player);
                checkAFK(player, playerProfile);
            }
        }

        private void checkAFK(Player player, PlayerProfile playerProfile) {
            if (playerProfile.isAfk() || !afkServices.areAfk(playerProfile)) {
                return;
            }

            if (!plugin.getBoolean(Booleans.AFK_KICK) || player.hasPermission(plugin.getString(Strings.PERM_AFK))) {
                AfkStatusEvent event = new AfkStatusEvent(player, true, AfkStatusEvent.Cause.ACTIVITY);
                plugin.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                Component afkAutoEnterMessage = plugin.getMiniMessage(
                        Messages.AFK_AUTO_ENTER,
                        true,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
                plugin.getServer().broadcast(afkAutoEnterMessage);
                playerProfile.setAfk(true);
                return;
            }

            AfkStatusEvent event = new AfkStatusEvent(player, false, AfkStatusEvent.Cause.KICK);
            plugin.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            Component afkKickMessage = plugin.getMiniMessage(
                    Messages.AFK_BROADCAST_KICK,
                    true,
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay()
            );
            plugin.getServer().broadcast(afkKickMessage);
            player.kick(plugin.getMiniMessage(Messages.AFK_KICKED, false));
        }

        private void checkTeleports(Player player) {
            PlayerTeleport playerTeleport = plugin.locationManager().getTeleport(player.getUniqueId());

            if (playerTeleport == null) {
                return;
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_TIMING_BYPASS)) || playerTeleport.getCountdown() <= 0) {
                Component message = playerTeleport.getMessage();

                player.teleportAsync(playerTeleport.getWantLocation());
                player.sendMessage(message);
                plugin.locationManager().removeTeleport(player.getUniqueId());
                return;
            }

            plugin.sendMiniMessages(player, Messages.TELEPORT_TIMING, String.format("%.1f", (playerTeleport.getCountdown() / 20F)));
            playerTeleport.decreaseCountdown(ticks);
        }
    }

}
