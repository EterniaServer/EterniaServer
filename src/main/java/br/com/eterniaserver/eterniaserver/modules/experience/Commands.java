package br.com.eterniaserver.eterniaserver.modules.experience;

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
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Utils;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @CommandAlias("%EXPERIENCE")
    static class Experience extends BaseCommand {

        private static final List<String> DEFAULT_TYPES = List.of("xp", "level");

        private final EterniaServer plugin;
        private final Services.Experience expService;
        private final DatabaseInterface database;

        public Experience(final EterniaServer plugin, Services.Experience experienceService) {
            this.plugin = plugin;
            this.expService = experienceService;
            this.database = EterniaLib.getDatabase();
        }

        @Default
        @CatchUnknown
        @HelpCommand
        @Syntax("%EXPERIENCE_SYNTAX")
        @CommandPermission("%EXPERIENCE_PERM")
        @Description("%EXPERIENCE_DESCRIPTION")
        public void xpHelp(CommandHelp help) {
            help.showHelp();
        }

        @CommandCompletion("@players 100 level")
        @Subcommand("%EXPERIENCE_SET")
        @Syntax("%EXPERIENCE_SET_SYNTAX")
        @Description("%EXPERIENCE_SET_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_SET_PERM")
        public void onSet(CommandSender sender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=9999999") Integer amount, @Default("level") String type) {
            if (!DEFAULT_TYPES.contains(type)) {
                plugin.sendMiniMessages(sender, Messages.EXP_INVALID_CHOICE);
                return;
            }

            Player target = onlineTarget.getPlayer();
            String label;
            if (type.equals(DEFAULT_TYPES.get(0))) {
                target.setExp(0);
                target.setLevel(0);
                target.giveExp(amount);
                label = plugin.getString(Strings.EXP_XP_LABEL);
            }
            else {
                target.setLevel(amount);
                label = plugin.getString(Strings.EXP_LEVEL_LABEL);
            }

            sendMessage(sender, target, Messages.EXP_SET_FROM, Messages.EXP_SETED, String.valueOf(amount), label);
        }

        @CommandCompletion("@players 100 level")
        @Subcommand("%EXPERIENCE_TAKE")
        @Syntax("%EXPERIENCE_TAKE_SYNTAX")
        @Description("%EXPERIENCE_TAKE_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_TAKE_PERM")
        public void onTake(CommandSender sender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=9999999") Integer amount, @Default("level") String type) {
            if (!DEFAULT_TYPES.contains(type)) {
                plugin.sendMiniMessages(sender, Messages.EXP_INVALID_CHOICE);
                return;
            }

            Player target = onlineTarget.getPlayer();
            String label;
            if (type.equals(DEFAULT_TYPES.get(0))) {
                int xp = playerActualXp(target);

                target.setExp(0);
                target.setLevel(0);
                target.giveExp(xp - amount);
                label = plugin.getString(Strings.EXP_XP_LABEL);
            }
            else {
                target.setLevel(target.getLevel() - amount);
                label = plugin.getString(Strings.EXP_LEVEL_LABEL);
            }

            sendMessage(sender, target, Messages.EXP_REMOVE_FROM, Messages.EXP_REMOVED, String.valueOf(amount), label);
        }

        @CommandCompletion("@players 100 level")
        @Subcommand("%EXPERIENCE_GIVE")
        @Syntax("%EXPERIENCE_GIVE_SYNTAX")
        @Description("%EXPERIENCE_GIVE_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_GIVE_PERM")
        public void onGive(CommandSender sender, OnlinePlayer onlineTarget, @Conditions("limits:min=1,max=9999999") Integer amount, @Default("level") String type) {
            if (!DEFAULT_TYPES.contains(type)) {
                plugin.sendMiniMessages(sender, Messages.EXP_INVALID_CHOICE);
                return;
            }

            Player target = onlineTarget.getPlayer();
            String label;
            if (type.equals(DEFAULT_TYPES.get(0))) {
                int xp = playerActualXp(target);
                target.setExp(0);
                target.setLevel(0);
                target.giveExp(xp + amount);
                label = plugin.getString(Strings.EXP_XP_LABEL);
            }
            else {
                target.setLevel(target.getLevel() + amount);
                label = plugin.getString(Strings.EXP_LEVEL_LABEL);
            }

            sendMessage(sender, target, Messages.EXP_GIVE_FROM, Messages.EXP_GIVED, String.valueOf(amount), label);
        }

        private void sendMessage(CommandSender sender,
                                 Player target,
                                 Messages from,
                                 Messages to,
                                 String amount,
                                 String typeLabel) {

            PlayerProfile targetProfile = database.get(PlayerProfile.class, target.getUniqueId());
            String targetName = targetProfile.getPlayerName();
            String targetDisplay = targetProfile.getPlayerDisplay();

            String[] senderNameDisplay = Utils.getNameAndDisplay(sender);
            String senderName = senderNameDisplay[0];
            String senderDisplay = senderNameDisplay[1];

            plugin.sendMiniMessages(sender, from, amount, targetName, targetDisplay, typeLabel);
            plugin.sendMiniMessages(target.getPlayer(), to, amount, senderName, senderDisplay, typeLabel);

        }

        @Subcommand("%EXPERIENCE_CHECK")
        @Description("%EXPERIENCE_CHECK_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_CHECK_PERM")
        public void onCheckLevel(Player player) {
            UUID uuid = player.getUniqueId();
            expService.getBalance(uuid).whenCompleteAsync(((expBalance, throwable) -> {
                if (throwable != null) {
                    EterniaLib.registerLog("EE-303-ExpCheck Error");
                    return;
                }

                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    int xp = playerActualXp(player);

                    player.setLevel(0);
                    player.setExp(0);
                    player.giveExp(expBalance.getBalance());

                    plugin.sendMiniMessages(player, Messages.EXP_BALANCE, String.valueOf(player.getLevel()));

                    player.setLevel(0);
                    player.setExp(0);
                    player.giveExp(xp);
                });
            }));
        }

        @CommandCompletion("10")
        @Subcommand("%EXPERIENCE_BOTTLE")
        @Syntax("%EXPERIENCE_BOTTLE_SYNTAX")
        @Description("%EXPERIENCE_BOTTLE_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_BOTTLE_PERM")
        public void onBottleLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer levelWanted) {
            int actualLevel = player.getLevel();
            int xp = playerActualXp(player);

            if (actualLevel < levelWanted || levelWanted < 1) {
                plugin.sendMiniMessages(player, Messages.EXP_INSUFFICIENT);
                return;
            }

            int xpWant = expService.getXPForLevel(levelWanted);

            ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta meta = item.getItemMeta();

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER, 0);
            dataContainer.set(plugin.getKey(ItemsKeys.TAG_INT_VALUE), PersistentDataType.INTEGER, xpWant);

            meta.displayName(plugin.parseColor(plugin.getString(Strings.MINI_MESSAGES_BOTTLE_EXP_NAME)));
            meta.lore(
                    plugin.getStringList(Lists.MINI_MESSAGES_BOTTLE_EXP_LORE)
                            .stream()
                            .map(x -> x.replace("%amount%", levelWanted + " Níveis"))
                            .map(plugin::parseColor)
                            .toList()
            );

            item.setItemMeta(meta);

            player.getInventory().addItem(item);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xp - xpWant);

            plugin.sendMiniMessages(player, Messages.EXP_BOTTLED);
        }

        @CommandCompletion("10")
        @Subcommand("%EXPERIENCE_WITHDRAW")
        @Syntax("%EXPERIENCE_WITHDRAW_SYNTAX")
        @Description("%EXPERIENCE_WITHDRAW_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_WITHDRAW_PERM")
        public void onWithdrawLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer level) {
            UUID uuid = player.getUniqueId();
            int wantedXp = expService.getXPForLevel(level);

            expService.getBalance(uuid).whenCompleteAsync((balance, throwable) -> {
                if (throwable != null) {
                    EterniaLib.registerLog("EE-302-ExpWithdraw Error");
                    return;
                }

                if (balance.getBalance() < wantedXp) {
                    plugin.sendMiniMessages(player, Messages.EXP_INSUFFICIENT);
                    return;
                }

                balance.setBalance(balance.getBalance() - wantedXp);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.giveExp(wantedXp);
                    plugin.sendMiniMessages(player, Messages.EXP_WITHDRAW, String.valueOf(level));
                });
            });
        }

        @CommandCompletion("10")
        @Subcommand("%EXPERIENCE_DEPOSIT")
        @Syntax("%EXPERIENCE_DEPOSIT_SYNTAX")
        @Description("%EXPERIENCE_DEPOSIT_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_DEPOSIT_PERM")
        public void onDepositLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer xpla) {
            UUID uuid = player.getUniqueId();
            int actualLevel = player.getLevel();

            if (actualLevel < xpla) {
                plugin.sendMiniMessages(player, Messages.EXP_INSUFFICIENT);
                return;
            }

            int amountToDeposit = expService.getXPForLevel(xpla);
            int playerActualXp = playerActualXp(player);

            player.setLevel(0);
            player.setExp(0);
            player.giveExp(playerActualXp - amountToDeposit);

            expService.getBalance(uuid).whenCompleteAsync((balance, throwable) -> {
                if (throwable != null) {
                    EterniaLib.registerLog("EE-301-ExpDeposit Error");
                    return;
                }

                balance.setBalance(balance.getBalance() + amountToDeposit);
                expService.updateBalance(balance);
                plugin.sendMiniMessages(player, Messages.EXP_DEPOSIT, String.valueOf(xpla));
            });
        }

        private int playerActualXp(Player player) {
            return expService.getXPForLevel(player.getLevel()) + (int) (player.getExpToLevel() * player.getExp());
        }

    }

}
