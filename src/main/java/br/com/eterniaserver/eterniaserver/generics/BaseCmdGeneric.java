package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.NBTItem;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.Connections;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.MSG;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BaseCmdGeneric extends BaseCommand {

    //TODO Refatorar o sistema em várias classes e subclasses extendendo uma após a outra.

    private final EterniaServer plugin;

    private final GetRuntime getRuntime;
    private final Scoreboard sc;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final ItemStack coali = new ItemStack(Material.COAL);
    private final ItemStack lapizi = new ItemStack(Material.LAPIS_LAZULI);
    private final ItemStack redstonei = new ItemStack(Material.REDSTONE);
    private final ItemStack ironi = new ItemStack(Material.IRON_INGOT);
    private final ItemStack goldi = new ItemStack(Material.GOLD_INGOT);
    private final ItemStack diamondi = new ItemStack(Material.DIAMOND);
    private final ItemStack esmeraldai = new ItemStack(Material.EMERALD);

    public BaseCmdGeneric(EterniaServer plugin) {
        this.plugin = plugin;

        this.sc = Bukkit.getScoreboardManager().getMainScoreboard();
        for (int i = 0; i < 16; i++) {
            if (sc.getTeam(Vars.arrData.get(i)) == null) {
                sc.registerNewTeam(Vars.arrData.get(i)).setColor(Vars.colors.get(i));
            }
        }

        this.getRuntime = new GetRuntime();

        if (EterniaLib.getMySQL()) {
            EterniaLib.getConnections().executeSQLQuery(connection -> {
                final PreparedStatement getHashMap = connection.prepareStatement(Constants.getQuerySelectAll(Configs.TABLE_PLAYER));
                final ResultSet resultSet = getHashMap.executeQuery();
                getPlayersProfiles(resultSet);
                getHashMap.close();
                resultSet.close();
            });
        } else {
            try (PreparedStatement getHashMap = Connections.getSQLite().prepareStatement(Constants.getQuerySelectAll(Configs.TABLE_PLAYER)); ResultSet resultSet = getHashMap.executeQuery()) {
                getPlayersProfiles(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        sendConsole(MSG.MSG_LOAD_DATA.replace(Constants.MODULE, "Player Profiles").replace(Constants.AMOUNT, String.valueOf(Vars.playerProfile.size())));

        if (EterniaServer.serverConfig.getBoolean("modules.home")) {
            final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.TABLE_LOCATIONS), Constants.NAME_STR, Constants.LOCATION_STR);
            temp.forEach((k, v) -> {
                final String[] split = v.split(":");
                final Location loc = new Location(Bukkit.getWorld(split[0]),
                        Double.parseDouble(split[1]),
                        (Double.parseDouble(split[2]) + 1),
                        Double.parseDouble(split[3]),
                        Float.parseFloat(split[4]),
                        Float.parseFloat(split[5]));
                Vars.locations.put(k, loc);
            });
        }

        if (EterniaServer.serverConfig.getBoolean("modules.teleports")) {
            final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.TABLE_LOCATIONS), Constants.NAME_STR, Constants.LOCATION_STR);
            temp.forEach((k, v) -> {
                final String[] split = v.split(":");
                final Location loc = new Location(Bukkit.getWorld(split[0]),
                        Double.parseDouble(split[1]),
                        (Double.parseDouble(split[2]) + 1),
                        Double.parseDouble(split[3]),
                        Float.parseFloat(split[4]),
                        Float.parseFloat(split[5]));
                Vars.locations.put(k, loc);
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
            player.sendMessage(MSG.MSG_SPEED);
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
        player.sendMessage(MSG.MSG_MEM.replace(Constants.MEM_USE, String.valueOf(getRuntime.freemem)).replace(Constants.MEM_MAX, String.valueOf(getRuntime.totalmem)));
        player.sendMessage(MSG.MSG_MEM_ONLINE.replace(Constants.HOURS, String.valueOf(getRuntime.hours)).replace(Constants.MINUTE, String.valueOf(getRuntime.minutes)).replace(Constants.SECONDS, String.valueOf(getRuntime.seconds)));
    }

    @CommandAlias("memall|memoryall")
    @CommandPermission("eternia.mem.all")
    public void onMemAll() {
        getRuntime.recalculateRuntime();
        sendConsole(MSG.MSG_MEM.replace(Constants.MEM_USE, String.valueOf(getRuntime.freemem)).replace(Constants.MEM_MAX, String.valueOf(getRuntime.totalmem)));
        sendConsole(MSG.MSG_MEM_ONLINE.replace(Constants.HOURS, String.valueOf(getRuntime.hours)).replace(Constants.MINUTE, String.valueOf(getRuntime.minutes)).replace(Constants.SECONDS, String.valueOf(getRuntime.seconds)));
    }

    @CommandAlias("glow")
    @CommandPermission("eternia.glow")
    public void onGlow(Player player) {
        if (!player.isGlowing()) {
            player.sendMessage(MSG.M_GLOW_ENABLED);
        } else {
            player.removePotionEffect(PotionEffectType.GLOWING);
            player.sendMessage(MSG.M_GLOW_DISABLED);
        }
        player.setGlowing(!player.isGlowing());
    }

    @CommandAlias("glow color")
    @CommandPermission("eternia.glow")
    @CommandCompletion("@colors")
    public void onGlowColor(Player player, String color) {
        final String dark = "escuro";
        final String light = "claro";
        switch (color.hashCode()) {
            case 1741606617:
                changeColor(player, Vars.arrData.get(8), "&8", "cinza " + dark);
                break;
            case 1741452496:
                changeColor(player, Vars.arrData.get(1), "&1", "azul " + dark);
                break;
            case 1741427506:
                changeColor(player, Vars.arrData.get(3), "&3", "ciano");
                break;
            case 1441664347:
                changeColor(player, Vars.arrData.get(4), "&4", "vermelho");
                break;
            case 686244985:
                changeColor(player, Vars.arrData.get(7), "&7", "cinza " + light);
                break;
            case 93818879:
                changeColor(player, Vars.arrData.get(0), "&0", "preto");
                break;
            case 98619139:
                changeColor(player, Vars.arrData.get(10), "&a", "verde");
                break;
            case 3178592:
                changeColor(player, Vars.arrData.get(6), "&6", "dourado");
                break;
            case 3027034:
                changeColor(player, Vars.arrData.get(9), "&9", "azul");
                break;
            case 3002044:
                changeColor(player, Vars.arrData.get(11), "&b", "azul " + light);
                break;
            case 112785:
                changeColor(player, Vars.arrData.get(12), "&c", "tomate");
                break;
            case -734239628:
                changeColor(player, Vars.arrData.get(14), "&e", "amarelo");
                break;
            case -976943172:
                changeColor(player, Vars.arrData.get(13), "&d", "rosa");
                break;
            case -1092352334:
                changeColor(player, Vars.arrData.get(5), "&5", "roxo");
                break;
            case -1844766387:
                changeColor(player, Vars.arrData.get(2), "&2", "verde " + dark);
                break;
            default:
                changeColor(player, Vars.arrData.get(15), "&f", "branco");
                break;
        }
    }

    @CommandAlias("reloadeternia|eterniareload")
    @CommandPermission("eternia.reload")
    public void onReload(CommandSender sender) {
        sender.sendMessage(MSG.MSG_RELOAD_START);
        plugin.getFiles().loadConfigs();
        plugin.getFiles().loadMessages();
        plugin.getFiles().loadBlocksRewards();
        plugin.getFiles().loadCommands();
        plugin.getFiles().loadChat();
        plugin.getFiles().loadKits();
        plugin.getFiles().loadRewards();
        plugin.getFiles().loadDatabase();
        sender.sendMessage(MSG.MSG_RELOAD_FINISH);
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
            player.sendMessage(MSG.MSG_NO_PERM);
            return;
        }

        if (targetS != null) {

            final Player target = targetS.getPlayer();
            final UUID uuid = UUIDFetcher.getUUIDOf(target.getName());

            if (APIFly.isOnPvP(uuid)) {
                player.sendMessage(InternMethods.putName(target, MSG.FLY_TARGET_IN_PVP.replace(Constants.AMOUNT, String.valueOf(EterniaServer.serverConfig.getInt("server.pvp-time") - APIFly.getPvPCooldown(uuid)))));
                return;
            }

            APIFly.changeFlyState(target);
            if (target.isFlying()) {
                target.sendMessage(InternMethods.putName(player, MSG.FLY_ENABLED_BY));
                player.sendMessage(InternMethods.putName(target, MSG.FLY_ENABLED_FOR));
                return;
            }
            target.sendMessage(InternMethods.putName(player, MSG.FLY_DISABLED_BY));
            player.sendMessage(InternMethods.putName(target, MSG.FLY_DISABLED_FOR));
            return;
        }

        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (APIFly.isOnPvP(uuid)) {
            player.sendMessage(MSG.FLY_IN_PVP.replace(Constants.AMOUNT, String.valueOf(EterniaServer.serverConfig.getInt("server.pvp-time") - APIFly.getPvPCooldown(uuid))));
            return;
        }

        APIFly.changeFlyState(player);
        if (player.isFlying()) {
            player.sendMessage(MSG.FLY_ENABLED);
            return;
        }
        player.sendMessage(MSG.FLY_DISABLED);
    }

    @CommandAlias("feed|saciar")
    @Syntax("<jogador>")
    @CommandPermission("eternia.feed")
    public void onFeed(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setFoodLevel(20);
            player.sendMessage(MSG.MSG_FEEDED);
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission("eternia.feed.other")) {
                targetP.setFoodLevel(20);
                player.sendMessage(InternMethods.putName(targetP, MSG.MSG_FEEDED_TARGET));
                player.sendMessage(MSG.MSG_FEEDED_TARGET.replace(Constants.TARGET, targetP.getDisplayName()));
                targetP.sendMessage(MSG.MSG_FEEDED);
            } else {
                player.sendMessage(MSG.MSG_NO_PERM);
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
            if (i.getItemMeta() != null && i.getType() != Material.AIR) {
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
        player.sendMessage(MSG.MSG_CONDENSER);
    }

    @CommandAlias("rain|chuva")
    @CommandPermission("eternia.rain")
    public void onRain(Player player) {
        player.getWorld().setStorm(true);
        player.sendMessage(MSG.MSG_WEATHER);
    }

    @CommandAlias("sun|sol")
    @CommandPermission("eternia.sun")
    public void onSun(Player player) {
        player.getWorld().setStorm(false);
        player.sendMessage(MSG.MSG_WEATHER);
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
            player.sendMessage(InternMethods.putName(targetP, MSG.MSG_LIGHTNING_SENT));
            targetP.sendMessage(InternMethods.putName(player, MSG.MSG_LIGHTNING_RECEIVED));
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
            Bukkit.broadcastMessage(InternMethods.putName(player, MSG.MSG_SUICIDE.replace(Constants.MESSAGE, sb.toString())));
        } else {
            player.setHealth(0);
        }
    }

    @CommandAlias("afk")
    @CommandPermission("eternia.afk")
    public void onAFK(Player player) {
        final String playerName = player.getName();
        if (Vars.afk.contains(playerName)) {
            Bukkit.broadcastMessage(InternMethods.putName(player, MSG.MSG_AFK_DISABLE));
            Vars.afk.remove(playerName);
        } else {
            Vars.afk.add(playerName);
            Bukkit.broadcastMessage(InternMethods.putName(player, MSG.MSG_AFK_ENABLE));
        }
    }

    @CommandAlias("item")
    @CommandPermission("eternia.item")
    public void itemHelp(CommandSender sender) {
        sender.sendMessage(MSG.ITEM_HELP_TITLE);
        if (sender.hasPermission("eternia.item.nbt")) {
            sender.sendMessage(MSG.getColor(MSG.HELP_FORMAT
                    .replace(Constants.COMMANDS, "item addkey &3<chave> <valor>")
                    .replace(Constants.MESSAGE, MSG.ITEM_HELP_ADDKEY)));
        }
        sender.sendMessage(MSG.getColor(MSG.HELP_FORMAT
                .replace(Constants.COMMANDS, "item clear lore")
                .replace(Constants.MESSAGE, MSG.ITEM_HELP_CLEAR_LORE)));
        sender.sendMessage(MSG.getColor(MSG.HELP_FORMAT
                .replace(Constants.COMMANDS, "item clear name")
                .replace(Constants.MESSAGE, MSG.ITEM_HELP_CLEAR_NAME)));
        sender.sendMessage(MSG.getColor(MSG.HELP_FORMAT
                .replace(Constants.COMMANDS, "item add lore")
                .replace(Constants.MESSAGE, MSG.ITEM_HELP_ADD_LORE)));
        sender.sendMessage(MSG.getColor(MSG.HELP_FORMAT
                .replace(Constants.COMMANDS, "item set lore")
                .replace(Constants.MESSAGE, MSG.ITEM_HELP_SET_LORE)));
        sender.sendMessage(MSG.getColor(MSG.HELP_FORMAT
                .replace(Constants.COMMANDS, "item set name")
                .replace(Constants.MESSAGE, MSG.ITEM_HELP_SET_NAME)));
    }

    @CommandAlias("item addkey")
    @Syntax("<chave> <valor>")
    @CommandPermission("eternia.item.nbt")
    public void onItemAddKey(Player player, String key, String value) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() != null && item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setString(key, value);
            player.sendMessage(MSG.ITEM_ADDKEY.replace(Constants.KEY, key).replace(Constants.VALUE, value));
        } else {
            player.sendMessage(MSG.ITEM_NO);
        }
    }

    @CommandAlias("item clear lore")
    @CommandPermission("eternia.item")
    public void onItemClearLore(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() != null && item.getType() != Material.AIR) {
            item.getLore().clear();
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(MSG.ITEM_LORE_CLEAR);
        } else {
            player.sendMessage(MSG.ITEM_NO);
        }
    }

    @CommandAlias("item clear name")
    @CommandPermission("eternia.item")
    public void onItemClearName(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() != null && item.getType() != Material.AIR) {
            item.getItemMeta().setDisplayName(item.getI18NDisplayName());
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(MSG.ITEM_NAME_CLEAR);
        } else {
            player.sendMessage(MSG.ITEM_NO);
        }
    }

    @CommandAlias("item add lore")
    @Syntax("<lore>")
    @CommandCompletion("<lore>")
    @CommandPermission("eternia.item")
    public void onItemAddLore(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() != null && item.getType() != Material.AIR) {
            name = MSG.getColor(name);
            List<String> lore = item.getLore();
            if (lore != null) {
                lore.add(name);
                item.setLore(lore);
            } else {
                item.setLore(List.of(name));
            }
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(MSG.ITEM_LORE_ADD.replace("%name%", name));
        } else {
            player.sendMessage(MSG.ITEM_NO);
        }
    }

    @CommandAlias("item set lore")
    @Syntax("<lore>")
    @CommandCompletion("<lore>")
    @CommandPermission("eternia.item")
    public void onItemSetLore(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() != null && item.getType() != Material.AIR) {
            name = MSG.getColor(name);
            item.setLore(List.of(name));
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(MSG.ITEM_LORE_SET.replace("%name%", name));
        } else {
            player.sendMessage(MSG.ITEM_NO);
        }
    }

    @CommandAlias("item set name")
    @Syntax("<nome>")
    @CommandCompletion("<nome>")
    @CommandPermission("eternia.item")
    public void onItemSetName(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() != null && item.getType() != Material.AIR) {
            name = MSG.getColor(name);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(MSG.ITEM_NAME_SET.replace("%name%", name));
        } else {
            player.sendMessage(MSG.ITEM_NO);
        }
    }

    private void changeGod(final Player player) {
        final String playerName = player.getName();
        if (Vars.god.contains(playerName)) {
            player.sendMessage(MSG.MSG_GOD_DISABLE);
            Vars.god.remove(playerName);
        } else {
            player.sendMessage(MSG.MSG_GOD_ENABLE);
            Vars.god.add(playerName);
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

    public void changeColor(final Player player, final String team, final String nameColor, final String color) {
        final String playerName = player.getName();
        Vars.glowingColor.put(playerName, nameColor);
        sc.getTeam(team).addEntry(playerName);
        player.sendMessage(MSG.M_GLOW_COLOR.replace(Constants.AMOUNT, color));
    }

    private void sendProfile(Player player, Player target) {
        final UUID uuid = UUIDFetcher.getUUIDOf(target.getName());
        final long millis = Vars.playerProfile.get(uuid).updateTimePlayed();
        String hms = MSG.getColor(String.format("&3%02d&8:&3%02d&8:&3%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
        player.sendMessage(MSG.MSG_PROFILE_TITLE);
        for (String line : EterniaServer.msgConfig.getStringList("generic.profile.custom")) {
            player.sendMessage(MSG.getColor(InternMethods.setPlaceholders(target, line)));
        }
        player.sendMessage(MSG.MSG_PROFILE_REGISTER.replace(Constants.PLAYER_DATA, sdf.format(new Date(Vars.playerProfile.get(uuid).firstLogin))));
        player.sendMessage(MSG.MSG_PROFILE_LAST.replace(Constants.PLAYER_LAST, sdf.format(new Date(Vars.playerProfile.get(uuid).lastLogin))));
        player.sendMessage(MSG.MSG_PROFILE_HOURS.replace(Constants.HOURS, hms));
        player.sendMessage(MSG.MSG_PROFILE_TITLE);
    }

    private void getPlayersProfiles(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            final PlayerProfile playerProfile = new PlayerProfile(
                    resultSet.getString(Constants.PLAYER_NAME_STR),
                    resultSet.getLong(Constants.TIME_STR),
                    resultSet.getLong(Constants.LAST_STR),
                    resultSet.getLong(Constants.HOURS_STR)
            );
            if (EterniaServer.serverConfig.getBoolean("modules.cash")) {
                playerProfile.cash = resultSet.getInt(Constants.CASH_STR);
            }
            if (EterniaServer.serverConfig.getBoolean("modules.economy")) {
                playerProfile.balance = resultSet.getDouble(Constants.BALANCE_STR);
            }
            if (EterniaServer.serverConfig.getBoolean("modules.experience")) {
                playerProfile.xp = resultSet.getInt(Constants.XP_STR);
            }
            if (EterniaServer.serverConfig.getBoolean("modules.home")) {
                String result = resultSet.getString(Constants.HOMES_STR);
                if (result != null) {
                    playerProfile.homes = new ArrayList<>(Arrays.asList(result.split(":")));
                }
            }
            if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
                playerProfile.muted = resultSet.getLong(Constants.MUTED_STR);
                playerProfile.playerDisplayName = resultSet.getString(Constants.PLAYER_DISPLAY_STR);
            }
            Vars.playerProfile.put(UUID.fromString(resultSet.getString(Constants.UUID_STR)), playerProfile);
        }
    }

    private void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

}