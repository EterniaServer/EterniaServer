package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.Connections;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.generics.APIEconomy;
import br.com.eterniaserver.eterniaserver.generics.APIPlayer;
import br.com.eterniaserver.eterniaserver.generics.PluginConfigs;
import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;
import br.com.eterniaserver.eterniaserver.generics.PluginVars;
import br.com.eterniaserver.eterniaserver.generics.UtilInternMethods;
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
    final boolean nickEnable = PluginMSGs.ECO_BALLIST.contains("%player_displayname%");

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
        sender.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.ECO_SET.replace(PluginConstants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(UtilInternMethods.putName(sender, PluginMSGs.ECO_RSET.replace(PluginConstants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("take")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    @Description(" Retira uma quantia de saldo de um jogador")
    public void onRemove(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=9999999")  Double money) {
        final Player targetP = target.getPlayer();
        APIEconomy.removeMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.ECO_REMOVE.replace(PluginConstants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(UtilInternMethods.putName(sender, PluginMSGs.ECO_RREMOVE.replace(PluginConstants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("give")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    @Description(" Dar uma quantia de saldo a um jogador")
    public void onGive(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=9999999")  Double money) {
        final Player targetP = target.getPlayer();
        APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.ECO_GIVE.replace(PluginConstants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(UtilInternMethods.putName(sender, PluginMSGs.ECO_RECEIVE.replace(PluginConstants.AMOUNT, String.valueOf(money))));
    }

    @CommandAlias("money")
    @Subcommand("money")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @Description(" Verifica o saldo de um jogador")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
            player.sendMessage(PluginMSGs.ECO_MONEY.replace(PluginConstants.AMOUNT, PluginVars.df2.format(money)));
        } else {
            if (player.hasPermission("eternia.money.admin")) {
                double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(target.getPlayer().getName()));
                player.sendMessage(PluginMSGs.ECO_OTHER.replace(PluginConstants.AMOUNT, PluginVars.df2.format(money)));
            } else {
                player.sendMessage(PluginMSGs.MSG_NO_PERM);
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
                player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.ECO_PAY.replace(PluginConstants.AMOUNT, String.valueOf(value))));
                targetP.sendMessage(UtilInternMethods.putName(player, PluginMSGs.ECO_PAY_ME.replace(PluginConstants.AMOUNT, String.valueOf(value))));
            } else {
                player.sendMessage(PluginMSGs.ECO_PAY_NO);
            }
        } else {
            player.sendMessage(PluginMSGs.ECO_AUTO);
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
                                "SELECT " + PluginConstants.UUID_STR +
                                        " FROM " + PluginConfigs.TABLE_PLAYER +
                                        " ORDER BY " + PluginConstants.BALANCE_STR + " DESC LIMIT 20;");
                        final ResultSet resultSet = getHashMap.executeQuery();
                        final List<UUID> tempList = new ArrayList<>();
                        UUID uuid;
                        while (resultSet.next()) {
                            if (tempList.size() < 10) {
                                uuid = UUID.fromString(resultSet.getString(PluginConstants.UUID_STR));
                                if (!EterniaServer.serverConfig.getStringList("money.blacklisted-baltop").contains(UUIDFetcher.getNameOf(uuid))) {
                                    tempList.add(uuid);
                                }
                            }
                        }
                        time = System.currentTimeMillis();
                        lista = tempList;
                        getHashMap.close();
                        resultSet.close();
                        showBaltop(sender);
                    });
                } else {
                    try (PreparedStatement getHashMap = Connections.getSQLite().prepareStatement(
                            "SELECT " + PluginConstants.UUID_STR +
                                    " FROM " + PluginConfigs.TABLE_PLAYER +
                                    " ORDER BY " + PluginConstants.BALANCE_STR + " DESC LIMIT 20;"); ResultSet resultSet = getHashMap.executeQuery()) {
                        final List<UUID> tempList = new ArrayList<>();
                        UUID uuid;
                        while (resultSet.next()) {
                            if (tempList.size() < 10) {
                                uuid = UUID.fromString(resultSet.getString(PluginConstants.UUID_STR));
                                if (!EterniaServer.serverConfig.getStringList("money.blacklisted-baltop").contains(UUIDFetcher.getNameOf(uuid))) {
                                    tempList.add(uuid);
                                }
                            }
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
            if (nickEnable) playerName = APIPlayer.getDisplayName(user);
            else playerName = APIPlayer.getName(user);
            sender.sendMessage(PluginMSGs.ECO_BALLIST
                    .replace(PluginConstants.POSITION, String.valueOf(lista.indexOf(user) + 1))
                    .replace(PluginConstants.PLAYER, playerName)
                    .replace("%player_name%", playerName)
                    .replace(PluginConstants.AMOUNT, PluginVars.df2.format(APIEconomy.getMoney(user))));
        }));
    }

}
