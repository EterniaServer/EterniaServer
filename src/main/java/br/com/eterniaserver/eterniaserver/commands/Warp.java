package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Delete;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.objects.LocationQuery;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp extends BaseCommand {

    private final EterniaServer plugin;

    public Warp(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("%spawn")
    @Syntax("%spawn_syntax")
    @Description("%spawn_description")
    @CommandPermission("%spawn_perm")
    @CommandCompletion("@players")
    public void onSpawn(Player player, @Optional OnlinePlayer targets) {
        User user = new User(player);

        if (plugin.locationManager().getTeleport(player.getUniqueId()) != null) {
            plugin.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            return;
        }

        Location location = plugin.getLocation("warp.spawn");

        if (targets == null) {
            if (spawnExists(location, player)) {
                plugin.locationManager().putTeleport(user.getUUID(), new PlayerTeleport(plugin.getInteger(Integers.COOLDOWN), location, plugin.getMessage(Messages.WARP_SPAWN_TELEPORTED, true)));
            }
            return;
        }

        User target = new User(targets.getPlayer());

        if (!user.hasPermission(plugin.getString(Strings.PERM_SPAWN_OTHER))) {
            plugin.sendMessage(player, Messages.SERVER_NO_PERM);
            return;
        }

        if (spawnExists(location, player)) {
            PaperLib.teleportAsync(target.getPlayer(), location);
            plugin.sendMessage(target.getPlayer(), Messages.WARP_SPAWN_TELEPORTED_BY, user.getName(), user.getDisplayName());
            plugin.sendMessage(player, Messages.WARP_SPAWN_TELEPORT_TARGET, target.getName(), target.getDisplayName());
        }
    }

    @CommandAlias("%spawnset")
    @Description("%spawnset_description")
    @CommandPermission("%spawnset_perm")
    public void onSetSpawn(Player player) {
        setWarp(player.getLocation(), "spawn");
        plugin.sendMessage(player, Messages.WARP_SPAWN_CREATED);
    }

    @CommandAlias("%warp")
    @Syntax("%warp_syntax")
    @Description("%warp_description")
    @CommandPermission("%warp_perm")
    public void onWarp(Player player, String nome) {
        User user = new User(player);

        if (plugin.locationManager().getTeleport(player.getUniqueId()) != null) {
            plugin.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            return;
        }

        if (!player.hasPermission(plugin.getString(Strings.PERM_WARP_PREFIX) + nome.toLowerCase())) {
            plugin.sendMessage(player, Messages.SERVER_NO_PERM);
            return;
        }

        Location location = plugin.getLocation("warp." + nome.toLowerCase());

        if (warpExists(location, player, nome)) {
            plugin.locationManager().putTeleport(user.getUUID(), new PlayerTeleport(plugin.getInteger(Integers.COOLDOWN), location, plugin.getMessage(Messages.WARP_TELEPORTED, true, nome)));
        }
    }

    @CommandAlias("%setwarp")
    @Syntax("%setwarp_syntax")
    @Description("%setwarp_description")
    @CommandPermission("%setwarp_perm")
    public void onSetWarp(Player player, String nome) {
        setWarp(player.getLocation(), nome.toLowerCase());
        plugin.sendMessage(player, Messages.WARP_CREATED, nome);
    }

    @CommandAlias("%delwarp")
    @Syntax("%delwarp_syntax")
    @Description("%delwarp_description")
    @CommandPermission("%delwarp_perm")
    public void onDelWarp(Player player, String nome) {
        if (plugin.getLocation("warp." + nome.toLowerCase()).equals(plugin.getError())) {
            plugin.sendMessage(player, Messages.WARP_NOT_FOUND, nome);
            return;
        }

        delWarp(nome.toLowerCase());
        plugin.sendMessage(player, Messages.WARP_DELETED, nome);
    }

    @CommandAlias("%listwarp")
    @Description("%listwarp_description")
    @CommandPermission("%listwarp_perm")
    public void onListWarp(Player player) {
        StringBuilder string = new StringBuilder();
        Object[] list = plugin.listWarp();
        int size = list.length;
        for (int i = 0; i < size; i++) {
            String line = list[i].toString();
            if (line.contains("warp.")) {
                String warp = line.replace("warp.", "");
                if (player.hasPermission(plugin.getString(Strings.PERM_WARP_PREFIX) + warp)) {
                    if (i + 1 != size) {
                        string.append(warp).append("§8").append(", ").append("§3");
                    } else {
                        string.append(warp).append("§3");
                    }
                }
            }
        }
        plugin.sendMessage(player, Messages.WARP_LIST, string.toString());
    }

    private boolean warpExists(final Location location, final Player player, final String nome) {
        if (location == plugin.getError()) {
            plugin.sendMessage(player, Messages.WARP_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    private boolean spawnExists(final Location location, final Player player) {
        if (location == plugin.getError()) {
            plugin.sendMessage(player, Messages.WARP_SPAWN_NOT_FOUND);
            return false;
        }
        return true;
    }

    public void setWarp(Location loc, String warp) {
        final String warpName = "warp." + warp;
        if (!plugin.getLocation(warpName).equals(plugin.getError())) {
            LocationQuery locationQuery = new LocationQuery(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            locationQuery.setLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            locationQuery.where.set("name", warpName);
            SQL.executeAsync(locationQuery);
        } else {
            Insert insert = new Insert(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            insert.columns.set("name", "world", "coord_x", "coord_y", "coord_z", "coord_yaw", "coord_pitch");
            insert.values.set(warpName, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            SQL.executeAsync(insert);
        }
        plugin.putLocation(warpName, loc);
    }

    public void delWarp(String warp) {
        final String warpName = "warp." + warp;
        plugin.removeLocation(warpName);

        Delete delete = new Delete(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
        delete.where.set("name", warpName);
        SQL.executeAsync(delete);
    }

}
