package br.com.eterniaserver.eterniaserver.commands;

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
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIEconomy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@CommandAlias("%eco")
public class Economy extends BaseCommand {

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("%eco_syntax")
    @CommandPermission("%eco_perm")
    @Description("%eco_description")
    public void ecoHelp(CommandHelp help) {
        help.showHelp();
    }

    @CommandCompletion("@players 100")
    @Syntax("%eco_set_syntax")
    @Subcommand("%eco_set")
    @Description("%eco_set_description")
    @CommandPermission("%eco_set_perm")
    public void onSet(CommandSender sender, OnlinePlayer targets, @Conditions("limits:min=1,max=2147483647") Double money) {
        User user = new User(sender);
        User target = new User(targets.getPlayer());

        APIEconomy.setMoney(target.getUUID(), money);
        user.sendMessage(Messages.ECO_SET_FROM, String.valueOf(money), target.getName(), target.getDisplayName());
        target.sendMessage(Messages.ECO_SETED, String.valueOf(money), user.getName(), user.getDisplayName());
    }

    @CommandCompletion("@players 100")
    @Syntax("%eco_take_syntax")
    @Subcommand("%eco_take")
    @Description("%eco_take_description")
    @CommandPermission("%eco_take_perm")
    public void onRemove(CommandSender sender, OnlinePlayer targets, @Conditions("limits:min=1,max=2147483647") Double money) {
        User user = new User(sender);
        User target = new User(targets.getPlayer());

        APIEconomy.removeMoney(target.getUUID(), money);
        user.sendMessage(Messages.ECO_REMOVE_FROM, String.valueOf(money), target.getName(), target.getDisplayName());
        target.sendMessage(Messages.ECO_REMOVED, String.valueOf(money), user.getName(), user.getDisplayName());
    }

    @CommandCompletion("@players 100")
    @Syntax("%eco_give_syntax")
    @Subcommand("%eco_give")
    @Description("%eco_give_description")
    @CommandPermission("%eco_give_perm")
    public void onGive(CommandSender sender, OnlinePlayer targets, @Conditions("limits:min=1,max=2147483647") Double money) {
        User user = new User(sender);
        User target = new User(targets.getPlayer());

        APIEconomy.addMoney(target.getUUID(), money);
        user.sendMessage(Messages.ECO_GIVE_FROM, String.valueOf(money), target.getName(), target.getDisplayName());
        target.sendMessage(Messages.ECO_GIVED, String.valueOf(money), user.getName(), user.getDisplayName());
    }

    @CommandCompletion("@players")
    @Syntax("%eco_money_syntax")
    @Subcommand("%eco_money")
    @CommandAlias("%eco_money_aliases")
    @Description("%eco_money_description")
    @CommandPermission("%eco_money_perm")
    public void onMoney(Player player, @Optional OnlinePlayer targets) {
        User user = new User(player);

        if (targets == null) {
            user.sendMessage(Messages.ECO_BALANCE, APIEconomy.format(APIEconomy.getMoney(user.getUUID())));
            return;
        }

        User target = new User(targets.getPlayer());

        if (player.hasPermission(EterniaServer.getString(Strings.PERM_MONEY_OTHER))) {
            user.sendMessage(Messages.ECO_BALANCE_OTHER, APIEconomy.format(APIEconomy.getMoney(target.getUUID())), target.getName(), target.getDisplayName());
            return;
        }

        user.sendMessage(Messages.SERVER_NO_PERM);
    }

    @CommandCompletion("@players 100")
    @Syntax("%eco_pay_syntax")
    @Subcommand("%eco_pay")
    @CommandAlias("%eco_pay_aliases")
    @Description("%eco_pay_description")
    @CommandPermission("%eco_pay_perm")
    public void onPay(Player player, OnlinePlayer targets, @Conditions("limits:min=1,max=2147483647")  Double value) {
        User user = new User(player);
        User target = new User(targets.getPlayer());

        if (target.getName().equals(user.getName())) {
            user.sendMessage(Messages.ECO_AUTO_PAY);
            return;
        }

        if (!APIEconomy.hasMoney(user.getUUID(), value)) {
            user.sendMessage(Messages.ECO_NO_MONEY);
            return;
        }

        APIEconomy.addMoney(target.getUUID(), value);
        APIEconomy.removeMoney(user.getUUID(), value);
        user.sendMessage(Messages.ECO_PAY, String.valueOf(value), target.getName(), target.getDisplayName());
        target.sendMessage(Messages.ECO_PAY_RECEIVED, String.valueOf(value), user.getName(), user.getDisplayName());
    }

    @Subcommand("%eco_baltop")
    @CommandAlias("%eco_baltop_aliases")
    @Description("%eco_baltop_description")
    @CommandPermission("%eco_baltop_perm")
    public void onBaltop(CommandSender sender) {
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - APIEconomy.getBalanceTopTime()) <= 300) {
            showBaltop(sender);
        } else {
            APIEconomy.updateBalanceTop(20).thenRun(() -> showBaltop(sender));
        }
    }

    private void showBaltop(CommandSender sender) {
        sender.sendMessage(EterniaServer.getMessage(Messages.ECO_BALTOP_TITLE, true));
        User user;
        for (int i = 0; i < 10; i++) {
            user = new User(APIEconomy.getBalanceTop().get(i));
            EterniaServer.sendMessage(sender, Messages.ECO_BALTOP_LIST, false,
                    String.valueOf(i + 1), user.getName(), user.getDisplayName(),
                    APIEconomy.format(APIEconomy.getMoney(user.getUUID())));
        }
    }

}
