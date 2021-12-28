package br.com.eterniaserver.eterniaserver.craft;

import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CommandData;
import br.com.eterniaserver.eterniaserver.objects.CustomKit;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CraftEterniaServer extends JavaPlugin {

    private Pattern filter;
    private Location error;

    private boolean chatMuted = false;
    private long nightMessageTime = System.currentTimeMillis();

    private final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");
    private final List<World> skippingWorlds = new ArrayList<>();
    private final List<String> shopList = new ArrayList<>();

    private final Map<UUID, Long> bedCooldown = new HashMap<>();
    private final Map<String, Location> locations = new HashMap<>();

    public final List<String> channels = new ArrayList<>();
    public final List<Material> elevatorMaterials = new ArrayList<>();
    public final List<ItemStack> menuGui = new ArrayList<>();
    public final List<List<String>> stringLists = new ArrayList<>();
    public final List<Map<String, Map<Double, List<String>>>> chanceMaps = new ArrayList<>();

    public final Map<String, Integer> guisInvert = new HashMap<>();
    public final Map<String, CustomKit> kitList = new HashMap<>();
    public final Map<String, CommandData> customCommandMap = new HashMap<>();
    public final Map<String, CustomPlaceholder> customPlaceholdersObjectsMap = new HashMap<>();
    public final Map<String, Map<Integer, List<String>>> scheduleMap = new HashMap<>();

    public final Map<Integer, ChannelObject> channelsMap = new HashMap<>();

    public List<Material> elevatorMaterials() {
        return elevatorMaterials;
    }

    public List<Map<String, Map<Double, List<String>>>> chanceMaps() {
        return chanceMaps;
    }

    public CraftEterniaServer() {
        super();
        for (int i = 0; i < Lists.values().length; i++) {
            stringLists.add(new ArrayList<>());
        }
        for (int i = 0; i < ChanceMaps.values().length; i++) {
            chanceMaps.add(new HashMap<>());
        }
    }

    public CraftEterniaServer(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        for (int i = 0; i < Lists.values().length; i++) {
            stringLists.add(new ArrayList<>());
        }
        for (int i = 0; i < ChanceMaps.values().length; i++) {
            chanceMaps.add(new HashMap<>());
        }
    }

    public void setFilter(Pattern pattern) {
        filter = pattern;
    }

    public Pattern getFilter() {
        return filter;
    }

    public List<String> getStringList(Lists configName) {
        return stringLists.get(configName.ordinal());
    }

    public List<Material> getElevatorMaterials() {
        return elevatorMaterials;
    }

    public List<ItemStack> getMenuGui() {
        return menuGui;
    }

    public Map<String, CustomPlaceholder> getCustomPlaceholders() {
        return customPlaceholdersObjectsMap;
    }

    public Map<String, CommandData> getCustomCommandMap() {
        return customCommandMap;
    }

    public Map<String, CustomKit> getKitList() {
        return kitList;
    }

    public Map<String, Integer> getGuisInvert() {
        return guisInvert;
    }

    public Map<String, Map<Integer, List<String>>> getScheduleMap() {
        return scheduleMap;
    }

    public Map<Integer, ChannelObject> getChannelsMap() {
        return channelsMap;
    }

    public Map<String, Map<Double, List<String>>> getChanceMap(ChanceMaps configName) {
        return chanceMaps.get(configName.ordinal());
    }

    public List<String> getChannels() {
        return channels;
    }

    public ChannelObject channelObject(int value) {
        return channelsMap.get(value);
    }

    public List<String> getShopList() {
        return shopList;
    }

    /**
     * Check if has a cooldown
     * @param cooldown
     * @param timeNeeded
     * @return if has
     */
    public boolean hasCooldown(long cooldown, int timeNeeded) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cooldown) >= timeNeeded;
    }

    /**
     * Get the left time of a cooldown without a cooldown related
     * @param cooldown
     * @return the time left in string
     */
    public String getTimeLeftOfCooldown(long cooldown) {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown - System.currentTimeMillis()));
    }

    /**
     * Get the left time of a cooldown
     * @param cooldown base
     * @param cooldown related
     * @return the time left in string
     */
    public String getTimeLeftOfCooldown(long cooldown, long cd) {
        return String.valueOf(cooldown - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cd));
    }

    /**
     * Check if a cooldown is in future
     * @param cooldown to check
     * @return if is
     */
    public boolean isInFutureCooldown(long cooldown) {
        return cooldown - System.currentTimeMillis() > 0;
    }

    /**
     * Translate color codes in a list
     * @param list to translate
     */
    public void putColorOnList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).replace('$', (char) 0x00A7));
        }
    }

    /**
     * Send a log error to console
     * @param errorMsg message
     * @param level of error
     */
    public void logError(String errorMsg, int level) {
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
    public String setPlaceholders(Player player, String message) {
        message = message.replace("%player_name%", player.getName());
        message = message.replace("%player_displayname%", player.getDisplayName());
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    /**
     * Just translate a color code to color
     * @param string
     * @return the color
     */
    public String getColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Get users that are sleeping
     * @param world to searching
     * @return a list of players objects
     */
    public List<Player> getSleeping(final World world) {
        List<Player> listOfSleepingPlayers = new ArrayList<>();
        for (Player player : world.getPlayers()) {
            if (player.isSleeping()) {
                listOfSleepingPlayers.add(player);
            }
        }
        return listOfSleepingPlayers;
    }

    /**
     * Get the list of warps
     * @return the array object
     */
    public Object[] listWarp() {
        return locations.keySet().toArray();
    }

    /**
     * Check if exists a location searching by warpname
     * @param warpName
     * @return if has
     */
    public boolean hasLocation(String warpName) {
        return locations.containsKey(warpName);
    }

    /**
     * Get a location searching by warp name
     * @param warpName
     * @return the location
     */
    public Location getLocation(String warpName) {
        return locations.getOrDefault(warpName, getError());
    }

    /**
     * Put a warp in memory
     * @param warpName
     * @param location
     */
    public void putLocation(String warpName, Location location) {
        locations.put(warpName, location);
    }

    /**
     * Remove a location from memory
     * @param warpName
     */
    public void removeLocation(String warpName) {
        locations.remove(warpName);
    }

    /**
     * Get the error location
     * @return the location
     */
    public Location getError() {
        return error;
    }

    /**
     * Set the error location
     * @param location
     */
    public void setError(Location location) {
        error = location;
    }

    /**
     * Change the state of lock chats
     */
    public void changeChatLockState() {
        chatMuted = !chatMuted;
    }

    /**
     * Checks if the chats are lock
     * @return if has
     */
    public boolean isChatMuted() {
        return chatMuted;
    }

    public String translateHex(final String message) {
        Matcher matcher = colorPattern.matcher(message);

        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            final String color = matcher.group(1);
            matcher.appendReplacement(builder,"&x&" +
                    color.charAt(1) + "&" +
                    color.charAt(2) + "&" +
                    color.charAt(3) + "&" +
                    color.charAt(4) + "&" +
                    color.charAt(5) + "&" +
                    color.charAt(6));
        }

        return matcher.appendTail(builder).toString().replace('&', (char) 0x00A7);
    }

    /**
     * Update the cooldown of Night Message Time
     */
    public void updateNightMessageTime() {
        nightMessageTime = System.currentTimeMillis();
    }

    /**
     * Get the Night Message Time cooldown
     * @return the time
     */
    public long getNightMessageTime() {
        return nightMessageTime;
    }

    /**
     * Start to skipping world
     * @param world
     */
    public void putInSkipping(World world) {
        skippingWorlds.add(world);
    }

    /**
     * Stop the world skipping
     * @param world
     */
    public void removeFromSkipping(World world) {
        skippingWorlds.remove(world);
    }

    /**
     * Checks if the world are skipping
     * @param world
     * @return if is
     */
    public boolean isSkipping(World world) {
        return skippingWorlds.contains(world);
    }

    /**
     * Get the Bed Cooldown the user by searching from uuid
     * @param uuid of user
     * @return the time
     */
    public long getBedCooldown(UUID uuid) {
        return bedCooldown.getOrDefault(uuid, 0L);
    }

    /**
     * Update the Bed Cooldown of user
     * @param uuid of user
     */
    public void updateBedCooldown(UUID uuid) {
        bedCooldown.put(uuid, System.currentTimeMillis());
    }

}
