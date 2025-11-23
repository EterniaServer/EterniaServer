package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eternialib.database.dtos.SearchField;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.dtos.BalanceDTO;
import br.com.eterniaserver.eterniaserver.api.dtos.BankDTO;
import br.com.eterniaserver.eterniaserver.api.dtos.BankMemberDTO;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Utils;
import br.com.eterniaserver.eterniaserver.modules.economy.Utils.AffiliateCommand;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankMember;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankBalance;

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

        public EconomyGeneric(EterniaServer plugin) {
            this.plugin = plugin;
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
            List<BalanceDTO> balances = EterniaServer.getExtraEconomyAPI().getBalanceTop();

            int pageSize = 10;
            int maxPage = balances.size() / pageSize;

            if (maxPage == 0) {
                maxPage = 1;
            }
            if (page == null) {
                page = 1;
            }

            if (page < 1 || page > maxPage) {
                MessageOptions maxOptions = new MessageOptions(String.valueOf(maxPage));
                EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_PAGE_LIMIT, maxOptions);
                return;
            }

            int startIndex = (page - 1) * pageSize;

            MessageOptions pageOptions = new MessageOptions(String.valueOf(page));
            EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_BALTOP_TITLE, pageOptions);

            for (int i = startIndex; i < balances.size() && i < (startIndex + pageSize); i++) {
                BalanceDTO ecoBalance = balances.get(i);
                UUID uuid = ecoBalance.playerUUID();
                double balance = ecoBalance.balance();

                PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, uuid);

                MessageOptions messageOptions = new MessageOptions(
                        false,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        EterniaServer.getEconomyAPI().format(balance)
                );
                EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_BALTOP_LIST, messageOptions);
            }

           Component component = Component.empty();
            if ((page - 1) > 0) {
                String previous = "/" + plugin.getString(Strings.ECO_BALTOP_COMMAND_NAME) + " " + (page - 1);
                ClickEvent previousPage = ClickEvent.runCommand(previous);
                component = component.append(Component.text(" <<<").clickEvent(previousPage));
            }
            component = component.append(EterniaLib.getChatCommons().parseMessage(Messages.ECO_PAGE, new MessageOptions(false)));
            if ((page + 1) < maxPage) {
                String next = "/" + plugin.getString(Strings.ECO_BALTOP_COMMAND_NAME) + " " + (page + 1);
                ClickEvent nextPage = ClickEvent.runCommand(next);
                component = component.append(Component.text(">>> ").clickEvent(nextPage));
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
                EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_BALANCE_IN_CONSOLE);
                return;
            } else {
                target = onlineTarget.getPlayer();
            }

            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());
            double value = EterniaServer.getEconomyAPI().getBalance(target);

            MessageOptions messageOptions = new MessageOptions(
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );
            EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_BALANCE, messageOptions);
        }

        @CommandCompletion("@players 10")
        @CommandAlias("%ECONOMY_PAY")
        @Syntax("%ECONOMY_PAY_SYNTAX")
        @Description("%ECONOMY_PAY_DESCRIPTION")
        @CommandPermission("%ECONOMY_PAY_PERM")
        public void onPay(Player player, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=2147483647") Double value) {
            Player target = onlineTarget.getPlayer();

            if (player.getUniqueId().equals(target.getUniqueId())) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_CANT_PAY_YOURSELF);
                return;
            }

            if (value <= 0) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_INVALID_VALUE);
                return;
            }

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());
            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

            if (!EterniaServer.getEconomyAPI().has(player, value)) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_INSUFFICIENT_BALANCE);
                return;
            }

            EterniaServer.getEconomyAPI().withdrawPlayer(player, value);
            EterniaServer.getEconomyAPI().depositPlayer(target, value);

            MessageOptions playerOptions = new MessageOptions(
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );
            MessageOptions targetOptions = new MessageOptions(
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );

            EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_PAYED, playerOptions);
            EterniaLib.getChatCommons().sendMessage(target, Messages.ECO_RECEIVED, targetOptions);
        }

        @CommandCompletion("@players 10")
        @Subcommand("%ECONOMY_GIVE")
        @Syntax("%ECONOMY_GIVE_SYNTAX")
        @Description("%ECONOMY_GIVE_DESCRIPTION")
        @CommandPermission("%ECONOMY_GIVE_PERM")
        public void onGive(CommandSender commandSender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=2147483647") Double value) {
            Player target = onlineTarget.getPlayer();

            if (value <= 0) {
                EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_INVALID_VALUE);
                return;
            }

            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

            EterniaServer.getEconomyAPI().depositPlayer(target, value);

            MessageOptions playerOptions = new MessageOptions(
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );
            EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_GIVED, playerOptions);

            String[] nameAndDisplay = Utils.getNameAndDisplay(commandSender);
            MessageOptions targetOptions = new MessageOptions(
                    nameAndDisplay[0],
                    nameAndDisplay[1],
                    EterniaServer.getEconomyAPI().format(value)
            );
            EterniaLib.getChatCommons().sendMessage(target, Messages.ECO_RECEIVED, targetOptions);
        }

        @CommandCompletion("@players 10")
        @Subcommand("%ECONOMY_TAKE")
        @Syntax("%ECONOMY_TAKE_SYNTAX")
        @Description("%ECONOMY_TAKE_DESCRIPTION")
        @CommandPermission("%ECONOMY_TAKE_PERM")
        public void onTake(CommandSender commandSender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=2147483647") Double value) {
            Player target = onlineTarget.getPlayer();

            if (value <= 0) {
                EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_INVALID_VALUE);
                return;
            }

            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

            if (!EterniaServer.getEconomyAPI().has(target, value)) {
                EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_INSUFFICIENT_BALANCE);
                return;
            }

            EterniaServer.getEconomyAPI().withdrawPlayer(target, value);

            MessageOptions playerOptions = new MessageOptions(
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    EterniaServer.getEconomyAPI().format(value)
            );
            EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_TAKED, playerOptions);

            String[] nameAndDisplay = Utils.getNameAndDisplay(commandSender);
            MessageOptions targetOptions = new MessageOptions(
                    nameAndDisplay[0],
                    nameAndDisplay[1],
                    EterniaServer.getEconomyAPI().format(value)
            );
            EterniaLib.getChatCommons().sendMessage(target, Messages.ECO_RETIRED, targetOptions);
        }
    }

    @CommandAlias("%ECONOMY_BANK")
    static class EconomyBank extends BaseCommand {

        private final EterniaServer plugin;

        public EconomyBank(EterniaServer plugin) {
            this.plugin = plugin;
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
                List<BankDTO> bankBalances = EterniaServer.getExtraEconomyAPI().getBankList();

                int pageSize = 10;
                int maxPage = bankBalances.size() / pageSize;

                if (maxPage == 0) {
                    maxPage = 1;
                }

                if (startPage < 1 || startPage > maxPage) {
                    MessageOptions maxOptions = new MessageOptions(String.valueOf(maxPage));
                    EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_PAGE_LIMIT, maxOptions);
                    return;
                }

                int startIndex = (startPage - 1) * pageSize;

                MessageOptions pageOptions = new MessageOptions(String.valueOf(startPage));
                EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_BANK_LIST_TITLE, pageOptions);

                for (int i = startIndex; i < bankBalances.size() && i < (startIndex + pageSize); i++) {
                    BankDTO bankBalance = bankBalances.get(i);

                    MessageOptions messageOptions = new MessageOptions(
                            false,
                            bankBalance.bankName(),
                            EterniaServer.getEconomyAPI().format(bankBalance.balance())
                    );
                    EterniaLib.getChatCommons().sendMessage(commandSender, Messages.ECO_BANK_LIST, messageOptions);
                }

                Component component = Component.empty();

                if ((startPage - 1) > 0) {
                    String previous = "/" + plugin.getString(Strings.ECO_BANK_LIST_COMMAND_NAME) + " " + (startPage - 1);
                    ClickEvent previousPage = ClickEvent.runCommand(previous);
                    component = component.append(Component.text(" <<<").clickEvent(previousPage));
                }
                component = component.append(EterniaLib.getChatCommons().parseMessage(Messages.ECO_PAGE, new MessageOptions(false)));
                if ((startPage + 1) < maxPage) {
                    String next = "/" + plugin.getString(Strings.ECO_BANK_LIST_COMMAND_NAME) + " " + (startPage + 1);
                    ClickEvent nextPage = ClickEvent.runCommand(next);
                    component = component.append(Component.text(">>> ").clickEvent(nextPage));
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
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NAME_LIMIT);
                return;
            }
            if (!bankName.matches("[a-zA-Z]+")) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NAME_INVALID);
                return;
            }

            double creationCost = plugin.getDouble(Doubles.ECO_BANK_CREATE_COST);
            if (!EterniaServer.getEconomyAPI().has(player, creationCost)) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_INSUFFICIENT_BALANCE);
                return;
            }

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                List<BankMember> bankMembers = EterniaLib.getDatabase().findAllBy(
                        BankMember.class,
                        "uuid",
                        player.getUniqueId()
                );

                MessageOptions bankNameOptions = new MessageOptions(bankName);
                for (BankMember bankMember : bankMembers) {
                    if (Enums.BankRole.OWNER.name().equals(bankMember.getRole())) {
                        EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_ALREADY_HAS_BANK, bankNameOptions);
                        return;
                    }
                }

                BankBalance checkBank = EterniaLib.getDatabase().get(BankBalance.class, bankName);

                if (checkBank != null && checkBank.getName() != null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_ALREADY_EXISTS, bankNameOptions);
                    return;
                }

                EterniaServer.getEconomyAPI().withdrawPlayer(player, creationCost);
                EterniaServer.getEconomyAPI().createBank(bankName, player);

                PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_CREATED, bankNameOptions);

                MessageOptions messageOptions = new MessageOptions(
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        bankName
                );
                plugin.getServer().broadcast(EterniaLib.getChatCommons().parseMessage(
                        Messages.ECO_BANK_CREATED_BROADCAST,
                        messageOptions
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
                BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);

                MessageOptions bankNameOptions = new MessageOptions(bankName);

                if (bankBalance == null || bankBalance.getName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_EXIST, bankNameOptions);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                BankMember bankMember = EterniaLib.getDatabase().findBy(
                        BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_MEMBER, bankNameOptions);
                    return;
                }

                if (!bankMember.getRole().equals(Enums.BankRole.OWNER.name())) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_OWNER, bankNameOptions);
                    return;
                }

                EterniaServer.getEconomyAPI().depositPlayer(player, bankBalance.getBalance());
                EterniaServer.getEconomyAPI().deleteBank(bankName);

                PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_DELETED, bankNameOptions);

                MessageOptions messageOptions = new MessageOptions(
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        bankName
                );
                plugin.getServer().broadcast(EterniaLib.getChatCommons().parseMessage(
                        Messages.ECO_BANK_DELETED_BROADCAST,
                        messageOptions
                ));
            });
        }

        @Subcommand("%ECONOMY_BANK_MY_BANKS")
        @Syntax("%ECONOMY_BANK_MY_BANKS_SYNTAX")
        @Description("%ECONOMY_BANK_MY_BANKS_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_MY_BANKS_PERM")
        public void onBankMyBanks(Player player) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                List<BankMember> bankMembers = EterniaLib.getDatabase().findAllBy(
                        BankMember.class,
                        "uuid",
                        player.getUniqueId()
                );

                if (bankMembers.isEmpty()) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NO_BANKS);
                    return;
                }

                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_MY_BANKS_TITLE);

                for (BankMember bankMember : bankMembers) {
                    MessageOptions playerOptions = new MessageOptions(
                            false,
                            bankMember.getBankName(),
                            bankMember.getRole()
                    );
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_MY_BANKS_LIST, playerOptions);
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
                BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);

                MessageOptions bankNameOptions = new MessageOptions(bankName);

                if (bankBalance == null || bankBalance.getName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_EXIST, bankNameOptions);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                BankMember bankMember = EterniaLib.getDatabase().findBy(
                        BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_MEMBER, bankNameOptions);
                    return;
                }

                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_INFO_TITLE, bankNameOptions);
                MessageOptions bankInfoBalance = new MessageOptions(
                        false,
                        EterniaServer.getEconomyAPI().format(bankBalance.getBalance())
                );
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_INFO_BALANCE, bankInfoBalance);
                MessageOptions bankInfoTax = new MessageOptions(
                        false,
                        (bankBalance.getTax() + plugin.getDouble(Doubles.ECO_BANK_TAX_VALUE)) + "%"
                );
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_INFO_TAX, bankInfoTax);

                List<BankMemberDTO> bankMembers = EterniaServer.getExtraEconomyAPI().getBankMembers(bankName);

                for (BankMemberDTO member : bankMembers) {
                    PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, member.playerUUID());

                    MessageOptions memberOptions = new MessageOptions(
                            false,
                            playerProfile.getPlayerName(),
                            playerProfile.getPlayerDisplay(),
                            member.playerRole()
                    );
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_INFO_MEMBERS, memberOptions);
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
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_INVALID_VALUE);
                return;
            }

            if (!EterniaServer.getEconomyAPI().has(player, value)) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_INSUFFICIENT_BALANCE);
                return;
            }

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);
                MessageOptions bankNameOptions = new MessageOptions(bankName);

                if (bankBalance == null || bankBalance.getName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_EXIST, bankNameOptions);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                BankMember bankMember = EterniaLib.getDatabase().findBy(
                        BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_MEMBER, bankNameOptions);
                    return;
                }

                EterniaServer.getEconomyAPI().withdrawPlayer(player, value);
                EterniaServer.getEconomyAPI().bankDeposit(bankName, value);

                MessageOptions playerOptions = new MessageOptions(
                        bankName,
                        EterniaServer.getEconomyAPI().format(value)
                );
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_DEPOSITED, playerOptions);
            });
        }

        @CommandCompletion("@banks 10")
        @Subcommand("%ECONOMY_BANK_WITHDRAW")
        @Syntax("%ECONOMY_BANK_WITHDRAW_SYNTAX")
        @Description("%ECONOMY_BANK_WITHDRAW_DESCRIPTION")
        @CommandPermission("%ECONOMY_BANK_WITHDRAW_PERM")
        public void onBankWithdraw(Player player, String bankName, @Conditions("limits:min=1,max=2147483647") Double value) {
            if (value <= 0) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_INVALID_VALUE);
                return;
            }

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);
                MessageOptions bankNameOptions = new MessageOptions(bankName);

                if (bankBalance == null || bankBalance.getName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_EXIST, bankNameOptions);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                BankMember bankMember = EterniaLib.getDatabase().findBy(
                        BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_MEMBER, bankNameOptions);
                    return;
                }

                if (!bankMember.getRole().equals(Enums.BankRole.OWNER.name()) && !bankMember.getRole().equals(Enums.BankRole.TRUSTED.name())) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NO_WITHDRAW_PERMISSION, bankNameOptions);
                    return;
                }

                if (!EterniaServer.getEconomyAPI().bankHas(bankName, value).transactionSuccess()) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_HAS_AMOUNT, bankNameOptions);
                    return;
                }

                EterniaServer.getEconomyAPI().bankWithdraw(bankName, value);
                EterniaServer.getEconomyAPI().depositPlayer(player, value);

                MessageOptions playerOptions = new MessageOptions(
                        bankName,
                        EterniaServer.getEconomyAPI().format(value)
                );
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_WITHDRAWN, playerOptions);
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
                BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);
                MessageOptions bankNameOptions = new MessageOptions(bankName);

                if (bankBalance == null || bankBalance.getName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_EXIST, bankNameOptions);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                BankMember bankMember = EterniaLib.getDatabase().findBy(
                        BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember == null || bankMember.getBankName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_MEMBER, bankNameOptions);
                    return;
                }

                if (!bankMember.getRole().equals(Enums.BankRole.OWNER.name())) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_OWNER, bankNameOptions);
                    return;
                }

                SearchField targetUuidSearch = new SearchField("uuid", target.getUniqueId());
                BankMember targetBankMember = EterniaLib.getDatabase().findBy(
                        BankMember.class,
                        bankNameSearch,
                        targetUuidSearch
                );

                PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

                if (targetBankMember == null || targetBankMember.getBankName() == null) {
                    MessageOptions options = new MessageOptions(
                            bankName,
                            targetProfile.getPlayerName(),
                            targetProfile.getPlayerDisplay()
                    );
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_TARGET_NOT_MEMBER, options);
                    return;
                }

                if (!(Enums.BankRole.TRUSTED.name().equals(role) || Enums.BankRole.MEMBER.name().equals(role))) {
                    MessageOptions options = new MessageOptions(bankName, role);
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_INVALID_ROLE, options);
                    return;
                }

                targetBankMember.setRole(role);
                EterniaLib.getDatabase().update(BankMember.class, targetBankMember);

                PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

                MessageOptions playerOptions = new MessageOptions(
                        targetProfile.getPlayerName(),
                        targetProfile.getPlayerDisplay(),
                        role,
                        bankName
                );
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_CHANGE_ROLE_TO, playerOptions);

                MessageOptions targetOptions = new MessageOptions(
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        role,
                        bankName
                );
                EterniaLib.getChatCommons().sendMessage(target, Messages.ECO_BANK_CHANGE_ROLE, targetOptions);
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
                BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);
                MessageOptions bankNameOptions = new MessageOptions(bankName);

                if (bankBalance == null || bankBalance.getName() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_NOT_EXIST, bankNameOptions);
                    return;
                }

                SearchField bankNameSearch = new SearchField("bankName", bankName);
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                BankMember bankMember = EterniaLib.getDatabase().findBy(
                        BankMember.class,
                        bankNameSearch,
                        uuidSearch
                );

                if (bankMember != null && bankMember.getBankName() != null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_ALREADY_MEMBER, bankNameOptions);
                    return;
                }

                SearchField targetUuidSearch = new SearchField("uuid", target.getUniqueId());
                BankMember targetBankMember = EterniaLib.getDatabase().findBy(
                        BankMember.class,
                        bankNameSearch,
                        targetUuidSearch
                );

                PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

                if (targetBankMember == null || targetBankMember.getBankName() == null) {
                    MessageOptions options = new MessageOptions(
                            bankName,
                            targetProfile.getPlayerName(),
                            targetProfile.getPlayerDisplay()
                    );
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_TARGET_NOT_OWNER, options);
                    return;
                }

                PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());
                MessageOptions playerOptions = new MessageOptions(
                        targetProfile.getPlayerName(),
                        targetProfile.getPlayerDisplay(),
                        bankName
                );
                EterniaLib.getChatCommons().sendMessage(player, Messages.ECO_BANK_AFFILIATE_REQUEST, playerOptions);

                MessageOptions targetOptions = new MessageOptions(
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay(),
                        bankName
                );
                EterniaLib.getChatCommons().sendMessage(target, Messages.ECO_BANK_AFFILIATE_REQUESTED, targetOptions);

                AffiliateCommand affiliateCommand = new AffiliateCommand(plugin, bankName, target, player);
                boolean result = EterniaLib.getAdvancedCmdManager().addConfirmationCommand(affiliateCommand);
                if (!result) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ALREADY_IN_CONFIRMATION);
                }
            });
        }
    }

}
