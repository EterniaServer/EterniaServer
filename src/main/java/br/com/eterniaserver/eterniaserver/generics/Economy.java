package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.Connections;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@CommandAlias("eco")
@CommandPermission("eternia.money.user")
public class Economy extends BaseCommand {

    private long time = 0;
    private List<UUID> lista;
    final boolean nickEnable = Strings.M_ECO_BALLIST.contains("%player_displayname%");

    @Default
    public void ecoHelp(CommandSender sender) {
        sender.sendMessage(Strings.MSG_ECO_HELP_TITLE);
        if (sender.hasPermission("eternia.money.admin")) {
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(Constants.COMMANDS, "eco set &3<jogador> <quantia>")
                    .replace(Constants.MESSAGE, Strings.MSG_ECO_HELP_SET)));
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(Constants.COMMANDS, "eco take &3<jogador> <quantia>")
                    .replace(Constants.MESSAGE, Strings.MSG_ECO_HELP_TAKE)));
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(Constants.COMMANDS, "eco give &3<jogador> <quantia>")
                    .replace(Constants.MESSAGE, Strings.MSG_ECO_HELP_GIVE)));
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(Constants.COMMANDS, "money &3<jogador>")
                    .replace(Constants.MESSAGE, Strings.MSG_ECO_HELP_MONEY_ADMIN)));
        } else {
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(Constants.COMMANDS, "money")
                    .replace(Constants.MESSAGE, Strings.MSG_ECO_HELP_MONEY)));
        }
        sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                .replace(Constants.COMMANDS, "pay")
                .replace(Constants.MESSAGE, Strings.MSG_ECO_HELP_PAY)));
        sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                .replace(Constants.COMMANDS, "baltop")
                .replace(Constants.MESSAGE, Strings.MSG_ECO_HELP_BALTOP)));
    }

    @Subcommand("set")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    public void onSet(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();

        APIEconomy.setMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(InternMethods.putName(targetP, Strings.M_ECO_SET.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(InternMethods.putName(sender, Strings.M_ECO_RSET.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("take")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    public void onRemove(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();

        APIEconomy.removeMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(InternMethods.putName(targetP, Strings.M_ECO_REMOVE.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(InternMethods.putName(sender, Strings.M_ECO_RREMOVE.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("give")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    public void onGive(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(InternMethods.putName(targetP, Strings.M_ECO_GIVE.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(InternMethods.putName(sender, Strings.M_ECO_RECEIVE.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @CommandAlias("money")
    @Subcommand("money")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
            player.sendMessage(Strings.M_ECO_MONEY.replace(Constants.AMOUNT, EterniaServer.df2.format(money)));
        } else {
            if (player.hasPermission("eternia.money.admin")) {
                double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(target.getPlayer().getName()));
                player.sendMessage(Strings.M_ECO_OTHER.replace(Constants.AMOUNT, EterniaServer.df2.format(money)));
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
                    player.sendMessage(InternMethods.putName(targetP, Strings.M_ECO_PAY.replace(Constants.AMOUNT, String.valueOf(value))));
                    targetP.sendMessage(InternMethods.putName(player, Strings.M_ECO_PAY_ME.replace(Constants.AMOUNT, String.valueOf(value))));
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
            showBaltop(sender);
        } else {
            CompletableFuture.runAsync(() -> {
                if (EterniaLib.getMySQL()) {
                    EterniaLib.getConnections().executeSQLQuery(connection -> {
                        final PreparedStatement getHashMap = connection.prepareStatement(
                                "SELECT " + Constants.UUID_STR +
                                        " FROM " + Configs.tablePlayer +
                                        " ORDER BY " + Constants.BALANCE_STR + " DESC LIMIT 10;");
                        final ResultSet resultSet = getHashMap.executeQuery();
                        final List<UUID> tempList = new ArrayList<>();
                        while (resultSet.next()) {
                            tempList.add(UUID.fromString(resultSet.getString(Constants.UUID_STR)));
                        }
                        time = System.currentTimeMillis();
                        lista = tempList;
                        getHashMap.close();
                        resultSet.close();
                        showBaltop(sender);
                    });
                } else {
                    try (PreparedStatement getHashMap = Connections.getSQLite().prepareStatement(
                            "SELECT " + Constants.UUID_STR +
                                    " FROM " + Configs.tablePlayer +
                                    " ORDER BY " + Constants.BALANCE_STR + " DESC LIMIT 10;"); ResultSet resultSet = getHashMap.executeQuery()) {
                        final List<UUID> tempList = new ArrayList<>();
                        while (resultSet.next()) {
                            tempList.add(UUID.fromString(resultSet.getString(Constants.UUID_STR)));
                        }
                        time = System.currentTimeMillis();
                        lista = tempList;
                        showBaltop(sender);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showBaltop(CommandSender sender) {
        lista.forEach((user -> {
            final String playerName;
            if (nickEnable) playerName = Vars.playerProfile.get(user).getPlayerDisplayName();
            else playerName = Vars.playerProfile.get(user).getPlayerName();
            sender.sendMessage(Strings.M_ECO_BALLIST
                    .replace(Constants.POSITION, String.valueOf(lista.indexOf(user) + 1))
                    .replace(Constants.PLAYER, playerName)
                    .replace("%player_name%", playerName)
                    .replace(Constants.AMOUNT, EterniaServer.df2.format(APIEconomy.getMoney(user))));
        }));
    }

}
