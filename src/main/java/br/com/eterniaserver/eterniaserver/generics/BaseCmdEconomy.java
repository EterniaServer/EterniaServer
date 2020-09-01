package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.Connections;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.strings.MSG;
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
public class BaseCmdEconomy extends BaseCommand {

    private long time = 0;
    private List<UUID> lista;
    final boolean nickEnable = MSG.ECO_BALLIST.contains("%player_displayname%");

    @Default
    @Syntax("<página>")
    @Description(" Ajuda para o /eco")
    @HelpCommand
    public void ecoHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("set")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    @Description(" Define o saldo de um jogador")
    public void onSet(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=9999999")  Double money) {
        final Player targetP = target.getPlayer();

        APIEconomy.setMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(UtilInternMethods.putName(targetP, MSG.ECO_SET.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(UtilInternMethods.putName(sender, MSG.ECO_RSET.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("take")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    @Description(" Retira uma quantia de saldo de um jogador")
    public void onRemove(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=9999999")  Double money) {
        final Player targetP = target.getPlayer();

        APIEconomy.removeMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(UtilInternMethods.putName(targetP, MSG.ECO_REMOVE.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(UtilInternMethods.putName(sender, MSG.ECO_RREMOVE.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("give")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    @Description(" Dar uma quantia de saldo a um jogador")
    public void onGive(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=9999999")  Double money) {
        final Player targetP = target.getPlayer();
        APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(UtilInternMethods.putName(targetP, MSG.ECO_GIVE.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(UtilInternMethods.putName(sender, MSG.ECO_RECEIVE.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @CommandAlias("money")
    @Subcommand("money")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @Description(" Verifica o saldo de um jogador")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
            player.sendMessage(MSG.ECO_MONEY.replace(Constants.AMOUNT, Vars.df2.format(money)));
        } else {
            if (player.hasPermission("eternia.money.admin")) {
                double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(target.getPlayer().getName()));
                player.sendMessage(MSG.ECO_OTHER.replace(Constants.AMOUNT, Vars.df2.format(money)));
            } else {
                player.sendMessage(MSG.MSG_NO_PERM);
            }
        }
    }

    @CommandAlias("pay")
    @Subcommand("pay")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @Description(" Paga uma quantia a um jogador")
    public void onPay(Player player, OnlinePlayer target, @Conditions("limits:min=1,max=9999999")  Double value) {
        final String playerName = player.getName();
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (!(targetP.equals(player))) {
            if (APIEconomy.getMoney(uuid) >= value) {
                APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetName), value);
                APIEconomy.removeMoney(uuid, value);
                player.sendMessage(UtilInternMethods.putName(targetP, MSG.ECO_PAY.replace(Constants.AMOUNT, String.valueOf(value))));
                targetP.sendMessage(UtilInternMethods.putName(player, MSG.ECO_PAY_ME.replace(Constants.AMOUNT, String.valueOf(value))));
            } else {
                player.sendMessage(MSG.ECO_PAY_NO);
            }
        } else {
            player.sendMessage(MSG.ECO_AUTO);
        }
    }

    @CommandAlias("baltop")
    @Subcommand("baltop")
    @Description(" Verifica a lista de mais ricos")
    public void onBaltop(CommandSender sender) {
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time) <= 300) {
            showBaltop(sender);
        } else {
            CompletableFuture.runAsync(() -> {
                if (EterniaLib.getMySQL()) {
                    EterniaLib.getConnections().executeSQLQuery(connection -> {
                        final PreparedStatement getHashMap = connection.prepareStatement(
                                "SELECT " + Constants.UUID_STR +
                                        " FROM " + Configs.TABLE_PLAYER +
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
                                    " FROM " + Configs.TABLE_PLAYER +
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
            sender.sendMessage(MSG.ECO_BALLIST
                    .replace(Constants.POSITION, String.valueOf(lista.indexOf(user) + 1))
                    .replace(Constants.PLAYER, playerName)
                    .replace("%player_name%", playerName)
                    .replace(Constants.AMOUNT, Vars.df2.format(APIEconomy.getMoney(user))));
        }));
    }

}
