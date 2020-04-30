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

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;
    private final Money moneyx;

    public Back(EterniaServer plugin, Messages messages, Money moneyx, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.moneyx = moneyx;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.back")) {
                if (vars.back.containsKey(player.getName())) {
                    double money = moneyx.getMoney(player.getName());
                    double valor = plugin.serverConfig.getInt("money.back");
                    if (player.hasPermission("eternia.backfree") || !(plugin.serverConfig.getBoolean("modules.economy"))) {
                        if (vars.teleports.containsKey(player)) {
                            messages.PlayerMessage("server.telep", player);
                        } else {
                            vars.teleports.put(player, new PlayerTeleport(player, vars.back.get(player.getName()), "back.free", plugin));
                        }
                    } else {
                        if (money >= valor) {
                            if (vars.teleports.containsKey(player)) {
                                messages.PlayerMessage("server.telep", player);
                            } else {
                                vars.teleports.put(player, new PlayerTeleport(player, vars.back.get(player.getName()), "back.nofree", plugin));
                            }
                        } else {
                            messages.PlayerMessage("back.nomoney", "%money%", valor, player);
                        }
                    }
                } else {
                    messages.PlayerMessage("back.notp", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}