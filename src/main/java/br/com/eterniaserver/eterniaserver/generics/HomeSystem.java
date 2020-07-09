package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;

    public HomeSystem(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        String query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-homes") + ";";
        HashMap<String, String> temp = EQueries.getMapString(query, "name", "location");

        AtomicInteger x = new AtomicInteger(0);
        temp.forEach((k, v) -> {
            System.out.println(k + "=" + v);
            final String[] split = v.split(":");
            final Location loc = new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    (Double.parseDouble(split[2]) + 1),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
            Vars.homes.put(k, loc);
            x.getAndIncrement();
            messages.sendConsole("server.load-data",  "%module%", "Home", "%amount%", x.get());

        });

        query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-home") + ";";
        temp = EQueries.getMapString(query, "player_name", "homes");
        x.set(0);
        temp.forEach((k, v) -> {
            final String[] homess = v.split(":");
            Vars.home.put(k, homess);
            x.getAndIncrement();
            messages.sendConsole("server.load-data",  "%module%", "PlayerHomes", "%amount%", x.get());
        });

    }

    @CommandAlias("delhome|delhouse|delcasa")
    @Syntax("<home>")
    @CommandPermission("eternia.delhome")
    public void onDelHome(Player player, String nome) {
        final String playerName = player.getName();

        if (existHome(nome.toLowerCase(), playerName)) {
            delHome(nome.toLowerCase(), playerName);
            messages.sendMessage("home.deleted", player);
        } else {
            messages.sendMessage("home.no-exists", player);
        }
    }

    @CommandAlias("home|house|casa|h")
    @Syntax("<home> <jogador>")
    @CommandPermission("eternia.home")
    public void onHome(Player player, String nome, @Optional OnlinePlayer target) {
        if (target == null) {
            Location location = getHome(nome.toLowerCase(), player.getName());
            if (location != plugin.error) {
                if (Vars.teleports.containsKey(player)) {
                        messages.sendMessage("server.telep", player);
                } else {
                    Vars.teleports.put(player, new PlayerTeleport(player, location, "home.done", plugin));
                }
            } else {
                messages.sendMessage("home.no-exists", player);
            }
        } else {
            if (player.hasPermission("eternia.home.other")) {
                Location location = getHome(nome.toLowerCase(), target.getPlayer().getName());
                if (location != plugin.error) {
                    Vars.teleports.put(player, new PlayerTeleport(player, location, "home.done", plugin));
                } else {
                    messages.sendMessage("home.no-exists", player);
                }
            } else {
                messages.sendMessage("server.no-perm", player);
            }
        }
    }

    @CommandAlias("homes|houses|casas")
    @Syntax("<jogador>")
    @CommandPermission("eternia.homes")
    public void onHomes(Player player, @Optional OnlinePlayer target) {
        StringBuilder accounts = new StringBuilder();
        String[] values;
        if (target != null) {
            if (player.hasPermission("eternia.homes.other")) {
                values = getHomes(target.getPlayer().getName());
                for (String line : values) {
                    accounts.append(line).append("&8, &3");
                }
                messages.sendMessage("home.list", "%homes%", messages.getColor(accounts.toString()), player);
            } else {
                messages.sendMessage("server.no-perm", player);
            }
        } else {
            values = getHomes(player.getName());
            for (String line : values) {
                accounts.append(line).append("&8, &3");
            }
            messages.sendMessage("home.list", "%homes%", messages.getColor(accounts.toString()), player);
        }
    }

    @CommandAlias("sethome|sethouse|setcasa")
    @Syntax("<nome>")
    @CommandPermission("eternia.sethome")
    public void onSetHome(Player player, String nome) {
        int i = 4;
        for (int v = 5; v <= 30; v++) if (player.hasPermission("eternia.sethome." + v)) i = v;
        if (nome.length() <= 12) {
            if (canHome(player.getName()) < i) {
                setHome(player.getLocation(), nome.toLowerCase(), player.getName());
                messages.sendMessage("home.created", player);
            } else {
                if (existHome(nome.toLowerCase(), player.getName())) {
                    setHome(player.getLocation(), nome.toLowerCase(), player.getName());
                    messages.sendMessage("home.created", player);
                } else {
                    ItemStack item = new ItemStack(Material.COMPASS);
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        final Location loc = player.getLocation();
                        final String saveloc = loc.getWorld().getName() +
                                ":" + ((int) loc.getX()) +
                                ":" + ((int) loc.getY()) +
                                ":" + ((int) loc.getZ()) +
                                ":" + ((int) loc.getYaw()) +
                                ":" + ((int) loc.getPitch());
                        meta.setLore(Collections.singletonList(saveloc));
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&e" + nome.toLowerCase() + "&8]"));
                        item.setItemMeta(meta);
                    }
                    PlayerInventory inventory = player.getInventory();
                    inventory.addItem(item);
                    messages.sendMessage("home.limit", player);
                }
            }
        } else {
            messages.sendMessage("home.exceeded", player);
        }
    }

    public void setHome(Location loc, String home, String jogador) {
        Vars.homes.put(home + "." + jogador, loc);
        boolean t = false;
        StringBuilder result = new StringBuilder();
        String[] values = getHomes(jogador);
        for (String line : values) {
            if (line.equals(home)) {
                result.append(line).append(":");
                t = true;
            } else {
                result.append(line).append(":");
            }
        }
        if (!t) {
            result.append(home).append(":");
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes='" + result + "' WHERE player_name='" + jogador + "';";
            EQueries.executeQuery(querie);
            values = result.toString().split(":");
            Vars.home.put(jogador, values);
            String saveloc = loc.getWorld().getName() +
                    ":" + ((int) loc.getX()) +
                    ":" + ((int) loc.getY()) +
                    ":" + ((int) loc.getZ()) +
                    ":" + ((int) loc.getYaw()) +
                    ":" + ((int) loc.getPitch());
            final String querie2 = "INSERT INTO " + plugin.serverConfig.getString("sql.table-homes") + " (name, location) VALUES ('" + home + "." + jogador + "', '" + saveloc + "')";
            EQueries.executeQuery(querie2);
        } else {
            String saveloc = loc.getWorld().getName() +
                    ":" + ((int) loc.getX()) +
                    ":" + ((int) loc.getY()) +
                    ":" + ((int) loc.getZ()) +
                    ":" + ((int) loc.getYaw()) +
                    ":" + ((int) loc.getPitch());
            final String querie3 = "UPDATE " + plugin.serverConfig.getString("sql.table-homes") + " SET location='" + saveloc + "' WHERE name='" + home + "." + jogador + "';";
            EQueries.executeQuery(querie3);
        }
    }

    public void delHome(String home, String jogador) {
        Vars.homes.remove(home + "." + jogador);
        StringBuilder nova = new StringBuilder();
        String[] values = getHomes(jogador);
        boolean t = true;
        for (String line : values) {
            if (!line.equals(home)) {
                nova.append(line).append(":");
                t = false;
            }
        }
        values = nova.toString().split(":");
        Vars.home.put(jogador, values);
        String querie;
        if (t) {
            querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes=':' WHERE player_name='" + jogador + "';";
        } else {
            querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes='" + nova + "' WHERE player_name='" + jogador + "';";
        }
        EQueries.executeQuery(querie);
        querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
        EQueries.executeQuery(querie);
    }

    public Location getHome(String home, String jogador) {
        if (Vars.homes.containsKey(home + "." + jogador)) {
            return Vars.homes.get(home + "." + jogador);
        } else {
            return plugin.error;
        }
    }

    public boolean existHome(String home, String jogador) {
        String[] homes = getHomes(jogador);
        for (String line : homes) {
            if (line.equals(home)) {
                return true;
            }
        }
        return false;
    }

    public int canHome(String jogador) {
        return getHomes(jogador) != null ? getHomes(jogador).length : 0;
    }

    public String[] getHomes(String jogador) {
        return Vars.home.get(jogador) != null ? Vars.home.get(jogador) : "".split(":");
    }

}
