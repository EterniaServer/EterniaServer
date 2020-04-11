package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
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
                    assert target != null;
                    if (target.hasPermission("eternia.timing.bypass")) {
                        target.teleport(player.getLocation());
                        new PlayerMessage("teleport.tpto", player.getName(), target);
                        new PlayerMessage("teleport.accept", player.getName(), target);
                        Vars.tpa_requests.remove(player.getName());
                    } else {
                        new PlayerMessage("teleport.timing", EterniaServer.configs.getInt("server.cooldown"), target);
                        new PlayerMessage("teleport.accept", player.getName(), target);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        {
                            target.teleport(player.getLocation());
                            new PlayerMessage("teleport.tpto", player.getName(), target);
                            Vars.tpa_requests.remove(player.getName());
                        }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                    }
                } else {
                    new PlayerMessage("teleport.noask", player);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }

}
