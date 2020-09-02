package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.FormatInfo;

import br.com.eterniaserver.eterniaserver.objects.SubPlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class UtilInternMethods {

    private UtilInternMethods() {
        throw new IllegalStateException("Utility class");
    }

    public static int getXPForLevel(int lvl) {
        if (lvl > 0 && lvl < 16) return (lvl * lvl) + 6 * lvl;
        else if (lvl > 15 && lvl < 31) return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
        else if (lvl >= 31) return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
        else return 0;
    }

    public static void setChatMuted(boolean bool) {
        PluginVars.chatMuted = bool;
    }

    public static long getCooldown(String name) {
        if (!PluginVars.bedCooldown.containsKey(name)) return 0;
        else return PluginVars.bedCooldown.get(name);
    }

    public static void addUUIF(Player p) {
        for (String s : EterniaServer.groupConfig.getKeys(false)) {
            if(s.equals("groups")) continue;
            int priority = EterniaServer.groupConfig.getInt(s + ".priority");
            if(EterniaServer.groupConfig.getString(s + ".perm").equals("") || p.hasPermission(EterniaServer.groupConfig.getString(s + ".perm"))) {
                if(PluginVars.uufi.containsKey(p.getName())) {
                    if(PluginVars.uufi.get(p.getName()).getPriority() < priority) {
                        PluginVars.uufi.put(p.getName(), new FormatInfo(priority, s));
                    }
                } else {
                    PluginVars.uufi.put(p.getName(), new FormatInfo(priority, s));
                }
            }
        }
    }

    public static void sendStaff(String message, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
                String format = EterniaServer.chatConfig.getString("staff.format");
                format = UtilInternMethods.setPlaceholders(player, format);
                format = PluginMSGs.getColor(format.replace(PluginConstants.MESSAGE, message));
                p.sendMessage(format);
            }
        }
    }

    public static void sendLocal(String message, Player player, int radius) {
        int pes = 0;
        final String format = getFormat(message, player);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (PluginVars.ignoredPlayer.get(player.getName()) != null && PluginVars.ignoredPlayer.get(player.getName()).contains(p)) return;
            final Boolean b = PluginVars.spy.get(p.getName());
            if ((player.getWorld() == p.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= Math.pow(radius, 2)) || radius <= 0) {
                pes += 1;
                p.sendMessage(format);
            } else if (p.hasPermission("eternia.spy") && Boolean.TRUE.equals(b)) {
                p.sendMessage(PluginMSGs.getColor("&8[&7SPY&8-&eL&8] &8" + player.getDisplayName() + ": " + message));
            }
        }
        if (pes <= 1) {
            player.sendMessage(PluginMSGs.M_CHAT_NOONE);
        }
    }

    private static String getFormat(String message, final Player player) {
        String format = UtilInternMethods.setPlaceholders(player, EterniaServer.chatConfig.getString("local.format"));
        if (player.hasPermission("eternia.chat.color")) {
            return PluginMSGs.getColor(format.replace(PluginConstants.MESSAGE, message));
        } else {
            return(format.replace(PluginConstants.MESSAGE, message));
        }
    }

    public static void removeUUIF(Player p) {
        PluginVars.uufi.remove(p.getName());
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

    public static String putName(CommandSender player, String string) {
        return string.replace("%player_name%", player.getName()).replace("%player_displayname%", player.getName());
    }

    public static SubPlaceholder getSubPlaceholder(final Player player, final UtilCustomPlaceholder cp) {
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
        PluginVars.tell.put(target.getName(), player.getName());
        player.sendMessage(PluginMSGs.M_CHAT_TO.
                replace(PluginConstants.PLAYER, playerDisplay).
                replace(PluginConstants.TARGET, targetDisplay).
                replace(PluginConstants.MESSAGE, s));
        target.sendMessage(PluginMSGs.M_CHAT_FROM.
                replace(PluginConstants.PLAYER, targetDisplay).
                replace(PluginConstants.TARGET, playerDisplay).
                replace(PluginConstants.MESSAGE, s));
        for (String p : PluginVars.spy.keySet()) {
            final Boolean b = PluginVars.spy.getOrDefault(p, false);
            if (Boolean.TRUE.equals(b) && !p.equals(player.getName()) && !p.equals(target.getName())) {
                final Player spyPlayer = Bukkit.getPlayerExact(p);
                if (spyPlayer != null && spyPlayer.isOnline()) {
                    spyPlayer.sendMessage(PluginMSGs.getColor("&8[&7SPY-&6P&8] &8" + playerDisplay + " -> " + targetDisplay + ": " + s));
                } else {
                    PluginVars.spy.remove(p);
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

    public static boolean stayMuted(long cooldown) {
        return cooldown - System.currentTimeMillis() > 0;
    }

}