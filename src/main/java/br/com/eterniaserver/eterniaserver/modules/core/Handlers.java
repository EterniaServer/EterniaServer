package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Services.AfkService;
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

    private final AfkService afkServices;

    public Handlers(EterniaServer plugin, AfkService afkServices) {
        this.plugin = plugin;
        this.afkServices = afkServices;
        this.serverMOTD = EterniaLib.getChatCommons().parseColor(plugin.getString(Strings.MINI_MESSAGES_SERVER_SERVER_LIST));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());
            event.setCancelled(playerProfile.isGod());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof Player) {
            PlayerProfile damagerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, damager.getUniqueId());
            damagerProfile.setPvpLastJoin(System.currentTimeMillis());
            damagerProfile.setOnPvP(true);
            EterniaLib.getChatCommons().sendMessage(damager, Messages.ENTERED_PVP);

            if (damager.isFlying() && !damager.hasPermission(plugin.getString(Strings.PERM_FLY_BYPASS))) {
                damager.setFlying(false);
                damager.setAllowFlight(false);
                EterniaLib.getChatCommons().sendMessage(damager, Messages.FLY_DISABLED_ENTERED_PVP);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String playerName = event.getName();
        UUID uuid = event.getUniqueId();
        PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, uuid);

        if (playerProfile.getUuid() == null) {
            playerProfile.setPlayerName(playerName);
            playerProfile.setPlayerDisplay(playerName);
            playerProfile.setUuid(uuid);
            playerProfile.setPlayedMinutes(0);
            playerProfile.setFirstJoin(new Timestamp(System.currentTimeMillis()));
            playerProfile.setLastJoin(new Timestamp(System.currentTimeMillis()));

            EterniaLib.getDatabase().insert(PlayerProfile.class, playerProfile);
        } else {
            playerProfile.setLastJoin(new Timestamp(System.currentTimeMillis()));
            EterniaLib.getDatabase().update(PlayerProfile.class, playerProfile);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

        playerProfile.setEnterMillis(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

        int minutesPlayed = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - playerProfile.getEnterMillis());

        playerProfile.setPlayedMinutes(playerProfile.getPlayedMinutes() + minutesPlayed);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> EterniaLib.getDatabase().update(PlayerProfile.class, playerProfile));

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.QUIT);

        MessageOptions options = new MessageOptions(
                playerProfile.getPlayerName(),
                playerProfile.getPlayerDisplay()
        );
        event.quitMessage(EterniaLib.getChatCommons().parseMessage(Messages.SERVER_LOGOUT, options));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.motd(serverMOTD);
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.equalsIgnoreCase("/tps") || message.equalsIgnoreCase("/mspt")) {
            MessageOptions options = new MessageOptions(String.format("%.2f", plugin.getServer().getTPS()[0]));
            EterniaLib.getChatCommons().sendMessage(event.getPlayer(), Messages.SERVER_TPS, options);
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
        PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.CHAT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.INTERACT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedBlock()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.MOVE);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

        if (!player.getName().equals(playerProfile.getPlayerName())) {
            playerProfile.setPlayerName(player.getName());
            playerProfile.setPlayerDisplay(player.getName());
            plugin.getServer().getScheduler().runTaskAsynchronously(
                    plugin,
                    () -> EterniaLib.getDatabase().update(PlayerProfile.class, playerProfile)
            );
        }

        MessageOptions options = new MessageOptions(
                playerProfile.getPlayerName(),
                playerProfile.getPlayerDisplay()
        );
        event.joinMessage(EterniaLib.getChatCommons().parseMessage(Messages.SERVER_LOGIN, options));

        afkServices.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.JOIN);
    }

}
