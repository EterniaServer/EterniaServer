package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAccept implements CommandExecutor {

    private final EterniaServer plugin;

    public TeleportAccept(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (Vars.tpa_requests.containsKey(player.getName())) {
                    Player target = Bukkit.getPlayer(Vars.tpa_requests.get(player.getName()));
                    if (target != null && target.hasPermission("eternia.timing.bypass")) {
                        PaperLib.teleportAsync(target, player.getLocation());
                        Messages.PlayerMessage("teleport.tpto", player.getName(), target);
                        Messages.PlayerMessage("teleport.accept", player.getName(), target);
                        Vars.tpa_requests.remove(player.getName());
                    } else if (target != null){
                        Messages.PlayerMessage("teleport.timing", EterniaServer.configs.getInt("server.cooldown"), target);
                        Messages.PlayerMessage("teleport.accept", player.getName(), target);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        {
                            PaperLib.teleportAsync(target, player.getLocation());
                            Messages.PlayerMessage("teleport.tpto", player.getName(), target);
                            Vars.tpa_requests.remove(player.getName());
                        }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                    }
                } else {
                    Messages.PlayerMessage("teleport.noask", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
