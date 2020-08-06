package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.objects.UUIDFetcher;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerJoin implements Listener {

    private final EterniaServer plugin;

    public OnPlayerJoin(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            plugin.getInternMethods().addUUIF(player);
            Vars.global.put(playerName, 0);
            playerMutedExist(uuid);
            if (player.hasPermission("eternia.spy")) {
                Vars.spy.put(playerName, true);
            }
            if (Vars.nickname.containsKey(playerName)) {
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', Vars.nickname.get(playerName)));
            }
        }

        final long time = System.currentTimeMillis();
        Vars.afkTime.put(playerName, time);
        if (!Vars.playerLogin.containsKey(uuid)) {
            Location location = getWarp();
            if (location != plugin.error) {
                PaperLib.teleportAsync(player, getWarp());
            }
            playerProfileCreate(uuid, playerName);
        } else {
            Vars.playerLast.put(uuid, time);
            if (!Vars.playerName.get(uuid).equals(playerName)) {
                UUIDFetcher.putUUIDAndName(uuid, playerName);
                Vars.playerName.put(uuid, playerName);
                EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_PLAYER, Strings.PLAYER_NAME, playerName, Strings.UUID, uuid.toString()));
            }
            EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_PLAYER, Strings.LAST, time, Strings.UUID, uuid.toString()));
        }

        playerKitsCreate(playerName);
        playerChecks(playerName);

        event.setJoinMessage(null);
        plugin.getEFiles().broadcastMessage(Strings.MSG_JOIN, Constants.PLAYER, player.getDisplayName());
    }

    private void playerProfileCreate(UUID uuid, String playerName) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_PLAYER, "(uuid, player_name, time, last, hours)",
                "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "')"));
        UUIDFetcher.putUUIDAndName(uuid, playerName);
        Vars.playerLogin.put(uuid, time);
        Vars.playerLast.put(uuid, time);
        Vars.playerHours.put(uuid, 0);
        Vars.playerName.put(uuid, playerName);
    }

    private void playerMutedExist(UUID uuid) {
        if (!Vars.playerMuted.containsKey(uuid)) {
            playerMutedCreate(uuid);
        }
    }

    private void playerChecks(String playerName) {
        Vars.afkTime.put(playerName, System.currentTimeMillis());
    }

    private void playerMutedCreate(UUID uuid) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_MUTED, Strings.UUID, uuid.toString(), Strings.TIME, time));
        Vars.playerMuted.put(uuid, time);
    }

    private void playerKitsCreate(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.kits")) {
            final long time = System.currentTimeMillis();
            for (String kit : EterniaServer.kitConfig.getConfigurationSection("kits").getKeys(true)) {
                final String kitName = kit + "." + playerName;
                if (!Vars.kitsCooldown.containsKey(kitName)) {
                    EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_KITS, Strings.NAME, kitName, Strings.COOLDOWN, time));
                    Vars.kitsCooldown.put(kitName, time);
                }
            }
        }
    }

    private Location getWarp() {
        return Vars.warps.containsKey("spawn") ? Vars.warps.get("spawn") : plugin.error;
    }

}