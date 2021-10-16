package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.api.events.OptimizedPlayerMoveEvent;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final Component serverMOTD;

    private final Services.Afk afkServices;

    public Handlers(final EterniaServer plugin, final Services.Afk afkServices) {
        this.plugin = plugin;
        this.afkServices = afkServices;
        this.serverMOTD = plugin.parseColor(plugin.getString(Strings.MINI_MESSAGES_SERVER_SERVER_LIST));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.motd(serverMOTD);
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.COMMAND);

        final String message = event.getMessage().toLowerCase();
        for (String line : plugin.getStringList(Lists.BLACKLISTED_COMMANDS)) {
            if (message.startsWith(line)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChatEvent(AsyncChatEvent event) {
        final Player player = event.getPlayer();
        final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.CHAT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.INTERACT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOptimizedPlayerMoveEvent(OptimizedPlayerMoveEvent event) {
        if (!event.hasChangedBlock()) return;

        final Player player = event.getPlayer();
        final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.MOVE);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.JOIN);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.QUIT);
    }

}
