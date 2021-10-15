package br.com.eterniaserver.eterniaserver.modules.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

final class Handlers implements Listener {

    private final Services.Core servicesCore;

    public Handlers(final Services.Core servicesCore) {
        this.servicesCore = servicesCore;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.motd(servicesCore.getServerMOTD());
    }

}
