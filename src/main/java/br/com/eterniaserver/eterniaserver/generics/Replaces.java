package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;

import me.clip.placeholderapi.PlaceholderAPI;
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

        String query = "SELECT * FROM " + EterniaServer.serverConfig.getString("sql.table-player") + ";";
        HashMap<String, String> temp = EQueries.getMapString(query, "player_name", "time");

        temp.forEach((k, v) -> Vars.kitsCooldown.put(k, Long.parseLong(v)));
        messages.sendConsole("server.load-data", Constants.MODULE, "Profile", Constants.AMOUNT, temp.size());

    }

    @CommandAlias("speed")
    @CommandPermission("eternia.speed")
    public void onSpeed(Player player, Integer speed) {
        if (speed > 0 && speed < 11) {
            player.setFlySpeed((float) speed / 10);
            player.setWalkSpeed((float) speed / 10);
        } else {
            messages.sendMessage("generic.others.invalid", player);
        }
    }

    @CommandAlias("profile|perfil")
    @CommandPermission("eternia.profile")
    public void onProfile(Player player) {
        final String playerName = player.getName();

        messages.sendMessage("generic.profile.register", "%player_register_data%", plugin.sdf.format(new Date(Vars.playerLogin.get(playerName))), player);
        for (String line : EterniaServer.msgConfig.getStringList("generic.profile.custom")) {
            player.sendMessage(messages.getColor(putPAPI(player, line)));
        }
    }

    @CommandAlias("mem|memory")
    @CommandPermission("eternia.mem")
    public void onMem(CommandSender player) {
        getRuntime.recalculateRuntime();
        messages.sendMessage("replaces.mem", "%use_memory%", getRuntime.freemem, "%max_memory%", getRuntime.totalmem, player);
        messages.sendMessage("replaces.online", "%hours%", getRuntime.hours, "%minutes%", getRuntime.minutes, "%seconds%", getRuntime.seconds, player);
    }

    @CommandAlias("memall|memoryall")
    @CommandPermission("eternia.mem.all")
    public void onMemAll() {
        getRuntime.recalculateRuntime();
        messages.broadcastMessage("replaces.mem", "%use_memory%", getRuntime.freemem, "%max_memory%", getRuntime.totalmem);
        messages.broadcastMessage("replaces.online", "%hours%", getRuntime.hours, "%minutes%", getRuntime.minutes, "%seconds%", getRuntime.seconds);
    }

    private String putPAPI(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
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
