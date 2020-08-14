package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class WarpSystem extends BaseCommand {

    public WarpSystem() {

        HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.TABLE_SHOP), Constants.NAME_STR, Constants.LOCATION_STR);
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
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Shops").replace(Constants.AMOUNT, String.valueOf(temp.size())));

        temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.TABLE_WARP), Constants.NAME_STR, Constants.LOCATION_STR);
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
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Warps").replace(Constants.AMOUNT, String.valueOf(temp.size())));

    }

    @CommandAlias("spawn")
    @Syntax("<jogador>")
    @CommandPermission("eternia.spawn")
    public void onSpawn(Player player, @Optional OnlinePlayer target) {
        final Location location = getWarp("spawn");
        if (target == null) {
            if (Vars.teleports.containsKey(player)) {
                player.sendMessage(Strings.MSG_IN_TELEPORT);
            } else if (spawnExists(location, player)) {
                Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.MSG_WARP_DONE));
            }
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission("eternia.spawn.other") && spawnExists(location, player)) {
                Vars.teleports.put(targetP, new PlayerTeleport(target.getPlayer(), location, Strings.MSG_WARP_DONE));
                player.sendMessage(Strings.MSG_SPAWN_TELEPORT_TARGET.replace(Constants.TARGET, targetP.getDisplayName()));
            } else if (!player.hasPermission("eternia.spawn.other")) {
                player.sendMessage(Strings.MSG_NO_PERM);
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
                       Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.MSG_WARP_DONE));
                   } else if (Vars.teleports.containsKey(player)) {
                       player.sendMessage(Strings.MSG_IN_TELEPORT);
                   }
               } else {
                   player.sendMessage(Strings.MSG_NO_PERM);
               }
        } else {
            final Location location = getShop(target.toLowerCase());
            if (shopExists(location, player) && !Vars.teleports.containsKey(player)) {
                Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.MSG_SHOP_DONE));
            } else if (Vars.teleports.containsKey(player)) {
                player.sendMessage(Strings.MSG_IN_TELEPORT);
            }
        }
    }

    @CommandAlias("setspawn")
    @CommandPermission("eternia.setspawn")
    public void onSetSpawn(Player player) {
        setWarp(player.getLocation(), "spawn");
        player.sendMessage(Strings.MSG_SPAWN_CREATED);
    }

    @CommandAlias("setshop|setloja")
    @CommandPermission("eternia.setshop")
    public void onSetShop(Player player) {
        setShop(player.getLocation(), player.getName().toLowerCase());
        player.sendMessage(Strings.MSG_SHOP_CREATED);
    }

    @CommandAlias("setwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.setwarp")
    public void onSetWarp(Player player, String nome) {
        setWarp(player.getLocation(), nome.toLowerCase());
        player.sendMessage(Strings.MSG_WARP_CREATED.replace(Constants.WARP, nome));
    }

    @CommandAlias("delwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.delwarp")
    public void onDelWarp(Player player, String nome) {
        if (Vars.warps.containsKey(nome.toLowerCase())) {
            delWarp(nome.toLowerCase());
            player.sendMessage(Strings.MSG_WARP_DELETED);
        } else {
            player.sendMessage(Strings.MSG_WARP_NO_EXISTS.replace(Constants.WARP, nome.toLowerCase()));
        }
    }

    @CommandAlias("listwarp|warplist|warps")
    @CommandPermission("eternia.listwarp")
    public void onListWarp(Player player) {
        StringBuilder string = new StringBuilder();
        Object[] list = Vars.warps.keySet().toArray();
        int size = list.length;
        for (int i = 0; i < size; i++) {
            String line = list[i].toString();
            if (player.hasPermission("eternia.warp." + line)) {
                if (i + 1 != size) {
                    string.append(line).append(EterniaServer.colors.get(8)).append(", ").append(EterniaServer.colors.get(3));
                } else {
                    string.append(line).append(EterniaServer.colors.get(3));
                }
            }
        }
        player.sendMessage(Strings.MSG_WARP_LIST.replace(Constants.VALUE, string.toString()));
    }

    @CommandAlias("warp")
    @Syntax("<warp>")
    @CommandPermission("eternia.warp")
    public void onWarp(Player player, String nome) {
        if (player.hasPermission("eternia.warp." + nome.toLowerCase())) {
            final Location location = getWarp(nome.toLowerCase());
            if (warpExists(location, player, nome) && !Vars.teleports.containsKey(player)) {
                Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.MSG_WARP_DONE));
            } else if (Vars.teleports.containsKey(player)) {
                player.sendMessage(Strings.MSG_IN_TELEPORT);
            }
        } else {
            player.sendMessage(Strings.MSG_NO_PERM);
        }
    }

    private boolean shopExists(final Location location, final Player player) {
        if (location == EterniaServer.error) {
            player.sendMessage(Strings.MSG_SHOP_NO_EXISTS);
            return false;
        }
        return true;
    }

    private boolean warpExists(final Location location, final Player player, final String nome) {
        if (location == EterniaServer.error) {
            player.sendMessage(Strings.MSG_WARP_NO_EXISTS.replace(Constants.WARP, nome));
            return false;
        }
        return true;
    }

    private boolean spawnExists(final Location location, final Player player) {
        if (location == EterniaServer.error) {
            player.sendMessage(Strings.MSG_SPAWN_NO_EXISTS);
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
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_SHOP, Constants.LOCATION_STR, saveloc, Constants.NAME_STR, shop));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_SHOP, Constants.NAME_STR, shop, Constants.LOCATION_STR, saveloc));
        }
        Vars.shops.put(shop, loc);
    }

    public Location getShop(String shop) {
        return Vars.shops.getOrDefault(shop, EterniaServer.error);
    }

    public void setWarp(Location loc, String warp) {
        final String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        if (Vars.warps.containsKey(warp)) {
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_WARP, Constants.LOCATION_STR, saveloc, Constants.NAME_STR, warp));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_WARP, Constants.NAME_STR, warp, Constants.LOCATION_STR, saveloc));
        }
        Vars.warps.put(warp, loc);
    }

    public void delWarp(String warp) {
        Vars.warps.remove(warp);
        EQueries.executeQuery(Constants.getQueryDelete(Configs.TABLE_WARP, Constants.NAME_STR, warp));
    }

    public Location getWarp(String warp) {
        return Vars.warps.getOrDefault(warp, EterniaServer.error);
    }

}
