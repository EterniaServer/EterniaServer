package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.acf.BaseCommand;
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
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.BuyingItem;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

final class Commands {

    @CommandAlias("%cash")
    static class Cash extends BaseCommand {

        private final EterniaServer plugin;

        private final Services.Cash cashService;

        public Cash(final EterniaServer plugin, final Services.Cash cashService) {
            this.plugin = plugin;
            this.cashService = cashService;
        }

        @CatchUnknown
        @HelpCommand
        @Subcommand("%CASH_HELP")
        @Syntax("%CASH_HELP_SYNTAX")
        @Description("%CASH_HELP_DESCRIPTION")
        @CommandPermission("%CASH_HELP_PERM")
        public void onCashHelp(CommandHelp help) {
            help.showHelp();
        }

        @Default
        @Description("%CASH_DESCRIPTION")
        @CommandPermission("%CASH_PERM")
        public void onCash(Player player) {
            player.closeInventory();
            player.openInventory(EterniaServer.getGuiAPI().getGUI(plugin.getString(Strings.CASH_MENU_TITLE), player));
        }

        @Subcommand("%CASH_BALANCE")
        @CommandCompletion("@players")
        @Syntax("%CASH_BALANCE_SYNTAX")
        @Description("%CASH_BALANCE_DESCRIPTION")
        @CommandPermission("%CASH_BALANCE_PERM")
        public void onCashBalance(CommandSender sender, @Optional String targetName) {
            if (targetName != null) {
                final UUID uuid = UUIDFetcher.getUUIDOf(targetName);
                final PlayerProfile target = EterniaServer.getUserAPI().getProfile(uuid);
                plugin.sendMiniMessages(sender, Messages.CASH_BALANCE_OTHER, target.getName(), target.getDisplayName(), String.valueOf(target.getCash()));
                return;
            }

            if (sender instanceof Player playerObject) {
                final PlayerProfile player = EterniaServer.getUserAPI().getProfile(playerObject.getUniqueId());
                plugin.sendMiniMessages(sender, Messages.CASH_BALANCE, String.valueOf(player.getCash()));
                return;
            }

            plugin.sendMiniMessages(sender, Messages.SERVER_NO_PLAYER);
        }

        @Subcommand("%CASH_ACCEPT")
        @Description("%CASH_ACCEPT_DESCRIPTION")
        @CommandPermission("%CASH_ACCEPT_PERM")
        public void onCashAccept(Player player) {
            final UUID uuid = player.getUniqueId();
            final BuyingItem buyingItem = cashService.getCashBuy(uuid);
            if (buyingItem == null) {
                plugin.sendMiniMessages(player, Messages.CASH_NOTHING_TO_BUY);
                return;
            }

            for (String line : buyingItem.getCommands()) {
                final String modifiedCommand = plugin.setPlaceholders(player, line);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), modifiedCommand);
            }

            for (String line : buyingItem.getMessages()) {
                final Component modifiedText = plugin.parseColor(plugin.setPlaceholders(player, line));
                player.sendMessage(modifiedText);
            }

            EterniaServer.getCashAPI().withdrawBalance(uuid, buyingItem.getCost());
            cashService.removeCashBuy(uuid);
            plugin.sendMiniMessages(player, Messages.CASH_BOUGHT);
        }

        @Subcommand("%CASH_DENY")
        @Description("%CASH_DENY_DESCRIPTION")
        @CommandPermission("%CASH_DENY_PERM")
        public void onCashDeny(Player player) {
            final UUID uuid = player.getUniqueId();
            final BuyingItem buyingItem = cashService.getCashBuy(uuid);
            if (buyingItem == null) {
                plugin.sendMiniMessages(player, Messages.CASH_NOTHING_TO_BUY);
                return;
            }

            plugin.sendMiniMessages(player, Messages.CASH_CANCELED);
            cashService.removeCashBuy(uuid);
        }

        @Subcommand("%CASH_PAY")
        @CommandCompletion("@players 10")
        @Syntax("%CASH_PAY_SYNTAX")
        @Description("%CASH_PAY_DESCRIPTION")
        @CommandPermission("%CASH_PAY_PERM")
        public void onCashPay(Player player, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=9999999") Integer value) {
            final UUID uuid = player.getUniqueId();
            final Player target = onlineTarget.getPlayer();
            if (!EterniaServer.getCashAPI().has(uuid, value)) {
                plugin.sendMiniMessages(player, Messages.CASH_NO_CASH);
                return;
            }

            EterniaServer.getCashAPI().withdrawBalance(uuid, value);
            EterniaServer.getCashAPI().depositBalance(target.getUniqueId(), value);
            final PlayerProfile playerProfile = EterniaServer.getUserAPI().getProfile(uuid);
            final PlayerProfile targetProfile = EterniaServer.getUserAPI().getProfile(target.getUniqueId());

            plugin.sendMiniMessages(target, Messages.CASH_RECEVEID, String.valueOf(value), playerProfile.getName(), playerProfile.getDisplayName());
            plugin.sendMiniMessages(player, Messages.CASH_SENT, String.valueOf(value), targetProfile.getName(), targetProfile.getDisplayName());
        }

        @Subcommand("%CASH_GIVE")
        @CommandCompletion("@players 10")
        @Syntax("%CASH_GIVE_SYNTAX")
        @Description("%CASH_GIVE_DESCRIPTION")
        @CommandPermission("%CASH_GIVE_PERM")
        public void onCashGive(CommandSender sender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=9999999") Integer value) {
            final Player target = onlineTarget.getPlayer();
            final UUID targetUUID = target.getUniqueId();

            EterniaServer.getCashAPI().depositBalance(targetUUID, value);
            PlayerProfile targetProfile = EterniaServer.getUserAPI().getProfile(targetUUID);

            plugin.sendMiniMessages(sender, Messages.CASH_SENT, String.valueOf(value), targetProfile.getName(), targetProfile.getDisplayName());
            if (sender instanceof Player player) {
                PlayerProfile playerProfile = EterniaServer.getUserAPI().getProfile(player.getUniqueId());
                plugin.sendMiniMessages(target, Messages.CASH_RECEVEID, String.valueOf(value), playerProfile.getName(), playerProfile.getDisplayName());
            }
            else {
                plugin.sendMiniMessages(target, Messages.CASH_RECEVEID, String.valueOf(value), sender.getName(), sender.getName());
            }
        }

        @Subcommand("%CASH_REMOVE")
        @CommandCompletion("@players 10")
        @Syntax("%CASH_REMOVE_SYNTAX")
        @CommandPermission("%CASH_REMOVE_PERM")
        @Description("%cCASH_REMOVE_DESCRIPTION")
        public void onCashRemove(CommandSender sender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=9999999") Integer value) {
            final Player target = onlineTarget.getPlayer();
            final UUID targetUUID = target.getUniqueId();

            EterniaServer.getCashAPI().withdrawBalance(targetUUID, value);
            PlayerProfile targetProfile = EterniaServer.getUserAPI().getProfile(targetUUID);

            plugin.sendMiniMessages(sender, Messages.CASH_REMOVED, String.valueOf(value), targetProfile.getName(), targetProfile.getDisplayName());
            if (sender instanceof Player player) {
                PlayerProfile playerProfile = EterniaServer.getUserAPI().getProfile(player.getUniqueId());
                plugin.sendMiniMessages(target, Messages.CASH_LOST, String.valueOf(value), playerProfile.getName(), playerProfile.getDisplayName());
            }
            else {
                plugin.sendMiniMessages(target, Messages.CASH_LOST, String.valueOf(value), sender.getName(), sender.getName());
            }
        }

    }

}
