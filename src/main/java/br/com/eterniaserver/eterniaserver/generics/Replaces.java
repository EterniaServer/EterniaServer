package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import br.com.eterniaserver.eterniaserver.objects.UUIDFetcher;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Replaces extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final GetRuntime getRuntime;

    public Replaces(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.getRuntime = new GetRuntime();

        HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_PLAYER), Strings.UUID, Strings.TIME);
        temp.forEach((k, v) -> Vars.playerLogin.put(UUID.fromString(k), Long.parseLong(v)));
        int size = temp.size();
        messages.sendConsole(Strings.MSG_LOAD_DATA, Constants.MODULE, "Register", Constants.AMOUNT, size);

        temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_PLAYER), Strings.UUID, Strings.LAST);
        temp.forEach((k, v) -> Vars.playerLast.put(UUID.fromString(k), Long.parseLong(v)));
        messages.sendConsole(Strings.MSG_LOAD_DATA, Constants.MODULE, "Last Login", Constants.AMOUNT, size);

        temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_PLAYER), Strings.UUID, Strings.HOURS);
        temp.forEach((k, v) -> Vars.playerHours.put(UUID.fromString(k), Integer.parseInt(v)));
        messages.sendConsole(Strings.MSG_LOAD_DATA, Constants.MODULE, "Hours", Constants.AMOUNT, size);

    }

    @CommandAlias("speed")
    @CommandPermission("eternia.speed")
    public void onSpeed(Player player, Integer speed) {
        if (speed > 0 && speed < 11) {
            player.setFlySpeed((float) speed / 10);
            player.setWalkSpeed((float) speed / 10);
        } else {
            messages.sendMessage(Strings.MSG_SPEED, player);
        }
    }

    @CommandAlias("profile|perfil")
    @CommandPermission("eternia.profile")
    public void onProfile(Player player) {
        messages.sendMessage(Strings.MSG_PROFILE_REGISTER, Constants.PLAYER_DATA, plugin.sdf.format(new Date(Vars.playerLogin.get(UUIDFetcher.getUUIDOf(player.getName())))), player);
        for (String line : EterniaServer.msgConfig.getStringList("generic.profile.custom")) {
            player.sendMessage(messages.getColor(putPAPI(player, line)));
        }
    }

    @CommandAlias("mem|memory")
    @CommandPermission("eternia.mem")
    public void onMem(CommandSender player) {
        getRuntime.recalculateRuntime();
        messages.sendMessage(Strings.MSG_MEM, Constants.MEM_USE, getRuntime.freemem, Constants.MEM_MAX, getRuntime.totalmem, player);
        messages.sendMessage(Strings.MSG_MEM_ONLINE, Constants.HOURS, getRuntime.hours, Constants.MINUTE, getRuntime.minutes, Constants.SECONDS, getRuntime.seconds, player);
    }

    @CommandAlias("memall|memoryall")
    @CommandPermission("eternia.mem.all")
    public void onMemAll() {
        getRuntime.recalculateRuntime();
        messages.broadcastMessage(Strings.MSG_MEM, Constants.MEM_USE, getRuntime.freemem, Constants.MEM_MAX, getRuntime.totalmem);
        messages.broadcastMessage(Strings.MSG_MEM_ONLINE, Constants.HOURS, getRuntime.hours, Constants.MINUTE, getRuntime.minutes, Constants.SECONDS, getRuntime.seconds);
    }

    private String putPAPI(Player player, String message) {
        return PlaceholderAPI.setPlaceholders((OfflinePlayer) player, message);
    }

} class GetRuntime {

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
