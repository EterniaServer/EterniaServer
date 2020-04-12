package br.com.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.homesmanager.sql.Queries;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {

    private final EterniaServer plugin;

    public Home(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.home")) {
                if (args.length == 1) {
                    final Location location = Queries.getHome(args[0].toLowerCase(), player.getName());
                    if (location != Vars.error) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(location);
                            Messages.PlayerMessage("home.suc", player);
                        } else {
                            Messages.PlayerMessage("teleport.timing", EterniaServer.configs.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player.getName(), player.getLocation());
                            Vars.moved.put(player.getName(), false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                if (!Vars.moved.get(player.getName())) {
                                    player.teleport(location);
                                    Messages.PlayerMessage("home.suc", player);
                                } else {
                                    Messages.PlayerMessage("warps.move", player);
                                }
                            }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                        }
                    } else {
                        Messages.PlayerMessage("home.no", player);
                    }
                } else {
                    Messages.PlayerMessage("home.use2", player);
                }
            } else {
                Messages.PlayerMessage("sem-permissao", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}