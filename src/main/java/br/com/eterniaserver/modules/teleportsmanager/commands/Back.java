package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.economymanager.sql.Queries;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {

    private final EterniaServer plugin;

    public Back(EterniaServer plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.back")) {
                if (Vars.back.containsKey(player.getName())) {
                    double money = Queries.getMoney(player.getName());
                    double valor = EterniaServer.configs.getInt("money.back");
                    if (player.hasPermission("eternia.backfree") || !(EterniaServer.configs.getBoolean("modules.economy"))) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(Vars.back.get(player.getName()));
                            Vars.back.remove(player.getName());
                            new PlayerMessage("back.free", player);
                        } else {
                            new PlayerMessage("teleport.timing", EterniaServer.configs.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player.getName(), player.getLocation());
                            Vars.moved.put(player.getName(), false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            {
                                if (!Vars.moved.get(player.getName())) {
                                    player.teleport(Vars.back.get(player.getName()));
                                    Vars.back.remove(player.getName());
                                    new PlayerMessage("back.free", player);
                                } else {
                                    new PlayerMessage("warps.move", player);
                                }
                            }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                        }
                    } else {
                        if (money >= valor) {
                            if (player.hasPermission("eternia.timing.bypass")) {
                                player.teleport(Vars.back.get(player.getName()));
                                Vars.back.remove(player.getName());
                                Queries.removeMoney(player.getName(), valor);
                                new PlayerMessage("back.nofree", valor, player);
                            } else {
                                new PlayerMessage("teleport.timing", EterniaServer.configs.getInt("server.cooldown"), player);
                                Vars.moved.put(player.getName(), false);
                                Vars.playerposition.put(player.getName(), player.getLocation());
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                                {
                                    if (!Vars.moved.get(player.getName())) {
                                        player.teleport(Vars.back.get(player.getName()));
                                        Vars.back.remove(player.getName());
                                        Queries.removeMoney(player.getName(), valor);
                                        new PlayerMessage("back.nofree", valor, player);
                                    } else {
                                        new PlayerMessage("warps.move", player);
                                    }
                                }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                            }
                        } else {
                            new PlayerMessage("back.nomoney", valor, player);
                        }
                    }
                } else {
                    new PlayerMessage("back.notp", player);
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