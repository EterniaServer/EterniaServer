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
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.cash.Entities.CashBalance;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @CommandAlias("%cash")
    static class Cash extends BaseCommand {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        private final Services.Cash cashService;

        public Cash(final EterniaServer plugin, final Services.Cash cashService) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
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
            if (targetName != null || sender instanceof Player) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    if (targetName != null) {
                        UUID uuid = EterniaLib.getUUIDOf(targetName);
                        PlayerProfile target = databaseInterface.get(PlayerProfile.class, uuid);
                        if (target.getUuid() != null) {
                            CashBalance cashBalance = databaseInterface.get(CashBalance.class, uuid);
                            plugin.sendMiniMessages(
                                    sender,
                                    Messages.CASH_BALANCE_OTHER,
                                    target.getPlayerName(),
                                    target.getPlayerDisplay(),
                                    String.valueOf(cashBalance.getBalance())
                            );
                        }
                        else {
                            plugin.sendMiniMessages(sender, Messages.SERVER_NO_PLAYER);

                        }
                        return;
                    }

                    Player playerObject = (Player) sender;
                    CashBalance cash = databaseInterface.get(CashBalance.class, playerObject.getUniqueId());
                    plugin.sendMiniMessages(sender, Messages.CASH_BALANCE, String.valueOf(cash.getBalance()));
                });
                return;
            }

            plugin.sendMiniMessages(sender, Messages.SERVER_NO_PLAYER);
        }

        @Subcommand("%CASH_ACCEPT")
        @Description("%CASH_ACCEPT_DESCRIPTION")
        @CommandPermission("%CASH_ACCEPT_PERM")
        public void onCashAccept(Player player) {
            UUID uuid = player.getUniqueId();
            Utils.BuyingItem buyingItem = cashService.getCashBuy(uuid);
            if (buyingItem == null) {
                plugin.sendMiniMessages(player, Messages.CASH_NOTHING_TO_BUY);
                return;
            }

            for (String line : buyingItem.getCommands()) {
                String modifiedCommand = plugin.setPlaceholders(player, line);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), modifiedCommand);
            }

            for (String line : buyingItem.getMessages()) {
                Component modifiedText = plugin.parseColor(plugin.setPlaceholders(player, line));
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
            UUID uuid = player.getUniqueId();
            Utils.BuyingItem buyingItem = cashService.getCashBuy(uuid);
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
            UUID uuid = player.getUniqueId();
            Player target = onlineTarget.getPlayer();

            if (!EterniaServer.getCashAPI().has(uuid, value)) {
                plugin.sendMiniMessages(player, Messages.CASH_NO_CASH);
                return;
            }

            EterniaServer.getCashAPI().withdrawBalance(uuid, value);
            EterniaServer.getCashAPI().depositBalance(target.getUniqueId(), value);
            PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, uuid);
            PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());

            plugin.sendMiniMessages(
                    target,
                    Messages.CASH_RECEVEID,
                    String.valueOf(value),
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay()
            );
            plugin.sendMiniMessages(
                    player,
                    Messages.CASH_SENT,
                    String.valueOf(value),
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay()
            );
        }

        @Subcommand("%CASH_GIVE")
        @CommandCompletion("@players 10")
        @Syntax("%CASH_GIVE_SYNTAX")
        @Description("%CASH_GIVE_DESCRIPTION")
        @CommandPermission("%CASH_GIVE_PERM")
        public void onCashGive(CommandSender sender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=9999999") Integer value) {
            Player target = onlineTarget.getPlayer();
            UUID targetUUID = target.getUniqueId();

            EterniaServer.getCashAPI().depositBalance(targetUUID, value);
            PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, targetUUID);

            plugin.sendMiniMessages(
                    sender,
                    Messages.CASH_SENT,
                    String.valueOf(value),
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay()
            );

            if (sender instanceof Player player) {
                PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());
                plugin.sendMiniMessages(
                        target,
                        Messages.CASH_RECEVEID,
                        String.valueOf(value),
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
            }
            else {
                plugin.sendMiniMessages(
                        target,
                        Messages.CASH_RECEVEID,
                        String.valueOf(value),
                        sender.getName(),
                        sender.getName()
                );
            }
        }

        @Subcommand("%CASH_REMOVE")
        @CommandCompletion("@players 10")
        @Syntax("%CASH_REMOVE_SYNTAX")
        @CommandPermission("%CASH_REMOVE_PERM")
        @Description("%cCASH_REMOVE_DESCRIPTION")
        public void onCashRemove(CommandSender sender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=9999999") Integer value) {
            Player target = onlineTarget.getPlayer();
            UUID targetUUID = target.getUniqueId();

            EterniaServer.getCashAPI().withdrawBalance(targetUUID, value);
            PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, targetUUID);

            plugin.sendMiniMessages(
                    sender,
                    Messages.CASH_REMOVED,
                    String.valueOf(value),
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay()
            );
            if (sender instanceof Player player) {
                PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());
                plugin.sendMiniMessages(
                        target,
                        Messages.CASH_LOST,
                        String.valueOf(value),
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
            }
            else {
                plugin.sendMiniMessages(
                        target,
                        Messages.CASH_LOST,
                        String.valueOf(value),
                        sender.getName(),
                        sender.getName()
                );
            }
        }

    }

}
