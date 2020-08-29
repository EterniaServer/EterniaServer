package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.strings.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.UUID;

@CommandAlias("xp")
@CommandPermission("eternia.xp.user")
public class BaseCmdExperience extends BaseCommand {

    @Default
    public void xpHelp(CommandSender sender) {
        sender.sendMessage(Strings.MSG_XP_HELP_TITLE);
        if (sender.hasPermission("eternia.xp.admin")) {
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(Constants.COMMANDS, "xp set &3<jogador> <quantia>")
                    .replace(Constants.MESSAGE, Strings.MSG_XP_HELP_SET)));
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(Constants.COMMANDS, "xp take &3<jogador> <quantia>")
                    .replace(Constants.MESSAGE, Strings.MSG_XP_HELP_TAKE)));
            sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                    .replace(Constants.COMMANDS, "xp give &3<jogador> <quantia>")
                    .replace(Constants.MESSAGE, Strings.MSG_XP_HELP_GIVE)));
        }
        sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                .replace(Constants.COMMANDS, "xp check")
                .replace(Constants.MESSAGE, Strings.MSG_XP_HELP_CHECK)));
        sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                .replace(Constants.COMMANDS, "xp bottle &3<quantia>")
                .replace(Constants.MESSAGE, Strings.MSG_XP_HELP_BOTTLE)));
        sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                .replace(Constants.COMMANDS, "xp deposit &3<quantia>")
                .replace(Constants.MESSAGE, Strings.MSG_XP_HELP_DEPOSIT)));
        sender.sendMessage(Strings.getColor(Strings.MSG_HELP_FORMAT
                .replace(Constants.COMMANDS, "xp withdraw &3<quantia>")
                .replace(Constants.MESSAGE, Strings.MSG_XP_HELP_WITHDRAW)));
    }

    @Subcommand("set")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.xp.admin")
    public void onSet(CommandSender sender, OnlinePlayer target, int money) {
        final Player targetP = target.getPlayer();

        targetP.setLevel(money);
        sender.sendMessage(InternMethods.putName(targetP, Strings.M_XP_SET.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(InternMethods.putName(sender, Strings.M_XP_RSET.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("take")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.xp.admin")
    public void onTake(CommandSender sender, OnlinePlayer target, int money) {
        final Player targetP = target.getPlayer();

        targetP.setLevel(targetP.getLevel() - money);
        sender.sendMessage(InternMethods.putName(targetP, Strings.M_XP_REMOVE.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(InternMethods.putName(sender, Strings.M_XP_RREMOVE.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("give")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.xp.admin")
    public void onGive(CommandSender sender, OnlinePlayer target, int money) {
        final Player targetP = target.getPlayer();

        targetP.setLevel(targetP.getLevel() + money);
        sender.sendMessage(InternMethods.putName(targetP, Strings.M_XP_GIVE.replace(Constants.AMOUNT, String.valueOf(money))));
        targetP.sendMessage(InternMethods.putName(sender, Strings.M_XP_RECEIVE.replace(Constants.AMOUNT, String.valueOf(money))));
    }

    @Subcommand("check")
    public void onCheckLevel(Player player) {
        int lvl = player.getLevel();
        float xp = player.getExp();
        player.setLevel(0);
        player.setExp(0);
        player.giveExp(APIExperience.getExp(UUIDFetcher.getUUIDOf(player.getName())));
        player.sendMessage(Strings.M_XP_CHECK.replace(Constants.AMOUNT, String.valueOf(player.getLevel())));
        player.setLevel(lvl);
        player.setExp(xp);
    }

    @Subcommand("bottle")
    @CommandCompletion("10")
    @Syntax("<quantia>")
    public void onBottleLevel(Player player, Integer xpWant) {
        int xpReal = InternMethods.getXPForLevel(player.getLevel());
        if (xpWant > 0 && xpReal > xpWant) {
            ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&eGarrafa de EXP&8]"));
            item.setItemMeta(meta);
            item.setLore(Collections.singletonList(String.valueOf(xpWant)));
            PlayerInventory inventory = player.getInventory();
            inventory.addItem(item);
            player.sendMessage(Strings.M_XP_BOTTLE);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xpReal - xpWant);
        } else {
            player.sendMessage(Strings.M_XP_INSUFFICIENT);
        }
    }

    @Subcommand("withdraw")
    @CommandCompletion("10")
    @Syntax("<quantia>")
    public void onWithdrawLevel(Player player, Integer level) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        int xpla = InternMethods.getXPForLevel(level);
        if (APIExperience.getExp(uuid) >= xpla) {
            APIExperience.removeExp(uuid, xpla);
            player.giveExp(xpla);
            player.sendMessage(Strings.M_XP_WITHDRAW.replace(Constants.AMOUNT, String.valueOf(player.getLevel())));
        } else {
            player.sendMessage(Strings.M_XP_INSUFFICIENT);
        }
    }

    @Subcommand("deposit")
    @CommandCompletion("10")
    @Syntax("<quantia>")
    public void onDepositLevel(Player player, Integer xpla) {
        int xpAtual = player.getLevel();
        if (xpAtual >= xpla) {
            int xp = InternMethods.getXPForLevel(xpla);
            int xpto = InternMethods.getXPForLevel(xpAtual);
            APIExperience.addExp(UUIDFetcher.getUUIDOf(player.getName()), xp);
            player.sendMessage(Strings.M_XP_DEPOSIT.replace(Constants.AMOUNT, String.valueOf(xpla)));
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xpto - xp);
        } else {
            player.sendMessage(Strings.M_XP_INSUFFICIENT);
        }
    }

}
