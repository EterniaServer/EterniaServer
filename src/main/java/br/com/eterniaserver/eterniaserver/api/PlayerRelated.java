package br.com.eterniaserver.eterniaserver.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

public class PlayerRelated {

    private static final Map<UUID, PlayerTeleport> teleports = new HashMap<>();
    private static final Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();
    private static final Map<UUID, Boolean> onAfk = new HashMap<>();
    private static final Map<UUID, Long> afkTime = new HashMap<>();

    private PlayerRelated() {
        throw new IllegalStateException("Utility class");
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
        onAfk.put(uuid, areAFK(uuid));
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
