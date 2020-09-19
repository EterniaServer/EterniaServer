package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class APIServer {

    private static int version = 0;

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

    public static void disableSpy(String playerName) {
        PluginVars.spy.put(playerName, false);
    }

    public static boolean isSpying(String playerName) {
        return PluginVars.spy.getOrDefault(playerName, false);
    }

    public static void putInTeleport(Player player, PlayerTeleport playerTeleport) {
        PluginVars.teleports.put(player, playerTeleport);
    }

    public static Object[] listWarp() {
        return PluginVars.locations.keySet().toArray();
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

    public static void putWarp(String warpName, Location location) {
        PluginVars.locations.put(warpName, location);
    }

    public static void removeWarp(String warpName) {
        PluginVars.locations.remove(warpName);
    }

    public static void putBedCooldown(String playerName) {
        PluginVars.bedCooldown.put(playerName, System.currentTimeMillis());
    }

    public static void putGlowing(String playerName, String nameColor) {
        PluginVars.glowingColor.put(playerName, nameColor);
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

    public static int getVersion() {
        if (version == 0) {
            String bukkitVersion = Bukkit.getBukkitVersion();
            if (bukkitVersion.contains("1.16")) version = 116;
            else if (bukkitVersion.contains("1.15")) version = 115;
            else if (bukkitVersion.contains("1.14")) version = 114;
            else version = 113;
        }
        return version;
    }

}
