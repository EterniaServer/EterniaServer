package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CatchUnknown;
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
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APICash;
import br.com.eterniaserver.eterniaserver.objects.CashItem;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("%cash")
public class Cash extends BaseCommand {

    @CatchUnknown
    @HelpCommand
    @Subcommand("%cash_help")
    @Syntax("%cash_help_syntax")
    @Description("%cash_help_description")
    @CommandPermission("%cash_help_perm")
    public void onCashHelp(CommandHelp help) {
        help.showHelp();
    }

    @Default
    @Description("%cash_description")
    @CommandPermission("%cash_perm")
    public void onCash(Player player) {
        player.closeInventory();
        Inventory gui = Bukkit.getServer().createInventory(player, EterniaServer.cash.menuGui.size(), "Cash");

        for (int i = 0; i < EterniaServer.cash.menuGui.size(); i++) {
            gui.setItem(i, EterniaServer.cash.menuGui.get(i));
        }

        player.openInventory(gui);
    }

    @Subcommand("%cash_balance")
    @CommandCompletion("@players")
    @Syntax("%cash_balance_syntax")
    @Description("%cash_balance_description")
    @CommandPermission("%cash_balance_perm")
    public void onCashBalance(Player player, @Optional String playerName) {
        User user = new User(player);
        if (playerName == null) {
            user.sendMessage(Messages.CASH_BALANCE, String.valueOf(APICash.getCash(user.getUUID())));
            return;
        }

        User target = new User(playerName);

        if (target.hasProfile()) {
            user.sendMessage(Messages.CASH_BALANCE_OTHER, playerName, target.getDisplayName(), String.valueOf(APICash.getCash(target.getUUID())));
            return;
        }

        user.sendMessage(Messages.SERVER_NO_PLAYER);
    }

    @Subcommand("%cash_accept")
    @Description("%cash_accept_description")
    @CommandPermission("%cash_accept_perm")
    public void onCashAccept(Player player) {
        User user = new User(player);

        if (!APICash.isBuying(user.getUUID())) {
            user.sendMessage(Messages.CASH_NOTHING_TO_BUY);
            return;
        }

        final CashItem cashItem = APICash.getCashBuy(user.getUUID());

        for (String line : cashItem.getCommands()) {
            final String modifiedCommand = APIServer.setPlaceholders(player, line);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), modifiedCommand);
        }

        for (String line : cashItem.getMessages()) {
            final String modifiedText = APIServer.setPlaceholders(player, line);
            player.sendMessage(modifiedText);
        }

        APICash.removeCash(user.getUUID(), cashItem.getCost());
        user.sendMessage(Messages.CASH_BOUGHT);
        APICash.removeCashBuy(user.getUUID());
    }

    @Subcommand("%cash_deny")
    @Description("%cash_deny_description")
    @CommandPermission("%cash_deny_perm")
    public void onCashDeny(Player player) {
        User user = new User(player);

        if (!APICash.isBuying(user.getUUID())) {
            user.sendMessage(Messages.CASH_NOTHING_TO_BUY);
            return;
        }

        user.sendMessage(Messages.CASH_CANCELED);
        APICash.removeCashBuy(user.getUUID());
    }

    @Subcommand("%cash_pay")
    @CommandCompletion("@players 10")
    @Syntax("%cash_pay_syntax")
    @Description("%cash_pay_description")
    @CommandPermission("%cash_pay_perm")
    public void onCashPay(Player player, OnlinePlayer targetP, @Conditions("limits:min=1,max=9999999") Integer value) {
        User user = new User(player);
        User target = new User(targetP.getPlayer());

        if (!APICash.hasCash(user.getUUID(), value)) {
            EterniaServer.msg.sendMessage(player, Messages.ECO_NO_MONEY);
            return;
        }

        APICash.removeCash(user.getUUID(), value);
        APICash.addCash(user.getUUID(), value);
        target.sendMessage(Messages.CASH_RECEVEID, String.valueOf(value), user.getName(), user.getDisplayName());
        user.sendMessage(Messages.CASH_SENT, String.valueOf(value), target.getName(), target.getDisplayName());
    }

    @Subcommand("%cash_give")
    @CommandCompletion("@players 10")
    @Syntax("%cash_give_syntax")
    @Description("%cash_give_description")
    @CommandPermission("%cash_give_perm")
    public void onCashGive(CommandSender sender, OnlinePlayer targetP, @Conditions("limits:min=1,max=9999999") Integer value) {
        User user = new User(sender);
        User target = new User(targetP.getPlayer());

        APICash.addCash(target.getUUID(), value);
        target.sendMessage(Messages.CASH_RECEVEID, String.valueOf(value), user.getName(), user.getDisplayName());
        user.sendMessage(Messages.CASH_SENT, String.valueOf(value), target.getName(), target.getDisplayName());
    }

    @Subcommand("%cash_remove")
    @CommandCompletion("@players 10")
    @Syntax("%cash_remove_syntax")
    @CommandPermission("%cash_remove_perm")
    @Description("%cash_remove_description")
    public void onCashRemove(CommandSender sender, OnlinePlayer targetP, @Conditions("limits:min=1,max=9999999") Integer value) {
        User user = new User(sender);
        User target = new User(targetP.getPlayer());

        APICash.removeCash(target.getUUID(), value);
        target.sendMessage(Messages.CASH_LOST, String.valueOf(value), user.getName(), user.getDisplayName());
        user.sendMessage(Messages.CASH_REMOVED, String.valueOf(value), target.getName(), target.getDisplayName());
    }

}
