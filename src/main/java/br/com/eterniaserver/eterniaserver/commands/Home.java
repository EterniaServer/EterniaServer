package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Delete;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.objects.LocationQuery;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Home extends BaseCommand {

    private final EterniaServer plugin;

    public Home(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("%delhome")
    @Syntax("%delhome_syntax")
    @Description("%delhome_description")
    @CommandPermission("%delhome_perm")
    public void onDelHome(Player player, String nome) {
        User user = new User(player);

        if (existHome(nome.toLowerCase(), user)) {
            delHome(nome.toLowerCase(), user.getName());
            plugin.sendMessage(player, Messages.HOME_DELETED, nome);
            return;
        }

        plugin.sendMessage(player, Messages.HOME_NOT_FOUND, nome);
    }

    @CommandAlias("%home")
    @Syntax("%home_syntax")
    @Description("%home_description")
    @CommandPermission("%home_perm")
    public void onHome(Player player, String nome, @Optional OnlinePlayer targets) {
        User user = new User(player);

        if (user.isTeleporting()) {
            plugin.sendMessage(player, Messages.SERVER_IN_TELEPORT);
        }

        if (targets == null) {
            Location location = plugin.getLocation(nome.toLowerCase() + "." + user.getName());
            if (locationExists(location, player, nome)) {
                user.putInTeleport(new PlayerTeleport(plugin, player, location, plugin.getMessage(Messages.HOME_GOING, true, nome)));
            }
            return;
        }

        if (user.hasPermission(plugin.getString(Strings.PERM_HOME_OTHER))) {
            Location location = plugin.getLocation(nome.toLowerCase() + "." + targets.getPlayer().getName());
            if (locationExists(location, player, nome)) {
                user.putInTeleport(new PlayerTeleport(plugin, player, location, plugin.getMessage(Messages.HOME_GOING, true, nome)));
            }
            return;
        }

        plugin.sendMessage(player, Messages.SERVER_NO_PERM);
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

        if (player.hasPermission(plugin.getString(Strings.PERM_HOME_OTHER))) {
            showHomes(player, target.getPlayer());
            return;
        }

        plugin.sendMessage(player, Messages.SERVER_NO_PERM);
    }

    private void showHomes(Player player, Player target) {
        final User user = new User(target);
        final StringBuilder result = new StringBuilder();
        final String finalResult;

        for (String actualHomeName : user.getHomes()) {
            result.append(ChatColor.DARK_AQUA).append(actualHomeName).append(ChatColor.DARK_GRAY).append(", ");
        }
        finalResult = result.toString();
        if (finalResult.length() > 2) {
            plugin.sendMessage(player, Messages.HOME_LIST, finalResult.substring(0, finalResult.length() - 2));
            return;
        }

        plugin.sendMessage(player, Messages.HOME_LIST, "");
    }

    @CommandAlias("%sethome")
    @Syntax("%sethome_syntax")
    @Description("%sethome_description")
    @CommandPermission("%sethome_perm")
    public void onSetHome(Player player, String nome) {
        User user = new User(player);
        int i = 4;
        for (int v = 5; v <= 93; v++) if (player.hasPermission(plugin.getString(Strings.PERM_SETHOME_LIMIT_PREFIX) + v)) i = v;

        nome = nome.replaceAll("[^a-zA-Z0-9]", "");
        if (nome.length() > 10) {
            plugin.sendMessage(player, Messages.HOME_STRING_LIMIT, nome);
            return;
        }

        if (canHome(user) < i || (existHome(nome.toLowerCase(), user))) {
            setHome(player.getLocation(), nome.toLowerCase(), user.getName());
            plugin.sendMessage(player, Messages.HOME_CREATED);
            return;
        }

        if (!user.hasPermission(plugin.getString(Strings.PERM_HOME_COMPASS))) {
            plugin.sendMessage(player, Messages.HOME_NO_PERM_TO_COMPASS);
            return;
        }

        final ItemStack item = new ItemStack(Material.COMPASS);
        final ItemMeta meta = item.getItemMeta();
        final Location loc = player.getLocation();

        meta.setDisplayName(plugin.getMessage(Messages.HOME_ITEM_NAME, false, nome));
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_WORLD), PersistentDataType.STRING, loc.getWorld().getName());
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_X), PersistentDataType.DOUBLE, loc.getX());
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_Y), PersistentDataType.DOUBLE, loc.getY());
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_Z), PersistentDataType.DOUBLE, loc.getZ());
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_YAW), PersistentDataType.FLOAT, loc.getYaw());
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_PITCH), PersistentDataType.FLOAT, loc.getPitch());
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_LOC_NAME), PersistentDataType.STRING, nome.toLowerCase());
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
        plugin.sendMessage(player, Messages.HOME_LIMIT_REACHED);
    }

    private boolean locationExists(final Location location, final Player player, final String nome) {
        if (location == plugin.getError()) {
            plugin.sendMessage(player, Messages.HOME_NOT_FOUND, nome);
            return false;
        }
        return true;
    }

    public void setHome(Location loc, String home, String jogador) {
        final String homeName = home + "." + jogador;
        final User user = new User(jogador);
        if (user.getUUID() == null) {
            return;
        }

        plugin.putLocation(homeName, loc);

        if (user.getHomes().contains(home)) {
            LocationQuery locationQuery = new LocationQuery(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
            locationQuery.setLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            locationQuery.where.set("name", homeName);
            SQL.execute(locationQuery);
            return;
        }

        user.getHomes().add(home);
        final StringBuilder result = new StringBuilder();
        for (String actualHomeName : user.getHomes()) {
            result.append(actualHomeName).append(":");
        }
        final String finalResult = result.toString();

        Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
        update.set.set("homes", finalResult.substring(0, finalResult.length() - 1));
        update.where.set("uuid", user.getUUID().toString());
        SQL.executeAsync(update);

        Insert insert = new Insert(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
        insert.columns.set("name", "world", "coord_x", "coord_y", "coord_z", "coord_yaw", "coord_pitch");
        insert.values.set(homeName, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        SQL.executeAsync(insert);
    }

    public void delHome(String home, String jogador) {
        final User user = new User(jogador);
        if (user.getUUID() == null) {
            return;
        }

        final String homeName = home + "." + jogador;
        final StringBuilder result = new StringBuilder();

        plugin.removeLocation(homeName);
        user.getHomes().remove(home);
        for (String actualHomeName : user.getHomes()) {
            result.append(actualHomeName).append(":");
        }
        final String finalResult = result.toString();

        final Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
        update.set.set("homes", finalResult.substring(0, finalResult.length() - 1));
        update.where.set("uuid", user.getUUID().toString());
        SQL.executeAsync(update);

        final Delete delete = new Delete(plugin.getString(Strings.TABLE_LOCATIONS) + Constants.NEW);
        delete.where.set("name", homeName);
        SQL.executeAsync(delete);
    }

    public boolean existHome(String home, User user) {
        return user.getHomes().contains(home);
    }

    public int canHome(User user) {
        return user.getHomes().size();
    }

}
