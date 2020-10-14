package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface APIChat {

    /**
     * Make a player send a message in staff chat
     * @param message the message
     * @param player the player object
     */
    static void sendStaff(String message, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
                String format = EterniaServer.chat.staffFormat;
                format = APIChat.setPlaceholders(player, format);
                format = APIServer.getColor(format.replace("%message%", message));
                p.sendMessage(format);
            }
        }
    }

    /**
     * Make a player send a message in local chat
     * @param message the message
     * @param player the player object
     * @param radius the radius range
     */
    static void sendLocal(String message, Player player, int radius) {
        int pes = 0;
        final String format = getLocalFormat(message, player);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (PluginVars.ignoredPlayer.get(player.getName()) != null && PluginVars.ignoredPlayer.get(player.getName()).contains(p)) return;
            final Boolean b = PluginVars.spy.get(p.getName());
            if ((player.getWorld() == p.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= Math.pow(radius, 2)) || radius <= 0) {
                pes += 1;
                p.sendMessage(format);
            } else if (p.hasPermission("eternia.spy") && Boolean.TRUE.equals(b)) {
                p.sendMessage(APIServer.getColor("&8[&7SPY&8-&eL&8] &8" + player.getDisplayName() + ": " + message));
            }
        }
        if (pes <= 1) {
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NO_ONE_NEAR);
        }
    }

    static void sendPrivate(final Player player, final Player target, final String s) {
        final String playerDisplay = player.getDisplayName();
        final String targetDisplay = target.getDisplayName();
        PluginVars.tell.put(target.getName(), player.getName());
        player.sendMessage(EterniaServer.msg.getMessage(Messages.CHAT_TELL_TO, false, s, player.getName(), playerDisplay, target.getName(), targetDisplay));
        target.sendMessage(EterniaServer.msg.getMessage(Messages.CHAT_TELL_FROM, false, s, target.getName(), targetDisplay, player.getName(), playerDisplay));
        for (String p : PluginVars.spy.keySet()) {
            final Boolean b = PluginVars.spy.getOrDefault(p, false);
            if (Boolean.TRUE.equals(b) && !p.equals(player.getName()) && !p.equals(target.getName())) {
                final Player spyPlayer = Bukkit.getPlayerExact(p);
                if (spyPlayer != null && spyPlayer.isOnline()) {
                    spyPlayer.sendMessage(APIServer.getColor("&8[&7SPY-&6P&8] &8" + playerDisplay + " -> " + targetDisplay + ": " + s));
                } else {
                    PluginVars.spy.remove(p);
                }
            }
        }
    }

    /**
     * Get the player's channel
     * @param uuid of player
     * @return the id of channel
     */
    static int getChannel(UUID uuid) {
        return PluginVars.playerProfile.get(uuid).getChatChannel();
    }

    /**
     * Set the channel of a player
     * @param uuid of player
     * @param channel id of channel
     */
    static void setChannel(UUID uuid, int channel) {
        PluginVars.playerProfile.get(uuid).setChatChannel(channel);
    }

    /**
     * Check if player received a tell
     * @param playerName of player
     * @return if received or not
     */
    static boolean receivedTell(String playerName) {
        return PluginVars.tell.containsKey(playerName);
    }

    /**
     * Get the name of the target that
     * is talking to the player
     * @param playerName of player
     * @return of target
     */
    static String getTellSender(String playerName) {
        return PluginVars.tell.get(playerName);
    }

    /**
     * Checks whether the player is
     * in a private conversation
     * @param playerName of player
     * @return if is or not
     */
    static boolean isTell(String playerName) {
        return PluginVars.chatLocked.containsKey(playerName);
    }

    /**
     * Sets a target for a player to
     * private conversation
     * @param playerName of player
     * @param targetName of target
     */
    static void setTelling(String playerName, String targetName) {
        PluginVars.chatLocked.put(playerName, targetName);
    }

    /**
     * Get the name of the target that
     * is locked to talking to the
     * player
     * @param playerName of player
     * @return of target
     */
    static String getTellingPlayerName(String playerName) {
        return PluginVars.chatLocked.get(playerName);
    }

    /**
     * Unlock the conversation of the
     * player
     * @param playerName of player
     */
    static void removeTelling(String playerName) {
        PluginVars.chatLocked.remove(playerName);
    }

    /**
     * Check if the player has someone
     * ignored
     * @param playerName of player
     * @return if has or not
     */
    static boolean hasIgnores(String playerName) {
        return PluginVars.ignoredPlayer.containsKey(playerName);
    }

    static List<Player> getIgnores(String playerName) {
        return PluginVars.ignoredPlayer.get(playerName);
    }

    static void putIgnored(String playerName, List<Player> list) {
        PluginVars.ignoredPlayer.put(playerName, list);
    }

    static boolean areIgnored(String playerName, Player target) {
        return PluginVars.ignoredPlayer.get(playerName).contains(target);
    }

    static String setPlaceholders(Player p, String s) {
        s = s.replace("%player_name%", p.getName());
        s = s.replace("%player_displayname%", p.getDisplayName());
        return PlaceholderAPI.setPlaceholders(p, s);
    }

    static void setChatMuted(boolean bool) {
        PluginVars.chatMuted = bool;
    }

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

    private static String getLocalFormat(String message, final Player player) {
        String format = APIChat.setPlaceholders(player, EterniaServer.chat.localFormat);
        if (player.hasPermission("eternia.chat.color")) {
            return APIServer.getColor(format.replace("%message%", message));
        } else {
            return(format.replace("%message%", message));
        }
    }

}