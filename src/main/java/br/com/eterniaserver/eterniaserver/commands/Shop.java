package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Delete;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.LocationQuery;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.eterniaserver.objects.User;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Shop extends BaseCommand {

    private final EterniaServer plugin;

    public Shop(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("%shop")
    @Syntax("%shop_syntax")
    @Description("%shop_description")
    @CommandCompletion("@list_of_shops")
    @CommandPermission("%shop_perm")
    public void onShop(Player player, @Optional String targets) {
        User user = new User(player);

        if (plugin.locationManager().getTeleport(user.getUUID()) != null) {
            plugin.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            return;
        }

        if (targets != null) {
            Location location = plugin.getLocation(targets.toLowerCase());
            if (shopExists(location, player, targets)) {
                plugin.locationManager().putTeleport(user.getUUID(), new PlayerTeleport(plugin.getInteger(Integers.COOLDOWN), location, plugin.getMessage(Messages.WARP_SHOP_PLAYER_TELEPORTED, true, targets)));
            }
            return;
        }

        Location location = plugin.getLocation("warp.shop");

        if (!player.hasPermission(plugin.getString(Strings.PERM_WARP_SHOP))) {
            plugin.sendMessage(player, Messages.SERVER_NO_PERM);
            return;
        }

        if (shopExists(location, player)) {
            plugin.locationManager().putTeleport(user.getUUID(), new PlayerTeleport(plugin.getInteger(Integers.COOLDOWN), location, plugin.getMessage(Messages.WARP_SHOP_TELEPORTED, true)));
        }
    }

    @CommandAlias("%setshop")
    @Description("%setshop_description")
    @CommandPermission("%setshop_perm")
    public void onSetShop(Player player) {
        setShop(player.getLocation(), player.getName().toLowerCase());
        plugin.sendMessage(player, Messages.WARP_SHOP_CREATED);
    }

    @CommandAlias("%delshop")
    @Description("%delshop_description")
    @CommandPermission("%delshop_perm")
    public void onDelShop(Player player) {
        delShop(player.getName().toLowerCase());
        plugin.sendMessage(player, Messages.WARP_SHOP_DELETED);
    }

    private boolean shopExists(final Location location, final Player player) {
        if (location == plugin.getError()) {
            plugin.sendMessage(player, Messages.WARP_SHOP_CENTRAL_NOT_FOUND);
            return false;
        }
        return true;
    }

    private boolean shopExists(final Location location, final Player player, final String nome) {
        if (location == plugin.getError()) {
            plugin.sendMessage(player, Messages.WARP_SHOP_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    public void setShop(Location loc, String shop) {
        if (!plugin.getLocation(shop).equals(plugin.getError())) {
            LocationQuery locationQuery = new LocationQuery(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            locationQuery.setLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            locationQuery.where.set("name", shop);
            SQL.executeAsync(locationQuery);
        } else {
            Insert insert = new Insert(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            insert.columns.set("name", "world", "coord_x", "coord_y", "coord_z", "coord_yaw", "coord_pitch");
            insert.values.set(shop, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            SQL.executeAsync(insert);
        }
        plugin.putLocation(shop, loc);
    }

    public void delShop(String shop) {
        plugin.removeLocation(shop);

        Delete delete = new Delete(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
        delete.where.set("name", shop);
        SQL.executeAsync(delete);
    }

}
