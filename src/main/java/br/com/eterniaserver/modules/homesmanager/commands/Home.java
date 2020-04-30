package br.com.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.homesmanager.HomesManager;

import br.com.eterniaserver.player.PlayerTeleport;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final HomesManager homesManager;
    private final Vars vars;

    public Home(EterniaServer plugin, Messages messages, HomesManager homesManager, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.homesManager = homesManager;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.home")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    if (args.length == 1) {
                        Location location = homesManager.getHome(args[0].toLowerCase(), player.getName());
                        if (location != vars.error) {
                            if (vars.teleports.containsKey(player)) {
                                messages.PlayerMessage("server.telep", player);
                            } else {
                                vars.teleports.put(player, new PlayerTeleport(player, location, "home.suc", plugin));
                            }
                        } else {
                            messages.PlayerMessage("home.noex", player);
                        }
                    } else if (args.length == 2) {
                        if (player.hasPermission("eternia.home.other")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target != null && target.isOnline()) {
                                Location location = homesManager.getHome(args[1].toLowerCase(), target.getName());
                                if (location != vars.error) {
                                    PaperLib.teleportAsync(player, location);
                                    messages.PlayerMessage("home.suc", player);
                                } else {
                                    messages.PlayerMessage("home.noex", player);
                                }
                            } else {
                                messages.PlayerMessage("server.player-offline", player);
                            }
                        } else {
                            messages.PlayerMessage("server.no-perm", player);
                        }
                    } else {
                        messages.PlayerMessage("home.use2", player);
                    }
                });
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}