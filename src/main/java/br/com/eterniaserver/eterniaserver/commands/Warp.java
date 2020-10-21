package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.core.Vars;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Warp extends BaseCommand {

    @CommandAlias("%spawn")
    @Syntax("%spawn_syntax")
    @Description("%spawn_description")
    @CommandPermission("%spawn_perm")
    @CommandCompletion("@players")
    public void onSpawn(Player player, @Optional OnlinePlayer targets) {
        User user = new User(player);

        if (user.isTeleporting()) {
            user.sendMessage(Messages.SERVER_IN_TELEPORT);
            return;
        }

        Location location = APIServer.getLocation("warp.spawn");

        if (targets == null) {
            if (spawnExists(location, player)) {
                user.putInTeleport(new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.WARP_SPAWN_TELEPORTED, true)));
            }
            return;
        }

        User target = new User(targets.getPlayer());

        if (!user.hasPermission("eternia.spawn.other")) {
            user.sendMessage(Messages.SERVER_NO_PERM);
            return;
        }

        if (spawnExists(location, player)) {
            PaperLib.teleportAsync(target.getPlayer(), location);
            target.sendMessage(Messages.WARP_SPAWN_TELEPORTED_BY, user.getName(), user.getDisplayName());
            user.sendMessage(Messages.WARP_SPAWN_TELEPORT_TARGET, target.getName(), target.getDisplayName());
        }
    }

    @CommandAlias("%spawnset")
    @Description("%spawnset_description")
    @CommandPermission("%spawnset_perm")
    public void onSetSpawn(Player player) {
        setWarp(player.getLocation(), "spawn");
        EterniaServer.msg.sendMessage(player, Messages.WARP_SPAWN_CREATED);
    }

    @CommandAlias("%shop")
    @Syntax("%shop_syntax")
    @Description("%shop_description")
    @CommandCompletion("@players")
    @CommandPermission("%shop_perm")
    public void onShop(Player player, @Optional String targets) {
        User user = new User(player);

        if (user.isTeleporting()) {
            user.sendMessage(Messages.SERVER_IN_TELEPORT);
            return;
        }

        if (targets != null) {
            Location location = APIServer.getLocation(targets.toLowerCase());
            if (shopExists(location, player, targets)) {
                user.putInTeleport(new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.WARP_SHOP_PLAYER_TELEPORTED, true, targets)));
            }
            return;
        }

        Location location = APIServer.getLocation("warp.shop");

        if (!player.hasPermission("eternia.warp.shop")) {
            user.sendMessage(Messages.SERVER_NO_PERM);
            return;
        }

        if (shopExists(location, player)) {
            user.putInTeleport(new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.WARP_SHOP_TELEPORTED, true)));
        }
    }

    @CommandAlias("%setshop")
    @Description("%setshop_description")
    @CommandPermission("%setshop_perm")
    public void onSetShop(Player player) {
        setShop(player.getLocation(), player.getName().toLowerCase());
        EterniaServer.msg.sendMessage(player, Messages.WARP_SHOP_CREATED);
    }

    @CommandAlias("%delshop")
    @Description("%delshop_description")
    @CommandPermission("%delshop_perm")
    public void onDelShop(Player player) {
        delShop(player.getName().toLowerCase());
        EterniaServer.msg.sendMessage(player, Messages.WARP_SHOP_DELETED);
    }

    @CommandAlias("%warp")
    @Syntax("%warp_syntax")
    @Description("%warp_description")
    @CommandPermission("%warp_perm")
    public void onWarp(Player player, String nome) {
        User user = new User(player);

        if (user.isTeleporting()) {
            user.sendMessage(Messages.SERVER_IN_TELEPORT);
            return;
        }

        if (!player.hasPermission("eternia.warp." + nome.toLowerCase())) {
            user.sendMessage(Messages.SERVER_NO_PERM);
            return;
        }

        Location location = APIServer.getLocation("warp." + nome.toLowerCase());

        if (warpExists(location, player, nome)) {
            user.putInTeleport(new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.WARP_TELEPORTED, true, nome)));
        }
    }

    @CommandAlias("%setwarp")
    @Syntax("%setwarp_syntax")
    @Description("%setwarp_description")
    @CommandPermission("%setwarp_perm")
    public void onSetWarp(Player player, String nome) {
        setWarp(player.getLocation(), nome.toLowerCase());
        EterniaServer.msg.sendMessage(player, Messages.WARP_CREATED, nome);
    }

    @CommandAlias("%delwarp")
    @Syntax("%delwarp_syntax")
    @Description("%delwarp_description")
    @CommandPermission("%delwarp_perm")
    public void onDelWarp(Player player, String nome) {
        if (APIServer.getLocation("warp." + nome.toLowerCase()).equals(Vars.getError())) {
            EterniaServer.msg.sendMessage(player, Messages.WARP_NOT_FOUND, nome);
            return;
        }

        delWarp(nome.toLowerCase());
        EterniaServer.msg.sendMessage(player, Messages.WARP_DELETED, nome);
    }

    @CommandAlias("%listwarp")
    @Description("%listwarp_description")
    @CommandPermission("%listwarp_perm")
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
                        string.append(warp).append(Vars.colors.get(8)).append(", ").append(Vars.colors.get(3));
                    } else {
                        string.append(warp).append(Vars.colors.get(3));
                    }
                }
            }
        }
        EterniaServer.msg.sendMessage(player, Messages.WARP_LIST, string.toString());
    }

    private boolean shopExists(final Location location, final Player player) {
        if (location == Vars.getError()) {
            EterniaServer.msg.sendMessage(player, Messages.WARP_SHOP_CENTRAL_NOT_FOUND);
            return false;
        }
        return true;
    }

    private boolean shopExists(final Location location, final Player player, final String nome) {
        if (location == Vars.getError()) {
            EterniaServer.msg.sendMessage(player, Messages.WARP_SHOP_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    private boolean warpExists(final Location location, final Player player, final String nome) {
        if (location == Vars.getError()) {
            EterniaServer.msg.sendMessage(player, Messages.WARP_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    private boolean spawnExists(final Location location, final Player player) {
        if (location == Vars.getError()) {
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
        if (!APIServer.getLocation(shop).equals(Vars.getError())) {
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
        if (!APIServer.getLocation(warpName).equals(Vars.getError())) {
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
