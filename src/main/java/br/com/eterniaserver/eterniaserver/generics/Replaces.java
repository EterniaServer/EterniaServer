package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;

public class Replaces extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final GetRuntime getRuntime;

    public Replaces(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.getRuntime = new GetRuntime();

        HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_PLAYER), Strings.PNAME, Strings.TIME);
        temp.forEach((k, v) -> Vars.kitsCooldown.put(k, Long.parseLong(v)));
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Profile", Constants.AMOUNT, temp.size());

    }

    @CommandAlias("speed")
    @CommandPermission("eternia.speed")
    public void onSpeed(Player player, Integer speed) {
        if (speed > 0 && speed < 11) {
            player.setFlySpeed((float) speed / 10);
            player.setWalkSpeed((float) speed / 10);
        } else {
            messages.sendMessage(Strings.M_SPEED, player);
        }
    }

    @CommandAlias("profile|perfil")
    @CommandPermission("eternia.profile")
    public void onProfile(Player player) {
        final String playerName = player.getName();

        messages.sendMessage(Strings.M_PROFILE_REGISTER, Constants.PLAYER_DATA, plugin.sdf.format(new Date(Vars.playerLogin.get(playerName))), player);
        for (String line : EterniaServer.msgConfig.getStringList("generic.profile.custom")) {
            player.sendMessage(messages.getColor(putPAPI(player, line)));
        }
    }

    @CommandAlias("mem|memory")
    @CommandPermission("eternia.mem")
    public void onMem(CommandSender player) {
        getRuntime.recalculateRuntime();
        messages.sendMessage(Strings.M_MEM, Constants.MEM_USE, getRuntime.freemem, Constants.MEM_MAX, getRuntime.totalmem, player);
        messages.sendMessage(Strings.M_MEM_ONLINE, Constants.HOURS, getRuntime.hours, Constants.MINUTE, getRuntime.minutes, Constants.SECONDS, getRuntime.seconds, player);
    }

    @CommandAlias("memall|memoryall")
    @CommandPermission("eternia.mem.all")
    public void onMemAll() {
        getRuntime.recalculateRuntime();
        messages.broadcastMessage(Strings.M_MEM, Constants.MEM_USE, getRuntime.freemem, Constants.MEM_MAX, getRuntime.totalmem);
        messages.broadcastMessage(Strings.M_MEM_ONLINE, Constants.HOURS, getRuntime.hours, Constants.MINUTE, getRuntime.minutes, Constants.SECONDS, getRuntime.seconds);
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
