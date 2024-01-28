package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


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
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof Player) {
            PlayerProfile damagerProfile = databaseInterface.get(PlayerProfile.class, damager.getUniqueId());
            damagerProfile.setPvpLastJoin(System.currentTimeMillis());
            damagerProfile.setOnPvP(true);
            plugin.sendMiniMessages(damager, Messages.ENTERED_PVP);

            if (damager.isFlying() && !damager.hasPermission(plugin.getString(Strings.PERM_FLY_BYPASS))) {
                damager.setFlying(false);
                damager.setAllowFlight(false);
                plugin.sendMiniMessages(damager, Messages.FLY_DISABLED_ENTERED_PVP);
            }
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

        playerProfile.setEnterMillis(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

        int minutesPlayed = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - playerProfile.getEnterMillis());

        playerProfile.setPlayedMinutes(playerProfile.getPlayedMinutes() + minutesPlayed);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> databaseInterface.update(PlayerProfile.class, playerProfile));

        event.quitMessage(plugin.getMiniMessage(
                Messages.SERVER_LOGOUT,
                true,
                playerProfile.getPlayerName(),
                playerProfile.getPlayerDisplay()
        ));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.motd(serverMOTD);
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.equalsIgnoreCase("/tps")) {
            plugin.sendMiniMessages(event.getPlayer(), Messages.SERVER_TPS, String.format("%.2f", plugin.getServer().getTPS()[0]));
            event.setCancelled(true);
            return;
        }
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

        if (!player.getName().equals(playerProfile.getPlayerName())) {
            playerProfile.setPlayerName(player.getName());
            playerProfile.setPlayerDisplay(player.getName());
            plugin.getServer().getScheduler().runTaskAsynchronously(
                    plugin,
                    () -> EterniaLib.getDatabase().update(PlayerProfile.class, playerProfile)
            );
        }

        event.joinMessage(plugin.getMiniMessage(
                Messages.SERVER_LOGIN,
                true,
                playerProfile.getPlayerName(),
                playerProfile.getPlayerDisplay()
        ));

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.JOIN);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.QUIT);
    }

}
