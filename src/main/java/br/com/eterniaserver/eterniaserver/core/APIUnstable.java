package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.FormatInfo;
import br.com.eterniaserver.eterniaserver.objects.SubPlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public interface APIUnstable {

    static void addUUIF(Player p) {
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

    static void sendStaff(String message, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
                String format = EterniaServer.chatConfig.getString("staff.format");
                format = APIUnstable.setPlaceholders(player, format);
                format = APIServer.getColor(format.replace(PluginConstants.MESSAGE, message));
                p.sendMessage(format);
            }
        }
    }

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
            EterniaServer.configs.sendMessage(player, Messages.CHAT_NO_ONE_NEAR);
        }
    }

    private static String getLocalFormat(String message, final Player player) {
        String format = APIUnstable.setPlaceholders(player, EterniaServer.chatConfig.getString("local.format"));
        if (player.hasPermission("eternia.chat.color")) {
            return APIServer.getColor(format.replace(PluginConstants.MESSAGE, message));
        } else {
            return(format.replace(PluginConstants.MESSAGE, message));
        }
    }

    static void removeUUFI(Player p) {
        PluginVars.uufi.remove(p.getName());
    }

    static String setPlaceholders(Player p, String s) {
        s = s.replace("%player_name%", p.getName());
        s = s.replace("%player_displayname%", p.getDisplayName());
        return PlaceholderAPI.setPlaceholders(p, s);
    }

    static String putName(Player player, Player target, String string) {
        return putName(player, string).replace("%target_name%", target.getName()).replace("%target_displayname%", target.getDisplayName());
    }

    static String putName(Player player, String string) {
        return string.replace("%player_name%", player.getName()).replace("%player_displayname%", player.getDisplayName());
    }

    static String putName(CommandSender player, String string) {
        return string.replace("%player_name%", player.getName()).replace("%player_displayname%", player.getName());
    }

    static SubPlaceholder getSubPlaceholder(final Player player, final UtilCustomPlaceholder cp) {
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

    static void sendPrivate(final Player player, final Player target, final String s) {
        final String playerDisplay = player.getDisplayName();
        final String targetDisplay = target.getDisplayName();
        PluginVars.tell.put(target.getName(), player.getName());
        player.sendMessage(EterniaServer.configs.getMessage(Messages.CHAT_TELL_TO, false, player.getName(), playerDisplay, target.getName(), targetDisplay));
        target.sendMessage(EterniaServer.configs.getMessage(Messages.CHAT_TELL_FROM, false, player.getName(), playerDisplay, target.getName(), targetDisplay));
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

    static String getTimeLeft(long cooldown) {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown - System.currentTimeMillis()));
    }

    static String getTimeLeft(long cooldown, long cd) {
        return String.valueOf(cooldown - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cd));
    }

    static boolean hasCooldown(long cooldown, int timeNeeded) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - cooldown) >= timeNeeded;
    }

    static boolean stayMuted(long cooldown) {
        return cooldown - System.currentTimeMillis() > 0;
    }

}