package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


final class Schedules {

    static class MainTick extends BukkitRunnable {

        private final EterniaServer plugin;

        private final Services.Afk afkServices;

        public MainTick(final EterniaServer plugin, final Services.Afk afkServices) {
            this.plugin = plugin;
            this.afkServices = afkServices;
        }

        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());
                checkAFK(player, playerProfile);
            }
        }

        private void checkAFK(Player player, PlayerProfile playerProfile) {
            if (playerProfile.getAfk() || !afkServices.areAfk(playerProfile)) return;

            if (!plugin.getBoolean(Booleans.AFK_KICK) && !playerProfile.getAfk() || !playerProfile.getAfk() && player.hasPermission(plugin.getString(Strings.PERM_AFK))) {
                Bukkit.broadcast(plugin.getMiniMessage(Messages.AFK_AUTO_ENTER, true, playerProfile.getPlayerName(), playerProfile.getPlayerDisplayName()));
                playerProfile.setOnAfk(true);
                return;
            }

            if (!playerProfile.getAfk()) {
                Bukkit.broadcast(plugin.getMiniMessage(Messages.AFK_BROADCAST_KICK, true, playerProfile.getPlayerName(), playerProfile.getPlayerDisplayName()));
                player.kick(plugin.getMiniMessage(Messages.AFK_KICKED, false));
            }

        }
    }

}
