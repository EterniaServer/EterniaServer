package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.generics.APIExperience;
import br.com.eterniaserver.eterniaserver.generics.UtilInternMethods;

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
public class Experience extends BaseCommand {

    @Default
    @HelpCommand
    @Syntax("<página>")
    @Description(" Ajuda para o sistema de experiência")
    public void xpHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("set")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.xp.admin")
    @Description(" Define o nível do jogador")
    public void onSet(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=9999999") Integer money) {
        final Player targetP = target.getPlayer();
        String playerDisplay = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();
        targetP.setLevel(money);
        EterniaServer.configs.sendMessage(sender, Messages.EXP_SET_FROM, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        EterniaServer.configs.sendMessage(targetP, Messages.EXP_SETED, String.valueOf(money), sender.getName(), playerDisplay);
    }

    @Subcommand("take")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.xp.admin")
    @Description(" Retira uma quantidade de nível do jogador")
    public void onTake(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=9999999") Integer money) {
        final Player targetP = target.getPlayer();
        targetP.setLevel(targetP.getLevel() - money);
        String playerDisplay = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();
        EterniaServer.configs.sendMessage(sender, Messages.EXP_REMOVE_FROM, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        EterniaServer.configs.sendMessage(targetP, Messages.EXP_REMOVED, String.valueOf(money), sender.getName(), playerDisplay);
    }

    @Subcommand("give")
    @CommandCompletion("@players 100")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.xp.admin")
    @Description(" Dá uma quantidade de nível do jogador")
    public void onGive(CommandSender sender, OnlinePlayer target, @Conditions("limits:min=1,max=9999999") Integer money) {
        final Player targetP = target.getPlayer();
        targetP.setLevel(targetP.getLevel() + money);
        String playerDisplay = sender instanceof Player ? ((Player) sender).getDisplayName() : sender.getName();
        EterniaServer.configs.sendMessage(sender, Messages.EXP_GIVE_FROM, String.valueOf(money), targetP.getName(), targetP.getDisplayName());
        EterniaServer.configs.sendMessage(targetP, Messages.EXP_GIVED, String.valueOf(money), sender.getName(), playerDisplay);
    }

    @Subcommand("check")
    @Description(" Verifica quantos leveis você possui guardado")
    public void onCheckLevel(Player player) {
        int lvl = player.getLevel();
        float xp = player.getExp();
        player.setLevel(0);
        player.setExp(0);
        player.giveExp(APIExperience.getExp(UUIDFetcher.getUUIDOf(player.getName())));
        EterniaServer.configs.sendMessage(player, Messages.EXP_BALANCE, String.valueOf(player.getLevel()));
        player.setLevel(lvl);
        player.setExp(xp);
    }

    @Subcommand("bottle")
    @CommandCompletion("10")
    @Syntax("<quantia>")
    @Description(" Converte uma quantia de nível para uma garra de EXP")
    public void onBottleLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer xpWant) {
        int xpReal = UtilInternMethods.getXPForLevel(player.getLevel());
        if (xpWant > 0 && xpReal > xpWant) {
            ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&eGarrafa de EXP&8]"));
            item.setItemMeta(meta);
            item.setLore(Collections.singletonList(String.valueOf(xpWant)));
            PlayerInventory inventory = player.getInventory();
            inventory.addItem(item);
            EterniaServer.configs.sendMessage(player, Messages.EXP_BOTTLED);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xpReal - xpWant);
        } else {
            EterniaServer.configs.sendMessage(player, Messages.EXP_INSUFFICIENT);
        }
    }

    @Subcommand("withdraw")
    @CommandCompletion("10")
    @Syntax("<quantia>")
    @Description(" Retira uma quantia de nível")
    public void onWithdrawLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer level) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        int xpla = UtilInternMethods.getXPForLevel(level);
        if (APIExperience.getExp(uuid) >= xpla) {
            APIExperience.removeExp(uuid, xpla);
            player.giveExp(xpla);
            EterniaServer.configs.sendMessage(player, Messages.EXP_WITHDRAW, String.valueOf(level));
        } else {
            EterniaServer.configs.sendMessage(player, Messages.EXP_BOTTLED);
        }
    }

    @Subcommand("deposit")
    @CommandCompletion("10")
    @Syntax("<quantia>")
    @Description(" Guarda uma quantia de nível")
    public void onDepositLevel(Player player, @Conditions("limits:min=1,max=9999999")  Integer xpla) {
        int xpAtual = player.getLevel();
        if (xpAtual >= xpla) {
            int xp = UtilInternMethods.getXPForLevel(xpla);
            int xpto = UtilInternMethods.getXPForLevel(xpAtual);
            APIExperience.addExp(UUIDFetcher.getUUIDOf(player.getName()), xp);
            EterniaServer.configs.sendMessage(player, Messages.EXP_DEPOSIT, String.valueOf(xpla));
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xpto - xp);
        } else {
            EterniaServer.configs.sendMessage(player, Messages.EXP_BOTTLED);
        }
    }

}
