package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.strings.MSG;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BaseCmdHome extends BaseCommand {

    @CommandAlias("delhome|delhouse|delcasa")
    @Syntax("<home>")
    @CommandPermission("eternia.delhome")
    public void onDelHome(Player player, String nome) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (existHome(nome.toLowerCase(), uuid)) {
            delHome(nome.toLowerCase(), playerName);
            player.sendMessage(MSG.M_HOME_DELETED);
        } else {
            player.sendMessage(MSG.M_HOME_NO);
        }
    }

    @CommandAlias("home|house|casa|h")
    @Syntax("<home> <jogador>")
    @CommandPermission("eternia.home")
    public void onHome(Player player, String nome, @Optional OnlinePlayer target) {
        if (target == null) {
            Location location = getHome(nome.toLowerCase(), player.getName());
            if (locationExists(location, player) && !Vars.teleports.containsKey(player)) {
                Vars.teleports.put(player, new PlayerTeleport(player, location, MSG.M_HOME_DONE));
            } else if (Vars.teleports.containsKey(player)) {
                player.sendMessage(MSG.MSG_IN_TELEPORT);
            }
        } else if (player.hasPermission("eternia.home.other")) {
            Location location = getHome(nome.toLowerCase(), target.getPlayer().getName());
            if (locationExists(location, player) && !Vars.teleports.containsKey(player)) {
                Vars.teleports.put(player, new PlayerTeleport(player, location, MSG.M_HOME_DONE));
            } else if (Vars.teleports.containsKey(player)) {
                player.sendMessage(MSG.MSG_IN_TELEPORT);
            }
        } else {
            player.sendMessage(MSG.MSG_NO_PERM);
        }
    }

    @CommandAlias("homes|houses|casas")
    @Syntax("<jogador>")
    @CommandPermission("eternia.homes")
    public void onHomes(Player player, @Optional OnlinePlayer target) {
        StringBuilder accounts = new StringBuilder();
        if (target != null) {
            if (player.hasPermission("eternia.homes.other")) {
                List<String> list = getHomes(UUIDFetcher.getUUIDOf(target.getPlayer().getName()));
                for (String line : list) {
                    accounts.append(line).append(Vars.colors.get(8)).append(", ").append(Vars.colors.get(3));
                }
                player.sendMessage(MSG.M_HOME_LIST.replace(Constants.HOMES, MSG.getColor(accounts.toString())));
            } else {
                player.sendMessage(MSG.MSG_NO_PERM);
            }
        } else {
            List<String> list = getHomes(UUIDFetcher.getUUIDOf(player.getName()));
            for (String line : list) {
                accounts.append(line).append(Vars.colors.get(8)).append(", ").append(Vars.colors.get(3));
            }
            player.sendMessage(MSG.M_HOME_LIST.replace(Constants.HOMES, MSG.getColor(accounts.toString())));
        }
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
            player.sendMessage(MSG.M_HOME_EXCEEDED);
            return;
        }

        if (canHome(uuid) < i || (existHome(nome.toLowerCase(), uuid))) {
            setHome(player.getLocation(), nome.toLowerCase(), playerName);
            player.sendMessage(MSG.M_HOME_CREATED);
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
        player.sendMessage(MSG.M_HOME_LIMIT);
    }

    private boolean locationExists(final Location location, final Player player) {
        if (location == Vars.error) {
            player.sendMessage(MSG.M_HOME_NO);
            return false;
        }
        return true;
    }

    public void setHome(Location loc, String home, String jogador) {
        final String homeName = home + "." + jogador;
        final UUID uuid = UUIDFetcher.getUUIDOf(jogador);

        Vars.locations.put(homeName, loc);
        boolean t = false;
        StringBuilder result = new StringBuilder();

        List<String> values = getHomes(uuid);
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
            values.add(home);
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.HOMES_STR, result.toString(), Constants.UUID_STR, uuid.toString()));
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_LOCATIONS, Constants.NAME_STR, homeName, Constants.LOCATION_STR, saveloc));
        } else {
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_LOCATIONS, Constants.LOCATION_STR, saveloc, Constants.NAME_STR, homeName));
        }
        Vars.playerProfile.get(uuid).homes = values;
    }

    public void delHome(String home, String jogador) {
        final UUID uuid = UUIDFetcher.getUUIDOf(jogador);
        final String homeName = home + "." + jogador;
        Vars.locations.remove(homeName);
        StringBuilder nova = new StringBuilder();

        List<String> newValues = new ArrayList<>();
        List<String> values = getHomes(uuid);
        int size = values.size();
        for (int i = 0; i < size; i++) {
            final String value = values.get(i);
            if (!value.equals(home)) {
                newValues.add(value);
                if (i + 1 != size) nova.append(value).append(":");
                else nova.append(value);
            }
        }
        Vars.playerProfile.get(uuid).homes = newValues;
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.HOMES_STR, nova.toString(), Constants.UUID_STR, uuid.toString()));
        EQueries.executeQuery(Constants.getQueryDelete(Configs.TABLE_LOCATIONS, Constants.NAME_STR, homeName));
    }

    public Location getHome(String home, String jogador) {
        return Vars.locations.getOrDefault(home + "." + jogador, Vars.error);
    }

    public boolean existHome(String home, UUID uuid) {
        List<String> homes = getHomes(uuid);
        for (String line : homes) if (line.equals(home)) return true;
        return false;
    }

    public int canHome(UUID uuid) {
        return getHomes(uuid) != null ? getHomes(uuid).size() : 0;
    }

    public List<String> getHomes(UUID uuid) {
        return Vars.playerProfile.get(uuid).getHomes();
    }

}
