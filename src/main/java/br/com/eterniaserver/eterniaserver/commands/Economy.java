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
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CommandAlias("%eco")
public class Economy extends BaseCommand {

    private final EterniaServer plugin;

    private final String baltopName;

    public Economy(final EterniaServer plugin, final String baltopName) {
        this.plugin = plugin;
        this.baltopName = baltopName;
    }

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

        EterniaServer.getEconomyAPI().setMoney(target.getUUID(), money);
        plugin.sendMessage(sender, Messages.ECO_SET_FROM, String.valueOf(money), target.getName(), target.getDisplayName());
        plugin.sendMessage(target.getPlayer(), Messages.ECO_SETED, String.valueOf(money), user.getName(), user.getDisplayName());
    }

    @CommandCompletion("@players 100")
    @Syntax("%eco_take_syntax")
    @Subcommand("%eco_take")
    @Description("%eco_take_description")
    @CommandPermission("%eco_take_perm")
    public void onRemove(CommandSender sender, OnlinePlayer targets, @Conditions("limits:min=1,max=2147483647") Double money) {
        User user = new User(sender);
        User target = new User(targets.getPlayer());

        EterniaServer.getEconomyAPI().removeMoney(target.getUUID(), money);
        plugin.sendMessage(sender, Messages.ECO_REMOVE_FROM, String.valueOf(money), target.getName(), target.getDisplayName());
        plugin.sendMessage(target.getPlayer(), Messages.ECO_REMOVED, String.valueOf(money), user.getName(), user.getDisplayName());
    }

    @CommandCompletion("@players 100")
    @Syntax("%eco_give_syntax")
    @Subcommand("%eco_give")
    @Description("%eco_give_description")
    @CommandPermission("%eco_give_perm")
    public void onGive(CommandSender sender, OnlinePlayer targets, @Conditions("limits:min=1,max=2147483647") Double money) {
        User user = new User(sender);
        User target = new User(targets.getPlayer());

        EterniaServer.getEconomyAPI().addMoney(target.getUUID(), money);
        plugin.sendMessage(sender, Messages.ECO_GIVE_FROM, String.valueOf(money), target.getName(), target.getDisplayName());
        plugin.sendMessage(target.getPlayer(), Messages.ECO_GIVED, String.valueOf(money), user.getName(), user.getDisplayName());
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
            plugin.sendMessage(player, Messages.ECO_BALANCE, EterniaServer.getEconomyAPI().format(EterniaServer.getEconomyAPI().getMoney(user.getUUID())));
            return;
        }

        User target = new User(targets.getPlayer());

        if (player.hasPermission(plugin.getString(Strings.PERM_MONEY_OTHER))) {
            plugin.sendMessage(player, Messages.ECO_BALANCE_OTHER, EterniaServer.getEconomyAPI().format(EterniaServer.getEconomyAPI().getMoney(target.getUUID())), target.getName(), target.getDisplayName());
            return;
        }

        plugin.sendMessage(player, Messages.SERVER_NO_PERM);
    }

    @CommandCompletion("@players 100")
    @Syntax("%eco_pay_syntax")
    @Subcommand("%eco_pay")
    @CommandAlias("%eco_pay_aliases")
    @Description("%eco_pay_description")
    @CommandPermission("%eco_pay_perm")
    public void onPay(Player player, OnlinePlayer targets, @Conditions("limits:min=1,max=2147483647") Double value) {
        User user = new User(player);
        User target = new User(targets.getPlayer());

        if (target.getName().equals(user.getName())) {
            plugin.sendMessage(player, Messages.ECO_AUTO_PAY);
            return;
        }

        if (!EterniaServer.getEconomyAPI().hasMoney(user.getUUID(), value)) {
            plugin.sendMessage(player, Messages.ECO_NO_MONEY);
            return;
        }

        EterniaServer.getEconomyAPI().addMoney(target.getUUID(), value);
        EterniaServer.getEconomyAPI().removeMoney(user.getUUID(), value);
        plugin.sendMessage(player, Messages.ECO_PAY, String.valueOf(value), target.getName(), target.getDisplayName());
        plugin.sendMessage(target.getPlayer(), Messages.ECO_PAY_RECEIVED, String.valueOf(value), user.getName(), user.getDisplayName());
    }

    @Subcommand("%eco_baltop")
    @CommandAlias("%eco_baltop_aliases")
    @Description("%eco_baltop_description")
    @CommandPermission("%eco_baltop_perm")
    public void onBaltop(CommandSender sender, @Optional Integer page) {
        if (page == null) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - EterniaServer.getEconomyAPI().getBalanceTopTime()) <= 300) {
                showBaltop(sender, 0);
                return;
            }
            EterniaServer.getEconomyAPI().updateBalanceTop().thenRun(() -> showBaltop(sender, 0));
            return;
        }

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - EterniaServer.getEconomyAPI().getBalanceTopTime()) <= 300) {
            showBaltop(sender, page);
            return;
        }
        EterniaServer.getEconomyAPI().updateBalanceTop().thenRun(() -> showBaltop(sender, page));
    }

    private void showBaltop(CommandSender sender, int page) {
        List<Map.Entry<UUID, Double>> entryList = new ArrayList<>(EterniaServer.getEconomyAPI().getBalanceTop().entrySet());
        int maxPage = entryList.size() / 10;
        if (maxPage == 0) {
            maxPage = 1;
        }
        if (page >= maxPage || page < 0) {
            plugin.sendMessage(sender, Messages.ECO_BALTOP_PAGE_LIMIT, true);
            return;
        }
        sender.sendMessage(plugin.getMessage(Messages.ECO_BALTOP_TITLE, true, String.valueOf(page + 1)));

        int pagecontar = page * 10;

        for (int i = pagecontar; i < pagecontar + 10; i++) {
            if (entryList.size() > i) {
                Map.Entry<UUID, Double> entry = entryList.get(i);
                User user = new User(entry.getKey());
                plugin.sendMessage(sender, Messages.ECO_BALTOP_LIST, false, " " + user.getName(), " " + user.getDisplayName(), EterniaServer.getEconomyAPI().format(entry.getValue()));
            } else {
                break;
            }
        }

        final BaseComponent[] baseComponents = new BaseComponent[3];
        final TextComponent textComponentLeft = new TextComponent(" <<<");
        if ((page - 1) > 0) {
            textComponentLeft.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + baltopName + " " + (page - 1)));
        }
        final TextComponent textComponentRight = new TextComponent(">>>");
        if ((page + 1) < maxPage) {
            textComponentRight.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + baltopName + " " + (page + 1)));
        }
        final TextComponent textComponentCenter = new TextComponent(plugin.getMessage(Messages.ECO_BALTOP_PAGE, false));
        baseComponents[0] = textComponentLeft;
        baseComponents[1] = textComponentCenter;
        baseComponents[2] = textComponentRight;
        sender.sendMessage(baseComponents);


    }



}
