package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.API.Money;

import br.com.eterniaserver.player.PlayerTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.back")) {
                if (Vars.back.containsKey(player.getName())) {
                    double money = Money.getMoney(player.getName());
                    double valor = EterniaServer.configs.getInt("money.back");
                    if (player.hasPermission("eternia.backfree") || !(EterniaServer.configs.getBoolean("modules.economy"))) {
                        if (Vars.teleports.containsKey(player)) {
                            Messages.PlayerMessage("server.telep", player);
                        } else {
                            Vars.teleports.put(player, new PlayerTeleport(player, Vars.back.get(player.getName()), "back.free"));
                        }
                    } else {
                        if (money >= valor) {
                            if (Vars.teleports.containsKey(player)) {
                                Messages.PlayerMessage("server.telep", player);
                            } else {
                                Vars.teleports.put(player, new PlayerTeleport(player, Vars.back.get(player.getName()), "back.nofree"));
                            }
                        } else {
                            Messages.PlayerMessage("back.nomoney", "%money%", valor, player);
                        }
                    }
                } else {
                    Messages.PlayerMessage("back.notp", player);
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