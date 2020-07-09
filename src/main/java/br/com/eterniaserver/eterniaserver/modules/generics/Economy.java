package br.com.eterniaserver.eterniaserver.modules.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class Economy extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final EconomyManager moneyx;

    public Economy(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.moneyx = plugin.getMoney();

        final String query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-money") + ";";
        final HashMap<String, String> temp = EQueries.getMapString(query, "player_name", "balance");

        temp.forEach((k, v) -> Vars.balances.put(k, Double.parseDouble(v)));
    }

    @CommandAlias("money|economy|balance|bal")
    @CommandCompletion("@players")
    @Syntax("<quantia>")
    @CommandPermission("eternia.money")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        DecimalFormat df2 = new DecimalFormat(".##");
        if (target == null) {
            double money = moneyx.getMoney(player.getName());
            messages.sendMessage("eco.money", "%money%", df2.format(money), player);
        } else {
            if (player.hasPermission("eternia.money.other")) {
                double money = moneyx.getMoney(target.getPlayer().getName());
                messages.sendMessage("eco.money-other", "%money%", df2.format(money), player);
            } else {
                messages.sendMessage("server.no-perm", player);
            }
        }
    }

    @CommandAlias("pay|pagar")
    @CommandCompletion("@players 100")
    @Syntax("<nome> <quantia>")
    @CommandPermission("eternia.pay")
    public void onPay(Player player, OnlinePlayer target, Double value) {
        final String playerName = player.getName();
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();

        if (!(targetP.equals(player))) {
            if (value > 0) {
                if (moneyx.getMoney(playerName) >= value) {
                    moneyx.addMoney(targetName, value);
                    moneyx.removeMoney(playerName, value);
                    messages.sendMessage("eco.pay", "%amount%", value, "%target_name%", targetName, player);
                    messages.sendMessage("eco.pay-me", "%amount%", value, "%target_name%", playerName, targetP);
                } else {
                    messages.sendMessage("eco.pay-nomoney", player);
                }
            } else {
                messages.sendMessage("eco.pay-nomoney", player);
            }
        } else {
            messages.sendMessage("eco.auto-pay", player);
        }
    }

    @CommandAlias("baltop|balancetop|moneytop")
    @CommandPermission("eternia.baltop")
    public void onBaltop(CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-money") + " ORDER BY balance DESC LIMIT " + 10 + ";";
            final List<String> accounts = EQueries.queryStringList(querie, "player_name");
            DecimalFormat df2 = new DecimalFormat(".##");
            messages.sendMessage("eco.baltop", sender);
            accounts.forEach(name -> messages.sendMessage("eco.ballist", "%position%", (accounts.indexOf(name) + 1), "%player_name%", name, "%money%", df2.format(moneyx.getMoney(name)), sender));
        });
    }

}
