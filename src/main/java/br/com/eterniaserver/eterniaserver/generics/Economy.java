package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Economy extends BaseCommand {

    private long time = 0;
    private ArrayList<String> lista;

    public Economy() {

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_MONEY), Strings.UUID, Strings.BALANCE);
        temp.forEach((k, v) -> Vars.balances.put(UUID.fromString(k), Double.parseDouble(v)));
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Economy").replace(Constants.AMOUNT, String.valueOf(temp.size())));
    }

    @CommandAlias("money|economy|balance|bal")
    @CommandCompletion("@players")
    @Syntax("<quantia>")
    @CommandPermission("eternia.money")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
            player.sendMessage(Strings.M_ECO_MONEY.replace(Constants.AMOUNT, EterniaServer.df2.format(money)));
        } else {
            if (player.hasPermission("eternia.money.other")) {
                double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(target.getPlayer().getName()));
                player.sendMessage(Strings.M_ECO_OTHER.replace(Constants.AMOUNT, EterniaServer.df2.format(money)));
            } else {
                player.sendMessage(Strings.MSG_NO_PERM);
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
                    player.sendMessage(Strings.M_ECO_PAY.replace(Constants.AMOUNT, String.valueOf(value)).replace(Constants.TARGET, targetP.getDisplayName()));
                    targetP.sendMessage(Strings.M_ECO_PAY_ME.replace(Constants.AMOUNT, String.valueOf(value)).replace(Constants.TARGET, player.getDisplayName()));
                } else {
                    player.sendMessage(Strings.M_ECO_PAY_NO);
                }
            } else {
                player.sendMessage(Strings.M_ECO_PAY_NO);
            }
        } else {
            player.sendMessage(Strings.M_ECO_AUTO);
        }
    }

    @CommandAlias("baltop|balancetop|moneytop")
    @CommandPermission("eternia.baltop")
    public void onBaltop(CommandSender sender) {
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time) <= 300) {
            lista.forEach((namet -> sender.sendMessage(Strings.M_ECO_BALLIST.replace(Constants.POSITION, String.valueOf(lista.indexOf(namet) + 1)).replace(Constants.PLAYER, namet).replace(Constants.AMOUNT, EterniaServer.df2.format(APIEconomy.getMoney(UUIDFetcher.getUUIDOf(namet)))))));
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
                list.forEach((namet -> sender.sendMessage(Strings.M_ECO_BALLIST.replace(Constants.POSITION, String.valueOf(lista.indexOf(namet) + 1)).replace(Constants.PLAYER, namet).replace(Constants.AMOUNT, EterniaServer.df2.format(APIEconomy.getMoney(UUIDFetcher.getUUIDOf(namet)))))));
            });
        }
    }

}
