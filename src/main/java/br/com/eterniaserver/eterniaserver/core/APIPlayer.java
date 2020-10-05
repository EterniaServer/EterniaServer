package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface APIPlayer {

    static String getFirstLogin(UUID uuid) {
        return PluginVars.playerProfile.containsKey(uuid) ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(PluginVars.playerProfile.get(uuid).getFirstLogin())) : "Sem registro";
    }

    static boolean isAFK(String playerName) {
        return PluginVars.afk.contains(playerName);
    }

    static String isAFKPlaceholder(String playerName) {
        return isAFK(playerName) ? EterniaServer.configs.afkPlaceholder : "";
    }

    static void removeFromAFK(String playerName) {
        PluginVars.afkTime.remove(playerName);
    }

    static void putAfk(String playerName) {
        PluginVars.afkTime.put(playerName, System.currentTimeMillis());
    }

    static void putInAfk(Player player) {
        PluginVars.afk.add(player.getName());
        PluginVars.playerLocationMap.put(player, player.getLocation());
    }

    static void removeAfk(String playerName) {
        PluginVars.afk.remove(playerName);
    }

    static boolean hasProfile(UUID uuid) {
        return PluginVars.playerProfile.containsKey(uuid);
    }

    static String getGlowColor(String playerName) {
        return PluginVars.glowingColor.getOrDefault(playerName, "");
    }

    static boolean isGod(String playerName) {
        return PluginVars.god.contains(playerName);
    }

    static void putGod(String playerName) {
        PluginVars.god.add(playerName);
    }

    static void removeGod(String playerName) {
        PluginVars.god.remove(playerName);
    }

    static String isGodPlaceholder(String playerName) {
        return isGod(playerName) ? EterniaServer.configs.godPlaceholder : "";
    }

    static long getMutedTime(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getMuted();
    }

    static void putMutedTime(UUID uuid, long time) {
        PluginVars.playerProfile.get(uuid).setMuted(time);
    }

    static String getDisplayName(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getPlayerDisplayName();
    }

    static String getName(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getPlayerName();
    }

    static void updateHome(UUID uuid, String home) {
        PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);
        if (!playerProfile.getHomes().contains(home)) {
            playerProfile.getHomes().add(home);
        }
    }

    static boolean hasTpaRequest(String playerName) {
        return PluginVars.tpaRequests.containsKey(playerName);
    }

    static String getTpaSender(String playerName) {
        return PluginVars.tpaRequests.get(playerName);
    }

    static void removeTpaRequest(String playerName) {
        PluginVars.tpaTime.remove(playerName);
        PluginVars.tpaRequests.remove(playerName);
    }

    static void putTpaRequest(String targetName, String playerName) {
        PluginVars.tpaRequests.remove(targetName);
        PluginVars.tpaRequests.put(targetName, playerName);
        PluginVars.tpaTime.put(targetName, System.currentTimeMillis());
    }

    static List<String> getHomes(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getHomes();
    }

    static boolean isTeleporting(Player player) {
        return PluginVars.teleports.containsKey(player);
    }

    static long getAndUpdateTimePlayed(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).updateTimePlayed();
    }

    static long getLastLogin(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getLastLogin();
    }

    static long getFirstLoginLong(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getFirstLogin();
    }

    static void changeFlyState(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            return;
        }

        player.setAllowFlight(true);
        player.setFlying(true);
    }

    static boolean isOnPvP(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).isOnPvP();
    }

    static int getPvPCooldown(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getOnPvP();
    }

    static void setIsOnPvP(UUID uuid) {
        PluginVars.playerProfile.get(uuid).setIsOnPvP();
    }

    static void updatePlayerProfile(UUID uuid, Player player, long time) {
        final String playerName = player.getName();
        PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);
        if (playerProfile.getPlayerName() == null) {
            final PlayerProfile newPlayerProfile = new PlayerProfile(playerName, time, time, 0);
            newPlayerProfile.setCash(playerProfile.getCash());
            newPlayerProfile.setBalance(playerProfile.getBalance());
            newPlayerProfile.setXp(playerProfile.getXp());
            newPlayerProfile.setMuted(time);
            EQueries.executeQuery(
                    "UPDATE " + EterniaServer.configs.tablePlayer +
                            " SET player_name='" + playerName +
                            "', player_display='" + playerName +
                            "', time='" + player.getFirstPlayed() +
                            "', last='" + time +
                            "', hours='" + 0 +
                            "', muted='" + time +
                            "' WHERE uuid='" + uuid.toString() + "'");
            playerProfile = newPlayerProfile;
            PluginVars.playerProfile.put(uuid, newPlayerProfile);
        }
        playerProfile.setLastLogin(time);
        if (!playerProfile.getPlayerName().equals(playerName)) {
            playerProfile.setPlayerName(playerName);
            PluginVars.playerProfile.put(uuid, playerProfile);
            EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "player_name", playerName, "uuid", uuid.toString()));
        }
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "last", time, "uuid", uuid.toString()));
    }

    static boolean hasNickRequest(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).isNickRequest();
    }

    static void updateNickName(Player player, UUID uuid) {
        PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);
        player.setDisplayName(playerProfile.getTempNick());
        EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CHANGED, player.getDisplayName());
        playerProfile.setPlayerDisplayName(playerProfile.getTempNick());
        saveToSQL(uuid);
    }

    static void removeNickRequest(UUID uuid) {
        PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);
        playerProfile.setTempNick(null);
        playerProfile.setNickRequest(false);
    }

    static void playerNick(final Player player, final String string) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (string.equals("clear")) {
            player.setDisplayName(playerName);
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CLEAR);
            return;
        }

        final PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);

        if (player.hasPermission("eternia.chat.color.nick")) {
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CHANGE_REQUEST, APIServer.getColor(string), String.valueOf(EterniaServer.configs.nickCost));
            playerProfile.setTempNick(string);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CHANGE_REQUEST, string, String.valueOf(EterniaServer.configs.nickCost));
            playerProfile.setTempNick(ChatColor.stripColor(string));
        }

        playerProfile.setTempNick(string);
        playerProfile.setNickRequest(true);
        EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_USE);
    }

    static void staffNick(final OnlinePlayer target, final Player player, final String string) {
        if (target != null) {
            changeNickName(target.getPlayer(), player, string);
            return;
        }

        if (string.equals("clear")) {
            final String playerName = player.getName();
            final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
            player.setDisplayName(playerName);
            PluginVars.playerProfile.get(uuid).setPlayerDisplayName(playerName);
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CLEAR);
            saveToSQL(uuid);
            return;
        }

        player.setDisplayName(APIServer.getColor(string));
    }

    private static void changeNickName(final Player target, final Player player, final String string) {
        final String targetName = target.getName();
        if (string.equals("clear")) {
            EterniaServer.msg.sendMessage(target, Messages.CHAT_NICK_CLEAR_BY, player.getName(), player.getDisplayName());
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CLEAR_FROM, targetName, target.getDisplayName());
            target.setDisplayName(targetName);
        } else {
            EterniaServer.msg.sendMessage(target, Messages.CHAT_NICK_CHANGED_BY, string, player.getName(), player.getDisplayName());
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CHANGED_FROM, string, player.getName(), player.getDisplayName());
            target.setDisplayName(string);
        }
    }

    private static void saveToSQL(UUID uuid) {
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "player_display", PluginVars.playerProfile.get(uuid).getPlayerDisplayName(), "uuid", uuid.toString()));
    }

}
