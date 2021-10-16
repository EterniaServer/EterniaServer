package br.com.eterniaserver.eterniaserver.craft;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CraftUser {

    private final EterniaServer plugin;

    public CraftUser(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, PlayerTeleport> teleports = new HashMap<>();
    private final Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();
    private final Map<UUID, UUID> tpaRequests = new HashMap<>();
    private final Map<UUID, Long> tpaTime = new HashMap<>();
    private final Map<UUID, Boolean> godMode = new HashMap<>();
    private final Map<UUID, Boolean> spy = new HashMap<>();
    private final Map<UUID, Location> back = new HashMap<>();
    private final Map<UUID, String> tell = new HashMap<>();
    private final Map<UUID, UUID> chatLocked = new HashMap<>();

    private final Map<String, Long> kitsCooldown = new HashMap<>();
    private final Map<String, UUID> playersName = new HashMap<>();

    private final Map<Player, Boolean> vanished = new HashMap<>();

    /**
     * Checks if a user is online to mention
     * @param playerName of user
     * @return if are
     */
    public boolean hasNameOnline(String playerName) {
        return playersName.containsKey(playerName);
    }

    /**
     * Get a UUID from mention. Can return null
     * @param playerName of user
     * @return the UUID of user
     */
    public UUID getUUIDFromMention(String playerName) {
        return playersName.get(playerName);
    }

    /**
     * Set a playerName online for mention
     * @param playerName of user
     * @param uuid of user
     */
    public void setNameOnline(String playerName, UUID uuid) {
        playersName.put(playerName, uuid);
    }

    /**
     * Get the UUID's with spy enabled
     * @return the keyset object
     */
    public Set<UUID> getSpyKeySet() {
        return spy.keySet();
    }

    /**
     * Removes a user from spy
     * @param uuid of user
     */
    public void removeFromSpy(UUID uuid) {
        spy.remove(uuid);
    }

    /**
     * Change the state of spy from user
     * @param uuid of user
     */
    public void changeSpyState(UUID uuid) {
        spy.put(uuid, !isSpying(uuid));
    }

    /**
     * Checks if a user are with spy enabled
     * @param uuid of user
     * @return if are
     */
    public boolean isSpying(UUID uuid) {
        return spy.getOrDefault(uuid, false);
    }

    /**
     * Change the state of vanish from user
     * @param player object of user
     */
    public void changeVanishState(Player player) {
        vanished.put(player, !isVanished(player));
    }
    
    /**
     * Check if a user is in vanish
     * @param player object of user
     * @return if are
     */
    public boolean isVanished(Player player) {
        return vanished.getOrDefault(player, false);
    }

    /**
     * Get the users in vanish
     * @return the players objects
     */
    public Set<Player> getVanishList() {
        return vanished.keySet();
    }

    /**
     * Change the godmode of user
     * @param uuid of user
     */
    public void changeGodModeState(UUID uuid) {
        godMode.put(uuid, !getGodMode(uuid));
    }

    /**
     * Check if a user are in godmode
     * @return if are
     */
    public boolean getGodMode(UUID uuid) {
        return godMode.getOrDefault(uuid, false);
    }

    /**
     * Update the last location user
     * @param uuid of user
     * @param location
     */
    public void putBackLocation(UUID uuid, Location location) {
        back.put(uuid, location);
    }

    /**
     * Check if a user has a last location
     * @param uuid of user
     * @return if has
     */
    public boolean hasBackLocation(UUID uuid) {
        return back.containsKey(uuid);
    }

    /**
     * Get the last location of user
     * @param uuid of user
     * @return the location
     */
    public Location getBackLocation(UUID uuid) {
        return back.get(uuid);
    }

    /**
     * Put a user in tell with other by the name
     * @param uuid of user
     * @param playerName of target
     */
    public void putInTell(UUID uuid, String playerName) {
        tell.put(uuid, playerName);
    }

    /**
     * Checks if a user received a private message
     * @param uuid of user
     * @return if has
     */
    public boolean receivedTell(UUID uuid) {
        return tell.containsKey(uuid);
    }

    /**
     * 
     * @param uuid
     * @return
     */
    public String getTellSender(UUID uuid) {
        return tell.get(uuid);
    }

    /**
     * Checks if a user are telling
     * @param uuid of user
     * @return return if are
     */
    public boolean isTell(UUID uuid) {
        return chatLocked.containsKey(uuid);
    }

    /**
     * Set a target to tell with you
     * @param userUUID of user
     * @param uuid of target
     */
    public void setTelling(UUID userUUID, UUID uuid) {
        chatLocked.put(userUUID, uuid);
    }

    /**
     * Get the player that are in tell with you
     * @param uuid of user
     * @return the UUID of target
     */
    public UUID getTellingPlayerName(UUID uuid) {
        return chatLocked.get(uuid);
    }

    /**
     * Unlock a player from telling
     * @param uuid of user
     */
    public void removeTelling(UUID uuid) {
        chatLocked.remove(uuid);
    }

    /**
     * Get the target user that request a tpa. Can return null
     * @param uuid of user
     * @return the uuid of target user
     */
    public UUID getTpaSender(UUID uuid) {
        return tpaRequests.get(uuid);
    }

    /**
     * Remove a tpa request from a user
     * @param uuid of user
     */
    public void removeTpaRequest(UUID uuid) {
        tpaTime.remove(uuid);
        tpaRequests.remove(uuid);
    }

    /**
     * Put a tpa request from one user to another user
     * @param target uuid of target user
     * @param uuid of user
     */
    public void putTpaRequest(UUID target, UUID uuid) {
        tpaRequests.put(target, uuid);
        tpaTime.put(target, System.currentTimeMillis());
    }

    /**
     * Check if a user has a tpa request searching by uuid
     * @param uuid of user
     * @return if the user have
     */
    public boolean hasTpaRequest(UUID uuid) {
        return tpaRequests.containsKey(uuid);
    }

    /**
     * Get the cooldown of a kit
     * @param kitname
     * @return the time
     */
    public long getKitCooldown(String kitname) {
        return kitsCooldown.get(kitname);
    }

    /**
     * Update the cooldown of a kit
     * @param kitName
     * @param time
     */
    public void putKitCooldown(String kitName, long time) {
        kitsCooldown.put(kitName, time);
    }

    /**
     * Generate
     * @param playerName
     */
    public void generatePlayerKits(String playerName) {
        for (String kit : plugin.getKitList().keySet()) {
            String kitName = kit + "." + playerName;

            if (!kitsCooldown.containsKey(kitName)) {
                Insert insert = new Insert(plugin.getString(Strings.TABLE_KITS));
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
    public PlayerTeleport getPlayerTeleport(UUID uuid) {
        return teleports.get(uuid);
    }

    /**
     * Checks if the user are teleporting
     * @param uuid of user
     * @return if the user are teleporting
     */
    public boolean areTeleporting(UUID uuid) {
        return teleports.containsKey(uuid);
    }

    /**
     * Remove the PlayerTeleport of user
     * @param uuid of user
     */
    public void removeFromTeleport(UUID uuid) {
        teleports.remove(uuid);
    }

    /**
     * Set a PlayerTeleport to a user
     * @param uuid of user
     * @param playerTeleport of user teleport
     */
    public void putInTeleport(UUID uuid, PlayerTeleport playerTeleport) {
        teleports.put(uuid, playerTeleport);
    }

    /**
     * Remove the user from memory
     * @param uuid'
     */
    public void playerLogout(UUID uuid) {
        godMode.remove(uuid);
        spy.remove(uuid);
        vanished.remove(Bukkit.getPlayer(uuid));
    }

    /**
     * Set a new PlayerProfile to a user
     * @param uuid of user
     * @param playerProfile of user
     */
    public void putProfile(UUID uuid, PlayerProfile playerProfile) {
        playerProfiles.put(uuid, playerProfile);
    }

    /**
     * Get a PlayerProfile searching for uuid. Can return null
     * @param uuid of user
     * @return PlayerProfile of user
     */
    public PlayerProfile getProfile(UUID uuid) {
        return playerProfiles.get(uuid);
    }

    /**
     * Checks if a user has a PlayerProfile
     * @param uuid of user
     * @return if the user has a PlayerProfile
     */
    public boolean hasProfile(UUID uuid) {
        return playerProfiles.containsKey(uuid);
    }

    /**
     * Returns the default time that user
     * stays on pvp
     * @return the int
     */
    public int getPvPTime() {
        return plugin.getInteger(Integers.PVP_TIME);
    }

    /**
     * Send a message to all server that a new
     * players come in
     * @param playerName name
     * @param playerDisplayName displayname
     */
    public void firstLoginMessage(final String playerName, final String playerDisplayName) {
        Bukkit.broadcastMessage(plugin.getMessage(Messages.SERVER_FIRST_LOGIN, true, playerName, playerDisplayName));
    }

    /**
     * Create a new PlayerProfile to a user uuid
     * @param uuid of user
     * @param playerName of user
     */
    public void createProfile(UUID uuid, String playerName) {
        long time = System.currentTimeMillis();

        Insert insert = new Insert(plugin.getString(Strings.TABLE_PLAYER));
        insert.columns.set("uuid", "player_name", "time", "last", "hours", "balance", "muted");
        insert.values.set(uuid.toString(), playerName, time, time, 0, plugin.getDouble(Doubles.START_MONEY), time);
        SQL.executeAsync(insert);

        PlayerProfile playerProfile = new PlayerProfile(playerName, playerName, time, time, 0);
        EterniaServer.getEconomyAPI().putInMoney(uuid, plugin.getDouble(Doubles.START_MONEY));
        playerProfile.setMuted(time);
        playerProfiles.put(uuid, playerProfile);
    }

    /**
     * @return the amount of saved PlayerProfile's
     */
    public int getProfileMapSize() {
        return playerProfiles.size();
    }

    public void sendPrivateMessage(final Player target,
                                   final String s,
                                   final Player player,
                                   final UUID uuid,
                                   final String playerName,
                                   final String playerDisplayName) {
        User user = new User(target);

        EterniaServer.getUserAPI().putInTell(user.getUUID(), playerName);
        player.sendMessage(user.getUUID(), plugin.getMessage(Messages.CHAT_TELL_TO, false, s, playerName, playerDisplayName, user.getName(), user.getDisplayName()));
        target.sendMessage(uuid, plugin.getMessage(Messages.CHAT_TELL_FROM, false, s, user.getName(), user.getDisplayName(), playerName, playerDisplayName));

        for (UUID uuidTemp : EterniaServer.getUserAPI().getSpyKeySet()) {
            if (EterniaServer.getUserAPI().isSpying(uuidTemp) && !uuidTemp.equals(uuid) && !uuidTemp.equals(user.getUUID())) {
                Player spyPlayer = Bukkit.getPlayer(uuidTemp);
                if (spyPlayer != null && spyPlayer.isOnline()) {
                    spyPlayer.sendMessage(uuid, plugin.getColor(plugin.getString(Strings.CONS_SPY)
                            .replace("{0}", playerName)
                            .replace("{1}", playerDisplayName)
                            .replace("{2}", user.getName())
                            .replace("{3}", user.getDisplayName())
                            .replace("{4}", s)));
                } else {
                    EterniaServer.getUserAPI().removeFromSpy(uuidTemp);
                }
            }
        }
    }

    public String getGodeModePlaceholder(final UUID uuid) {
        return getGodMode(uuid) ? plugin.getString(Strings.GOD_PLACEHOLDER) : "";
    }

    public void changeNick(String nick, final Player player, final String playerName, final UUID uuid, final PlayerProfile playerProfile) {
        if (nick.equals(plugin.getString(Strings.CLEAR_STRING))) {
            plugin.sendMessage(player, Messages.CHAT_NICK_CLEAR);
            playerProfile.setPlayerDisplayName(playerName);
            player.setDisplayName(playerName);

            Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
            update.set.set("player_display", playerProfile.getPlayerDisplayName());
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
            return;
        }

        if (!player.hasPermission(plugin.getString(Strings.PERM_CHAT_COLOR_NICK))) {
            nick = ChatColor.stripColor(nick);
        }

        playerProfile.setPlayerDisplayName(nick);
        player.setDisplayName(nick);
        plugin.sendMessage(player, Messages.CHAT_NICK_CHANGE, nick);

        Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
        update.set.set("player_display", playerProfile.getPlayerDisplayName());
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
    }

    public void updateProfile(final PlayerProfile playerProfile, final UUID uuid, final String playerName) {
        playerProfile.setLastLogin(System.currentTimeMillis());
        if (!playerProfile.getPlayerName().equals(playerName)) {
            playerProfile.setPlayerName(playerName);

            Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
            update.set.set("player_name", playerName);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
        }

        Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
        update.set.set("last", System.currentTimeMillis());
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
    }

    public String getFirstLoginPlaceholder(final PlayerProfile playerProfile) {
        if (playerProfile != null) {
            return new SimpleDateFormat(plugin.getString(Strings.DATA_FORMAT)).format(new Date(playerProfile.getFirstLogin()));
        }
        return plugin.getString(Strings.NO_REGISTER);
    }

    public void teleportToSpawn(final Player player) {
        player.teleport(plugin.getLocation("warp.spawn"));
    }

    public long getBedCooldown(final UUID uuid) {
        return plugin.getBedCooldown(uuid);
    }

    public void updateBedCooldown(final UUID uuid) {
        plugin.updateBedCooldown(uuid);
    }

}
