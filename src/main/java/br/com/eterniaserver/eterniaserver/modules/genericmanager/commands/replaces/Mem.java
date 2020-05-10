package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.replaces;

import  br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;

public class Mem implements CommandExecutor {

    private final Messages messages;

    public Mem(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.mem")) {
                Runtime r = Runtime.getRuntime();
                long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();
                long totalmem = r.totalMemory() / 1048576;
                long freemem = totalmem - (r.freeMemory() / 1048576);
                int seconds = (int) (milliseconds / 1000) % 60;
                int minutes = (int) ((milliseconds / (1000*60)) % 60);
                int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
                messages.PlayerMessage("replaces.mem", "%use_memory%", freemem, "%max_memory%", totalmem, player);
                messages.PlayerMessage("replaces.online", "%hours%", hours, "%minutes%", minutes, "%seconds%", seconds, player);
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Runtime r = Runtime.getRuntime();
            long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();
            long totalmem = r.totalMemory() / 1048576;
            long freemem = totalmem - (r.freeMemory() / 1048576);
            int seconds = (int) (milliseconds / 1000) % 60 ;
            int minutes = (int) ((milliseconds / (1000*60)) % 60);
            int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
            messages.ConsoleMessage("replaces.mem", "%use_memory%", freemem, "%max_memory%", totalmem);
            messages.ConsoleMessage("replaces.online", "%hours%", hours, "%minutes%", minutes, "%seconds%", seconds);
        }
        return true;
    }

}
