package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.utils.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class WarpSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;

    public WarpSystem(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_SHOP), Strings.NAME, Strings.LOC);
        temp.forEach((k, v) -> {
            final String[] split = v.split(":");
            final Location loc = new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    (Double.parseDouble(split[2]) + 1),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
            Vars.shops.put(k, loc);
        });
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Shops", Constants.AMOUNT, temp.size());

        temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_WARP), Strings.NAME, Strings.LOC);
        temp.forEach((k, v) -> {
            final String[] split = v.split(":");
            final Location loc = new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    (Double.parseDouble(split[2]) + 1),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
            Vars.warps.put(k, loc);
        });
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Warps", Constants.AMOUNT, temp.size());

    }

    @CommandAlias("spawn")
    @Syntax("<jogador>")
    @CommandPermission("eternia.spawn")
    public void onSpawn(Player player, @Optional OnlinePlayer target) {
        final Location location = getWarp("spawn");
        if (target == null) {
            if (Vars.teleports.containsKey(player)) {
                messages.sendMessage(Strings.M_TELEP, player);
            } else if (spawnExists(location, player)) {
                Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.M_WARP_DONE));
            }
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission("eternia.spawn.other") && spawnExists(location, player)) {
                Vars.teleports.put(targetP, new PlayerTeleport(target.getPlayer(), location, Strings.M_WARP_DONE));
                messages.sendMessage(Strings.M_SPAWN_TARGET, Constants.TARGET, targetP.getDisplayName(), player);
            } else if (!player.hasPermission("eternia.spawn.other")) {
                messages.sendMessage(Strings.M_NO_PERM, player);
            }
        }
    }

    @CommandAlias("shop|loja")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.shop.player")
    public void onShop(Player player, @Optional String target) {
        if (target == null) {
               final Location location = getWarp("shop");
               if (player.hasPermission("eternia.warp.shop")) {
                   if (shopExists(location, player) && !Vars.teleports.containsKey(player)) {
                       Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.M_WARP_DONE));
                   } else if (Vars.teleports.containsKey(player)) {
                       messages.sendMessage(Strings.M_TELEP, player);
                   }
               } else {
                   messages.sendMessage(Strings.M_NO_PERM, player);
               }
        } else {
            final Location location = getShop(target.toLowerCase());
            if (shopExists(location, player) && !Vars.teleports.containsKey(player)) {
                Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.M_SHOP_DONE));
            } else if (Vars.teleports.containsKey(player)) {
                messages.sendMessage(Strings.M_TELEP, player);
            }
        }
    }

    @CommandAlias("setspawn")
    @CommandPermission("eternia.setspawn")
    public void onSetSpawn(Player player) {
        setWarp(player.getLocation(), "spawn");
        messages.sendMessage(Strings.M_SPAWN_CREATE, player);
    }

    @CommandAlias("setshop|setloja")
    @CommandPermission("eternia.setshop")
    public void onSetShop(Player player) {
        setShop(player.getLocation(), player.getName().toLowerCase());
        messages.sendMessage(Strings.M_SHOP_CREATE, player);
    }

    @CommandAlias("setwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.setwarp")
    public void onSetWarp(Player player, String nome) {
        setWarp(player.getLocation(), nome.toLowerCase());
        messages.sendMessage(Strings.M_WARP_CREATE, Constants.WARP, nome, player);
    }

    @CommandAlias("delwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.delwarp")
    public void onDelWarp(Player player, String nome) {
        if (Vars.warps.containsKey(nome.toLowerCase())) {
            delWarp(nome.toLowerCase());
            messages.sendMessage(Strings.M_WARP_DELETE, player);
        } else {
            messages.sendMessage(Strings.M_WARP_NO, Constants.WARP, nome.toLowerCase(), player);
        }
    }

    @CommandAlias("listwarp|warplist|warps")
    @CommandPermission("eternia.listwarp")
    public void onListWarp(Player player) {
        StringBuilder string = new StringBuilder();
        for (String name : Vars.warps.keySet()) string.append(name).append(plugin.colors.get(8)).append(", ").append(plugin.colors.get(3));
        messages.sendMessage(Strings.M_WARP_LIST, Constants.VALUE, string.substring(0, string.length() - 1), player);
    }

    @CommandAlias("warp")
    @Syntax("<warp>")
    @CommandPermission("eternia.warp")
    public void onWarp(Player player, String nome) {
        if (player.hasPermission("eternia.warp." + nome.toLowerCase())) {
            final Location location = getWarp(nome.toLowerCase());
            if (warpExists(location, player, nome) && !Vars.teleports.containsKey(player)) {
                Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.M_WARP_DONE));
            } else if (Vars.teleports.containsKey(player)) {
                messages.sendMessage(Strings.M_TELEP, player);
            }
        } else {
            messages.sendMessage(Strings.M_NO_PERM, player);
        }
    }

    private boolean shopExists(final Location location, final Player player) {
        if (location == plugin.error) {
            messages.sendMessage(Strings.M_SHOP_NO, player);
            return false;
        }
        return true;
    }

    private boolean warpExists(final Location location, final Player player, final String nome) {
        if (location == plugin.error) {
            messages.sendMessage(Strings.M_WARP_NO, Constants.WARP, nome, player);
            return false;
        }
        return true;
    }

    private boolean spawnExists(final Location location, final Player player) {
        if (location == plugin.error) {
            messages.sendMessage(Strings.M_SPAWN_NO, player);
            return false;
        }
        return true;
    }

    public void setShop(Location loc, String shop) {
        final String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        if (Vars.shops.containsKey(shop)) {
            EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_SHOP, Strings.LOC, saveloc, Strings.NAME, shop));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_SHOP, Strings.NAME, shop, Strings.LOC, saveloc));
        }
        Vars.shops.put(shop, loc);
    }

    public Location getShop(String shop) {
        return Vars.shops.containsKey(shop) ? Vars.shops.get(shop) : plugin.error;
    }

    public void setWarp(Location loc, String warp) {
        final String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        if (Vars.warps.containsKey(warp)) {
            EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_WARP, Strings.LOC, saveloc, Strings.NAME, warp));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_WARP, Strings.NAME, warp, Strings.LOC, saveloc));
        }
        Vars.warps.put(warp, loc);
    }

    public void delWarp(String warp) {
        Vars.warps.remove(warp);
        EQueries.executeQuery(Constants.getQueryDelete(Constants.TABLE_WARP, Strings.NAME, warp));
    }

    public Location getWarp(String warp) {
        return Vars.warps.containsKey(warp) ? Vars.warps.get(warp) : plugin.error;
    }

}
