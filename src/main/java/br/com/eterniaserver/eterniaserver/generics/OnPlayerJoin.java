package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            InternMethods.addUUIF(player);
            Vars.global.put(playerName, 0);
            playerMutedExist(uuid);
            if (player.hasPermission("eternia.spy")) {
                Vars.spy.put(playerName, true);
            }
            if (Vars.playerProfile.containsKey(uuid)) {
                player.setDisplayName(Vars.playerProfile.get(uuid).getPlayerDisplayName());
            }
        }

        final long time = System.currentTimeMillis();
        Vars.afkTime.put(playerName, time);
        if (!Vars.playerProfile.containsKey(uuid)) {
            Location location = getWarp();
            if (location != EterniaServer.error) {
                PaperLib.teleportAsync(player, getWarp());
            }
            playerProfileCreate(uuid, playerName);
        } else {
            final PlayerProfile playerProfile = Vars.playerProfile.get(uuid);
            playerProfile.setLastLogin(time);
            if (!playerProfile.getPlayerName().equals(playerName)) {
                playerProfile.setPlayerName(playerName);
                Vars.playerProfile.put(uuid, playerProfile);
                EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_PLAYER, Strings.PLAYER_NAME, playerName, Strings.UUID, uuid.toString()));
            }
            EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_PLAYER, Strings.LAST, time, Strings.UUID, uuid.toString()));
        }

        playerKitsCreate(playerName);
        playerChecks(playerName);

        event.setJoinMessage(null);
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_JOIN.replace(Constants.PLAYER, player.getDisplayName()));
    }

    private void playerProfileCreate(UUID uuid, String playerName) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_PLAYER, "(uuid, player_name, time, last, hours)",
                "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "')"));
        Vars.playerProfile.put(uuid, new PlayerProfile(
                playerName,
                time,
                time,
                0
        ));
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
            for (String kit : EterniaServer.kitConfig.getConfigurationSection("kits").getKeys(false)) {
                final String kitName = kit + "." + playerName;
                if (!Vars.kitsCooldown.containsKey(kitName)) {
                    EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_KITS, Strings.NAME, kitName, Strings.COOLDOWN, time));
                    Vars.kitsCooldown.put(kitName, time);
                }
            }
        }
    }

    private Location getWarp() {
        return Vars.warps.getOrDefault("spawn", EterniaServer.error);
    }

}