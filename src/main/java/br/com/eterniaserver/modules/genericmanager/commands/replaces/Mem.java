package br.com.eterniaserver.modules.genericmanager.commands.replaces;

import br.com.eterniaserver.configs.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;

public class Mem implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.mem")) {
                Runtime r = Runtime.getRuntime();
                long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();
                long totalmem = r.totalMemory() / 1048576;
                long freemem = r.freeMemory() / 1048576;
                int seconds = (int) (milliseconds / 1000) % 60;
                int minutes = (int) ((milliseconds / (1000*60)) % 60);
                int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
                Messages.PlayerMessage("replaces.mem", freemem, totalmem, player);
                Messages.PlayerMessage("replaces.online", hours, minutes, seconds, player);
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Runtime r = Runtime.getRuntime();
            long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();
            long totalmem = r.totalMemory() / 1048576;
            long freemem = r.freeMemory() / 1048576;
            int seconds = (int) (milliseconds / 1000) % 60 ;
            int minutes = (int) ((milliseconds / (1000*60)) % 60);
            int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
            Messages.ConsoleMessage("replaces.mem", freemem, totalmem);
            Messages.ConsoleMessage("replaces.online", hours, minutes, seconds);
        }
        return true;
    }

}
