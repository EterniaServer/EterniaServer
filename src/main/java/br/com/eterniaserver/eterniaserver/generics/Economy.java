package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.objects.UUIDFetcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Economy extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private long time = 0;
    private ArrayList<String> lista;

    public Economy(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_MONEY), Strings.UUID, Strings.BALANCE);
        temp.forEach((k, v) -> Vars.balances.put(UUID.fromString(k), Double.parseDouble(v)));
        messages.sendConsole(Strings.MSG_LOAD_DATA, Constants.MODULE, "Economy", Constants.AMOUNT, temp.size());
    }

    @CommandAlias("money|economy|balance|bal")
    @CommandCompletion("@players")
    @Syntax("<quantia>")
    @CommandPermission("eternia.money")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
            messages.sendMessage(Strings.M_ECO_MONEY, Constants.AMOUNT, plugin.df2.format(money), player);
        } else {
            if (player.hasPermission("eternia.money.other")) {
                double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(target.getPlayer().getName()));
                messages.sendMessage(Strings.M_ECO_OTHER, Constants.AMOUNT, plugin.df2.format(money), player);
            } else {
                messages.sendMessage(Strings.MSG_NO_PERM, player);
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
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (!(targetP.equals(player))) {
            if (value > 0) {
                if (APIEconomy.getMoney(uuid) >= value) {
                    APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetName), value);
                    APIEconomy.removeMoney(uuid, value);
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
            lista.forEach(name -> messages.sendMessage(Strings.M_ECO_BALLIST, Constants.POSITION, (lista.indexOf(name) + 1), Constants.PLAYER, name, Constants.AMOUNT, plugin.df2.format(APIEconomy.getMoney(UUIDFetcher.getUUIDOf(name))), sender));
        } else {
            CompletableFuture.runAsync(() -> {
                String name = "yurinogueira";
                final ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    double maior = 0.0;
                    for (Map.Entry<UUID, Double> entry : Vars.balances.entrySet()) {
                        final String playerName = UUIDFetcher.getNameOf(entry.getKey());
                        if (entry.getValue() > maior && !list.contains(playerName) && !EterniaServer.serverConfig.getStringList("money.no-baltop").contains(playerName)) {
                            maior = entry.getValue();
                            name = playerName;
                        }
                    }
                    list.add(name);
                }
                lista = list;
                time = System.currentTimeMillis();
                lista.forEach((namet -> messages.sendMessage(Strings.M_ECO_BALLIST, Constants.POSITION, (lista.indexOf(namet) + 1), Constants.PLAYER, namet, Constants.AMOUNT, plugin.df2.format(APIEconomy.getMoney(UUIDFetcher.getUUIDOf(namet))), sender)));
            });
        }
    }

}
