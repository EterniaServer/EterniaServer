package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
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
    private final Strings strings;
    private final Vars vars;

    public Replaces(EterniaServer plugin, Messages messages, Strings strings, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
        this.vars = vars;
    }

    @CommandAlias("speed")
    @CommandPermission("eternia.speed")
    public void onSpeed(Player player, Integer speed) {
        if (speed > 0 && speed < 11) {
            player.setFlySpeed((float) speed / 10);
            player.setWalkSpeed((float) speed / 10);
        } else {
            messages.sendMessage("other.speed-no", player);
        }
    }

    @CommandAlias("profile|perfil")
    @CommandPermission("eternia.profile")
    public void onProfile(Player player) throws ParseException {
        Date date = plugin.sdf.parse(vars.player_login.get(player.getName()));
        messages.sendMessage("profile.data", "%player_register_data%", plugin.sdf.format(date), player);
        for (String line : plugin.msgConfig.getStringList("profile.custom")) {
            String modifiedText;
            if (plugin.hasPlaceholderAPI) {
                modifiedText = messages.putPAPI(player, line);
            } else {
                modifiedText = line.replace("%player_name%", player.getName());
            }
            player.sendMessage(strings.getColor(modifiedText));
        }
    }

    @CommandAlias("mem|memory")
    @CommandPermission("eternia.mem")
    public void onMem(CommandSender player) {
        Runtime r = Runtime.getRuntime();
        long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();
        long totalmem = r.totalMemory() / 1048576;
        long freemem = totalmem - (r.freeMemory() / 1048576);
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        messages.sendMessage("replaces.mem", "%use_memory%", freemem, "%max_memory%", totalmem, player);
        messages.sendMessage("replaces.online", "%hours%", hours, "%minutes%", minutes, "%seconds%", seconds, player);
    }

    @CommandAlias("memall|memoryall")
    @CommandPermission("eternia.mem.all")
    public void onMemAll() {
        Runtime r = Runtime.getRuntime();
        long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();
        long totalmem = r.totalMemory() / 1048576;
        long freemem = totalmem - (r.freeMemory() / 1048576);
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        messages.BroadcastMessage("replaces.mem", "%use_memory%", freemem, "%max_memory%", totalmem);
        messages.BroadcastMessage("replaces.online", "%hours%", hours, "%minutes%", minutes, "%seconds%", seconds);
    }

}
