package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
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
    private long time = 0;
    private ArrayList<String> lista;

    public Economy(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_MONEY), Strings.PNAME, Strings.BALANCE);
        temp.forEach((k, v) -> Vars.balances.put(k, Double.parseDouble(v)));
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Economy", Constants.AMOUNT, temp.size());
    }

    @CommandAlias("money|economy|balance|bal")
    @CommandCompletion("@players")
    @Syntax("<quantia>")
    @CommandPermission("eternia.money")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(player.getName());
            messages.sendMessage(Strings.M_ECO_MONEY, Constants.AMOUNT, plugin.df2.format(money), player);
        } else {
            if (player.hasPermission("eternia.money.other")) {
                double money = APIEconomy.getMoney(target.getPlayer().getName());
                messages.sendMessage(Strings.M_ECO_OTHER, Constants.AMOUNT, plugin.df2.format(money), player);
            } else {
                messages.sendMessage(Strings.M_NO_PERM, player);
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
                if (APIEconomy.getMoney(playerName) >= value) {
                    APIEconomy.addMoney(targetName, value);
                    APIEconomy.removeMoney(playerName, value);
                    messages.sendMessage(Strings.M_ECO_PAY, Constants.AMOUNT, value, Constants.TARGET, targetName, player);
                    messages.sendMessage(Strings.M_ECO_PAY_ME, Constants.AMOUNT, value, Constants.TARGET, playerName, targetP);
                } else {
                    messages.sendMessage(Strings.M_ECO_PAY_NO, player);
                }
            } else {
                messages.sendMessage(Strings.M_ECO_PAY_NO, player);
            }
        } else {
            messages.sendMessage(Strings.M_ECO_AUTO, player);
        }
    }

    @CommandAlias("baltop|balancetop|moneytop")
    @CommandPermission("eternia.baltop")
    public void onBaltop(CommandSender sender) {
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time) <= 300) {
            lista.forEach(name -> messages.sendMessage(Strings.M_ECO_BALLIST, Constants.POSITION, (lista.indexOf(name) + 1), Constants.PLAYER, name, Constants.AMOUNT, plugin.df2.format(APIEconomy.getMoney(name)), sender));
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    double maior = 0.0;
                    String name = "";
                    for (Map.Entry<String, Double> entry : Vars.balances.entrySet()) {
                        final String playerName = entry.getKey();
                        if (entry.getValue() > maior && !list.contains(playerName) && !EterniaServer.serverConfig.getStringList("no-baltop").contains(playerName)) {
                            maior = entry.getValue();
                            name = playerName;
                        }
                    }
                    list.add(name);
                }
                lista = list;
                time = System.currentTimeMillis();
                list.forEach(name -> messages.sendMessage(Strings.M_ECO_BALLIST, Constants.POSITION, (list.indexOf(name) + 1), Constants.PLAYER, name, Constants.AMOUNT, plugin.df2.format(APIEconomy.getMoney(name)), sender));
            });
        }
    }

}
