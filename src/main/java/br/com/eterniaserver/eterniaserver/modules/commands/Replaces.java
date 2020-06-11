package br.com.eterniaserver.eterniaserver.modules.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.util.Date;

public class Replaces extends BaseCommand {

    private final EterniaServer plugin;
    private final Messages messages;
    private final GetRuntime getRuntime;

    public Replaces(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.getRuntime = new GetRuntime();
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
    public void onProfile(Player player) throws ParseException {
        final String playerName = player.getName();

        Date date = plugin.sdf.parse(plugin.getVars().player_login.get(playerName));
        messages.sendMessage("generic.profile.register", "%player_register_data%", plugin.sdf.format(date), player);
        for (String line : plugin.msgConfig.getStringList("generic.profile.custom")) {
            String modifiedText;
            if (plugin.hasPlaceholderAPI) modifiedText = messages.putPAPI(player, line);
            else modifiedText = line.replace("%player_name%", playerName);
            player.sendMessage(plugin.getStrings().getColor(modifiedText));
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

} class GetRuntime {

    public long freemem;
    public long totalmem;
    public int seconds;
    public int minutes;
    public int hours;

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
