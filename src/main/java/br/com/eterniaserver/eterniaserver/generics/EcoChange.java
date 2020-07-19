package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

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
    public void onSet(Player sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();

        APIEconomy.setMoney(targetName, money);
        messages.sendMessage("eco.eco-set", Constants.AMOUNT.get(), money, Constants.TARGET.get(), targetP.getDisplayName(), sender);
        messages.sendMessage("eco.eco-rset", Constants.AMOUNT.get(), money, Constants.TARGET.get(), sender.getDisplayName(), targetP);
    }

    @Subcommand("remove|remover")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onRemove(Player sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();

        APIEconomy.removeMoney(targetName, money);
        messages.sendMessage("eco.eco-remove", Constants.AMOUNT.get() ,money, Constants.TARGET.get(), targetP.getDisplayName(), sender);
        messages.sendMessage("eco.eco-rremove", Constants.AMOUNT.get(), money, Constants.TARGET.get(), sender.getDisplayName(), targetP);
    }

    @Subcommand("give|dar")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onGive(Player sender, OnlinePlayer target, Double money) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();

        APIEconomy.addMoney(targetName, money);
        messages.sendMessage("eco.eco-give", Constants.AMOUNT.get(), money, Constants.TARGET.get(), targetP.getDisplayName(), sender);
        messages.sendMessage("eco.eco-receive", Constants.AMOUNT.get(), money, Constants.TARGET.get(), sender.getDisplayName(), targetP);
    }

}
