package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

final class Handlers implements Listener {

    private final EterniaServer plugin;

    public Handlers(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!EterniaServer.getEconomyAPI().hasAccount(player)) {
                EterniaServer.getEconomyAPI().createPlayerAccount(player);
            }
        });
    }

}
