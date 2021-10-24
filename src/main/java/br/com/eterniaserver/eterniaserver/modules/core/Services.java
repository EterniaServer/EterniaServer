package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

final class Services {

    static class Afk {

        private final EterniaServer plugin;

        protected Afk(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        protected boolean areAfk(PlayerProfile playerProfile) {
            final int secondsAfk = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - playerProfile.getLastMove());
            final int limitTime = plugin.getInteger(Integers.AFK_TIMER);

            return secondsAfk > limitTime;
        }

        protected void exitFromAfk(Player player, PlayerProfile playerProfile, AfkStatusEvent.Cause cause) {
            if (!playerProfile.getAfk()) {
                playerProfile.setLastMove(System.currentTimeMillis());
                return;
            }

            final AfkStatusEvent event = new AfkStatusEvent(player, false, cause);
            plugin.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                playerProfile.setAfk(false);
                playerProfile.setLastMove(System.currentTimeMillis());
                Bukkit.broadcast(plugin.getMiniMessage(Messages.AFK_LEAVE, true, playerProfile.getName(), playerProfile.getDisplayName()));
            }
        }
    }

}
