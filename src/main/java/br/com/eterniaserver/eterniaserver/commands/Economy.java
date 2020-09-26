package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Conditions;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.Connections;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.Configs;

import br.com.eterniaserver.eterniaserver.generics.APIEconomy;
import br.com.eterniaserver.eterniaserver.generics.APIPlayer;
import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
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
    public void onSet(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=2147483647")  Double money) {
        final Player targetP = target.getPlayer();
        APIEconomy.setMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        String playerDisplay = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();
        Configs.instance.sendMessage(sender, Messages.EcoSetFrom, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        Configs.instance.sendMessage(targetP, Messages.EcoSeted, String.valueOf(money), sender.getName(), playerDisplay);
    }

    @Subcommand("take")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    @Description(" Retira uma quantia de saldo de um jogador")
    public void onRemove(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=2147483647")  Double money) {
        final Player targetP = target.getPlayer();
        APIEconomy.removeMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        String playerDisplay = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();
        Configs.instance.sendMessage(sender, Messages.EcoRemoveFrom, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        Configs.instance.sendMessage(targetP, Messages.EcoRemoved, String.valueOf(money), sender.getName(), playerDisplay);
    }

    @Subcommand("give")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.money.admin")
    @Description(" Dar uma quantia de saldo a um jogador")
    public void onGive(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=2147483647")  Double money) {
        final Player targetP = target.getPlayer();
        APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        String playerDisplay = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();
        Configs.instance.sendMessage(sender, Messages.EcoGiveFrom, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        Configs.instance.sendMessage(targetP, Messages.EcoGived, String.valueOf(money), sender.getName(), playerDisplay);
    }

    @CommandAlias("money")
    @Subcommand("money")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @Description(" Verifica o saldo de um jogador")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
            Configs.instance.sendMessage(player, Messages.EcoBalance, APIEconomy.format(money));
            return;
        }

        if (player.hasPermission("eternia.money.admin")) {
            Player targetP = target.getPlayer();
            String targetName = targetP.getName();
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(targetName));
            Configs.instance.sendMessage(player, Messages.EcoBalanceOther, APIEconomy.format(money), targetName, targetP.getDisplayName());
            return;
        }

        Configs.instance.sendMessage(player, Messages.ServerNoPerm);
    }

    @CommandAlias("pay")
    @Subcommand("pay")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @Description(" Paga uma quantia a um jogador")
    public void onPay(Player player, OnlinePlayer target, @Conditions("limits:min=1,max=2147483647")  Double value) {
        final String playerName = player.getName();
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (!(targetP.equals(player))) {
            if (APIEconomy.getMoney(uuid) >= value) {
                APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetName), value);
                APIEconomy.removeMoney(uuid, value);
                Configs.instance.sendMessage(player, Messages.EcoPay, String.valueOf(value), targetName, targetP.getDisplayName());
                Configs.instance.sendMessage(targetP, Messages.EcoPayReceived, String.valueOf(value), playerName, player.getDisplayName());
            } else {
                Configs.instance.sendMessage(player, Messages.EcoNoMoney);
            }
        } else {
            Configs.instance.sendMessage(player, Messages.EcoAutoPay);
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
                                        " FROM " + Configs.instance.tablePlayer +
                                        " ORDER BY " + PluginConstants.BALANCE_STR + " DESC LIMIT 20;");
                        final ResultSet resultSet = getHashMap.executeQuery();
                        final List<UUID> tempList = new ArrayList<>();
                        UUID uuid;
                        while (resultSet.next()) {
                            if (tempList.size() < 10) {
                                uuid = UUID.fromString(resultSet.getString(PluginConstants.UUID_STR));
                                if (!Configs.instance.blacklistedBaltop.contains(UUIDFetcher.getNameOf(uuid))) {
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
                                    " FROM " + Configs.instance.tablePlayer +
                                    " ORDER BY " + PluginConstants.BALANCE_STR + " DESC LIMIT 20;"); ResultSet resultSet = getHashMap.executeQuery()) {
                        final List<UUID> tempList = new ArrayList<>();
                        UUID uuid;
                        while (resultSet.next()) {
                            if (tempList.size() < 10) {
                                uuid = UUID.fromString(resultSet.getString(PluginConstants.UUID_STR));
                                if (!Configs.instance.blacklistedBaltop.contains(UUIDFetcher.getNameOf(uuid))) {
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
            final String playerName = APIPlayer.getName(user);
            final String playerDisplay = APIPlayer.getDisplayName(user);
            Configs.instance.sendMessage(sender, Messages.EcoBaltopList, false,
                    String.valueOf(lista.indexOf(user) + 1),
                    playerName,
                    playerDisplay,
                    APIEconomy.format(APIEconomy.getMoney(user)));
        }));
    }

}
