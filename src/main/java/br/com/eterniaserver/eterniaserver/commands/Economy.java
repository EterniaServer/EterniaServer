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
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIEconomy;
import br.com.eterniaserver.eterniaserver.core.APIPlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CommandAlias("eco")
@CommandPermission("eternia.money.user")
public class Economy extends BaseCommand {

    @Default
    @Syntax("<página>")
    @Description(" Ajuda para o /eco")
    @CatchUnknown
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
        EterniaServer.msg.sendMessage(sender, Messages.ECO_SET_FROM, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        EterniaServer.msg.sendMessage(targetP, Messages.ECO_SETED, String.valueOf(money), sender.getName(), playerDisplay);
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
        EterniaServer.msg.sendMessage(sender, Messages.ECO_REMOVE_FROM, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        EterniaServer.msg.sendMessage(targetP, Messages.ECO_REMOVED, String.valueOf(money), sender.getName(), playerDisplay);
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
        EterniaServer.msg.sendMessage(sender, Messages.ECO_GIVE_FROM, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        EterniaServer.msg.sendMessage(targetP, Messages.ECO_GIVED, String.valueOf(money), sender.getName(), playerDisplay);
    }

    @CommandAlias("money")
    @Subcommand("money")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @Description(" Verifica o saldo de um jogador")
    public void onMoney(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
            EterniaServer.msg.sendMessage(player, Messages.ECO_BALANCE, APIEconomy.format(money));
            return;
        }

        if (player.hasPermission("eternia.money.admin")) {
            Player targetP = target.getPlayer();
            String targetName = targetP.getName();
            double money = APIEconomy.getMoney(UUIDFetcher.getUUIDOf(targetName));
            EterniaServer.msg.sendMessage(player, Messages.ECO_BALANCE_OTHER, APIEconomy.format(money), targetName, targetP.getDisplayName());
            return;
        }

        EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
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
                EterniaServer.msg.sendMessage(player, Messages.ECO_PAY, String.valueOf(value), targetName, targetP.getDisplayName());
                EterniaServer.msg.sendMessage(targetP, Messages.ECO_PAY_RECEIVED, String.valueOf(value), playerName, player.getDisplayName());
            } else {
                EterniaServer.msg.sendMessage(player, Messages.ECO_NO_MONEY);
            }
        } else {
            EterniaServer.msg.sendMessage(player, Messages.ECO_AUTO_PAY);
        }
    }

    @CommandAlias("baltop")
    @Subcommand("baltop")
    @Description(" Verifica a lista de mais ricos")
    public void onBaltop(CommandSender sender) {
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - APIEconomy.getBalanceTopTime()) <= 300) {
            showBaltop(sender);
        } else {
            APIEconomy.updateBalanceTop(20).thenRun(() -> showBaltop(sender));
        }
    }

    private void showBaltop(CommandSender sender) {
        APIEconomy.getBalanceTop().forEach((user -> {
            final String playerName = APIPlayer.getName(user);
            final String playerDisplay = APIPlayer.getDisplayName(user);
            EterniaServer.msg.sendMessage(sender, Messages.ECO_BALTOP_LIST, false,
                    String.valueOf(APIEconomy.getBalanceTop().indexOf(user) + 1),
                    playerName,
                    playerDisplay,
                    APIEconomy.format(APIEconomy.getMoney(user)));
        }));
    }

}
