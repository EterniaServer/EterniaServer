package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.paperlib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BaseCmdWarp extends BaseCommand {

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
                PaperLib.teleportAsync(targetP, location);
                targetP.sendMessage(InternMethods.putName(player, Strings.SPAWN_BY));
                player.sendMessage(InternMethods.putName(targetP, Strings.MSG_SPAWN_TELEPORT_TARGET));
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
        if (Vars.locations.containsKey("warp." + nome.toLowerCase())) {
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
        Object[] list = Vars.locations.keySet().toArray();
        int size = list.length;
        for (int i = 0; i < size; i++) {
            String line = list[i].toString();
            if (line.contains("warp.")) {
                String warp = line.replace("warp.", "");
                if (player.hasPermission("eternia.warp." + warp)) {
                    if (i + 1 != size) {
                        string.append(warp).append(Vars.colors.get(8)).append(", ").append(Vars.colors.get(3));
                    } else {
                        string.append(warp).append(Vars.colors.get(3));
                    }
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
        if (location == Vars.error) {
            player.sendMessage(Strings.MSG_SHOP_NO_EXISTS);
            return false;
        }
        return true;
    }

    private boolean warpExists(final Location location, final Player player, final String nome) {
        if (location == Vars.error) {
            player.sendMessage(Strings.MSG_WARP_NO_EXISTS.replace(Constants.WARP, nome));
            return false;
        }
        return true;
    }

    private boolean spawnExists(final Location location, final Player player) {
        if (location == Vars.error) {
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
        if (Vars.locations.containsKey(shop)) {
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_LOCATIONS, Constants.LOCATION_STR, saveloc, Constants.NAME_STR, shop));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_LOCATIONS, Constants.NAME_STR, shop, Constants.LOCATION_STR, saveloc));
        }
        Vars.locations.put(shop, loc);
    }

    public void setWarp(Location loc, String warp) {
        final String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        final String warpName = "warp." + warp;
        if (Vars.locations.containsKey(warpName)) {
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_LOCATIONS, Constants.LOCATION_STR, saveloc, Constants.NAME_STR, warpName));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_LOCATIONS, Constants.NAME_STR, warpName, Constants.LOCATION_STR, saveloc));
        }
        Vars.locations.put(warpName, loc);
    }

    public void delWarp(String warp) {
        final String warpName = "warp." + warp;
        Vars.locations.remove(warpName);
        EQueries.executeQuery(Constants.getQueryDelete(Configs.TABLE_LOCATIONS, Constants.NAME_STR, warpName));
    }

    public Location getShop(String shop) {
        return Vars.locations.getOrDefault(shop, Vars.error);
    }

    public Location getWarp(String warp) {
        return Vars.locations.getOrDefault("warp." + warp, Vars.error);
    }

}
