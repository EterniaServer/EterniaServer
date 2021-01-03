package br.com.eterniaserver.eterniaserver.api;

import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.objects.CommandToRun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

import net.md_5.bungee.api.ChatColor;

public class ServerRelated {
    
    private static int version = 0;

    private static Location error;

    private static boolean chatMuted = false;

    private static long nightMessageTime = System.currentTimeMillis();
    private static final List<World> skippingWorlds = new ArrayList<>();
    private static final List<String> shopList = new ArrayList<>();

    private static final Map<UUID, Long> bedCooldown = new HashMap<>();
    private static final Map<String, String> rewards = new HashMap<>();
    private static final Map<String, Location> locations = new HashMap<>();
    private static final Map<UUID, CommandToRun> commandsToRun = new HashMap<>();

    private ServerRelated() {
        throw new IllegalStateException("Utility class");
    }

    public static List<String> getShopList() {
        return shopList;
    }

    /**
     * Get a command to be run. Can return null
     * @param uuid of user
     * @return the CommandToRun object
     */
    public static CommandToRun getCommandToRun(UUID uuid) {
        return commandsToRun.get(uuid);
    }

    /**
     * Put a Command to be check and ask to user
     * @param uuid of user
     * @param commandToRun object
     */
    public static void putCommandToRun(UUID uuid, CommandToRun commandToRun) {
        commandsToRun.put(uuid, commandToRun);
    }

    /**
     * Remove a command from commandsToRun map
     * @param uuid of user
     */
    public static void removeCommandToRun(UUID uuid) {
        commandsToRun.remove(uuid);
    }

    /**
     * Check if has a cooldown
     * @param cooldown
     * @param timeNeeded
     * @return if has
     */
    public static boolean hasCooldown(long cooldown, int timeNeeded) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cooldown) >= timeNeeded;
    }

    /**
     * Get the left time of a cooldown without a cooldown related
     * @param cooldown
     * @return the time left in string
     */
    public static String getTimeLeftOfCooldown(long cooldown) {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown - System.currentTimeMillis()));
    }

    /**
     * Get the left time of a cooldown
     * @param cooldown base
     * @param cooldown related
     * @return the time left in string
     */
    public static String getTimeLeftOfCooldown(long cooldown, long cd) {
        return String.valueOf(cooldown - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cd));
    }

    /**
     * Check if a cooldown is in future
     * @param cooldown to check
     * @return if is
     */
    public static boolean isInFutureCooldown(long cooldown) {
        return cooldown - System.currentTimeMillis() > 0;
    }

    /**
     * Translate color codes in a list
     * @param list to translate
     */
    public static void putColorOnList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).replace('$', (char) 0x00A7));
        }
    }

    /**
     * Send a log error to console
     * @param errorMsg message
     * @param level of error
     */
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

    /**
     * Set the placeholders from PlaceHolderAPI
     * @param player object of user
     * @param message to replace
     * @return message with placeholders
     */
    public static String setPlaceholders(Player player, String message) {
        message = message.replace("%player_name%", player.getName());
        message = message.replace("%player_displayname%", player.getDisplayName());
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    /**
     * Get a enum Colors from string
     * @param colorName
     * @return enum Colors
     */
    public static Colors colorFromString(String colorName) {
        for (Colors b : Colors.values()) {
            if (b.name().equalsIgnoreCase(colorName)) {
                return b;
            }
        }
        return Colors.WHITE;
    }

    /**
     * Just translate a color code to color
     * @param string
     * @return the color
     */
    public static String getColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Get users that are sleeping
     * @param world to searching
     * @return a list of players objects
     */
    public static List<Player> getSleeping(final World world) {
        List<Player> listOfSleepingPlayers = new ArrayList<>();
        for (Player player : world.getPlayers()) {
            if (player.isSleeping()) {
                listOfSleepingPlayers.add(player);
            }
        }
        return listOfSleepingPlayers;
    }

    /**
     * Convert level to exp
     * @param lvl amount
     * @return the exp amount
     */
    public static int getXPForLevel(int lvl) {
        if (lvl > 0 && lvl < 16) return (lvl * lvl) + 6 * lvl;
        else if (lvl > 15 && lvl < 31) return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
        else if (lvl >= 31) return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
        else return 0;
    }

    /**
     * Get the list of warps
     * @return the array object
     */
    public static Object[] listWarp() {
        return locations.keySet().toArray();
    }

    /**
     * Check if exists a location searching by warpname
     * @param warpName
     * @return if has
     */
    public static boolean hasLocation(String warpName) {
        return locations.containsKey(warpName);
    }

    /**
     * Get a location searching by warp name
     * @param warpName
     * @return the location
     */
    public static Location getLocation(String warpName) {
        return locations.getOrDefault(warpName, ServerRelated.getError());
    }

    /**
     * Put a warp in memory
     * @param warpName
     * @param location
     */
    public static void putLocation(String warpName, Location location) {
        locations.put(warpName, location);
    }

    /**
     * Remove a location from memory
     * @param warpName
     */
    public static void removeLocation(String warpName) {
        locations.remove(warpName);
    }

    /**
     * Get the error location
     * @return the location
     */
    public static Location getError() {
        return error;
    }

    /**
     * Set the error location
     * @param location
     */
    public static void setError(Location location) {
        error = location;
    }

    /**
     * Change the state of lock chats
     */
    public static void changeChatLockState() {
        chatMuted = !chatMuted;
    }

    /**
     * Checks if the chats are lock
     * @return if has
     */
    public static boolean isChatMuted() {
        return chatMuted;
    }

    /**
     * Get the version compatibility of Plugin
     * @return the int version
     */
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

    /**
     * Add a new reward to the memory
     * @param key of reward
     * @param value reward
     */
    public static void updateRewardMap(String key, String value) {
        rewards.put(key, value);
    }

    /**
     * Get the amount of rewards in memory
     * @return the size
     */
    public static int getRewardMapSize() {
        return rewards.size();
    }

    /**
     * Checks if exists a reward
     * @param key of reward
     * @return if has
     */
    public static boolean hasReward(String key) {
        return rewards.containsKey(key);
    }

    /**
     * Add a reward to the memory
     * @param key of reward
     * @param reward
     */
    public static void addReward(String key, String reward) {
        rewards.put(key, reward);
    }

    /**
     * Remove a reward from the memory
     * @param key
     */
    public static void removeReward(String key) {
        rewards.remove(key);
    }

    /**
     * Get a reward name searching by string key
     * @param key
     * @return the reward name
     */
    public static String getReward(String key) {
        return rewards.get(key);
    }

    /**
     * Update the cooldown of Night Message Time
     */
    public static void updateNightMessageTime() {
        nightMessageTime = System.currentTimeMillis();
    }

    /**
     * Get the Night Message Time cooldown
     * @return the time
     */
    public static long getNightMessageTime() {
        return nightMessageTime;
    }

    /**
     * Start to skipping world
     * @param world
     */
    public static void putInSkipping(World world) {
        skippingWorlds.add(world);
    }

    /**
     * Stop the world skipping
     * @param world
     */
    public static void removeFromSkipping(World world) {
        skippingWorlds.remove(world);
    }

    /**
     * Checks if the world are skipping
     * @param world
     * @return if is
     */
    public static boolean isSkipping(World world) {
        return skippingWorlds.contains(world);
    }

    /**
     * Get the Bed Cooldown the user by searching from uuid
     * @param uuid of user
     * @return the time
     */
    public static long getBedCooldown(UUID uuid) {
        return bedCooldown.getOrDefault(uuid, 0L);
    }

    /**
     * Update the Bed Cooldown of user
     * @param uuid of user
     */
    public static void updateBedCooldown(UUID uuid) {
        bedCooldown.put(uuid, System.currentTimeMillis());
    }

}
