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
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import br.com.eterniaserver.eterniaserver.core.APICash;
import br.com.eterniaserver.eterniaserver.core.APIPlayer;
import br.com.eterniaserver.eterniaserver.core.APIChat;
import br.com.eterniaserver.eterniaserver.objects.CashItem;
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
        player.closeInventory();
        Inventory gui = Bukkit.getServer().createInventory(player, EterniaServer.configs.menuGui.size(), "Cash");

        for (int i = 0; i < EterniaServer.configs.menuGui.size(); i++) {
            gui.setItem(i, EterniaServer.configs.menuGui.get(i));
        }

        player.openInventory(gui);
    }

    @Subcommand("balance")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @Description(" Mostra o saldo atual de cash de um jogador")
    public void onCashBalance(Player player, @Optional String playerName) {
        if (playerName == null) {
            EterniaServer.configs.sendMessage(player, Messages.CASH_BALANCE, String.valueOf(APICash.getCash(UUIDFetcher.getUUIDOf(player.getName()))));
            return;
        }

        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        final OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);

        if (APIPlayer.hasProfile(uuid)) {
            String displayName = target.isOnline() ? target.getPlayer().getDisplayName() : playerName;
            EterniaServer.configs.sendMessage(player, Messages.CASH_BALANCE_OTHER, playerName, displayName, String.valueOf(APICash.getCash(uuid)));
            return;
        }

        EterniaServer.configs.sendMessage(player, Messages.SERVER_NO_PLAYER);
    }

    @Subcommand("accept")
    @Description(" Aceita uma compra da loja de cash")
    public void onCashAccept(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (!APICash.isBuying(uuid)) {
            EterniaServer.configs.sendMessage(player, Messages.CASH_NOTHING_TO_BUY);
            return;
        }

        final CashItem cashItem = APICash.getCashBuy(uuid);

        for (String line : cashItem.getCommands()) {
            final String modifiedCommand = APIChat.setPlaceholders(player, line);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), modifiedCommand);
        }

        for (String line : cashItem.getMessages()) {
            final String modifiedText = APIChat.setPlaceholders(player, line);
            player.sendMessage(modifiedText);
        }

        APICash.removeCash(uuid, cashItem.getCost());
        EterniaServer.configs.sendMessage(player, Messages.CASH_BOUGHT);
        APICash.removeCashBuy(uuid);
    }

    @Subcommand("deny")
    @Description(" Recusa uma compra da loja de cash")
    public void onCashDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (!APICash.isBuying(uuid)) {
            EterniaServer.configs.sendMessage(player, Messages.CASH_NOTHING_TO_BUY);
            return;
        }

        EterniaServer.configs.sendMessage(player, Messages.CASH_CANCELED);
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
            EterniaServer.configs.sendMessage(player, Messages.ECO_NO_MONEY);
            return;
        }

        APICash.removeCash(uuid, value);
        APICash.addCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        EterniaServer.configs.sendMessage(target, Messages.CASH_RECEVEID, String.valueOf(value), player.getName(), player.getDisplayName());
        EterniaServer.configs.sendMessage(player, Messages.CASH_SENT, String.valueOf(value), target.getName(), target.getDisplayName());
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
        EterniaServer.configs.sendMessage(target, Messages.CASH_RECEVEID, String.valueOf(value), player.getName(), senderDisplay);
        EterniaServer.configs.sendMessage(player, Messages.CASH_SENT, String.valueOf(value), target.getName(), target.getDisplayName());
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
        EterniaServer.configs.sendMessage(target, Messages.CASH_LOST, String.valueOf(value), player.getName(), senderDisplay);
        EterniaServer.configs.sendMessage(player, Messages.CASH_REMOVED, String.valueOf(value), target.getName(), target.getDisplayName());
    }

}
