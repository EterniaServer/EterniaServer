package br.com.eterniaserver.eterniaserver.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

public class PlayerRelated {

    private static final Map<String, Long> kitsCooldown = new HashMap<>();

    private static final Map<UUID, PlayerTeleport> teleports = new HashMap<>();

    private static final Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();

    private static final Map<UUID, Boolean> onAfk = new HashMap<>();
    private static final Map<UUID, Long> afkTime = new HashMap<>();

    private static final Map<UUID, UUID> tpaRequests = new HashMap<>();
    private static final Map<UUID, Long> tpaTime = new HashMap<>();

    private static final Map<Player, Boolean> vanished = new HashMap<>();

    private static final Map<UUID, Boolean> godMode = new HashMap<>();
    private static final Map<UUID, Location> playerLocationMap = new HashMap<>();
    private static final Map<UUID, Integer> playersInPortal = new HashMap<>();
    private static final Map<UUID, Boolean> spy = new HashMap<>();
    private static final Map<UUID, Location> back = new HashMap<>();
    private static final Map<UUID, String> glowingColor = new HashMap<>();

    private static final Map<UUID, String> tell = new HashMap<>();
    private static final Map<UUID, UUID> chatLocked = new HashMap<>();

    private static final Map<String, UUID> playersName = new HashMap<>();

    private PlayerRelated() {
        throw new IllegalStateException("Utility class");
    }

    public static void putLocationOfUser(UUID uuid, Location location) {
        playerLocationMap.put(uuid, location);
    }

    public static Location getLocationOfUser(UUID uuid, Location location) {
        return playerLocationMap.getOrDefault(uuid, location);
    }

    /**
     * Get the time of a user that are in portal
     * @param uuid of user
     * @return the time
     */
    public static int getInPortal(UUID uuid) {
        return playersInPortal.getOrDefault(uuid, -1);
    }

    /**
     * Put a time from a user that are in portal
     * @param uuid of user
     * @param time
     */
    public static void putInPortal(UUID uuid, int time) {
        playersInPortal.put(uuid, time);
    }

    /**
     * Checks if a user is online to mention
     * @param playerName of user
     * @return if are
     */
    public static boolean hasNameOnline(String playerName) {
        return playersName.containsKey(playerName);
    }

    /**
     * Get a UUID from mention. Can return null
     * @param playerName of user
     * @return the UUID of user
     */
    public static UUID getUUIDFromMention(String playerName) {
        return playersName.get(playerName);
    }

    /**
     * Set a playerName online for mention
     * @param playerName of user
     * @param uuid of user
     */
    public static void setNameOnline(String playerName, UUID uuid) {
        playersName.put(playerName, uuid);
    }

    /**
     * Get the UUID's with spy enabled
     * @return the keyset object
     */
    public static Set<UUID> getSpyKeySet() {
        return spy.keySet();
    }

    /**
     * Removes a user from spy
     * @param uuid of user
     */
    public static void removeFromSpy(UUID uuid) {
        spy.remove(uuid);
    }

    /**
     * Change the state of spy from user
     * @param uuid of user
     */
    public static void changeSpyState(UUID uuid) {
        spy.put(uuid, !isSpying(uuid));
    }

    /**
     * Checks if a user are with spy enabled
     * @param uuid of user
     * @return if are
     */
    public static boolean isSpying(UUID uuid) {
        return spy.getOrDefault(uuid, false);
    }

    /**
     * Change the state of vanish from user
     * @param player object of user
     */
    public static void changeVanishState(Player player) {
        vanished.put(player, !isVanished(player));
    }
    
    /**
     * Check if a user is in vanish
     * @param player object of user
     * @return if are
     */
    public static boolean isVanished(Player player) {
        return vanished.getOrDefault(player, false);
    }

    /**
     * Get the users in vanish
     * @return the players objects
     */
    public static Set<Player> getVanishList() {
        return vanished.keySet();
    }

    /**
     * Change the godmode of user
     * @param uuid of user
     */
    public static void changeGodModeState(UUID uuid) {
        godMode.put(uuid, !getGodMode(uuid));
    }

    /**
     * Check if a user are in godmode
     * @return if are
     */
    public static boolean getGodMode(UUID uuid) {
        return godMode.getOrDefault(uuid, false);
    }

    /**
     * Update the last location user
     * @param uuid of user
     * @param location
     */
    public static void putBackLocation(UUID uuid, Location location) {
        back.put(uuid, location);
    }

    /**
     * Check if a user has a last location
     * @param uuid of user
     * @return if has
     */
    public static boolean hasBackLocation(UUID uuid) {
        return back.containsKey(uuid);
    }

    /**
     * Get the last location of user
     * @param uuid of user
     * @return the location
     */
    public static Location getBackLocation(UUID uuid) {
        return back.get(uuid);
    }

    /**
     * Put a user in tell with other by the name
     * @param uuid of user
     * @param playerName of target
     */
    public static void putInTell(UUID uuid, String playerName) {
        tell.put(uuid, playerName);
    }

    /**
     * Checks if a user received a private message
     * @param uuid of user
     * @return if has
     */
    public static boolean receivedTell(UUID uuid) {
        return tell.containsKey(uuid);
    }

    /**
     * 
     * @param uuid
     * @return
     */
    public static String getTellSender(UUID uuid) {
        return tell.get(uuid);
    }

    /**
     * Checks if a user are telling
     * @param uuid of user
     * @return return if are
     */
    public static boolean isTell(UUID uuid) {
        return chatLocked.containsKey(uuid);
    }

    /**
     * Set a target to tell with you
     * @param userUUID of user
     * @param uuid of target
     */
    public static void setTelling(UUID userUUID, UUID uuid) {
        chatLocked.put(userUUID, uuid);
    }

    /**
     * Get the player that are in tell with you
     * @param uuid of user
     * @return the UUID of target
     */
    public static UUID getTellingPlayerName(UUID uuid) {
        return chatLocked.get(uuid);
    }

    /**
     * Unlock a player from telling
     * @param uuid of user
     */
    public static void removeTelling(UUID uuid) {
        chatLocked.remove(uuid);
    }

    /**
     * Set a glow color to user
     * @param uuid of user
     * @param nameColor
     */
    public static void putGlowing(UUID uuid, String nameColor) {
        glowingColor.put(uuid, nameColor);
    }

    /**
     * Get the glow color of user
     * @param uuid of user
     * @return the color
     */
    public static String getGlowColor(UUID uuid) {
        return glowingColor.getOrDefault(uuid, "");
    }

    /**
     * Get the time of last TPA
     * @param uuid of user
     * @return the time
     */
    public static long getTPATime(UUID uuid) {
        return tpaTime.getOrDefault(uuid, 0L);
    }

    /**
     * Get the target user that request a tpa. Can return null
     * @param uuid of user
     * @return the uuid of target user
     */
    public static UUID getTpaSender(UUID uuid) {
        return tpaRequests.get(uuid);
    }

    /**
     * Remove a tpa request from a user
     * @param uuid of user
     */
    public static void removeTpaRequest(UUID uuid) {
        tpaTime.remove(uuid);
        tpaRequests.remove(uuid);
    }

    /**
     * Put a tpa request from one user to another user
     * @param target uuid of target user
     * @param uuid of user
     */
    public static void putTpaRequest(UUID target, UUID uuid) {
        tpaRequests.put(target, uuid);
        tpaTime.put(target, System.currentTimeMillis());
    }

    /**
     * Check if a user has a tpa request searching by uuid
     * @param uuid of user
     * @return if the user have
     */
    public static boolean hasTpaRequest(UUID uuid) {
        return tpaRequests.containsKey(uuid);
    }

    /**
     * Get the cooldown of a kit
     * @param kitname
     * @return the time
     */
    public static long getKitCooldown(String kitname) {
        return kitsCooldown.get(kitname);
    }

    /**
     * Update the cooldown of a kit
     * @param kitName
     * @param time
     */
    public static void putKitCooldown(String kitName, long time) {
        kitsCooldown.put(kitName, time);
    }

    /**
     * Generate
     * @param playerName
     */
    public static void generatePlayerKits(String playerName) {
        for (String kit : EterniaServer.getKitList().keySet()) {
            String kitName = kit + "." + playerName;

            if (!kitsCooldown.containsKey(kitName)) {
                Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_KITS));
                insert.columns.set("name", "cooldown");
                insert.values.set(kitName, System.currentTimeMillis());
                SQL.executeAsync(insert);

                kitsCooldown.put(kitName, System.currentTimeMillis());
            }
        }
    }

    /**
     * Get a PlayerTeleport searching from uuid. Can return null
     * @param uuid of user
     * @return PlayerTeleport of user
     */
    public static PlayerTeleport getPlayerTeleport(UUID uuid) {
        return teleports.get(uuid);
    }

    /**
     * Checks if the user are teleporting
     * @param uuid of user
     * @return if the user are teleporting
     */
    public static boolean areTeleporting(UUID uuid) {
        return teleports.containsKey(uuid);
    }

    /**
     * Remove the PlayerTeleport of user
     * @param uuid of user
     */
    public static void removeFromTeleport(UUID uuid) {
        teleports.remove(uuid);
    }

    /**
     * Set a PlayerTeleport to a user
     * @param uuid of user
     * @param playerTeleport of user teleport
     */
    public static void putInTeleport(UUID uuid, PlayerTeleport playerTeleport) {
        teleports.put(uuid, playerTeleport);
    }

    /**
     * Remove the user from memory
     * @param uuid'
     */
    public static void playerLogout(UUID uuid) {
        afkTime.remove(uuid);
        onAfk.remove(uuid);
        godMode.remove(uuid);
        spy.remove(uuid);
        vanished.remove(Bukkit.getPlayer(uuid));
    }

    /**
     * Update the time of last action of user
     * @param uuid of user
     */
    public static void updateAFKTime(UUID uuid) {
        afkTime.put(uuid, System.currentTimeMillis());
    }

    /**
     * Get the last time the user made an action searching from uuid
     * @param uuid of user
     * @return the time
    */
    public static long getAFKTime(UUID uuid) {
        return afkTime.getOrDefault(uuid, System.currentTimeMillis());
    }

    /**
     * Checks if the user are AFK
     * @param uuid of user
     * @return if the user are AFK
     */
    public static boolean areAFK(UUID uuid) {
        return onAfk.getOrDefault(uuid, false);
    }

    /**
     * Change the boolean state of a user
     * @param uuid of user
     */
    public static void changeAFKState(UUID uuid) {
        onAfk.put(uuid, !areAFK(uuid));
    }

    /**
     * Set a new PlayerProfile to a user
     * @param uuid of user
     * @param playerProfile of user
     */
    public static void putProfile(UUID uuid, PlayerProfile playerProfile) {
        playerProfiles.put(uuid, playerProfile);
    }

    /**
     * Get a PlayerProfile searching for uuid. Can return null
     * @param uuid of user
     * @return PlayerProfile of user
     */
    public static PlayerProfile getProfile(UUID uuid) {
        return playerProfiles.get(uuid);
    }

    /**
     * Checks if a user has a PlayerProfile
     * @param uuid of user
     * @return if the user has a PlayerProfile
     */
    public static boolean hasProfile(UUID uuid) {
        return playerProfiles.containsKey(uuid);
    }

    /**
     * Create a new PlayerProfile to a user uuid
     * @param uuid of user
     * @param playerName of user
     */
    public static void createProfile(UUID uuid, String playerName) {
        long time = System.currentTimeMillis();

        Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_PLAYER));
        insert.columns.set("uuid", "player_name", "time", "last", "hours", "balance", "muted");
        insert.values.set(uuid.toString(), playerName, time, time, 0, EterniaServer.getDouble(Doubles.START_MONEY), time);
        SQL.executeAsync(insert);

        PlayerProfile playerProfile = new PlayerProfile(playerName, time, time, 0);
        playerProfile.setBalance(EterniaServer.getDouble(Doubles.START_MONEY));
        playerProfile.setMuted(time);
        playerProfiles.put(uuid, playerProfile);
    }

    /**
     * @return the amount of saved PlayerProfile's
     */
    public static int getProfileMapSize() {
        return playerProfiles.size();
    }

}
