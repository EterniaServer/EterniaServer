package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.eterniaserver.player.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {

    private final EterniaServer plugin;
    private final TeleportsManager teleportsManager;
    private final Messages messages;
    private final Vars vars;

    public Warp(EterniaServer plugin, TeleportsManager teleportsManager, Messages messages, Vars vars) {
        this.plugin = plugin;
        this.teleportsManager = teleportsManager;
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.warp." + args[0].toLowerCase())) {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        final Location location = teleportsManager.getWarp(args[0].toLowerCase());
                        if (location != vars.error) {
                            if (vars.teleports.containsKey(player)) {
                                messages.PlayerMessage("server.telep", player);
                            } else {
                                vars.teleports.put(player, new PlayerTeleport(player, location, "warps.warp", plugin));
                            }
                        } else {
                            messages.PlayerMessage("warps.noexist", "%warp_name%", args[0], player);
                        }
                    });
                } else {
                    messages.PlayerMessage("server.no-perm", player);
                }
            } else {
                messages.PlayerMessage("warps.use", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
