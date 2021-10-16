package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

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

    }

}
