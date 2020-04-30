package br.com.eterniaserver.modules.genericmanager.commands.replaces;

import br.com.eterniaserver.configs.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.management.ManagementFactory;

public class MemAll implements CommandExecutor {

    private final Messages messages;

    public MemAll(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("eternia.mem.all")) {
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
        return true;
    }

}
