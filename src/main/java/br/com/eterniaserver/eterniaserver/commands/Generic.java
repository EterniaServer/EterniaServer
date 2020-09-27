package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.Connections;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.generics.*;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Generic extends BaseCommand {

    private final EterniaServer plugin;
    private final UtilGetRuntime getRuntime;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final ItemStack coali = new ItemStack(Material.COAL);
    private final ItemStack lapizi = new ItemStack(Material.LAPIS_LAZULI);
    private final ItemStack redstonei = new ItemStack(Material.REDSTONE);
    private final ItemStack ironi = new ItemStack(Material.IRON_INGOT);
    private final ItemStack goldi = new ItemStack(Material.GOLD_INGOT);
    private final ItemStack diamondi = new ItemStack(Material.DIAMOND);
    private final ItemStack esmeraldai = new ItemStack(Material.EMERALD);

    public Generic(EterniaServer plugin) {
        this.plugin = plugin;

        this.getRuntime = new UtilGetRuntime();

        if (EterniaLib.getMySQL()) {
            EterniaLib.getConnections().executeSQLQuery(connection -> {
                final PreparedStatement getHashMap = connection.prepareStatement(PluginConstants.getQuerySelectAll(EterniaServer.configs.tablePlayer));
                final ResultSet resultSet = getHashMap.executeQuery();
                getPlayersProfiles(resultSet);
                getHashMap.close();
                resultSet.close();
            });
        } else {
            try (PreparedStatement getHashMap = Connections.getSQLite().prepareStatement(PluginConstants.getQuerySelectAll(EterniaServer.configs.tablePlayer)); ResultSet resultSet = getHashMap.executeQuery()) {
                getPlayersProfiles(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        sendConsole(PluginMSGs.MSG_LOAD_DATA.replace(PluginConstants.MODULE, "Player Profiles").replace(PluginConstants.AMOUNT, String.valueOf(APIServer.getProfileMapSize())));

        if (EterniaServer.configs.moduleHomes || EterniaServer.configs.moduleTeleports) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
                final Map<String, String> temp = EQueries.getMapString(PluginConstants.getQuerySelectAll(EterniaServer.configs.tableLocations), PluginConstants.NAME_STR, PluginConstants.LOCATION_STR);
                temp.forEach((k, v) -> {
                    final String[] split = v.split(":");
                    Location loc = new Location(Bukkit.getWorld(split[0]),
                            Double.parseDouble(split[1]),
                            (Double.parseDouble(split[2]) + 1),
                            Double.parseDouble(split[3]),
                            Float.parseFloat(split[4]),
                            Float.parseFloat(split[5]));
                    APIServer.putLocation(k, loc);
                });
            });
        }
    }

    @CommandAlias("speed")
    @CommandPermission("eternia.speed")
    public void onSpeed(Player player, Integer speed) {
        if (speed > 0 && speed < 11) {
            player.setFlySpeed((float) speed / 10);
            player.setWalkSpeed((float) speed / 10);
        } else {
            player.sendMessage(PluginMSGs.MSG_SPEED);
        }
    }

    @CommandAlias("profile|perfil")
    @CommandPermission("eternia.profile")
    @CommandCompletion("@players")
    public void onProfile(Player player, @Optional OnlinePlayer onlinePlayer) {
        if (onlinePlayer != null) {
            sendProfile(player, onlinePlayer.getPlayer());
        } else {
            sendProfile(player, player);
        }
    }

    @CommandAlias("mem|memory")
    @CommandPermission("eternia.mem")
    public void onMem(CommandSender player) {
        getRuntime.recalculateRuntime();
        player.sendMessage(PluginMSGs.MSG_MEM.replace(PluginConstants.MEM_USE, String.valueOf(getRuntime.getFreemem())).replace(PluginConstants.MEM_MAX, String.valueOf(getRuntime.getTotalmem())));
        player.sendMessage(PluginMSGs.MSG_MEM_ONLINE.replace(PluginConstants.HOURS, String.valueOf(getRuntime.getHours())).replace(PluginConstants.MINUTE, String.valueOf(getRuntime.getMinutes())).replace(PluginConstants.SECONDS, String.valueOf(getRuntime.getSeconds())));
    }

    @CommandAlias("memall|memoryall")
    @CommandPermission("eternia.mem.all")
    public void onMemAll() {
        getRuntime.recalculateRuntime();
        sendConsole(PluginMSGs.MSG_MEM.replace(PluginConstants.MEM_USE, String.valueOf(getRuntime.getFreemem())).replace(PluginConstants.MEM_MAX, String.valueOf(getRuntime.getTotalmem())));
        sendConsole(PluginMSGs.MSG_MEM_ONLINE.replace(PluginConstants.HOURS, String.valueOf(getRuntime.getHours())).replace(PluginConstants.MINUTE, String.valueOf(getRuntime.getMinutes())).replace(PluginConstants.SECONDS, String.valueOf(getRuntime.getSeconds())));
    }

    @CommandAlias("reloadeternia|eterniareload")
    @CommandPermission("eternia.reload")
    public void onReload(CommandSender sender) {
        sender.sendMessage(PluginMSGs.MSG_RELOAD_START);
        plugin.getFiles().loadMessages();
        plugin.getFiles().loadBlocksRewards();
        plugin.getFiles().loadCommands();
        plugin.getFiles().loadChat();
        plugin.getFiles().loadKits();
        plugin.getFiles().loadRewards();
        plugin.getFiles().loadDatabase();
        sender.sendMessage(PluginMSGs.MSG_RELOAD_FINISH);
    }

    @CommandAlias("god")
    @Syntax("<jogador>")
    @CommandPermission("eternia.god")
    public void onGod(Player player, @Optional OnlinePlayer target) {
        if (target != null) {
            changeGod(target.getPlayer());
        } else {
            changeGod(player);
        }
    }

    @CommandAlias("fly")
    @CommandPermission("eternia.fly")
    public void onFly(Player player, @Optional OnlinePlayer targetS) {
        final String worldName = player.getWorld().getName();

        if ((worldName.equals("evento") || worldName.equals("world_evento")) && !player.hasPermission("eternia.fly.evento")) {
            EterniaServer.configs.sendMessage(player, Messages.SERVER_NO_PERM);
            return;
        }

        if (targetS != null && player.hasPermission("eternia.fly.others")) {

            final Player target = targetS.getPlayer();
            final UUID uuid = UUIDFetcher.getUUIDOf(target.getName());

            if (APIPlayer.isOnPvP(uuid)) {
                player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.FLY_TARGET_IN_PVP.replace(PluginConstants.AMOUNT, String.valueOf(EterniaServer.configs.pvpTime - APIPlayer.getPvPCooldown(uuid)))));
                return;
            }

            APIPlayer.changeFlyState(target);
            if (target.isFlying()) {
                target.sendMessage(UtilInternMethods.putName(player, PluginMSGs.FLY_ENABLED_BY));
                player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.FLY_ENABLED_FOR));
                return;
            }
            target.sendMessage(UtilInternMethods.putName(player, PluginMSGs.FLY_DISABLED_BY));
            player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.FLY_DISABLED_FOR));
            return;
        }

        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (APIPlayer.isOnPvP(uuid)) {
            player.sendMessage(PluginMSGs.FLY_IN_PVP.replace(PluginConstants.AMOUNT, String.valueOf(EterniaServer.configs.pvpTime - APIPlayer.getPvPCooldown(uuid))));
            return;
        }

        APIPlayer.changeFlyState(player);
        if (player.isFlying()) {
            player.sendMessage(PluginMSGs.FLY_ENABLED);
            return;
        }
        player.sendMessage(PluginMSGs.FLY_DISABLED);
    }

    @CommandAlias("flydebug")
    @CommandPermission("eternia.admin")
    public void onFlyDebug() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @CommandAlias("feed|saciar")
    @Syntax("<jogador>")
    @CommandPermission("eternia.feed")
    public void onFeed(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setFoodLevel(20);
            player.sendMessage(PluginMSGs.MSG_FEEDED);
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission("eternia.feed.other")) {
                targetP.setFoodLevel(20);
                player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.MSG_FEEDED_TARGET));
                player.sendMessage(PluginMSGs.MSG_FEEDED_TARGET.replace(PluginConstants.TARGET, targetP.getDisplayName()));
                targetP.sendMessage(PluginMSGs.MSG_FEEDED);
            } else {
                EterniaServer.configs.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        }
    }

    @CommandAlias("blocks|condenser")
    @CommandPermission("eternia.blocks")
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
        player.sendMessage(PluginMSGs.MSG_CONDENSER);
    }

    @CommandAlias("rain|chuva")
    @CommandPermission("eternia.rain")
    public void onRain(Player player) {
        player.getWorld().setStorm(true);
        player.sendMessage(PluginMSGs.MSG_WEATHER);
    }

    @CommandAlias("sun|sol")
    @CommandPermission("eternia.sun")
    public void onSun(Player player) {
        player.getWorld().setStorm(false);
        player.sendMessage(PluginMSGs.MSG_WEATHER);
    }

    @CommandAlias("thor|lightning")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.thor")
    public void onThor(Player player, @Optional OnlinePlayer target) {
        final World world = player.getWorld();
        if (target != null) {
            final Player targetP = target.getPlayer();
            world.strikeLightning(targetP.getLocation());
            player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.MSG_LIGHTNING_SENT));
            targetP.sendMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_LIGHTNING_RECEIVED));
        } else {
            world.strikeLightning(player.getTargetBlock(null, 100).getLocation());
        }
    }

    @CommandAlias("suicide|suicidio")
    @Syntax("<mensagem>")
    @CommandPermission("eternia.suicide")
    public void onSuicide(Player player, String[] args) {
        if (args.length >= 1) {
            StringBuilder sb = new StringBuilder();
            for (java.lang.String arg : args) sb.append(arg).append(" ");
            player.setHealth(0);
            Bukkit.broadcastMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_SUICIDE.replace(PluginConstants.MESSAGE, sb.toString())));
        } else {
            player.setHealth(0);
        }
    }

    @CommandAlias("afk")
    @CommandPermission("eternia.afk")
    public void onAFK(Player player) {
        final String playerName = player.getName();
        if (APIPlayer.isAFK(playerName)) {
            Bukkit.broadcastMessage(EterniaServer.configs.getMessage(Messages.AFK_LEAVE, true, playerName, player.getDisplayName()));
            APIPlayer.removeAfk(playerName);
        } else {
            APIPlayer.putInAfk(player);
            Bukkit.broadcastMessage(EterniaServer.configs.getMessage(Messages.AFK_ENTER, true, playerName, player.getDisplayName()));
        }
    }

    private void changeGod(final Player player) {
        final String playerName = player.getName();
        if (APIPlayer.isGod(playerName)) {
            player.sendMessage(PluginMSGs.MSG_GOD_DISABLE);
            APIPlayer.removeGod(playerName);
        } else {
            player.sendMessage(PluginMSGs.MSG_GOD_ENABLE);
            APIPlayer.putGod(playerName);
        }
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

    private void sendProfile(Player player, Player target) {
        final UUID uuid = UUIDFetcher.getUUIDOf(target.getName());
        final long millis = APIPlayer.getAndUpdateTimePlayed(uuid);
        String hms = PluginMSGs.getColor(String.format("&3%02d&8:&3%02d&8:&3%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
        player.sendMessage(PluginMSGs.MSG_PROFILE_TITLE);
        for (String line : EterniaServer.msgConfig.getStringList("generic.profile.custom")) {
            player.sendMessage(PluginMSGs.getColor(UtilInternMethods.setPlaceholders(target, line)));
        }
        player.sendMessage(PluginMSGs.MSG_PROFILE_REGISTER.replace(PluginConstants.PLAYER_DATA, sdf.format(new Date(APIPlayer.getFirstLoginLong(uuid)))));
        player.sendMessage(PluginMSGs.MSG_PROFILE_LAST.replace(PluginConstants.PLAYER_LAST, sdf.format(new Date(APIPlayer.getLastLogin(uuid)))));
        player.sendMessage(PluginMSGs.MSG_PROFILE_HOURS.replace(PluginConstants.HOURS, hms));
        player.sendMessage(PluginMSGs.MSG_PROFILE_TITLE);
    }

    private void getPlayersProfiles(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            final PlayerProfile playerProfile = new PlayerProfile(
                    resultSet.getString(PluginConstants.PLAYER_NAME_STR),
                    resultSet.getLong(PluginConstants.TIME_STR),
                    resultSet.getLong(PluginConstants.LAST_STR),
                    resultSet.getLong(PluginConstants.HOURS_STR)
            );
            if (EterniaServer.configs.moduleCash) {
                playerProfile.setCash(resultSet.getInt(PluginConstants.CASH_STR));
            }
            if (EterniaServer.configs.moduleEconomy) {
                playerProfile.setBalance(resultSet.getDouble(PluginConstants.BALANCE_STR));
            }
            if (EterniaServer.configs.moduleExperience) {
                playerProfile.setXp(resultSet.getInt(PluginConstants.XP_STR));
            }
            if (EterniaServer.configs.moduleHomes) {
                String result = resultSet.getString(PluginConstants.HOMES_STR);
                if (result != null) {
                    for (String home : result.split(":")) {
                        if (!playerProfile.getHomes().contains(home)) {
                            playerProfile.getHomes().add(home);
                        }
                    }
                }
            }
            if (EterniaServer.configs.moduleChat) {
                playerProfile.setMuted(resultSet.getLong(PluginConstants.MUTED_STR));
                playerProfile.setPlayerDisplayName(resultSet.getString(PluginConstants.PLAYER_DISPLAY_STR));
            }
            APIServer.putProfile(UUID.fromString(resultSet.getString(PluginConstants.UUID_STR)), playerProfile);
        }
    }

    private void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
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