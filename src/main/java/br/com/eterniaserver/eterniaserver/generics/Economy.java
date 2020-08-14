package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
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

@CommandAlias("eco")
@CommandPermission("eternia.money.user")
public class Economy extends BaseCommand implements Constants{

    private long time = 0;
    private ArrayList<String> lista;

    public Economy() {
        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.TABLE_MONEY), UUID_STR, BALANCE_STR);
        temp.forEach((k, v) -> Vars.balances.put(UUID.fromString(k), Double.parseDouble(v)));
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(MODULE, "Economy").replace(AMOUNT, String.valueOf(temp.size())));
    }

    @Default
    public void ecoHelp(CommandSender sender) {
        sender.sendMessage(Strings.MSG_ECO_HELP_TITLE);
        if (sender.hasPermission("eternia.money.admin")) {
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(COMMANDS, "eco set &3<jogador> <quantia>")
                    .replace(MESSAGE, Strings.MSG_ECO_HELP_SET)));
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(COMMANDS, "eco take &3<jogador> <quantia>")
                    .replace(MESSAGE, Strings.MSG_ECO_HELP_TAKE)));
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(COMMANDS, "eco give &3<jogador> <quantia>")
                    .replace(MESSAGE, Strings.MSG_ECO_HELP_GIVE)));
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(COMMANDS, "money &3<jogador>")
                    .replace(MESSAGE, Strings.MSG_ECO_HELP_MONEY_ADMIN)));
        } else {
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(COMMANDS, "money")
                    .replace(MESSAGE, Strings.MSG_ECO_HELP_MONEY)));
        }
        sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                .replace(COMMANDS, "pay")
                .replace(MESSAGE, Strings.MSG_ECO_HELP_PAY)));
        sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                .replace(COMMANDS, "baltop")
                .replace(MESSAGE, Strings.MSG_ECO_HELP_BALTOP)));
    }

    @Subcommand("set")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    public void onSet(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.setMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(Strings.M_ECO_SET.replace(AMOUNT, String.valueOf(money)).replace(TARGET, targetP.getDisplayName()));
        targetP.sendMessage(Strings.M_ECO_RSET.replace(AMOUNT, String.valueOf(money)).replace(TARGET, senderName));
    }

    @Subcommand("take")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    public void onRemove(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.removeMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(Strings.M_ECO_REMOVE.replace(AMOUNT, String.valueOf(money)).replace(TARGET, targetP.getDisplayName()));
        targetP.sendMessage(Strings.M_ECO_RREMOVE.replace(AMOUNT, String.valueOf(money)).replace(TARGET, senderName));
    }

    @Subcommand("give")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    public void onGive(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(Strings.M_ECO_GIVE.replace(AMOUNT, String.valueOf(money)).replace(TARGET, targetP.getDisplayName()));
        targetP.sendMessage(Strings.M_ECO_RECEIVE.replace(AMOUNT, String.valueOf(money)).replace(TARGET, senderName));
    }

    @CommandAlias("money")
    @Subcommand("money")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
            player.sendMessage(Strings.M_ECO_MONEY.replace(AMOUNT, EterniaServer.df2.format(money)));
        } else {
            if (player.hasPermission("eternia.money.admin")) {
                double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(target.getPlayer().getName()));
                player.sendMessage(Strings.M_ECO_OTHER.replace(AMOUNT, EterniaServer.df2.format(money)));
            } else {
                player.sendMessage(Strings.MSG_NO_PERM);
            }
        }
    }

    @CommandAlias("pay")
    @Subcommand("pay")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
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
                    player.sendMessage(Strings.M_ECO_PAY.replace(AMOUNT, String.valueOf(value)).replace(TARGET, targetP.getDisplayName()));
                    targetP.sendMessage(Strings.M_ECO_PAY_ME.replace(AMOUNT, String.valueOf(value)).replace(TARGET, player.getDisplayName()));
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

    @CommandAlias("baltop")
    @Subcommand("baltop")
    public void onBaltop(CommandSender sender) {
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time) <= 300) {
            lista.forEach((namet -> sender.sendMessage(Strings.M_ECO_BALLIST.replace(POSITION, String.valueOf(lista.indexOf(namet) + 1)).replace(PLAYER, namet).replace(AMOUNT, EterniaServer.df2.format(APIEconomy.getMoney(UUIDFetcher.getUUIDOf(namet)))))));
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
                list.forEach((namet -> sender.sendMessage(Strings.M_ECO_BALLIST.replace(POSITION, String.valueOf(lista.indexOf(namet) + 1)).replace(PLAYER, namet).replace(AMOUNT, EterniaServer.df2.format(APIEconomy.getMoney(UUIDFetcher.getUUIDOf(namet)))))));
            });
        }
    }

}
