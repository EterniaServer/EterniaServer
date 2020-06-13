package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.FormatInfo;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class Checks {

    private final EterniaServer plugin;
    private final Vars vars;

    public Checks(EterniaServer plugin) {
        this.plugin = plugin;
        this.vars = plugin.getVars();
    }

    public int getXPForLevel(int lvl) {
        if (lvl > 0 && lvl < 16) return (lvl * lvl) + 6 * lvl;
        else if (lvl > 15 && lvl < 31) return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
        else if (lvl >= 31) return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
        else return 0;
    }

    public long getCooldown(String name) {
        if (!vars.bed_cooldown.containsKey(name)) return 0;
        else return vars.bed_cooldown.get(name);
    }

    public void addUUIF(Player p) {
        for (String s : plugin.groupConfig.getKeys(false)) {
            if(s.equals("groups")) continue;
            int priority = plugin.groupConfig.getInt(s + ".priority");
            if(plugin.groupConfig.getString(s + ".perm").equals("") || p.hasPermission(plugin.groupConfig.getString(s + ".perm"))) {
                if(vars.uufi.containsKey(p.getName())) {
                    if(vars.uufi.get(p.getName()).getPriority() < priority) {
                        vars.uufi.put(p.getName(), new FormatInfo(priority, s));
                    }
                } else {
                    vars.uufi.put(p.getName(), new FormatInfo(priority, s));
                }
            }
        }
    }

    public void removeUUIF(Player p) {
        vars.uufi.remove(p.getName());
    }

    public String setPlaceholders(Player p, String s) {
        s = s.contains("%player_name%") ? s.replace("%player_name%", p.getName()) : s;
        s = s.contains("%display_name%") ? s.replace("%display_name%", p.getDisplayName()) : s;
        return PlaceholderAPI.setPlaceholders(p, s);
    }

    public String setRelationalPlaceholders(Player p, Player p2, String s) {
        return PlaceholderAPI.setRelationalPlaceholders(p, p2, s);
    }

    public String setBothPlaceholders(Player p, Player to, String cc) {
        return setRelationalPlaceholders(p, to, setPlaceholders(p, cc));
    }

}