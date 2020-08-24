package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.Connections;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Profile extends BaseCommand {

    private final GetRuntime getRuntime;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public Profile() {
        this.getRuntime = new GetRuntime();

        if (EterniaLib.getMySQL()) {
            EterniaLib.getConnections().executeSQLQuery(connection -> {
                final PreparedStatement getHashMap = connection.prepareStatement(Constants.getQuerySelectAll(Configs.tablePlayer));
                final ResultSet resultSet = getHashMap.executeQuery();
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
                getHashMap.close();
                resultSet.close();
            });
        } else {
            try (PreparedStatement getHashMap = Connections.getSQLite().prepareStatement(Constants.getQuerySelectAll(Configs.tablePlayer)); ResultSet resultSet = getHashMap.executeQuery()) {
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
                        playerProfile.homes = new ArrayList<>(Arrays.asList(resultSet.getString(Constants.HOMES_STR).split(":")));
                    }
                    if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
                        playerProfile.muted = resultSet.getLong(Constants.MUTED_STR);
                        playerProfile.playerDisplayName = resultSet.getString(Constants.PLAYER_DISPLAY_STR);
                    }
                    Vars.playerProfile.put(UUID.fromString(resultSet.getString(Constants.UUID_STR)), playerProfile);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        sendConsole(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Player Profiles").replace(Constants.AMOUNT, String.valueOf(Vars.playerProfile.size())));

        if (EterniaServer.serverConfig.getBoolean("modules.home")) {
            final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.tableLocations), Constants.NAME_STR, Constants.LOCATION_STR);
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
            final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.tableLocations), Constants.NAME_STR, Constants.LOCATION_STR);
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
            player.sendMessage(Strings.MSG_SPEED);
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

    private void sendProfile(Player player, Player target) {
        final UUID uuid = UUIDFetcher.getUUIDOf(target.getName());
        final long millis = Vars.playerProfile.get(uuid).updateTimePlayed();
        String hms = Strings.getColor(String.format("&3%02d&8:&3%02d&8:&3%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
        player.sendMessage(Strings.MSG_PROFILE_TITLE);
        for (String line : EterniaServer.msgConfig.getStringList("generic.profile.custom")) {
            player.sendMessage(Strings.getColor(InternMethods.setPlaceholders(target, line)));
        }
        player.sendMessage(Strings.MSG_PROFILE_REGISTER.replace(Constants.PLAYER_DATA, sdf.format(new Date(Vars.playerProfile.get(uuid).firstLogin))));
        player.sendMessage(Strings.MSG_PROFILE_LAST.replace(Constants.PLAYER_LAST, sdf.format(new Date(Vars.playerProfile.get(uuid).lastLogin))));
        player.sendMessage(Strings.MSG_PROFILE_HOURS.replace(Constants.HOURS, hms));
        player.sendMessage(Strings.MSG_PROFILE_TITLE);
    }

    @CommandAlias("mem|memory")
    @CommandPermission("eternia.mem")
    public void onMem(CommandSender player) {
        getRuntime.recalculateRuntime();
        player.sendMessage(Strings.MSG_MEM.replace(Constants.MEM_USE, String.valueOf(getRuntime.freemem)).replace(Constants.MEM_MAX, String.valueOf(getRuntime.totalmem)));
        player.sendMessage(Strings.MSG_MEM_ONLINE.replace(Constants.HOURS, String.valueOf(getRuntime.hours)).replace(Constants.MINUTE, String.valueOf(getRuntime.minutes)).replace(Constants.SECONDS, String.valueOf(getRuntime.seconds)));
    }

    @CommandAlias("memall|memoryall")
    @CommandPermission("eternia.mem.all")
    public void onMemAll() {
        getRuntime.recalculateRuntime();
        sendConsole(Strings.MSG_MEM.replace(Constants.MEM_USE, String.valueOf(getRuntime.freemem)).replace(Constants.MEM_MAX, String.valueOf(getRuntime.totalmem)));
        sendConsole(Strings.MSG_MEM_ONLINE.replace(Constants.HOURS, String.valueOf(getRuntime.hours)).replace(Constants.MINUTE, String.valueOf(getRuntime.minutes)).replace(Constants.SECONDS, String.valueOf(getRuntime.seconds)));
    }

    private void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

}

class GetRuntime {

    long freemem;
    long totalmem;
    int seconds;
    int minutes;
    int hours;

    public void recalculateRuntime() {
        Runtime runtime = Runtime.getRuntime();
        long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();
        totalmem = runtime.totalMemory() / 1048576;
        freemem = totalmem - (runtime.freeMemory() / 1048576);
        seconds = (int) (milliseconds / 1000) % 60;
        minutes = (int) ((milliseconds / (1000*60)) % 60);
        hours   = (int) ((milliseconds / (1000*60*60)) % 24);
    }


}
