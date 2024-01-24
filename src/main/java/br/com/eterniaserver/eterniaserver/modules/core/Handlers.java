package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Services.Afk;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import io.papermc.paper.event.player.AsyncChatEvent;

import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;

import java.sql.Timestamp;
import java.util.UUID;


final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final Component serverMOTD;

    private final Afk afkServices;
    private final DatabaseInterface databaseInterface;

    public Handlers(final EterniaServer plugin, Afk afkServices) {
        this.plugin = plugin;
        this.afkServices = afkServices;
        this.databaseInterface = EterniaLib.getDatabase();
        this.serverMOTD = plugin.parseColor(plugin.getString(Strings.MINI_MESSAGES_SERVER_SERVER_LIST));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());
            event.setCancelled(playerProfile.isGod());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName();
        UUID uuid = event.getUniqueId();
        PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, uuid);

        if (playerProfile.getUuid() == null) {
            playerProfile.setPlayerName(playerName);
            playerProfile.setPlayerDisplay(playerName);
            playerProfile.setUuid(uuid);
            playerProfile.setPlayedMinutes(0);
            playerProfile.setFirstJoin(new Timestamp(System.currentTimeMillis()));
            playerProfile.setLastJoin(new Timestamp(System.currentTimeMillis()));

            databaseInterface.insert(PlayerProfile.class, playerProfile);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.motd(serverMOTD);
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        for (String line : plugin.getStringList(Lists.BLACKLISTED_COMMANDS)) {
            if (message.startsWith(line)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAsyncChatEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.CHAT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.INTERACT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedBlock()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.MOVE);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Entities.PlayerProfile playerProfile = EterniaLib.getDatabase().get(
                Entities.PlayerProfile.class, player.getUniqueId()
        );

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.JOIN);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.QUIT);
    }

}
