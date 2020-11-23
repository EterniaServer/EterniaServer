package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.sql.queries.Select;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.core.Vars;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.Runtime;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Generic extends BaseCommand {

    private final EterniaServer plugin;
    private final Runtime getRuntime;

    private final SimpleDateFormat sdf = new SimpleDateFormat(EterniaServer.getString(Strings.DATA_FORMAT));
    private final ItemStack coali = new ItemStack(Material.COAL);
    private final ItemStack lapizi = new ItemStack(Material.LAPIS_LAZULI);
    private final ItemStack redstonei = new ItemStack(Material.REDSTONE);
    private final ItemStack ironi = new ItemStack(Material.IRON_INGOT);
    private final ItemStack goldi = new ItemStack(Material.GOLD_INGOT);
    private final ItemStack diamondi = new ItemStack(Material.DIAMOND);
    private final ItemStack esmeraldai = new ItemStack(Material.EMERALD);

    public Generic(EterniaServer plugin) {

        this.plugin = plugin;
        this.getRuntime = new Runtime();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(EterniaServer.getString(Strings.TABLE_LOCATIONS)).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
                Vars.setError(new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666));
                while (resultSet.next()) {
                    final String[] split = resultSet.getString("location").split(":");
                    Location loc = new Location(Bukkit.getWorld(split[0]),
                            Double.parseDouble(split[1]),
                            Double.parseDouble(split[2]) + 1D,
                            Double.parseDouble(split[3]),
                            Float.parseFloat(split[4]),
                            Float.parseFloat(split[5]));
                    APIServer.putLocation(resultSet.getString("name"), getCenter(loc));
                }
            } catch (SQLException ignored) {
                APIServer.logError("Erro ao carregar database", 3);
            }
        });

        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(EterniaServer.getString(Strings.TABLE_PLAYER)).queryString()); ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()) {
                final PlayerProfile playerProfile = new PlayerProfile(
                        resultSet.getString("player_name"),
                        resultSet.getLong("time"),
                        resultSet.getLong("last"),
                        resultSet.getLong("hours")
                );
                getModules(playerProfile, resultSet);
                APIServer.putProfile(UUID.fromString(resultSet.getString("uuid")), playerProfile);
            }
        } catch (SQLException ignored) {
            APIServer.logError("Erro ao carregar database", 3);
        }
        sendConsole(EterniaServer.getMessage(Messages.SERVER_DATA_LOADED, true, "Player Profiles", String.valueOf(APIServer.getProfileMapSize())));
    }

    @CommandAlias("%reload")
    @Syntax("%reload_syntax")
    @Description("%reload_description")
    @CommandPermission("%reload_perm")
    @CommandCompletion("configs|messages|constants|blocks|chat|kits|cash|rewards|schedules")
    public void onReload(String module) {
        switch (module) {
            case "configs":
                plugin.configs();
                break;
            case "messages":
                plugin.messages();
                break;
            case "constants":
                plugin.constants();
                break;
            case "blocks":
                plugin.blocks();
                break;
            case "chat":
                plugin.chat();
                break;
            case "kits":
                plugin.kits();
                break;
            case "cash":
                plugin.cash();
                break;
            case "rewards":
                plugin.rewards();
                break;
            case "schedules":
                plugin.schedule();
                break;
            default:
                plugin.configs();
                plugin.messages();
                plugin.constants();
                plugin.blocks();
                plugin.chat();
                plugin.kits();
                plugin.cash();
                plugin.rewards();
                plugin.schedule();
        }
    }

    @CommandAlias("%speed")
    @Syntax("%speed_syntax")
    @Description("%speed_description")
    @CommandPermission("%speed_perm")
    public void onSpeed(Player player, Integer speed) {
        User user = new User(player);

        if (speed <= 0 || speed >= 11) {
            user.sendMessage(Messages.SPEED_LIMIT);
            return;
        }

        player.setFlySpeed((float) (speed / 10.0D));
        player.setWalkSpeed((float) (speed / 10.0D));
        user.sendMessage(Messages.SPEED_SET, String.valueOf((float) (speed / 10.0D)));
    }

    @CommandAlias("%god")
    @Syntax("%god_syntax")
    @Description("%god_description")
    @CommandPermission("%god_perm")
    public void onGod(Player player, @Optional OnlinePlayer targets) {
        if (targets == null) {
            changeGameMode(new User(player));
            return;
        }

        changeGameMode(new User(targets.getPlayer()));
    }

    @CommandAlias("%profile")
    @CommandPermission("%profile_perm")
    @Syntax("%profile_syntax")
    @Description("%profile_description")
    @CommandCompletion("@players")
    public void onProfile(Player player, @Optional OnlinePlayer onlinePlayer) {
        if (onlinePlayer == null) {
            sendProfile(player, player);
            return;
        }

        sendProfile(player, onlinePlayer.getPlayer());
    }

    @CommandAlias("%mem")
    @CommandPermission("%mem_perm")
    @Description("%mem_description")
    public void onMem(CommandSender player) {
        User user = new User(player);

        getRuntime.recalculateRuntime();
        user.sendMessage(Messages.STATS_MEM, String.valueOf(getRuntime.getFreeMem()), String.valueOf(getRuntime.getTotalMem()));
        user.sendMessage(Messages.STATS_HOURS, String.valueOf(getRuntime.getDays()), String.valueOf(getRuntime.getHours()), String.valueOf(getRuntime.getMinutes()), String.valueOf(getRuntime.getSeconds()));
    }

    @CommandAlias("%mem_all")
    @CommandPermission("%mem_all_perm")
    @Description("%mem_all_description")
    public void onMemAll() {
        getRuntime.recalculateRuntime();
        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.STATS_MEM, true, String.valueOf(getRuntime.getFreeMem()), String.valueOf(getRuntime.getTotalMem())));
        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.STATS_HOURS, true, String.valueOf(getRuntime.getDays()), String.valueOf(getRuntime.getHours()), String.valueOf(getRuntime.getMinutes()), String.valueOf(getRuntime.getSeconds())));
    }

    @CommandAlias("%fly")
    @CommandPermission("%fly_perm")
    @Syntax("%fly_syntax")
    @Description("%fly_description")
    @CommandCompletion("@players")
    public void onFly(Player player, @Optional OnlinePlayer targetS) {
        User user = new User(player);
        String worldName = player.getWorld().getName();

        if (!player.hasPermission(EterniaServer.getString(Strings.PERM_FLY_BYPASS)) && EterniaServer.getStringList(Lists.BLACKLISTED_WORLDS_FLY).contains(worldName)) {
            user.sendMessage(Messages.SERVER_NO_PERM);
            return;
        }

        if (targetS != null && player.hasPermission(EterniaServer.getString(Strings.PERM_FLY_OTHER))) {

            User target = new User(targetS.getPlayer());

            if (target.isOnPvP()) {
                user.sendMessage(Messages.FLY_TARGET_ARE_PVP, String.valueOf(EterniaServer.getInteger(Integers.PVP_TIME) - target.getPvPCooldown()));
                return;
            }

            target.changeFlyState();
            if (target.getPlayer().isFlying()) {
                target.sendMessage(Messages.FLY_ENABLED_BY, user.getName(), user.getDisplayName());
                user.sendMessage(Messages.FLY_ENABLED_FROM, target.getName(), target.getDisplayName());
                return;
            }
            target.sendMessage(Messages.FLY_DISABLED_BY, user.getName(), user.getDisplayName());
            user.sendMessage(Messages.FLY_DISABLED_FROM, target.getName(), target.getDisplayName());
            return;
        }

        if (user.isOnPvP()) {
            user.sendMessage(Messages.FLY_ARE_PVP, String.valueOf(EterniaServer.getInteger(Integers.PVP_TIME) - user.getPvPCooldown()));
            return;
        }

        user.changeFlyState();
        if (player.isFlying()) {
            user.sendMessage(Messages.FLY_ENABLED);
            return;
        }
        user.sendMessage(Messages.FLY_DISABLED);
    }

    @CommandAlias("%fly_debug")
    @Description("%fly_debug_description")
    @CommandPermission("%fly_debug_perm")
    public void onFlyDebug() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @CommandAlias("%feed")
    @Syntax("%feed_syntax")
    @CommandPermission("%feed_perm")
    @Description("%feed_description")
    public void onFeed(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setFoodLevel(20);
            EterniaServer.sendMessage(player, Messages.FEED_YOURSELF);
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission(EterniaServer.getString(Strings.PERM_FEED_OTHER))) {
                targetP.setFoodLevel(20);
                EterniaServer.sendMessage(player, Messages.FEED_RECEIVED, player.getName(), player.getDisplayName());
                EterniaServer.sendMessage(player, Messages.FEED_TARGET, targetP.getName(), targetP.getDisplayName());
            } else {
                EterniaServer.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        }
    }

    @CommandAlias("%condenser")
    @CommandPermission("%condenser_perm")
    @Description("%condenser_description")
    public void onBlocks(Player player) {
        int coal = 0;
        int lapiz = 0;
        int redstone = 0;
        int iron = 0;
        int gold = 0;
        int diamond = 0;
        int esmeralda = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null && i.getType() != Material.AIR) {
                coal += checkItems(i, coali);
                lapiz += checkItems(i, lapizi);
                redstone += checkItems(i, redstonei);
                iron += checkItems(i, ironi);
                gold += checkItems(i, goldi);
                diamond += checkItems(i, diamondi);
                esmeralda += checkItems(i, esmeraldai);
            }
        }
        convertItems(coal, Material.COAL, Material.COAL_BLOCK, player);
        convertItems(lapiz, Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, player);
        convertItems(redstone, Material.REDSTONE, Material.REDSTONE_BLOCK, player);
        convertItems(iron, Material.IRON_INGOT, Material.IRON_BLOCK, player);
        convertItems(gold, Material.GOLD_INGOT, Material.GOLD_BLOCK, player);
        convertItems(diamond, Material.DIAMOND, Material.DIAMOND_BLOCK, player);
        convertItems(esmeralda, Material.EMERALD, Material.EMERALD_BLOCK, player);
        EterniaServer.sendMessage(player, Messages.ITEM_CONDENSER);
    }

    @CommandAlias("%thor")
    @Syntax("%thor_syntax")
    @CommandCompletion("@players")
    @CommandPermission("%thor_perm")
    @Description("%thor_description")
    public void onThor(Player player, @Optional OnlinePlayer target) {
        final World world = player.getWorld();
        if (target != null) {
            final Player targetP = target.getPlayer();
            world.strikeLightning(targetP.getLocation());
            EterniaServer.sendMessage(targetP, Messages.LIGHTNING_RECEIVED, player.getName(), player.getDisplayName());
            EterniaServer.sendMessage(player, Messages.LIGHTNING_TARGET, targetP.getName(), targetP.getDisplayName());
        } else {
            EterniaServer.sendMessage(player, Messages.LIGHTNING_CURSOR);
            world.strikeLightning(player.getTargetBlock(null, 100).getLocation());
        }
    }

    @CommandAlias("%suicide")
    @Syntax("%suicide_syntax")
    @CommandPermission("%suicide_perm")
    @Description("%suicide_description")
    public void onSuicide(Player player, String message) {
        player.setHealth(0);
        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SUICIDE_BROADCAST, true, player.getName(), player.getDisplayName(), message));
    }

    @CommandAlias("%afk")
    @CommandPermission("%afk_perm")
    @Description("%afk_description")
    public void onAFK(Player player) {
        User user = new User(player);
        user.changeAfkState();

        if (user.isAfk()) {
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.AFK_ENTER, true, user.getName(), user.getDisplayName()));
            return;
        }
        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.AFK_LEAVE, true, user.getName(), user.getDisplayName()));
    }

    private int checkItems(ItemStack item1, ItemStack item2) {
        if (item1.isSimilar(item2) && item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())) return item1.getAmount();
        else return 0;
    }

    private void convertItems(int items, Material material, Material block, Player player) {
        int amount = items / 9;
        if (amount != 0) {
            player.getInventory().removeItem(new ItemStack(material, amount * 9));
            player.getInventory().addItem(new ItemStack(block, amount));
        }
    }

    private void sendProfile(Player player, Player targets) {
        User target = new User(targets);
        final long millis = target.getAndUpdateTimePlayed();
        String hms = APIServer.getColor(String.format("&3%02d&8:&3%02d&8:&3%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
        player.sendMessage(EterniaServer.getMessage(Messages.PROFILE_TITLE, false));
        for (String object : EterniaServer.getStringList(Lists.PROFILE_CUSTOM_MESSAGES)) {
            player.sendMessage(APIServer.getColor(APIServer.setPlaceholders(targets, object)));
        }
        player.sendMessage(EterniaServer.getMessage(Messages.PROFILE_REGISTER_DATA, false, sdf.format(new Date(target.getFirstLogin()))));
        player.sendMessage(EterniaServer.getMessage(Messages.PROFILE_LAST_LOGIN, false, sdf.format(new Date(target.getLastLogin()))));
        player.sendMessage(EterniaServer.getMessage(Messages.PROFILE_ACCOUNT_HOURS, false, hms));
        player.sendMessage(EterniaServer.getMessage(Messages.PROFILE_TITLE, false));
    }

    private void getModules(PlayerProfile playerProfile, ResultSet resultSet) throws SQLException {
        if (EterniaServer.getBoolean(Booleans.MODULE_CASH)) {
            playerProfile.setCash(resultSet.getInt("cash"));
        }
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            playerProfile.setBalance(resultSet.getDouble("balance"));
        }
        if (EterniaServer.getBoolean(Booleans.MODULE_EXPERIENCE)) {
            playerProfile.setXp(resultSet.getInt("xp"));
        }
        if (EterniaServer.getBoolean(Booleans.MODULE_HOMES)) {
            String result = resultSet.getString("homes");
            if (result != null) {
                for (String home : result.split(":")) {
                    if (!playerProfile.getHomes().contains(home)) {
                        playerProfile.getHomes().add(home);
                    }
                }
            }
        }
        if (EterniaServer.getBoolean(Booleans.MODULE_CHAT)) {
            playerProfile.setMuted(resultSet.getLong("muted"));
            playerProfile.setPlayerDisplayName(resultSet.getString("player_display"));
        }
    }

    private void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public void changeGameMode(User user) {
        user.changeGodModeState();
        if (user.getGodMode()) {
            user.sendMessage(Messages.GODMODE_ENABLED);
            return;
        }
        user.sendMessage(Messages.GODMODE_DISABLED);
    }

    public Location getCenter(Location loc) {
        return new Location(loc.getWorld(),
                getRelativeCoord(loc.getBlockX()),
                getRelativeCoord(loc.getBlockY()),
                getRelativeCoord(loc.getBlockZ()));
    }

    private double getRelativeCoord(int i) {
        double d = i;
        d = d < 0 ? d - .5 : d + .5;
        return d;
    }

}