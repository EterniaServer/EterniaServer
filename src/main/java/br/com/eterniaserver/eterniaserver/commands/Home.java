package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Delete;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
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

public class Home extends BaseCommand {

    @CommandAlias("%delhome")
    @Syntax("%delhome_syntax")
    @Description("%delhome_description")
    @CommandPermission("%delhome_perm")
    public void onDelHome(Player player, String nome) {
        User user = new User(player);

        if (existHome(nome.toLowerCase(), user)) {
            delHome(nome.toLowerCase(), user.getName());
            user.sendMessage(Messages.HOME_DELETED, nome);
            return;
        }

        EterniaServer.sendMessage(player, Messages.HOME_NOT_FOUND, nome);
    }

    @CommandAlias("%home")
    @Syntax("%home_syntax")
    @Description("%home_description")
    @CommandPermission("%home_perm")
    public void onHome(Player player, String nome, @Optional OnlinePlayer targets) {
        User user = new User(player);

        if (user.isTeleporting()) {
            user.sendMessage(Messages.SERVER_IN_TELEPORT);
        }

        if (targets == null) {
            Location location = ServerRelated.getLocation(nome.toLowerCase() + "." + user.getName());
            if (locationExists(location, player, nome)) {
                user.putInTeleport(new PlayerTeleport(player, location, EterniaServer.getMessage(Messages.HOME_GOING, true, nome)));
            }
            return;
        }

        if (user.hasPermission(EterniaServer.getString(Strings.PERM_HOME_OTHER))) {
            Location location = ServerRelated.getLocation(nome.toLowerCase() + "." + targets.getPlayer().getName());
            if (locationExists(location, player, nome)) {
                user.putInTeleport(new PlayerTeleport(player, location, EterniaServer.getMessage(Messages.HOME_GOING, true, nome)));
            }
            return;
        }

        user.sendMessage(Messages.SERVER_NO_PERM);
    }

    @CommandAlias("%homes")
    @Syntax("%homes_syntax")
    @Description("%homes_description")
    @CommandPermission("%homes_perm")
    public void onHomes(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            showHomes(player, player);
            return;
        }

        if (player.hasPermission(EterniaServer.getString(Strings.PERM_HOME_OTHER))) {
            showHomes(player, target.getPlayer());
            return;
        }

        EterniaServer.sendMessage(player, Messages.SERVER_NO_PERM);
    }

    private void showHomes(Player player, Player target) {
        User user = new User(target);

        StringBuilder accounts = new StringBuilder();
        List<String> list = user.getHomes();
        for (int i = 0; i < list.size(); i++) {
            if (i + 1 == list.size()) {
                accounts.append(ChatColor.DARK_AQUA).append(list.get(i));
            } else {
                accounts.append(ChatColor.DARK_AQUA).append(list.get(i)).append(ChatColor.DARK_GRAY).append(", ");
            }
        }

        EterniaServer.sendMessage(player, Messages.HOME_LIST, accounts.toString());
    }

    @CommandAlias("%sethome")
    @Syntax("%sethome_syntax")
    @Description("%sethome_description")
    @CommandPermission("%sethome_perm")
    public void onSetHome(Player player, String nome) {
        User user = new User(player);
        int i = 4;
        for (int v = 5; v <= 93; v++) if (player.hasPermission(EterniaServer.getString(Strings.PERM_SETHOME_LIMIT_PREFIX) + v)) i = v;

        nome = nome.replaceAll("[^a-zA-Z0-9]", "");
        if (nome.length() > 10) {
            user.sendMessage(Messages.HOME_STRING_LIMIT, nome);
            return;
        }

        if (canHome(user) < i || (existHome(nome.toLowerCase(), user))) {
            setHome(player.getLocation(), nome.toLowerCase(), user.getName());
            user.sendMessage(Messages.HOME_CREATED);
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
        user.sendMessage(Messages.HOME_LIMIT_REACHED);
    }

    private boolean locationExists(final Location location, final Player player, final String nome) {
        if (location == ServerRelated.getError()) {
            EterniaServer.sendMessage(player, Messages.HOME_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    public void setHome(Location loc, String home, String jogador) {
        String homeName = home + "." + jogador;
        User user = new User(jogador);

        ServerRelated.putLocation(homeName, loc);
        boolean t = false;
        StringBuilder result = new StringBuilder();

        List<String> values = user.getHomes();
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
            user.updateHome(home);

            Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
            update.set.set("homes", result.toString());
            update.where.set("uuid", user.getUUID().toString());
            SQL.executeAsync(update);

            Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_LOCATIONS));
            insert.columns.set("name", "location");
            insert.values.set(homeName, saveloc);
            SQL.executeAsync(insert);
        } else {
            Update update = new Update(EterniaServer.getString(Strings.TABLE_LOCATIONS));
            update.set.set("location", saveloc);
            update.where.set("name", homeName);
            SQL.executeAsync(update);
        }
    }

    public void delHome(String home, String jogador) {
        User user = new User(jogador);
        final String homeName = home + "." + jogador;
        ServerRelated.removeLocation(homeName);
        StringBuilder nova = new StringBuilder();

        List<String> values = user.getHomes();
        int size = values.size();
        for (int i = 0; i < size; i++) {
            final String value = values.get(i);
            if (!value.equals(home)) {
                if (i + 1 != size) nova.append(value).append(":");
                else nova.append(value);
            }
        }
        user.getHomes().remove(home);
        Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
        update.set.set("homes", nova.toString());
        update.where.set("uuid", user.getUUID().toString());
        SQL.executeAsync(update);

        Delete delete = new Delete(EterniaServer.getString(Strings.TABLE_LOCATIONS));
        delete.where.set("name", homeName);
        SQL.executeAsync(delete);
    }

    public boolean existHome(String home, User user) {
        List<String> homes = user.getHomes();
        for (String line : homes) if (line.equals(home)) return true;
        return false;
    }

    public int canHome(User user) {
        return user.getHomes() != null ? user.getHomes().size() : 0;
    }

}
