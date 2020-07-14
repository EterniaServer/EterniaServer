package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Economy extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final EconomyManager moneyx;
    private long time = 0;
    private ArrayList<String> lista;

    public Economy(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.moneyx = plugin.getMoney();

        final String query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-money") + ";";
        final HashMap<String, String> temp = EQueries.getMapString(query, "player_name", "balance");

        temp.forEach((k, v) -> Vars.balances.put(k, Double.parseDouble(v)));
        messages.sendConsole("server.load-data", Constants.MODULE.get(), "Economy", Constants.AMOUNT.get(), temp.size());
    }

    @CommandAlias("money|economy|balance|bal")
    @CommandCompletion("@players")
    @Syntax("<quantia>")
    @CommandPermission("eternia.money")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = moneyx.getMoney(player.getName());
            messages.sendMessage("eco.money", Constants.AMOUNT.get(), plugin.df2.format(money), player);
        } else {
            if (player.hasPermission("eternia.money.other")) {
                double money = moneyx.getMoney(target.getPlayer().getName());
                messages.sendMessage("eco.money-other", Constants.AMOUNT.get(), plugin.df2.format(money), player);
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
                    messages.sendMessage("eco.pay", Constants.AMOUNT.get(), value, Constants.TARGET.get(), targetName, player);
                    messages.sendMessage("eco.pay-me", Constants.AMOUNT.get(), value, Constants.TARGET.get(), playerName, targetP);
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
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time) <= 300) {
            lista.forEach(name -> messages.sendMessage("eco.ballist", "%position%", (lista.indexOf(name) + 1), Constants.PLAYER.get(), name, Constants.AMOUNT.get(), plugin.df2.format(moneyx.getMoney(name)), sender));
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    double maior = 0;
                    String name = "";
                    for (Map.Entry<String, Double> entry : Vars.balances.entrySet()) {
                        if (entry.getValue() > maior && !list.contains(entry.getKey())) {
                            maior = entry.getValue();
                            name = entry.getKey();
                        }
                    }
                    list.add(name);
                }
                lista = list;
                time = System.currentTimeMillis();
                list.forEach(name -> messages.sendMessage("eco.ballist", "%position%", (list.indexOf(name) + 1), Constants.PLAYER.get(), name, Constants.AMOUNT.get(), plugin.df2.format(moneyx.getMoney(name)), sender));
            });
        }
    }

}
