package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.generics.APIPlayer;
import br.com.eterniaserver.eterniaserver.generics.APIServer;
import br.com.eterniaserver.eterniaserver.generics.PluginConfigs;
import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;
import br.com.eterniaserver.eterniaserver.generics.PluginVars;
import br.com.eterniaserver.eterniaserver.generics.UtilInternMethods;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.paperlib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp extends BaseCommand {

    @CommandAlias("spawn")
    @Syntax("<jogador>")
    @CommandPermission("eternia.spawn")
    public void onSpawn(Player player, @Optional OnlinePlayer target) {
        final Location location = APIServer.getWarp("warp.spawn");
        if (target == null) {
            if (APIPlayer.isTeleporting(player)) {
                player.sendMessage(PluginMSGs.MSG_IN_TELEPORT);
            } else if (spawnExists(location, player)) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, location, PluginMSGs.MSG_WARP_DONE));
            }
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission("eternia.spawn.other") && spawnExists(location, player)) {
                PaperLib.teleportAsync(targetP, location);
                targetP.sendMessage(UtilInternMethods.putName(player, PluginMSGs.SPAWN_BY));
                player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.MSG_SPAWN_TELEPORT_TARGET));
            } else if (!player.hasPermission("eternia.spawn.other")) {
                player.sendMessage(PluginMSGs.MSG_NO_PERM);
            }
        }
    }

    @CommandAlias("shop|loja")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.shop.player")
    public void onShop(Player player, @Optional String target) {
        if (target == null) {
               final Location location = APIServer.getWarp("warp.shop");
               if (player.hasPermission("eternia.warp.shop")) {
                   if (shopExists(location, player) && !APIPlayer.isTeleporting(player)) {
                       APIServer.putInTeleport(player, new PlayerTeleport(player, location, PluginMSGs.MSG_WARP_DONE));
                   } else if (APIPlayer.isTeleporting(player)) {
                       player.sendMessage(PluginMSGs.MSG_IN_TELEPORT);
                   }
               } else {
                   player.sendMessage(PluginMSGs.MSG_NO_PERM);
               }
        } else {
            final Location location = APIServer.getWarp(target.toLowerCase());
            if (shopExists(location, player) && !APIPlayer.isTeleporting(player)) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, location, PluginMSGs.MSG_SHOP_DONE));
            } else if (APIPlayer.isTeleporting(player)) {
                player.sendMessage(PluginMSGs.MSG_IN_TELEPORT);
            }
        }
    }

    @CommandAlias("setspawn")
    @CommandPermission("eternia.setspawn")
    public void onSetSpawn(Player player) {
        setWarp(player.getLocation(), "spawn");
        player.sendMessage(PluginMSGs.MSG_SPAWN_CREATED);
    }

    @CommandAlias("setshop|setloja")
    @CommandPermission("eternia.setshop")
    public void onSetShop(Player player) {
        setShop(player.getLocation(), player.getName().toLowerCase());
        player.sendMessage(PluginMSGs.MSG_SHOP_CREATED);
    }

    @CommandAlias("setwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.setwarp")
    public void onSetWarp(Player player, String nome) {
        setWarp(player.getLocation(), nome.toLowerCase());
        player.sendMessage(PluginMSGs.MSG_WARP_CREATED.replace(PluginConstants.WARP, nome));
    }

    @CommandAlias("delwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.delwarp")
    public void onDelWarp(Player player, String nome) {
        if (!APIServer.getWarp("warp." + nome.toLowerCase()).equals(PluginVars.error)) {
            delWarp(nome.toLowerCase());
            player.sendMessage(PluginMSGs.MSG_WARP_DELETED);
        } else {
            player.sendMessage(PluginMSGs.MSG_WARP_NO_EXISTS.replace(PluginConstants.WARP, nome.toLowerCase()));
        }
    }

    @CommandAlias("listwarp|warplist|warps")
    @CommandPermission("eternia.listwarp")
    public void onListWarp(Player player) {
        StringBuilder string = new StringBuilder();
        Object[] list = APIServer.listWarp();
        int size = list.length;
        for (int i = 0; i < size; i++) {
            String line = list[i].toString();
            if (line.contains("warp.")) {
                String warp = line.replace("warp.", "");
                if (player.hasPermission("eternia.warp." + warp)) {
                    if (i + 1 != size) {
                        string.append(warp).append(PluginVars.colors.get(8)).append(", ").append(PluginVars.colors.get(3));
                    } else {
                        string.append(warp).append(PluginVars.colors.get(3));
                    }
                }
            }
        }
        player.sendMessage(PluginMSGs.MSG_WARP_LIST.replace(PluginConstants.VALUE, string.toString()));
    }

    @CommandAlias("warp")
    @Syntax("<warp>")
    @CommandPermission("eternia.warp")
    public void onWarp(Player player, String nome) {
        if (player.hasPermission("eternia.warp." + nome.toLowerCase())) {
            final Location location = APIServer.getWarp("warp." + nome.toLowerCase());
            if (warpExists(location, player, nome) && !APIPlayer.isTeleporting(player)) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, location, PluginMSGs.MSG_WARP_DONE));
            } else if (APIPlayer.isTeleporting(player)) {
                player.sendMessage(PluginMSGs.MSG_IN_TELEPORT);
            }
        } else {
            player.sendMessage(PluginMSGs.MSG_NO_PERM);
        }
    }

    private boolean shopExists(final Location location, final Player player) {
        if (location == PluginVars.error) {
            player.sendMessage(PluginMSGs.MSG_SHOP_NO_EXISTS);
            return false;
        }
        return true;
    }

    private boolean warpExists(final Location location, final Player player, final String nome) {
        if (location == PluginVars.error) {
            player.sendMessage(PluginMSGs.MSG_WARP_NO_EXISTS.replace(PluginConstants.WARP, nome));
            return false;
        }
        return true;
    }

    private boolean spawnExists(final Location location, final Player player) {
        if (location == PluginVars.error) {
            player.sendMessage(PluginMSGs.MSG_SPAWN_NO_EXISTS);
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
        if (!APIServer.getWarp(shop).equals(PluginVars.error)) {
            EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_LOCATIONS, PluginConstants.LOCATION_STR, saveloc, PluginConstants.NAME_STR, shop));
        } else {
            EQueries.executeQuery(PluginConstants.getQueryInsert(PluginConfigs.TABLE_LOCATIONS, PluginConstants.NAME_STR, shop, PluginConstants.LOCATION_STR, saveloc));
        }
        APIServer.putWarp(shop, loc);
    }

    public void setWarp(Location loc, String warp) {
        final String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        final String warpName = "warp." + warp;
        if (!APIServer.getWarp(warpName).equals(PluginVars.error)) {
            EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_LOCATIONS, PluginConstants.LOCATION_STR, saveloc, PluginConstants.NAME_STR, warpName));
        } else {
            EQueries.executeQuery(PluginConstants.getQueryInsert(PluginConfigs.TABLE_LOCATIONS, PluginConstants.NAME_STR, warpName, PluginConstants.LOCATION_STR, saveloc));
        }
        APIServer.putWarp(warpName, loc);
    }

    public void delWarp(String warp) {
        final String warpName = "warp." + warp;
        APIServer.removeWarp(warpName);
        EQueries.executeQuery(PluginConstants.getQueryDelete(PluginConfigs.TABLE_LOCATIONS, PluginConstants.NAME_STR, warpName));
    }

}
