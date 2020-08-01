package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("eco")
@CommandPermission("eternia.eco")
public class EcoChange extends BaseCommand {

    private final EFiles messages;

    public EcoChange(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
    }

    @Subcommand("set|definir")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onSet(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.setMoney(targetName, money);
        messages.sendMessage(Strings.M_ECO_SET, Constants.AMOUNT, money, Constants.TARGET, targetP.getDisplayName(), sender);
        messages.sendMessage(Strings.M_ECO_RSET, Constants.AMOUNT, money, Constants.TARGET, senderName, targetP);
    }

    @Subcommand("take|remover")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onRemove(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.removeMoney(targetName, money);
        messages.sendMessage(Strings.M_ECO_REMOVE, Constants.AMOUNT ,money, Constants.TARGET, targetP.getDisplayName(), sender);
        messages.sendMessage(Strings.M_ECO_RREMOVE, Constants.AMOUNT, money, Constants.TARGET, senderName, targetP);
    }

    @Subcommand("give|dar")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onGive(CommandSender sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();
        final String senderName = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();

        APIEconomy.addMoney(targetName, money);
        messages.sendMessage(Strings.M_ECO_GIVE, Constants.AMOUNT, money, Constants.TARGET, targetP.getDisplayName(), sender);
        messages.sendMessage(Strings.M_ECO_RECEIVE, Constants.AMOUNT, money, Constants.TARGET, senderName, targetP);
    }

}
