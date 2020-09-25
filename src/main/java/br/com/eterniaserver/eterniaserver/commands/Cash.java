package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
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
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Configs;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.MessagesEnum;

import br.com.eterniaserver.eterniaserver.generics.APICash;
import br.com.eterniaserver.eterniaserver.generics.APIPlayer;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;
import br.com.eterniaserver.eterniaserver.generics.UtilInternMethods;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

@CommandAlias("cash")
@CommandPermission("eternia.cash")
public class Cash extends BaseCommand {

    @Subcommand("help")
    @HelpCommand
    @Syntax("<página>")
    @Description(" Ajuda para o sistema de Cash")
    public void onCashHelp(CommandHelp help) {
        help.showHelp();
    }

    @Default
    @Description(" Abre a GUI da loja de Cash")
    public void onCash(Player player) {
        Inventory gui = Bukkit.getServer().createInventory(player, APICash.getCashGuiSize(), "Cash");

        for (int i = 0; i < APICash.getCashGuiSize(); i++) {
            gui.setItem(i, APICash.getItemCashGui(i));
        }

        player.openInventory(gui);
    }

    @Subcommand("balance")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @Description(" Mostra o saldo atual de cash de um jogador")
    public void onCashBalance(Player player, @Optional String playerName) {
        if (playerName == null) {
            Configs.instance.sendMessage(player, MessagesEnum.CashBalance, String.valueOf(APICash.getCash(UUIDFetcher.getUUIDOf(player.getName()))));
            return;
        }

        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        final OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);

        if (APIPlayer.hasProfile(uuid)) {
            String displayName = target.isOnline() ? target.getPlayer().getDisplayName() : playerName;
            Configs.instance.sendMessage(player, MessagesEnum.CashBalanceOther, playerName, displayName, String.valueOf(APICash.getCash(uuid)));
            return;
        }

        Configs.instance.sendMessage(player, MessagesEnum.ServerNoPlayer);
    }

    @Subcommand("accept")
    @Description(" Aceita uma compra da loja de cash")
    public void onCashAccept(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (!APICash.isBuying(uuid)) {
            Configs.instance.sendMessage(player, MessagesEnum.CashNothingToBuy);
            return;
        }

        final String cashString = APICash.getCashBuy(uuid);

        for (String line : EterniaServer.cashConfig.getStringList(cashString + ".commands")) {
            final String modifiedCommand = UtilInternMethods.setPlaceholders(player, line);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), modifiedCommand);
        }

        for (String line : EterniaServer.cashConfig.getStringList(cashString + ".messages")) {
            final String modifiedText = UtilInternMethods.setPlaceholders(player, line);
            player.sendMessage(PluginMSGs.getColor(modifiedText));
        }

        APICash.removeCash(uuid, EterniaServer.cashConfig.getInt(cashString + ".cost"));
        Configs.instance.sendMessage(player, MessagesEnum.CashBought);
        APICash.removeCashBuy(uuid);
    }

    @Subcommand("deny")
    @Description(" Recusa uma compra da loja de cash")
    public void onCashDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (!APICash.isBuying(uuid)) {
            Configs.instance.sendMessage(player, MessagesEnum.CashNothingToBuy);
            return;
        }

        Configs.instance.sendMessage(player, MessagesEnum.CashCanceled);
        APICash.removeCashBuy(uuid);
    }

    @Subcommand("pay")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @Description(" Paga uma quantia de cash a um jogador")
    public void onCashPay(Player player, OnlinePlayer targetP, @Conditions("limits:min=1,max=9999999") Integer value) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        final Player target = targetP.getPlayer();

        if (!APICash.hasCash(uuid, value)) {
            player.sendMessage(PluginMSGs.MSG_NO_MONEY);
            return;
        }

        APICash.removeCash(uuid, value);
        APICash.addCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        Configs.instance.sendMessage(target, MessagesEnum.CashReceveid, String.valueOf(value), player.getName(), player.getDisplayName());
        Configs.instance.sendMessage(player, MessagesEnum.CashSent, String.valueOf(value), target.getName(), target.getDisplayName());
    }

    @Subcommand("give")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @Description(" Dá uma quantia de cash a um jogador")
    @CommandPermission("eternia.cash.admin")
    public void onCashGive(CommandSender player, OnlinePlayer targetP, @Conditions("limits:min=1,max=9999999") Integer value) {
        final Player target = targetP.getPlayer();
        APICash.addCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        final String senderDisplay = (player instanceof Player) ? ((Player) player).getDisplayName() : player.getName();
        Configs.instance.sendMessage(target, MessagesEnum.CashReceveid, String.valueOf(value), player.getName(), senderDisplay);
        Configs.instance.sendMessage(player, MessagesEnum.CashSent, String.valueOf(value), target.getName(), target.getDisplayName());
    }

    @Subcommand("remove")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.cash.admin")
    @Description(" Remove uma quantia de cash de um jogador")
    public void onCashRemove(CommandSender player, OnlinePlayer targetP, @Conditions("limits:min=1,max=9999999") Integer value) {
        final Player target = targetP.getPlayer();
        APICash.removeCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        final String senderDisplay = (player instanceof Player) ? ((Player) player).getDisplayName() : player.getName();
        Configs.instance.sendMessage(target, MessagesEnum.CashLost, String.valueOf(value), player.getName(), senderDisplay);
        Configs.instance.sendMessage(player, MessagesEnum.CashRemoved, String.valueOf(value), target.getName(), target.getDisplayName());
    }

}
