package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import net.kyori.adventure.text.Component;

final class Services {

    static class Core {

        private final Component serverMOTD;

        protected Core(final EterniaServer plugin) {
            this.serverMOTD = plugin.parseColor(plugin.getString(Strings.MINI_MESSAGES_SERVER_SERVER_LIST));
        }

        protected Component getServerMOTD() {
            return this.serverMOTD;
        }

    }

}
