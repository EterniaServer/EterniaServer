package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("eco")
@CommandPermission("eternia.eco")
public class EcoChange extends BaseCommand {

    private final EFiles messages;
    private final EconomyManager moneyx;

    public EcoChange(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
        this.moneyx = plugin.getMoney();
    }

    @Subcommand("set|definir")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onSet(CommandSender sender, OnlinePlayer target, Double money) {
        final String senderName = sender.getName();
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();

        moneyx.setMoney(targetName, money);
        messages.sendMessage("eco.eco-set", "%money%", money, "%target_name%", targetName, sender);
        messages.sendMessage("eco.eco-rset", "%money%", money, "%target_name%", senderName, targetP);
    }

    @Subcommand("remove|remover")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onRemove(CommandSender sender, OnlinePlayer target, Double money) {
        final String senderName = sender.getName();
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();

        moneyx.removeMoney(targetName, money);
        messages.sendMessage("eco.eco-remove", "%money%" ,money, "%target_name%", targetName, sender);
        messages.sendMessage("eco.eco-rremove", "%money%", money, "%target_name%", senderName, targetP);
    }

    @Subcommand("give|dar")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    public void onGive(CommandSender sender, OnlinePlayer target, Double money) {
        final String senderName = sender.getName();
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();

        moneyx.addMoney(targetName, money);
        messages.sendMessage("eco.eco-give", "%money%", money, "%target_name%", targetName, sender);
        messages.sendMessage("eco.eco-receive", "%money%", money, "%target_name%", senderName, targetP);
    }

}
