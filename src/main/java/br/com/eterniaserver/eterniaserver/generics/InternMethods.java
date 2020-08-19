package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.utils.FormatInfo;

import br.com.eterniaserver.eterniaserver.utils.SubPlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class InternMethods {

    private InternMethods() {
        throw new IllegalStateException("Utility class");
    }

    public static int getXPForLevel(int lvl) {
        if (lvl > 0 && lvl < 16) return (lvl * lvl) + 6 * lvl;
        else if (lvl > 15 && lvl < 31) return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
        else if (lvl >= 31) return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
        else return 0;
    }

    public static void setChatMuted(boolean bool) {
        Vars.chatMuted = bool;
    }

    public static long getCooldown(String name) {
        if (!Vars.bedCooldown.containsKey(name)) return 0;
        else return Vars.bedCooldown.get(name);
    }

    public static void addUUIF(Player p) {
        for (String s : EterniaServer.groupConfig.getKeys(false)) {
            if(s.equals("groups")) continue;
            int priority = EterniaServer.groupConfig.getInt(s + ".priority");
            if(EterniaServer.groupConfig.getString(s + ".perm").equals("") || p.hasPermission(EterniaServer.groupConfig.getString(s + ".perm"))) {
                if(Vars.uufi.containsKey(p.getName())) {
                    if(Vars.uufi.get(p.getName()).getPriority() < priority) {
                        Vars.uufi.put(p.getName(), new FormatInfo(priority, s));
                    }
                } else {
                    Vars.uufi.put(p.getName(), new FormatInfo(priority, s));
                }
            }
        }
    }

    public static void removeUUIF(Player p) {
        Vars.uufi.remove(p.getName());
    }

    public static String setPlaceholders(Player p, String s) {
        s = s.replace("%player_name%", p.getName());
        s = s.replace("%player_displayname%", p.getDisplayName());
        return PlaceholderAPI.setPlaceholders(p, s);
    }

    public static String putName(Player player, Player target, String string) {
        return putName(player, string).replace("%target_name%", target.getName()).replace("%target_displayname%", target.getDisplayName());
    }

    public static String putName(Player player, String string) {
        return string.replace("%player_name%", player.getName()).replace("%player_displayname%", player.getDisplayName());
    }

    public static SubPlaceholder getSubPlaceholder(final Player player, final CustomPlaceholder cp) {
        SubPlaceholder bestPlaceholder = null;
        for (SubPlaceholder subPlaceholder : cp.getPlaceholders()) {
            if (subPlaceholder.hasPerm(player)) {
                if (bestPlaceholder == null)
                    bestPlaceholder = subPlaceholder;
                else {
                    if (bestPlaceholder.getPriority() < subPlaceholder.getPriority())
                        bestPlaceholder = subPlaceholder;
                }
            }
        }
        return bestPlaceholder;
    }

    public static void sendPrivate(final Player player, final Player target, final String s) {
        final String playerDisplay = player.getDisplayName();
        final String targetDisplay = target.getDisplayName();
        Vars.tell.put(target.getName(), player.getName());
        player.sendMessage(Strings.M_CHAT_TO.
                replace(Constants.PLAYER, playerDisplay).
                replace(Constants.TARGET, targetDisplay).
                replace(Constants.MESSAGE, s));
        target.sendMessage(Strings.M_CHAT_FROM.
                replace(Constants.PLAYER, targetDisplay).
                replace(Constants.TARGET, playerDisplay).
                replace(Constants.MESSAGE, s));
        for (String p : Vars.spy.keySet()) {
            final Boolean b = Vars.spy.getOrDefault(p, false);
            if (Boolean.TRUE.equals(b) && !p.equals(player.getName()) && !p.equals(target.getName())) {
                final Player spyPlayer = Bukkit.getPlayerExact(p);
                if (spyPlayer != null && spyPlayer.isOnline()) {
                    spyPlayer.sendMessage(Strings.getColor("&8[&7SPY-&6P&8] &8" + playerDisplay + " -> " + targetDisplay + ": " + s));
                } else {
                    Vars.spy.remove(p);
                }
            }
        }
    }

    public static String getTimeLeft(long cooldown) {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown - System.currentTimeMillis()));
    }

    public static String getTimeLeft(long cooldown, long cd) {
        return String.valueOf(cooldown - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cd));
    }

    public static boolean hasCooldown(long cooldown, int timeNeeded) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cooldown) >= timeNeeded;
    }

    public  static boolean stayMuted(long cooldown) {
        return cooldown - System.currentTimeMillis() > 0;
    }

}