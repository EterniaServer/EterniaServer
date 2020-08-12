package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.utils.FormatInfo;

import br.com.eterniaserver.eterniaserver.utils.SubPlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;

public class InternMethods {

    public static int getXPForLevel(int lvl) {
        if (lvl > 0 && lvl < 16) return (lvl * lvl) + 6 * lvl;
        else if (lvl > 15 && lvl < 31) return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
        else if (lvl >= 31) return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
        else return 0;
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

}