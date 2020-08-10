package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("eco")
@CommandPermission("eternia.eco")
public class EcoChange extends BaseCommand {

    @Subcommand("set|definir")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onSet(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.setMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(Strings.M_ECO_SET.replace(Constants.AMOUNT, String.valueOf(money)).replace(Constants.TARGET, targetP.getDisplayName()));
        targetP.sendMessage(Strings.M_ECO_RSET.replace(Constants.AMOUNT, String.valueOf(money)).replace(Constants.TARGET, senderName));
    }

    @Subcommand("take|remover")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onRemove(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.removeMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(Strings.M_ECO_REMOVE.replace(Constants.AMOUNT, String.valueOf(money)).replace(Constants.TARGET, targetP.getDisplayName()));
        targetP.sendMessage(Strings.M_ECO_RREMOVE.replace(Constants.AMOUNT, String.valueOf(money)).replace(Constants.TARGET, senderName));
    }

    @Subcommand("give|dar")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onGive(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.addMoney(UUIDFetcher.getUUIDOf(targetP.getName()), money);
        sender.sendMessage(Strings.M_ECO_GIVE.replace(Constants.AMOUNT, String.valueOf(money)).replace(Constants.TARGET, targetP.getDisplayName()));
        targetP.sendMessage(Strings.M_ECO_RECEIVE.replace(Constants.AMOUNT, String.valueOf(money)).replace(Constants.TARGET, senderName));
    }

}
