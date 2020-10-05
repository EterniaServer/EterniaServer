package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.core.APIPlayer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.PluginVars;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp extends BaseCommand {

    @CommandAlias("spawn")
    @Syntax("<jogador>")
    @CommandPermission("eternia.spawn")
    public void onSpawn(Player player, @Optional OnlinePlayer target) {
        final Location location = APIServer.getLocation("warp.spawn");
        if (target == null) {
            if (APIPlayer.isTeleporting(player)) {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            } else if (spawnExists(location, player)) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.WARP_SPAWN_TELEPORTED, true)));
            }
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission("eternia.spawn.other") && spawnExists(location, player)) {
                PaperLib.teleportAsync(targetP, location);
                EterniaServer.msg.sendMessage(targetP, Messages.WARP_SPAWN_TELEPORTED_BY, player.getName(), player.getDisplayName());
                EterniaServer.msg.sendMessage(player, Messages.WARP_SPAWN_TELEPORT_TARGET, targetP.getName(), targetP.getDisplayName());
            } else if (!player.hasPermission("eternia.spawn.other")) {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        }
    }

    @CommandAlias("shop|loja")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.shop.player")
    public void onShop(Player player, @Optional String target) {
        if (target == null) {
            final Location location = APIServer.getLocation("warp.shop");
            if (player.hasPermission("eternia.warp.shop")) {
                if (shopExists(location, player) && !APIPlayer.isTeleporting(player)) {
                    APIServer.putInTeleport(player, new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.WARP_SHOP_TELEPORTED, true)));
                } else if (APIPlayer.isTeleporting(player)) {
                    EterniaServer.msg.sendMessage(player, Messages.SERVER_IN_TELEPORT);
                }
            } else {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        } else {
            final Location location = APIServer.getLocation(target.toLowerCase());
            if (shopExists(location, player, target) && !APIPlayer.isTeleporting(player)) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.WARP_SHOP_PLAYER_TELEPORTED, true, target)));
            } else if (APIPlayer.isTeleporting(player)) {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            }
        }
    }

    @CommandAlias("setspawn")
    @CommandPermission("eternia.setspawn")
    public void onSetSpawn(Player player) {
        setWarp(player.getLocation(), "spawn");
        EterniaServer.msg.sendMessage(player, Messages.WARP_SPAWN_CREATED);
    }

    @CommandAlias("setshop|setloja")
    @CommandPermission("eternia.setshop")
    public void onSetShop(Player player) {
        setShop(player.getLocation(), player.getName().toLowerCase());
        EterniaServer.msg.sendMessage(player, Messages.WARP_SHOP_CREATED);
    }

    @CommandAlias("delshop|delloja")
    @CommandPermission("eternia.setshop")
    public void onDelShop(Player player) {
        delShop(player.getName().toLowerCase());
        EterniaServer.msg.sendMessage(player, Messages.WARP_SHOP_DELETED);
    }

    @CommandAlias("setwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.setwarp")
    public void onSetWarp(Player player, String nome) {
        setWarp(player.getLocation(), nome.toLowerCase());
        EterniaServer.msg.sendMessage(player, Messages.WARP_CREATED, nome);
    }

    @CommandAlias("delwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.delwarp")
    public void onDelWarp(Player player, String nome) {
        if (!APIServer.getLocation("warp." + nome.toLowerCase()).equals(PluginVars.getError())) {
            delWarp(nome.toLowerCase());
            EterniaServer.msg.sendMessage(player, Messages.WARP_DELETED, nome);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.WARP_NOT_FOUND, nome);
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
        EterniaServer.msg.sendMessage(player, Messages.WARP_LIST, string.toString());
    }

    @CommandAlias("warp")
    @Syntax("<warp>")
    @CommandPermission("eternia.warp")
    public void onWarp(Player player, String nome) {
        if (player.hasPermission("eternia.warp." + nome.toLowerCase())) {
            final Location location = APIServer.getLocation("warp." + nome.toLowerCase());
            if (warpExists(location, player, nome) && !APIPlayer.isTeleporting(player)) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.WARP_TELEPORTED, true, nome)));
            } else if (APIPlayer.isTeleporting(player)) {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            }
        } else {
            EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
        }
    }

    private boolean shopExists(final Location location, final Player player) {
        if (location == PluginVars.getError()) {
            EterniaServer.msg.sendMessage(player, Messages.WARP_SHOP_CENTRAL_NOT_FOUND);
            return false;
        }
        return true;
    }

    private boolean shopExists(final Location location, final Player player, final String nome) {
        if (location == PluginVars.getError()) {
            EterniaServer.msg.sendMessage(player, Messages.WARP_SHOP_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    private boolean warpExists(final Location location, final Player player, final String nome) {
        if (location == PluginVars.getError()) {
            EterniaServer.msg.sendMessage(player, Messages.WARP_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    private boolean spawnExists(final Location location, final Player player) {
        if (location == PluginVars.getError()) {
            EterniaServer.msg.sendMessage(player, Messages.WARP_SPAWN_NOT_FOUND);
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
        if (!APIServer.getLocation(shop).equals(PluginVars.getError())) {
            EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tableLocations, "location", saveloc, "name", shop));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(EterniaServer.configs.tableLocations, "name", shop, "location", saveloc));
        }
        APIServer.putLocation(shop, loc);
    }

    public void setWarp(Location loc, String warp) {
        final String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        final String warpName = "warp." + warp;
        if (!APIServer.getLocation(warpName).equals(PluginVars.getError())) {
            EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tableLocations, "location", saveloc, "name", warpName));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(EterniaServer.configs.tableLocations, "name", warpName, "location", saveloc));
        }
        APIServer.putLocation(warpName, loc);
    }

    public void delWarp(String warp) {
        final String warpName = "warp." + warp;
        APIServer.removeLocation(warpName);
        EQueries.executeQuery(Constants.getQueryDelete(EterniaServer.configs.tableLocations, "name", warpName));
    }

    public void delShop(String shop) {
        APIServer.removeLocation(shop);
        EQueries.executeQuery(Constants.getQueryDelete(EterniaServer.configs.tableLocations, "name", shop));
    }

}
