package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.enums.Colors;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

public class APIServer {

    private APIServer() {
        throw new IllegalStateException("Utility class");
    }

    public static void setChatMuted(boolean bool) {
        Vars.chatMuted = bool;
    }

    public static boolean isChatMuted() {
        return Vars.chatMuted;
    }

    public static boolean hasCooldown(long cooldown, int timeNeeded) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cooldown) >= timeNeeded;
    }

    public static String getTimeLeftOfCooldown(long cooldown) {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown - System.currentTimeMillis()));
    }

    public static String getTimeLeftOfCooldown(long cooldown, long cd) {
        return String.valueOf(cooldown - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cd));
    }

    public static boolean isInFutureCooldown(long cooldown) {
        return cooldown - System.currentTimeMillis() > 0;
    }

    public static Object[] listWarp() {
        return Vars.locations.keySet().toArray();
    }

    public static boolean hasLocation(String warpName) {
        return Vars.locations.containsKey(warpName);
    }

    public static Location getLocation(String warpName) {
        return Vars.locations.getOrDefault(warpName, Vars.getError());
    }

    public static void putLocation(String warpName, Location location) {
        Vars.locations.put(warpName, location);
    }

    public static void removeLocation(String warpName) {
        Vars.locations.remove(warpName);
    }

    public static Set<Player> getVanishList() {
        return Vars.vanished.keySet();
    }

    public static int getVersion() {
        if (Vars.getVersion() == 0) {
            String bukkitVersion = Bukkit.getBukkitVersion();
            if (bukkitVersion.contains("1.16")) Vars.setVersion(116);
            else if (bukkitVersion.contains("1.15")) Vars.setVersion(115);
            else if (bukkitVersion.contains("1.14")) Vars.setVersion(114);
            else Vars.setVersion(113);
        }
        return Vars.getVersion();
    }

    public static void updateRewardMap(String key, String value) {
        Vars.rewards.put(key, value);
    }

    public static int getRewardMapSize() {
        return Vars.rewards.size();
    }

    public static boolean hasReward(String key) {
        return Vars.rewards.containsKey(key);
    }

    public static void addReward(String key, String reward) {
        Vars.rewards.put(key, reward);
    }

    public static void removeReward(String key) {
        Vars.rewards.remove(key);
    }

    public static void putColorOnList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).replace('$', (char) 0x00A7));
        }
    }

    public static String getReward(String key) {
        return Vars.rewards.get(key);
    }

    public static void logError(String errorMsg, int level) {
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
        Bukkit.getConsoleSender().sendMessage(("$8[$aE$9S$8] " + errorLevel + "$8:$3" + errorMsg + "$8.").replace('$', (char) 0x00A7));
    }

    public static String setPlaceholders(Player p, String s) {
        s = s.replace("%player_name%", p.getName());
        s = s.replace("%player_displayname%", p.getDisplayName());
        return PlaceholderAPI.setPlaceholders(p, s);
    }

    public static Colors colorFromString(String colorName) {
        for (Colors b : Colors.values()) {
            if (b.name().equalsIgnoreCase(colorName)) {
                return b;
            }
        }
        return Colors.WHITE;
    }

    public static String getColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<Player> getSleeping(final World world) {
        return world.getPlayers().stream().filter(Player::isSleeping).collect(toList());
    }

    public static int getXPForLevel(int lvl) {
        if (lvl > 0 && lvl < 16) return (lvl * lvl) + 6 * lvl;
        else if (lvl > 15 && lvl < 31) return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
        else if (lvl >= 31) return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
        else return 0;
    }

}
