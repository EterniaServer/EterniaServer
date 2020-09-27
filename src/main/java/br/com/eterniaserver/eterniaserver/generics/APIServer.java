package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public interface APIServer {

    static boolean isChatMuted() {
        return PluginVars.chatMuted;
    }

    static void removeFromSpy(String playerName) {
        PluginVars.spy.remove(playerName);
    }

    static void putSpy(String playerName) {
        PluginVars.spy.put(playerName, true);
    }

    static void disableSpy(String playerName) {
        PluginVars.spy.put(playerName, false);
    }

    static boolean isSpying(String playerName) {
        return PluginVars.spy.getOrDefault(playerName, false);
    }

    static void putInTeleport(Player player, PlayerTeleport playerTeleport) {
        PluginVars.teleports.put(player, playerTeleport);
    }

    static Object[] listWarp() {
        return PluginVars.locations.keySet().toArray();
    }

    static void putBackLocation(String playerName, Location location) {
        PluginVars.back.put(playerName, location);
    }

    static Scoreboard getScoreboard() {
        if (PluginVars.getScoreboard() == null) {
            Scoreboard tempScoreBoard = Bukkit.getScoreboardManager().getMainScoreboard();
            List<Colors> colors = Arrays.asList(Colors.values());
            for (int i = 0; i < 16; i++) {
                if (tempScoreBoard.getTeam(colors.get(i).name()) == null) {
                    tempScoreBoard.registerNewTeam(colors.get(i).name()).setColor(PluginVars.colors.get(i));
                }
            }
            PluginVars.setScoreboard(tempScoreBoard);
        }
        return PluginVars.getScoreboard();
    }

    static boolean hasBackLocation(String playerName) {
        return PluginVars.back.containsKey(playerName);
    }

    static Location getBackLocation(String playerName) {
        return PluginVars.back.get(playerName);
    }

    static boolean hasLocation(String warpName) {
        return PluginVars.locations.containsKey(warpName);
    }

    static Location getLocation(String warpName) {
        return PluginVars.locations.getOrDefault(warpName, PluginVars.error);
    }

    static void putLocation(String warpName, Location location) {
        PluginVars.locations.put(warpName, location);
    }

    static void removeLocation(String warpName) {
        PluginVars.locations.remove(warpName);
    }

    static void putBedCooldown(String playerName) {
        PluginVars.bedCooldown.put(playerName, System.currentTimeMillis());
    }

    static void putGlowing(String playerName, String nameColor) {
        PluginVars.glowingColor.put(playerName, nameColor);
    }

    static void putProfile(UUID uuid, PlayerProfile playerProfile) {
        PluginVars.playerProfile.put(uuid, playerProfile);
    }

    static int getProfileMapSize() {
        return PluginVars.playerProfile.size();
    }

    static void playerProfileCreate(UUID uuid, String playerName, long firstPlayed) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(PluginConstants.getQueryInsert(EterniaServer.configs.tablePlayer, "(uuid, player_name, time, last, hours, balance, muted)",
                "('" + uuid.toString() + "', '" + playerName + "', '" + firstPlayed + "', '" + time + "', '" + 0 + "', '" + EterniaServer.configs.startMoney + "', '" + time + "')"));
        final PlayerProfile playerProfile = new PlayerProfile(
                playerName,
                firstPlayed,
                time,
                0
        );
        playerProfile.setBalance(EterniaServer.configs.startMoney);
        playerProfile.setMuted(time);
        PluginVars.playerProfile.put(uuid, playerProfile);
    }

    static void playerKitsCreate(String playerName) {
        final long time = System.currentTimeMillis();
        for (String kit : EterniaServer.kitConfig.getConfigurationSection("kits").getKeys(false)) {
            final String kitName = kit + "." + playerName;
            if (!PluginVars.kitsCooldown.containsKey(kitName)) {
                EQueries.executeQuery(PluginConstants.getQueryInsert(EterniaServer.configs.tableKits, PluginConstants.NAME_STR, kitName, PluginConstants.COOLDOWN_STR, time));
                PluginVars.kitsCooldown.put(kitName, time);
            }
        }
    }

    static long getKitCooldown(String kit) {
        return PluginVars.kitsCooldown.get(kit);
    }

    static void putKitCooldown(String kit, long time) {
        PluginVars.kitsCooldown.put(kit, time);
    }

    static int getVersion() {
        if (PluginVars.getVersion() == 0) {
            String bukkitVersion = Bukkit.getBukkitVersion();
            if (bukkitVersion.contains("1.16")) PluginVars.setVersion(116);
            else if (bukkitVersion.contains("1.15")) PluginVars.setVersion(115);
            else if (bukkitVersion.contains("1.14")) PluginVars.setVersion(114);
            else PluginVars.setVersion(113);
        }
        return PluginVars.getVersion();
    }

    static void updateRewardMap(Map<String, String> map) {
        map.forEach(PluginVars.rewards::put);
    }

    static int getRewardMapSize() {
        return PluginVars.rewards.size();
    }

    static boolean hasReward(String key) {
        return PluginVars.rewards.containsKey(key);
    }

    static String getReward(String key) {
        return PluginVars.rewards.get(key);
    }

    static void logError(String errorMsg, int level) {
        String errorLevel;
        switch (level) {
            case 1:
                errorLevel = ChatColor.GREEN + "Leve";
                break;
            case 2:
                errorLevel = ChatColor.YELLOW + "Aviso";
                break;
            default:
                errorLevel = ChatColor.RED + "Erro";
        }
        Bukkit.getConsoleSender().sendMessage("$8[$aE$9S$8] " + errorLevel + "$8:$3" + errorMsg + "$8.");
    }

    static Colors colorFromString(String colorName) {
        for (Colors b : Colors.values()) {
            if (b.name().equalsIgnoreCase(colorName)) {
                return b;
            }
        }
        return Colors.WHITE;
    }

}
