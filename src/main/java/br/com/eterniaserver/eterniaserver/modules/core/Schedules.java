package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


final class Schedules {

    static class MainTick extends BukkitRunnable {

        private final EterniaServer plugin;
        private final Services.Afk afkServices;

        private final int ticks;

        public MainTick(final EterniaServer plugin, final Services.Afk afkServices) {
            this.plugin = plugin;
            this.afkServices = afkServices;
            this.ticks = plugin.getInteger(Integers.PLUGIN_TICKS);
        }

        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());

                checkTeleports(player);
                checkAFK(player, playerProfile);
            }
        }

        private void checkAFK(Player player, PlayerProfile playerProfile) {
            if (playerProfile.getAfk() || !afkServices.areAfk(playerProfile)) return;

            if (!plugin.getBoolean(Booleans.AFK_KICK) && !playerProfile.getAfk() || !playerProfile.getAfk() && player.hasPermission(plugin.getString(Strings.PERM_AFK))) {
                final AfkStatusEvent event = new AfkStatusEvent(player, true, AfkStatusEvent.Cause.ACTIVITY);
                plugin.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) return;

                Bukkit.broadcast(plugin.getMiniMessage(Messages.AFK_AUTO_ENTER, true, playerProfile.getName(), playerProfile.getDisplayName()));
                playerProfile.setAfk(true);
                return;
            }

            if (!playerProfile.getAfk()) {
                final AfkStatusEvent event = new AfkStatusEvent(player, false, AfkStatusEvent.Cause.KICK);
                plugin.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) return;

                Bukkit.broadcast(plugin.getMiniMessage(Messages.AFK_BROADCAST_KICK, true, playerProfile.getName(), playerProfile.getDisplayName()));
                player.kick(plugin.getMiniMessage(Messages.AFK_KICKED, false));
            }
        }

        private void checkTeleports(Player player) {
            PlayerTeleport playerTeleport = plugin.locationManager().getTeleport(player.getUniqueId());

            if (playerTeleport == null) return;

            if (player.hasPermission(plugin.getString(Strings.PERM_TIMING_BYPASS)) || playerTeleport.getCountdown() <= 0) {
                PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                player.sendMessage(playerTeleport.getMessage());
                plugin.locationManager().removeTeleport(player.getUniqueId());
                return;
            }

            plugin.sendMiniMessages(player, Messages.TELEPORT_TIMING, String.format("%.1f", (playerTeleport.getCountdown() / 20F)));
            playerTeleport.decreaseCountdown(ticks);
        }
    }

}
