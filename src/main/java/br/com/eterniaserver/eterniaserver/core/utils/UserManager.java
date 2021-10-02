package br.com.eterniaserver.eterniaserver.core.utils;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.User;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    private static final Map<UUID, PlayerProfile> profileMap = new ConcurrentHashMap<>();
    private static final Map<UUID, User> userMap = new ConcurrentHashMap<>();

    /**
     * Try to get a UUID from a playerName and
     * then check and return User from Cache
     * @param playerName in String
     * @return the User object
     */
    public static User get(final String playerName) {
        return getUserFromMapping(UUIDFetcher.getUUIDOf(playerName));
    }

    /**
     * Check and return User from Cache
     * by UUID
     * @param uuid of User
     * @return the User object
     */
    public static User get(final UUID uuid) {
        return getUserFromMapping(uuid);
    }

    /**
     * Check and return User from Cache
     * by Player
     * @param player object of User
     * @return the User object
     */
    public static User get(final Player player) {
        return getUserFromMapping(player.getUniqueId());
    }

    /**
     * Check if commandSender is a
     * instance of a Player Object
     * and return the User
     * @param commandSender object of User
     * @return the User object
     */
    public static User get(final CommandSender commandSender) {
        return commandSender instanceof Player ? get((Player) commandSender) : new User(commandSender);
    }

    private static User getUserFromMapping(final UUID uuid) {
        final var user = userMap.get(uuid);
        return user == null ? userMap.put(uuid, createUser(uuid)) : user;
    }

    private static User createUser(final UUID uuid) {
        final var offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        final var player = offlinePlayer.getPlayer();
        final var playerName = offlinePlayer.getName();

        if (!EterniaServer.getUserAPI().hasProfile(uuid)) {
            final var playerProfile = EterniaServer.getUserAPI().createPlayerProfile(uuid, playerName);
            final var playerDisplayName = playerProfile.getPlayerDisplayName();

            return new User(playerName, playerDisplayName, uuid, offlinePlayer, player, player != null);
        }

        final var playerProfile = EterniaServer.getUserAPI().getProfile(uuid);
        final var playerDisplayName = playerProfile.getPlayerDisplayName();

        return new User(playerName, playerDisplayName, uuid, offlinePlayer, player, false);
    }
}
