package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.*;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Home extends BaseCommand {

    @CommandAlias("delhome|delhouse|delcasa")
    @Syntax("<home>")
    @CommandPermission("eternia.delhome")
    public void onDelHome(Player player, String nome) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (existHome(nome.toLowerCase(), uuid)) {
            delHome(nome.toLowerCase(), playerName);
            EterniaServer.msg.sendMessage(player, Messages.HOME_DELETED, nome);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.HOME_NOT_FOUND, nome);
        }
    }

    @CommandAlias("home|house|casa|h")
    @Syntax("<home> <jogador>")
    @CommandPermission("eternia.home")
    public void onHome(Player player, String nome, @Optional OnlinePlayer target) {
        if (target == null) {
            Location location = APIServer.getLocation(nome.toLowerCase() + "." + player.getName());
            if (locationExists(location, player, nome) && !APIPlayer.isTeleporting(player)) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.HOME_GOING, true, nome)));
            } else if (APIPlayer.isTeleporting(player)) {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            }
        } else if (player.hasPermission("eternia.home.other")) {
            Location location = APIServer.getLocation(nome.toLowerCase() + "." + target.getPlayer().getName());
            if (locationExists(location, player, nome) && !APIPlayer.isTeleporting(player)) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, location, EterniaServer.msg.getMessage(Messages.HOME_GOING, true, nome)));
            } else if (APIPlayer.isTeleporting(player)) {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            }
        } else {
            EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
        }
    }

    @CommandAlias("homes|houses|casas")
    @Syntax("<jogador>")
    @CommandPermission("eternia.homes")
    public void onHomes(Player player, @Optional OnlinePlayer target) {
        if (target != null) {
            if (player.hasPermission("eternia.homes.other")) {
                showHomes(target.getPlayer());
            } else {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        } else {
            showHomes(player);
        }
    }

    private void showHomes(Player player) {
        StringBuilder accounts = new StringBuilder();
        List<String> list = APIPlayer.getHomes(UUIDFetcher.getUUIDOf(player.getName()));
        for (int i = 0; i < list.size(); i++) {
            if (i + 1 == list.size()) {
                accounts.append(ChatColor.DARK_AQUA).append(list.get(i));
            } else {
                accounts.append(ChatColor.DARK_AQUA).append(list.get(i)).append(ChatColor.DARK_GRAY).append(", ");
            }
        }
        EterniaServer.msg.sendMessage(player, Messages.HOME_LIST, accounts.toString());
    }

    @CommandAlias("sethome|sethouse|setcasa")
    @Syntax("<nome>")
    @CommandPermission("eternia.sethome")
    public void onSetHome(Player player, String nome) {
        int i = 4;
        for (int v = 5; v <= 93; v++) if (player.hasPermission("eternia.sethome." + v)) i = v;
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        nome = nome.replaceAll("[^a-zA-Z0-9]", "");
        if (nome.length() > 10) {
            EterniaServer.msg.sendMessage(player, Messages.HOME_STRING_LIMIT, nome);
            return;
        }

        if (canHome(uuid) < i || (existHome(nome.toLowerCase(), uuid))) {
            setHome(player.getLocation(), nome.toLowerCase(), playerName);
            EterniaServer.msg.sendMessage(player, Messages.HOME_CREATED);
            return;
        }

        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        final Location loc = player.getLocation();
        final String saveloc = loc.getWorld().getName() + ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) + ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        meta.setLore(Collections.singletonList(saveloc));
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&e" + nome.toLowerCase() + "&8]"));
        item.setItemMeta(meta);
        PlayerInventory inventory = player.getInventory();
        inventory.addItem(item);
        EterniaServer.msg.sendMessage(player, Messages.HOME_LIMIT_REACHED);
    }

    private boolean locationExists(final Location location, final Player player, final String nome) {
        if (location == PluginVars.getError()) {
            EterniaServer.msg.sendMessage(player, Messages.HOME_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    public void setHome(Location loc, String home, String jogador) {
        final String homeName = home + "." + jogador;
        final UUID uuid = UUIDFetcher.getUUIDOf(jogador);

        APIServer.putLocation(homeName, loc);
        boolean t = false;
        StringBuilder result = new StringBuilder();

        List<String> values = APIPlayer.getHomes(uuid);
        int size = values.size();
        for (int i = 0; i < size; i++) {
            final String value = values.get(i);
            if (value.equals(home)) {
                if (i + 1 != size) result.append(value).append(":");
                else result.append(value);
                t = true;
            } else {
                result.append(value).append(":");
            }
        }

        final String saveloc = loc.getWorld().getName() + ":" + ((int) loc.getX()) + ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (!t) {
            result.append(home);
            APIPlayer.updateHome(uuid, home);
            values.add(home);
            EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, Constants.HOMES_STR, result.toString(), Constants.UUID_STR, uuid.toString()));
            EQueries.executeQuery(Constants.getQueryInsert(EterniaServer.configs.tableLocations, Constants.NAME_STR, homeName, Constants.LOCATION_STR, saveloc));
        } else {
            EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tableLocations, Constants.LOCATION_STR, saveloc, Constants.NAME_STR, homeName));
        }
    }

    public void delHome(String home, String jogador) {
        final UUID uuid = UUIDFetcher.getUUIDOf(jogador);
        final String homeName = home + "." + jogador;
        APIServer.removeLocation(homeName);
        StringBuilder nova = new StringBuilder();

        List<String> values = APIPlayer.getHomes(uuid);
        int size = values.size();
        for (int i = 0; i < size; i++) {
            final String value = values.get(i);
            if (!value.equals(home)) {
                if (i + 1 != size) nova.append(value).append(":");
                else nova.append(value);
            }
        }
        APIPlayer.getHomes(uuid).remove(home);
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, Constants.HOMES_STR, nova.toString(), Constants.UUID_STR, uuid.toString()));
        EQueries.executeQuery(Constants.getQueryDelete(EterniaServer.configs.tableLocations, Constants.NAME_STR, homeName));
    }

    public boolean existHome(String home, UUID uuid) {
        List<String> homes = APIPlayer.getHomes(uuid);
        for (String line : homes) if (line.equals(home)) return true;
        return false;
    }

    public int canHome(UUID uuid) {
        return APIPlayer.getHomes(uuid) != null ? APIPlayer.getHomes(uuid).size() : 0;
    }

}
