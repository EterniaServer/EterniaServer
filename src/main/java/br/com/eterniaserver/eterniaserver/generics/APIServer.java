package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class APIServer {

    private APIServer() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isChatMuted() {
        return PluginVars.chatMuted;
    }

    public static void removeFromSpy(String playerName) {
        PluginVars.spy.remove(playerName);
    }

    public static void putSpy(String playerName) {
        PluginVars.spy.put(playerName, true);
    }

    public static void putInTeleport(Player player, PlayerTeleport playerTeleport) {
        PluginVars.teleports.put(player, playerTeleport);
    }

    public static void putBackLocation(String playerName, Location location) {
        PluginVars.back.put(playerName, location);
    }

    public static boolean hasWarp(String warpName) {
        return PluginVars.locations.containsKey(warpName);
    }

    public static Location getWarp(String warpName) {
        return PluginVars.locations.getOrDefault(warpName, PluginVars.error);
    }

    public static void putBedCooldown(String playerName) {
        PluginVars.bedCooldown.put(playerName, System.currentTimeMillis());
    }

    public static void playerProfileCreate(UUID uuid, String playerName, long firstPlayed) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(PluginConstants.getQueryInsert(PluginConfigs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance, muted)",
                "('" + uuid.toString() + "', '" + playerName + "', '" + firstPlayed + "', '" + time + "', '" + 0 + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "', '" + time + "')"));
        final PlayerProfile playerProfile = new PlayerProfile(
                playerName,
                firstPlayed,
                time,
                0
        );
        playerProfile.balance = EterniaServer.serverConfig.getDouble("money.start");
        playerProfile.muted = time;
        PluginVars.playerProfile.put(uuid, playerProfile);
    }

    public static void playerKitsCreate(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.kits")) {
            final long time = System.currentTimeMillis();
            for (String kit : EterniaServer.kitConfig.getConfigurationSection("kits").getKeys(false)) {
                final String kitName = kit + "." + playerName;
                if (!PluginVars.kitsCooldown.containsKey(kitName)) {
                    EQueries.executeQuery(PluginConstants.getQueryInsert(PluginConfigs.TABLE_KITS, PluginConstants.NAME_STR, kitName, PluginConstants.COOLDOWN_STR, time));
                    PluginVars.kitsCooldown.put(kitName, time);
                }
            }
        }
    }

}
