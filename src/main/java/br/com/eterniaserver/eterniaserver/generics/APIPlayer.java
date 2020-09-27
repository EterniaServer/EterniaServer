package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface APIPlayer {

    static String getFirstLogin(UUID uuid) {
        return PluginVars.playerProfile.containsKey(uuid) ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(PluginVars.playerProfile.get(uuid).firstLogin)) : "Sem registro";
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
        return PluginVars.playerProfile.get(uuid).muted;
    }

    static void putMutedTime(UUID uuid, long time) {
        PluginVars.playerProfile.get(uuid).muted = time;
    }

    static String getDisplayName(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getPlayerDisplayName();
    }

    static String getName(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getPlayerName();
    }

    static void updateHome(UUID uuid, List<String> values) {
        PluginVars.playerProfile.get(uuid).homes = values;
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
        return Objects.requireNonNullElseGet(PluginVars.playerProfile.get(uuid).homes, ArrayList::new);
    }

    static int getChannel(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).chatChannel;
    }

    static void setChannel(UUID uuid, int channel) {
        PluginVars.playerProfile.get(uuid).chatChannel = channel;
    }

    static boolean receivedTell(String playerName) {
        return PluginVars.tell.containsKey(playerName);
    }

    static String getTellSender(String playerName) {
        return PluginVars.tell.get(playerName);
    }

    static boolean isTell(String playerName) {
        return PluginVars.chatLocked.containsKey(playerName);
    }

    static void setTelling(String playerName, String targetName) {
        PluginVars.chatLocked.put(playerName, targetName);
    }

    static String getTellingPlayerName(String playerName) {
        return PluginVars.chatLocked.get(playerName);
    }

    static void removeTelling(String playerName) {
        PluginVars.chatLocked.remove(playerName);
    }

    static boolean hasIgnoreds(String playerName) {
        return PluginVars.ignoredPlayer.containsKey(playerName);
    }

    static List<Player> getIgnoreds(String playerName) {
        return PluginVars.ignoredPlayer.get(playerName);
    }

    static void putIgnored(String playerName, List<Player> list) {
        PluginVars.ignoredPlayer.put(playerName, list);
    }

    static boolean areIgnored(String playerName, Player target) {
        return PluginVars.ignoredPlayer.get(playerName).contains(target);
    }

    static boolean isTeleporting(Player player) {
        return PluginVars.teleports.containsKey(player);
    }

    static long getAndUpdateTimePlayed(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).updateTimePlayed();
    }

    static long getLastLogin(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).lastLogin;
    }

    static long getFirstLoginLong(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).firstLogin;
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
        if (playerProfile.playerName == null) {
            final PlayerProfile newPlayerProfile = new PlayerProfile(playerName, time, time, 0);
            newPlayerProfile.cash = playerProfile.cash;
            newPlayerProfile.balance = playerProfile.balance;
            newPlayerProfile.xp = playerProfile.xp;
            newPlayerProfile.muted = time;
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
        playerProfile.lastLogin = time;
        if (!playerProfile.getPlayerName().equals(playerName)) {
            playerProfile.setPlayerName(playerName);
            PluginVars.playerProfile.put(uuid, playerProfile);
            EQueries.executeQuery(PluginConstants.getQueryUpdate(EterniaServer.configs.tablePlayer, PluginConstants.PLAYER_NAME_STR, playerName, PluginConstants.UUID_STR, uuid.toString()));
        }
        EQueries.executeQuery(PluginConstants.getQueryUpdate(EterniaServer.configs.tablePlayer, PluginConstants.LAST_STR, time, PluginConstants.UUID_STR, uuid.toString()));
    }

    static boolean hasNickRequest(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).nickRequest;
    }

    static void updateNickName(Player player, UUID uuid) {
        PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);
        player.setDisplayName(playerProfile.tempNick);
        player.sendMessage(UtilInternMethods.putName(player, PluginMSGs.M_CHAT_NEWNICK));
        playerProfile.playerDisplayName = playerProfile.tempNick;
        saveToSQL(uuid);
    }

    static void removeNickRequest(UUID uuid) {
        PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);
        playerProfile.tempNick = null;
        playerProfile.nickRequest = false;
    }

    static void playerNick(final Player player, final String string) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (string.equals(PluginConstants.CLEAR_STR)) {
            player.setDisplayName(playerName);
            player.sendMessage(PluginMSGs.M_CHAT_REMOVE_NICK);
            return;
        }

        if (player.hasPermission("eternia.chat.color.nick")) {
            player.sendMessage(PluginMSGs.M_CHAT_NICK_MONEY.replace(PluginConstants.NEW_NAME, PluginMSGs.getColor(string)).replace(PluginConstants.AMOUNT, String.valueOf(EterniaServer.configs.nickCost)));
        } else {
            player.sendMessage(PluginMSGs.M_CHAT_NICK_MONEY.replace(PluginConstants.NEW_NAME, string).replace(PluginConstants.AMOUNT, String.valueOf(EterniaServer.configs.nickCost)));
        }

        final PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);

        playerProfile.tempNick = string;
        playerProfile.nickRequest = true;
        player.sendMessage(PluginMSGs.M_CHAT_NICK_MONEY_2);
    }

    static void staffNick(final OnlinePlayer target, final Player player, final String string) {
        if (target != null) {
            changeNickName(target.getPlayer(), player, string);
            return;
        }

        if (string.equals(PluginConstants.CLEAR_STR)) {
            final String playerName = player.getName();
            final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
            player.setDisplayName(playerName);
            PluginVars.playerProfile.get(uuid).playerDisplayName = playerName;
            player.sendMessage(PluginMSGs.M_CHAT_REMOVE_NICK);
            saveToSQL(uuid);
            return;
        }

        player.setDisplayName(PluginMSGs.getColor(string));
    }

    private static void changeNickName(final Player target, final Player player, final String string) {
        final String targetName = target.getName();
        if (string.equals(PluginConstants.CLEAR_STR)) {
            target.setDisplayName(targetName);
            target.sendMessage(PluginMSGs.M_CHAT_REMOVE_NICK);
            player.sendMessage(PluginMSGs.M_CHAT_REMOVE_NICK);
        } else {
            target.setDisplayName(string);
            player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.M_CHAT_NEWNICK));
            target.sendMessage(UtilInternMethods.putName(target, PluginMSGs.M_CHAT_NEWNICK));
        }
    }

    private static void saveToSQL(UUID uuid) {
        EQueries.executeQuery(PluginConstants.getQueryUpdate(EterniaServer.configs.tablePlayer, PluginConstants.PLAYER_DISPLAY_STR, PluginVars.playerProfile.get(uuid).getPlayerDisplayName(), PluginConstants.UUID_STR, uuid.toString()));
    }

}
