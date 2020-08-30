package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class EventPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        final long time = System.currentTimeMillis();

        Vars.afkTime.put(playerName, time);
        if (!Vars.playerProfile.containsKey(uuid)) {
            Location location = getWarp();
            if (location != Vars.error) {
                PaperLib.teleportAsync(player, getWarp());
            }
            playerProfileCreate(uuid, playerName, player.getFirstPlayed());
        } else {
            PlayerProfile playerProfile = Vars.playerProfile.get(uuid);
            if (playerProfile.playerName == null) {
                final PlayerProfile newPlayerProfile = new PlayerProfile(playerName, time, time, 0);
                newPlayerProfile.cash = playerProfile.cash;
                newPlayerProfile.balance = playerProfile.balance;
                newPlayerProfile.xp = playerProfile.xp;
                newPlayerProfile.muted = time;
                EQueries.executeQuery(
                        "UPDATE " + Configs.TABLE_PLAYER +
                                " SET player_name='" + playerName +
                                "', player_display='" + playerName +
                                "', time='" + player.getFirstPlayed() +
                                "', last='" + time +
                                "', hours='" + 0 +
                                "', muted='" + time +
                                "' WHERE uuid='" + uuid.toString() + "'");
                playerProfile = newPlayerProfile;
                Vars.playerProfile.put(uuid, newPlayerProfile);
            }
            playerProfile.lastLogin = time;
            if (!playerProfile.getPlayerName().equals(playerName)) {
                playerProfile.setPlayerName(playerName);
                Vars.playerProfile.put(uuid, playerProfile);
                EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.PLAYER_NAME_STR, playerName, Constants.UUID_STR, uuid.toString()));
            }
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.LAST_STR, time, Constants.UUID_STR, uuid.toString()));
        }

        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            InternMethods.addUUIF(player);
            if (player.hasPermission("eternia.spy")) {
                Vars.spy.put(playerName, true);
            }
            if (Vars.playerProfile.containsKey(uuid)) {
                player.setDisplayName(Vars.playerProfile.get(uuid).getPlayerDisplayName());
            }
        }

        playerKitsCreate(playerName);

        event.setJoinMessage(null);
        Bukkit.broadcastMessage(InternMethods.putName(player, Strings.MSG_JOIN));
    }

    private void playerProfileCreate(UUID uuid, String playerName, long firstPlayed) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance, muted)",
                "('" + uuid.toString() + "', '" + playerName + "', '" + firstPlayed + "', '" + time + "', '" + 0 + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "', '" + time + "')"));
        final PlayerProfile playerProfile = new PlayerProfile(
                playerName,
                firstPlayed,
                time,
                0
        );
        playerProfile.balance = EterniaServer.serverConfig.getDouble("money.start");
        playerProfile.muted = time;
        Vars.playerProfile.put(uuid, playerProfile);

    }

    private void playerKitsCreate(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.kits")) {
            final long time = System.currentTimeMillis();
            for (String kit : EterniaServer.kitConfig.getConfigurationSection("kits").getKeys(false)) {
                final String kitName = kit + "." + playerName;
                if (!Vars.kitsCooldown.containsKey(kitName)) {
                    EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_KITS, Constants.NAME_STR, kitName, Constants.COOLDOWN_STR, time));
                    Vars.kitsCooldown.put(kitName, time);
                }
            }
        }
    }

    private Location getWarp() {
        return Vars.locations.getOrDefault("warp.spawn", Vars.error);
    }

}