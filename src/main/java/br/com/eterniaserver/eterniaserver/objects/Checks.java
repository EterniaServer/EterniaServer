package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.FormatInfo;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Checks {

    private final EterniaServer plugin;
    private final FileConfiguration serverConfig;

    public Checks(EterniaServer plugin) {
        this.plugin = plugin;
        this.serverConfig = plugin.getServerConfig();
    }

    public int getXPForLevel(int lvl) {
        if (lvl > 0 && lvl < 16) return (lvl * lvl) + 6 * lvl;
        else if (lvl > 15 && lvl < 31) return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
        else if (lvl >= 31) return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
        else return 0;
    }

    public long getCooldown(String name) {
        if (!EterniaServer.bed_cooldown.containsKey(name)) return 0;
        else return EterniaServer.bed_cooldown.get(name);
    }

    public void addUUIF(Player p) {
        for (String s : plugin.groupConfig.getKeys(false)) {
            if(s.equals("groups")) continue;
            int priority = plugin.groupConfig.getInt(s + ".priority");
            if(plugin.groupConfig.getString(s + ".perm").equals("") || p.hasPermission(plugin.groupConfig.getString(s + ".perm"))) {
                if(EterniaServer.uufi.containsKey(p.getName())) {
                    if(EterniaServer.uufi.get(p.getName()).getPriority() < priority) {
                        EterniaServer.uufi.put(p.getName(), new FormatInfo(priority, s));
                    }
                } else {
                    EterniaServer.uufi.put(p.getName(), new FormatInfo(priority, s));
                }
            }
        }
    }

    public ItemStack getSpawner(ItemMeta meta, ItemStack item, String mobFormatted) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + serverConfig.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
        List<String> newLore = new ArrayList<>();
        if (serverConfig.getBoolean("spawners.enable-lore")) {
            for (String line : serverConfig.getStringList("spawners.lore")) {
                newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
            }
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public void removeUUIF(Player p) {
        EterniaServer.uufi.remove(p.getName());
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