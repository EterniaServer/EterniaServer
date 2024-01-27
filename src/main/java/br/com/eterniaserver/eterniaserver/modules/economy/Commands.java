package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eternialib.database.dtos.SearchField;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Utils;
import br.com.eterniaserver.eterniaserver.modules.economy.Utils.AffiliateCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @CommandAlias("%ECONOMY")
    static class EconomyGeneric extends BaseCommand {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        public EconomyGeneric(EterniaServer plugin) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
        }

        @Default
        @CatchUnknown
        @HelpCommand
        @Syntax("%ECONOMY_SYNTAX")
        @CommandPermission("%ECONOMY_PERM")
        @Description("%ECONOMY_DESCRIPTION")
        public void onEconomyHelp(CommandHelp help) {
            help.showHelp();
        }

        @CommandCompletion("1")
        @CommandAlias("%ECONOMY_BALTOP")
        @Syntax("%ECONOMY_BALTOP_SYNTAX")
        @Description("%ECONOMY_BALTOP_DESCRIPTION")
        @CommandPermission("%ECONOMY_BALTOP_PERM")
        public void onBalanceTop(CommandSender commandSender, @Optional Integer page) {
            List<Entities.EcoBalance> balances = EterniaServer.getExtraEconomyAPI().getBalanceTop();

            int pageSize = 10;
            int maxPage = balances.size() / pageSize;

            if (maxPage == 0) {
                maxPage = 1;
            }
            if (page == null) {
                page = 1;
            }

            if (page < 1 || page > maxPage) {
                plugin.sendMiniMessages(commandSender, Messages.ECO_PAGE_LIMIT, String.valueOf(maxPage));
                return;
            }

            int startIndex = (page - 1) * pageSize;

            plugin.sendMiniMessages(commandSender, Messages.ECO_BALTOP_TITLE, String.valueOf(page));

            System.out.println(startIndex);
            System.out.println(pageSize);
            for (int i = startIndex; i < balances.size() && i < (startIndex + pageSize); i++) {
                Entities.EcoBalance ecoBalance = balances.get(i);
                UUID uuid = ecoBalance.getUuid();
                double balance = ecoBalance.getBalance();

                PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, uuid);

                plugin.sendMiniMessages(
                        commandSender,
                        Messages.ECO_BALTOP_LIST,
                        false,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        EterniaServer.getEconomyAPI().format(balance)
                );
            }

           Component component = Component.empty();
            if ((page - 1) > 0) {
                component = component.append(Component.text(" <<<").clickEvent(ClickEvent.clickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/" + plugin.getString(Strings.ECO_BALTOP_COMMAND_NAME) + " " + (page - 1)
                )));
            }
            component = component.append(plugin.parseColor(plugin.getMessage(Messages.ECO_PAGE, false)));
            if ((page + 1) < maxPage) {
                component = component.append(Component.text(">>> ").clickEvent(ClickEvent.clickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/" + plugin.getString(Strings.ECO_BALTOP_COMMAND_NAME) + " " + (page + 1))
                ));
            }
            commandSender.sendMessage(component);
        }

        @CommandCompletion("@players")
        @CommandAlias("%ECONOMY_BALANCE")
        @Syntax("%ECONOMY_BALANCE_SYNTAX")
        @Description("%ECONOMY_BALANCE_DESCRIPTION")
        @CommandPermission("%ECONOMY_BALANCE_PERM")
        public void onBalance(CommandSender commandSender, @Optional OnlinePlayer onlineTarget) {
            Player target;
            if (onlineTarget == null && commandSender instanceof Player player) {
                target = player;
            } else if (onlineTarget == null) {
                plugin.sendMiniMessages(commandSender, Messages.ECO_BALANCE_IN_CONSOLE);
                return;
            } else {
                target = onlineTarget.getPlayer();
            }

            PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());
            double value = EterniaServer.getEconomyAPI().getBalance(target);

            plugin.sendMiniMessages(
                    commandSender,
                    Messages.ECO_BALANCE,
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );
        }

        @CommandCompletion("@players 10")
        @CommandAlias("%ECONOMY_PAY")
        @Syntax("%ECONOMY_PAY_SYNTAX")
        @Description("%ECONOMY_PAY_DESCRIPTION")
        @CommandPermission("%ECONOMY_PAY_PERM")
        public void onPay(Player player, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=2147483647") Double value) {
            Player target = onlineTarget.getPlayer();

            if (player.getUniqueId().equals(target.getUniqueId())) {
                plugin.sendMiniMessages(player, Messages.ECO_CANT_PAY_YOURSELF);
                return;
            }

            if (value <= 0) {
                plugin.sendMiniMessages(player, Messages.ECO_INVALID_VALUE);
                return;
            }

            PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());
            PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());

            if (!EterniaServer.getEconomyAPI().has(player, value)) {
                plugin.sendMiniMessages(player, Messages.ECO_INSUFFICIENT_BALANCE);
                return;
            }

            EterniaServer.getEconomyAPI().withdrawPlayer(player, value);
            EterniaServer.getEconomyAPI().depositPlayer(target, value);

            plugin.sendMiniMessages(
                    player,
                    Messages.ECO_PAYED,
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );

            plugin.sendMiniMessages(
                    target,
                    Messages.ECO_RECEIVED,
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );
        }

        @CommandCompletion("@players 10")
        @Subcommand("%ECONOMY_GIVE")
        @Syntax("%ECONOMY_GIVE_SYNTAX")
        @Description("%ECONOMY_GIVE_DESCRIPTION")
        @CommandPermission("%ECONOMY_GIVE_PERM")
        public void onGive(CommandSender commandSender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=2147483647") Double value) {
            Player target = onlineTarget.getPlayer();

            if (value <= 0) {
                plugin.sendMiniMessages(commandSender, Messages.ECO_INVALID_VALUE);
                return;
            }

            PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());

            EterniaServer.getEconomyAPI().depositPlayer(target, value);

            plugin.sendMiniMessages(
                    commandSender,
                    Messages.ECO_GIVED,
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );

            String[] nameAndDisplay = Utils.getNameAndDisplay(commandSender);

            plugin.sendMiniMessages(
                    target,
                    Messages.ECO_RECEIVED,
                    nameAndDisplay[0],
                    nameAndDisplay[1],
                    EterniaServer.getEconomyAPI().format(value)
            );
        }

        @CommandCompletion("@players 10")
        @Subcommand("%ECONOMY_TAKE")
        @Syntax("%ECONOMY_TAKE_SYNTAX")
        @Description("%ECONOMY_TAKE_DESCRIPTION")
        @CommandPermission("%ECONOMY_TAKE_PERM")
        public void onTake(CommandSender commandSender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=2147483647") Double value) {
            Player target = onlineTarget.getPlayer();

            if (value <= 0) {
                plugin.sendMiniMessages(commandSender, Messages.ECO_INVALID_VALUE);
                return;
            }

            PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());

            if (!EterniaServer.getEconomyAPI().has(target, value)) {
                plugin.sendMiniMessages(commandSender, Messages.ECO_INSUFFICIENT_BALANCE);
                return;
            }

            EterniaServer.getEconomyAPI().withdrawPlayer(target, value);

            plugin.sendMiniMessages(
                    commandSender,
                    Messages.ECO_TAKED,
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );

            String[] nameAndDisplay = Utils.getNameAndDisplay(commandSender);

            plugin.sendMiniMessages(
                    target,
                    Messages.ECO_RETIRED,
                    nameAndDisplay[0],
                    nameAndDisplay[1],
                    EterniaServer.getEconomyAPI().format(value)
            );
        }
    }

    @CommandAlias("%ECONOMY_BANK")
    static class EconomyBank extends BaseCommand {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        public EconomyBank(EterniaServer plugin) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
        }

        @Default
        @CatchUnknown
        @HelpCommand
        @Syntax("%ECONOMY_BANK_SYNTAX")
        @CommandPermission("%ECONOMY_BANK_PERM")
        @Description("%ECONOMY_BANK_DESCRIPTION")
        public void onBankHelp(CommandHelp help) {
            help.showHelp();
        }

        @Subcommand("%ECONOMY_BANK_LIST")
        @Syntax("%ECONOMY_BANK_LIST_SYNTAX")
        @Description("%ECONOMY_BANK_LIST_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_LIST_PERM")
        public void onBankList(CommandSender commandSender, @Optional Integer page) {
            if (page == null) {
                page = 1;
            }
            final int startPage = page;

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                List<Entities.BankBalance> bankBalances = EterniaServer.getExtraEconomyAPI().getBankList();

                int pageSize = 10;
                int maxPage = bankBalances.size() / pageSize;

                if (maxPage == 0) {
                    maxPage = 1;
                }

                if (startPage < 1 || startPage > maxPage) {
                    plugin.sendMiniMessages(commandSender, Messages.ECO_PAGE_LIMIT, String.valueOf(maxPage));
                    return;
                }

                int startIndex = (startPage - 1) * pageSize;

                plugin.sendMiniMessages(commandSender, Messages.ECO_BANK_LIST_TITLE, String.valueOf(startPage));

                for (int i = startIndex; i < bankBalances.size() && i < (startIndex + pageSize); i++) {
                    Entities.BankBalance bankBalance = bankBalances.get(i);

                    plugin.sendMiniMessages(
                            commandSender,
                            Messages.ECO_BANK_LIST,
                            false,
                            bankBalance.getName(),
                            EterniaServer.getEconomyAPI().format(bankBalance.getBalance())
                    );
                }

                Component component = Component.empty();
                if ((startPage - 1) > 0) {
                    component = component.append(Component.text(" <<<").clickEvent(ClickEvent.clickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/" + plugin.getString(Strings.ECO_BANK_LIST_COMMAND_NAME) + " " + (startPage - 1)
                    )));
                }
                component = component.append(plugin.parseColor(plugin.getMessage(Messages.ECO_PAGE, false)));
                if ((startPage + 1) < maxPage) {
                    component = component.append(Component.text(">>> ").clickEvent(ClickEvent.clickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/" + plugin.getString(Strings.ECO_BANK_LIST_COMMAND_NAME) + " " + (startPage + 1))
                    ));
                }
                commandSender.sendMessage(component);
            });
        }

        @Subcommand("%ECONOMY_BANK_CREATE")
        @Syntax("%ECONOMY_BANK_CREATE_SYNTAX")
        @Description("%ECONOMY_BANK_CREATE_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_CREATE_PERM")
        public void onBankCreate(Player player, String bankName) {
            if (bankName.length() > plugin.getInteger(Integers.ECONOMY_BANK_NAME_SIZE_LIMIT)) {
                plugin.sendMiniMessages(player, Messages.ECO_BANK_NAME_LIMIT);
                return;
            }
            if (!bankName.matches("[a-zA-Z]+")) {
                plugin.sendMiniMessages(player, Messages.ECO_BANK_NAME_INVALID);
                return;
            }

            double creationCost = plugin.getDouble(Doubles.ECO_BANK_CREATE_COST);
            if (!EterniaServer.getEconomyAPI().has(player, creationCost)) {
                plugin.sendMiniMessages(player, Messages.ECO_INSUFFICIENT_BALANCE);
                return;
            }

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                List<Entities.BankMember> bankMembers = databaseInterface.findAllBy(
                        Entities.BankMember.class,
                        "uuid",
                        player.getUniqueId()
                );
                for (Entities.BankMember bankMember : bankMembers) {
                    if (Enums.BankRole.OWNER.name().equals(bankMember.getRole())) {
                        plugin.sendMiniMessages(player, Messages.ECO_BANK_ALREADY_HAS_BANK, bankName);
                        return;
                    }
                }

                Entities.BankBalance checkBank = databaseInterface.get(Entities.BankBalance.class, bankName);

                if (checkBank != null && checkBank.getName() != null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_ALREADY_EXISTS, bankName);
                    return;
                }

                EterniaServer.getEconomyAPI().withdrawPlayer(player, creationCost);
                EterniaServer.getEconomyAPI().createBank(bankName, player);

                PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

                plugin.sendMiniMessages(player, Messages.ECO_BANK_CREATED, bankName);

                plugin.getServer().broadcast(plugin.getMiniMessage(
                        Messages.ECO_BANK_CREATED_BROADCAST,
                        true,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        bankName
                ));
            });

        }

        @CommandCompletion("@banks")
        @Subcommand("%ECONOMY_BANK_DELETE")
        @Syntax("%ECONOMY_BANK_DELETE_SYNTAX")
        @Description("%ECONOMY_BANK_DELETE_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_DELETE_PERM")
        public void onBankDelete(Player player, String bankName) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                Entities.BankBalance bankBalance = databaseInterface.get(Entities.BankBalance.class, bankName);

                if (bankBalance == null || bankBalance.getName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_EXIST, bankName);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                Entities.BankMember bankMember = databaseInterface.findBy(
                        Entities.BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_MEMBER, bankName);
                    return;
                }

                if (!bankMember.getRole().equals(Enums.BankRole.OWNER.name())) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_OWNER, bankName);
                    return;
                }

                EterniaServer.getEconomyAPI().depositPlayer(player, bankBalance.getBalance());
                EterniaServer.getEconomyAPI().deleteBank(bankName);

                PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

                plugin.sendMiniMessages(player, Messages.ECO_BANK_DELETED, bankName);

                plugin.getServer().broadcast(plugin.getMiniMessage(
                        Messages.ECO_BANK_DELETED_BROADCAST,
                        true,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        bankName
                ));
            });
        }

        @Subcommand("%ECONOMY_BANK_MY_BANKS")
        @Syntax("%ECONOMY_BANK_MY_BANKS_SYNTAX")
        @Description("%ECONOMY_BANK_MY_BANKS_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_MY_BANKS_PERM")
        public void onBankMyBanks(Player player) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                List<Entities.BankMember> bankMembers = databaseInterface.findAllBy(
                        Entities.BankMember.class,
                        "uuid",
                        player.getUniqueId()
                );

                if (bankMembers.isEmpty()) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NO_BANKS);
                    return;
                }

                plugin.sendMiniMessages(player, Messages.ECO_BANK_MY_BANKS_TITLE);

                for (Entities.BankMember bankMember : bankMembers) {
                    plugin.sendMiniMessages(
                            player,
                            Messages.ECO_BANK_MY_BANKS_LIST,
                            false,
                            bankMember.getBankName(),
                            bankMember.getRole()
                    );
                }
            });
        }

        @CommandCompletion("@banks")
        @Subcommand("%ECONOMY_BANK_INFO")
        @Syntax("%ECONOMY_BANK_INFO_SYNTAX")
        @Description("%ECONOMY_BANK_INFO_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_INFO_PERM")
        public void onBankInfo(Player player, String bankName) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                Entities.BankBalance bankBalance = databaseInterface.get(Entities.BankBalance.class, bankName);

                if (bankBalance == null || bankBalance.getName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_EXIST, bankName);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                Entities.BankMember bankMember = databaseInterface.findBy(
                        Entities.BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_MEMBER, bankName);
                    return;
                }

                plugin.sendMiniMessages(player, Messages.ECO_BANK_INFO_TITLE, bankName);
                plugin.sendMiniMessages(player, Messages.ECO_BANK_INFO_BALANCE, false, EterniaServer.getEconomyAPI().format(bankBalance.getBalance()));
                plugin.sendMiniMessages(player, Messages.ECO_BANK_INFO_TAX, false,(bankBalance.getTax() + plugin.getDouble(Doubles.ECO_BANK_TAX_VALUE)) + "%");

                List<Entities.BankMember> bankMembers = databaseInterface.findAllBy(
                        Entities.BankMember.class,
                        "bankName",
                        bankName
                );

                for (Entities.BankMember member : bankMembers) {
                    PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, member.getUuid());

                    plugin.sendMiniMessages(
                            player,
                            Messages.ECO_BANK_INFO_MEMBERS,
                            false,
                            playerProfile.getPlayerName(),
                            playerProfile.getPlayerDisplay(),
                            member.getRole()
                    );
                }
            });
        }

        @CommandCompletion("@banks 10")
        @Subcommand("%ECONOMY_BANK_DEPOSIT")
        @Syntax("%ECONOMY_BANK_DEPOSIT_SYNTAX")
        @Description("%ECONOMY_BANK_DEPOSIT_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_DEPOSIT_PERM")
        public void onBankDeposit(Player player, String bankName, @Conditions("limits:min=1,max=2147483647") Double value) {
            if (value <= 0) {
                plugin.sendMiniMessages(player, Messages.ECO_INVALID_VALUE);
                return;
            }

            if (!EterniaServer.getEconomyAPI().has(player, value)) {
                plugin.sendMiniMessages(player, Messages.ECO_INSUFFICIENT_BALANCE);
                return;
            }

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                Entities.BankBalance bankBalance = databaseInterface.get(Entities.BankBalance.class, bankName);
                if (bankBalance == null || bankBalance.getName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_EXIST, bankName);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                Entities.BankMember bankMember = databaseInterface.findBy(
                        Entities.BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_MEMBER, bankName);
                    return;
                }

                EterniaServer.getEconomyAPI().withdrawPlayer(player, value);
                EterniaServer.getEconomyAPI().bankDeposit(bankName, value);

                plugin.sendMiniMessages(
                        player,
                        Messages.ECO_BANK_DEPOSITED,
                        bankName,
                        EterniaServer.getEconomyAPI().format(value)
                );
            });
        }

        @CommandCompletion("@banks 10")
        @Subcommand("%ECONOMY_BANK_WITHDRAW")
        @Syntax("%ECONOMY_BANK_WITHDRAW_SYNTAX")
        @Description("%ECONOMY_BANK_WITHDRAW_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_WITHDRAW_PERM")
        public void onBankWithdraw(Player player, String bankName, @Conditions("limits:min=1,max=2147483647") Double value) {
            if (value <= 0) {
                plugin.sendMiniMessages(player, Messages.ECO_INVALID_VALUE);
                return;
            }

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                Entities.BankBalance bankBalance = databaseInterface.get(Entities.BankBalance.class, bankName);
                if (bankBalance == null || bankBalance.getName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_EXIST, bankName);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                Entities.BankMember bankMember = databaseInterface.findBy(
                        Entities.BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_MEMBER, bankName);
                    return;
                }

                if (!bankMember.getRole().equals(Enums.BankRole.OWNER.name()) && !bankMember.getRole().equals(Enums.BankRole.TRUSTED.name())) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NO_WITHDRAW_PERMISSION, bankName);
                    return;
                }

                if (!EterniaServer.getEconomyAPI().bankHas(bankName, value).transactionSuccess()) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_HAS_AMOUNT, bankName);
                    return;
                }

                EterniaServer.getEconomyAPI().bankWithdraw(bankName, value);
                EterniaServer.getEconomyAPI().depositPlayer(player, value);

                plugin.sendMiniMessages(
                        player,
                        Messages.ECO_BANK_WITHDRAWN,
                        bankName,
                        EterniaServer.getEconomyAPI().format(value)
                );
            });
        }

        @CommandCompletion("@banks @players MEMBER")
        @Subcommand("%ECONOMY_BANK_CHANGE_ROLE")
        @Syntax("%ECONOMY_BANK_CHANGE_ROLE_SYNTAX")
        @Description("%ECONOMY_BANK_CHANGE_ROLE_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_CHANGE_ROLE_PERM")
        public void onBankChangeRole(Player player, String bankName, OnlinePlayer onlineTarget, String role) {
            Player target = onlineTarget.getPlayer();

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                Entities.BankBalance bankBalance = databaseInterface.get(Entities.BankBalance.class, bankName);
                if (bankBalance == null || bankBalance.getName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_EXIST, bankName);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                Entities.BankMember bankMember = databaseInterface.findBy(
                        Entities.BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_MEMBER, bankName);
                    return;
                }

                if (!bankMember.getRole().equals(Enums.BankRole.OWNER.name())) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_OWNER, bankName);
                    return;
                }

                SearchField targetUuidSearch = new SearchField("uuid", target.getUniqueId());
                Entities.BankMember targetBankMember = databaseInterface.findBy(
                        Entities.BankMember.class,
                        bankNameSearch,
                        targetUuidSearch
                );

                PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());

                if (targetBankMember == null || targetBankMember.getBankName() == null) {
                    plugin.sendMiniMessages(
                            player,
                            Messages.ECO_BANK_TARGET_NOT_MEMBER,
                            bankName,
                            targetProfile.getPlayerName(),
                            targetProfile.getPlayerDisplay()
                    );
                    return;
                }

                if (!(Enums.BankRole.TRUSTED.name().equals(role) || Enums.BankRole.MEMBER.name().equals(role))) {
                    plugin.sendMiniMessages(
                            player,
                            Messages.ECO_BANK_INVALID_ROLE,
                            bankName,
                            role
                    );
                    return;
                }

                targetBankMember.setRole(role);
                databaseInterface.update(Entities.BankMember.class, targetBankMember);

                PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

                plugin.sendMiniMessages(
                        player,
                        Messages.ECO_BANK_CHANGE_ROLE_TO,
                        targetProfile.getPlayerName(),
                        targetProfile.getPlayerDisplay(),
                        role,
                        bankName
                );

                plugin.sendMiniMessages(
                        target,
                        Messages.ECO_BANK_CHANGE_ROLE,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        role,
                        bankName
                );
            });
        }

        @CommandCompletion("@banks @players")
        @Subcommand("%ECONOMY_BANK_AFFILIATE")
        @Syntax("%ECONOMY_BANK_AFFILIATE_SYNTAX")
        @Description("%ECONOMY_BANK_AFFILIATE_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_AFFILIATE_PERM")
        public void onBankAffiliate(Player player, String bankName, OnlinePlayer onlineTarget) {
            Player target = onlineTarget.getPlayer();

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                Entities.BankBalance bankBalance = databaseInterface.get(Entities.BankBalance.class, bankName);
                if (bankBalance == null || bankBalance.getName() == null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_NOT_EXIST, bankName);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                Entities.BankMember bankMember = databaseInterface.findBy(
                        Entities.BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember != null && bankMember.getBankName() != null) {
                    plugin.sendMiniMessages(player, Messages.ECO_BANK_ALREADY_MEMBER, bankName);
                    return;
                }

                SearchField targetUuidSearch = new SearchField("uuid", target.getUniqueId());
                Entities.BankMember targetBankMember = databaseInterface.findBy(
                        Entities.BankMember.class,
                        bankNameSearch,
                        targetUuidSearch
                );

                PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());

                if (targetBankMember == null || targetBankMember.getBankName() == null) {
                    plugin.sendMiniMessages(
                            player,
                            Messages.ECO_BANK_TARGET_NOT_OWNER,
                            bankName,
                            targetProfile.getPlayerName(),
                            targetProfile.getPlayerDisplay()
                    );
                    return;
                }

                PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());
                plugin.sendMiniMessages(
                        player,
                        Messages.ECO_BANK_AFFILIATE_REQUEST,
                        targetProfile.getPlayerName(),
                        targetProfile.getPlayerDisplay(),
                        bankName
                );
                plugin.sendMiniMessages(
                        target,
                        Messages.ECO_BANK_AFFILIATE_REQUESTED,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        bankName
                );

                AffiliateCommand affiliateCommand = new AffiliateCommand(plugin, bankName, target, player);
                EterniaLib.getAdvancedCmdManager().addConfirmationCommand(affiliateCommand);
            });
        }
    }

}
