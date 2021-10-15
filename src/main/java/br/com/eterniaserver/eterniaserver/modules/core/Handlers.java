package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

final class Handlers implements Listener {

    private final Component serverMOTD;

    public Handlers(final EterniaServer plugin) {
        serverMOTD = plugin.parseColor(plugin.getString(Strings.MINI_MESSAGES_SERVER_SERVER_LIST));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.motd(serverMOTD);
    }

}
