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
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@CommandAlias("%xp")
public class Experience extends BaseCommand {

    private final EterniaServer plugin;

    public Experience(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("%xp_syntax")
    @CommandPermission("%xp_perm")
    @Description("%xp_description")
    public void xpHelp(CommandHelp help) {
        help.showHelp();
    }

    @CommandCompletion("@players 100")
    @Syntax("%xp_set_syntax")
    @Subcommand("%xp_set")
    @Description("%xp_set_description")
    @CommandPermission("%xp_set_perm")
    public void onSet(CommandSender sender, OnlinePlayer targets, @Conditions("limits:min=1,max=9999999") Integer amount) {
        User user = new User(sender);
        User target = new User(targets.getPlayer());

        target.setLevel(amount);
        plugin.sendMessage(sender, Messages.EXP_SET_FROM, String.valueOf(amount), target.getName(), target.getDisplayName());
        plugin.sendMessage(target.getPlayer(), Messages.EXP_SETED, String.valueOf(amount), user.getName(), user.getDisplayName());
    }

    @CommandCompletion("@players 100")
    @Syntax("%xp_take_syntax")
    @Subcommand("%xp_take")
    @Description("%xp_take_description")
    @CommandPermission("%xp_take_perm")
    public void onTake(CommandSender sender, OnlinePlayer targets, @Conditions("limits:min=1,max=9999999") Integer amount) {
        User user = new User(sender);
        User target = new User(targets.getPlayer());

        target.setLevel(target.getLevel() - amount);
        plugin.sendMessage(sender, Messages.EXP_REMOVE_FROM, String.valueOf(amount), target.getName(), target.getDisplayName());
        plugin.sendMessage(target.getPlayer(), Messages.EXP_REMOVED, String.valueOf(amount), user.getName(), user.getDisplayName());
    }

    @CommandCompletion("@players 100")
    @Syntax("%xp_give_syntax")
    @Subcommand("%xp_give")
    @Description("%xp_give_description")
    @CommandPermission("%xp_give_perm")
    public void onGive(CommandSender sender, OnlinePlayer targets, @Conditions("limits:min=1,max=9999999") Integer amount) {
        User user = new User(sender);
        User target = new User(targets.getPlayer());

        target.setLevel(target.getLevel() + amount);
        plugin.sendMessage(sender, Messages.EXP_GIVE_FROM, String.valueOf(amount), target.getName(), target.getDisplayName());
        plugin.sendMessage(target.getPlayer(), Messages.EXP_GIVED, String.valueOf(amount), user.getName(), user.getDisplayName());
    }

    @Subcommand("%xp_check")
    @Description("%xp_check_description")
    @CommandPermission("%xp_check_perm")
    public void onCheckLevel(Player player) {
        User user = new User(player);
        int lvl = user.getLevel();
        float xp = user.getGameExp();
        user.setLevel(0);
        user.setGameExp(0);
        user.giveGameExp(user.getExp());
        plugin.sendMessage(player, Messages.EXP_BALANCE, String.valueOf(user.getLevel()));
        user.setLevel(lvl);
        user.setGameExp(xp);
    }

    @CommandCompletion("10")
    @Syntax("%xp_bottle_syntax")
    @Subcommand("%xp_bottle")
    @Description("%xp_bottle_description")
    @CommandPermission("%xp_bottle_perm")
    public void onBottleLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer xpWant) {
        User user = new User(player);

        int xpReal = plugin.getXPForLevel(user.getLevel());
        if (xpWant <= 0 || xpReal <= xpWant) {
            plugin.sendMessage(player, Messages.EXP_INSUFFICIENT);
            return;
        }

        final ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        final ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER, 0);
        meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_INT_VALUE), PersistentDataType.INTEGER, xpWant);
        meta.setDisplayName(plugin.getString(Strings.BOTTLE_EXP_NAME));
        meta.setLore(List.of(ChatColor.GREEN + String.valueOf(xpWant)));
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
        plugin.sendMessage(player, Messages.EXP_BOTTLED);
        user.setLevel(0);
        user.setGameExp(0);
        user.giveGameExp(xpReal - xpWant);
    }

    @CommandCompletion("10")
    @Syntax("%xp_withdraw_syntax")
    @Subcommand("%xp_withdraw")
    @Description("%xp_withdraw_description")
    @CommandPermission("%xp_withdraw_perm")
    public void onWithdrawLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer level) {
        User user = new User(player);

        int xpla = plugin.getXPForLevel(level);
        if (user.getExp() < xpla) {
            plugin.sendMessage(player, Messages.EXP_INSUFFICIENT);
            return;
        }

        user.removeExp(xpla);
        user.giveGameExp(xpla);
        plugin.sendMessage(player, Messages.EXP_WITHDRAW, String.valueOf(level));
    }

    @CommandCompletion("10")
    @Syntax("%xp_deposit_syntax")
    @Subcommand("%xp_deposit")
    @Description("%xp_deposit_description")
    @CommandPermission("%xp_deposit_perm")
    public void onDepositLevel(Player player, @Conditions("limits:min=1,max=9999999")  Integer xpla) {
        User user = new User(player);

        int xpAtual = user.getLevel();
        if (xpAtual < xpla) {
            plugin.sendMessage(player, Messages.EXP_INSUFFICIENT);
            return;
        }

        int xp = plugin.getXPForLevel(xpla);
        int xpto = plugin.getXPForLevel(xpAtual);
        user.addExp(xp);
        user.setLevel(0);
        user.setGameExp(0);
        user.giveGameExp(xpto - xp);
        plugin.sendMessage(player, Messages.EXP_DEPOSIT, String.valueOf(xpla));
    }

}
