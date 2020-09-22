package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;
import java.util.UUID;

public class APIServer {

    private static Scoreboard scoreboard;

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

    public static Scoreboard getScoreboard() {
        if (scoreboard == null) {
            Scoreboard tempScoreBoard = Bukkit.getScoreboardManager().getMainScoreboard();
            for (int i = 0; i < 16; i++) {
                if (tempScoreBoard.getTeam(PluginVars.arrData.get(i)) == null) {
                    tempScoreBoard.registerNewTeam(PluginVars.arrData.get(i)).setColor(PluginVars.colors.get(i));
                }
            }
            scoreboard = tempScoreBoard;
        }
        return scoreboard;
    }

    public static boolean hasBackLocation(String playerName) {
        return PluginVars.back.containsKey(playerName);
    }

    public static Location getBackLocation(String playerName) {
        return PluginVars.back.get(playerName);
    }

    public static boolean hasLocation(String warpName) {
        return PluginVars.locations.containsKey(warpName);
    }

    public static Location getLocation(String warpName) {
        return PluginVars.locations.getOrDefault(warpName, PluginVars.error);
    }

    public static void putLocation(String warpName, Location location) {
        PluginVars.locations.put(warpName, location);
    }

    public static void removeLocation(String warpName) {
        PluginVars.locations.remove(warpName);
    }

    public static void putBedCooldown(String playerName) {
        PluginVars.bedCooldown.put(playerName, System.currentTimeMillis());
    }

    public static void putGlowing(String playerName, String nameColor) {
        PluginVars.glowingColor.put(playerName, nameColor);
    }

    public static void putProfile(UUID uuid, PlayerProfile playerProfile) {
        PluginVars.playerProfile.put(uuid, playerProfile);
    }

    public static int getProfileMapSize() {
        return PluginVars.playerProfile.size();
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

    public static long getKitCooldown(String kit) {
        return PluginVars.kitsCooldown.get(kit);
    }

    public static void putKitCooldown(String kit, long time) {
        PluginVars.kitsCooldown.put(kit, time);
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

    public static void updateRewardMap(Map<String, String> map) {
        map.forEach(PluginVars.rewards::put);
    }

    public static int getRewardMapSize() {
        return PluginVars.rewards.size();
    }

    public static boolean hasReward(String key) {
        return PluginVars.rewards.containsKey(key);
    }

    public static String getReward(String key) {
        return PluginVars.rewards.get(key);
    }

}
