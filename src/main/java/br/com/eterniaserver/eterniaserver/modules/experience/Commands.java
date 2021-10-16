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
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import com.google.common.collect.ImmutableList;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

final class Commands {

    @CommandAlias("%EXPERIENCE")
    static class Experience extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.Experience experienceService;
        private final List<String> DEFAULT_TYPES = ImmutableList.of("xp", "level");

        public Experience(final EterniaServer plugin, Services.Experience experienceService) {
            this.plugin = plugin;
            this.experienceService = experienceService;
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

            final String typeLabel;
            final Player target = onlineTarget.getPlayer();
            final String senderDisplay = senderDisplay(sender);

            if (type.equals(DEFAULT_TYPES.get(0))) {
                target.setExp(0);
                target.setLevel(0);
                target.giveExp(amount);
                typeLabel = plugin.getString(Strings.EXP_XP_LABEL);
            }
            else {
                target.setLevel(amount);
                typeLabel = plugin.getString(Strings.EXP_LEVEL_LABEL);
            }

            plugin.sendMiniMessages(sender, Messages.EXP_SET_FROM, String.valueOf(amount), target.getName(), target.getDisplayName(), typeLabel);
            plugin.sendMiniMessages(target.getPlayer(), Messages.EXP_SETED, String.valueOf(amount), sender.getName(), senderDisplay, typeLabel);
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

            final Player target = onlineTarget.getPlayer();
            final String senderDisplay = senderDisplay(sender);
            final String typeLabel;

            if (type.equals(DEFAULT_TYPES.get(0))) {
                final int xp = playerActualXp(target);

                target.setExp(0);
                target.setLevel(0);
                target.giveExp(xp - amount);
                typeLabel = plugin.getString(Strings.EXP_XP_LABEL);
            }
            else {
                target.setLevel(target.getLevel() - amount);
                typeLabel = plugin.getString(Strings.EXP_LEVEL_LABEL);
            }

            plugin.sendMiniMessages(sender, Messages.EXP_REMOVE_FROM, String.valueOf(amount), target.getName(), target.getDisplayName(), typeLabel);
            plugin.sendMiniMessages(target.getPlayer(), Messages.EXP_REMOVED, String.valueOf(amount), sender.getName(), senderDisplay, typeLabel);
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

            final Player target = onlineTarget.getPlayer();
            final String senderDisplay = senderDisplay(sender);
            final String typeLabel;

            if (type.equals(DEFAULT_TYPES.get(0))) {
                final int xp = playerActualXp(target);

                target.setExp(0);
                target.setLevel(0);
                target.giveExp(xp + amount);
                typeLabel = plugin.getString(Strings.EXP_XP_LABEL);
            }
            else {
                target.setLevel(target.getLevel() + amount);
                typeLabel = plugin.getString(Strings.EXP_LEVEL_LABEL);
            }

            plugin.sendMiniMessages(sender, Messages.EXP_GIVE_FROM, String.valueOf(amount), target.getName(), target.getDisplayName(), typeLabel);
            plugin.sendMiniMessages(target.getPlayer(), Messages.EXP_GIVED, String.valueOf(amount), sender.getName(), senderDisplay, typeLabel);
        }

        @Subcommand("%EXPERIENCE_CHECK")
        @Description("%EXPERIENCE_CHECK_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_CHECK_PERM")
        public void onCheckLevel(Player player) {
            final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());
            final int xp = playerActualXp(player);

            player.setLevel(0);
            player.setExp(0);
            player.giveExp(playerProfile.getExp());

            plugin.sendMiniMessages(player, Messages.EXP_BALANCE, String.valueOf(player.getLevel()));

            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xp);
        }

        @CommandCompletion("10")
        @Subcommand("%EXPERIENCE_BOTTLE")
        @Syntax("%EXPERIENCE_BOTTLE_SYNTAX")
        @Description("%EXPERIENCE_BOTTLE_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_BOTTLE_PERM")
        public void onBottleLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer xpWant) {
            final int xp = playerActualXp(player);

            if (xpWant <= 0 || xp <= xpWant) {
                plugin.sendMiniMessages(player, Messages.EXP_INSUFFICIENT);
                return;
            }

            final ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
            final ItemMeta meta = item.getItemMeta();

            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER, 0);
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_INT_VALUE), PersistentDataType.INTEGER, xpWant);
            meta.displayName(plugin.parseColor(plugin.getString(Strings.MINI_MESSAGES_BOTTLE_EXP_NAME)));
            meta.lore(plugin.getStringList(Lists.MINI_MESSAGES_BOTTLE_EXP_LORE).stream().map(plugin::parseColor).collect(Collectors.toList()));
            item.setItemMeta(meta);

            player.getInventory().addItem(item);
            plugin.sendMiniMessages(player, Messages.EXP_BOTTLED);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xp - xpWant);
        }

        @CommandCompletion("10")
        @Subcommand("%EXPERIENCE_WITHDRAW")
        @Syntax("%EXPERIENCE_WITHDRAW_SYNTAX")
        @Description("%EXPERIENCE_WITHDRAW_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_WITHDRAW_PERM")
        public void onWithdrawLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer level) {
            final UUID uuid = player.getUniqueId();
            final PlayerProfile playerProfile = plugin.userManager().get(uuid);
            final int wantedXp = experienceService.getXPForLevel(level);

            if (playerProfile.getExp() < wantedXp) {
                plugin.sendMiniMessages(player, Messages.EXP_INSUFFICIENT);
                return;
            }

            playerProfile.setExp(playerProfile.getExp() - wantedXp);
            player.giveExp(wantedXp);
            experienceService.setDatabaseExp(uuid, playerProfile.getExp());

            plugin.sendMiniMessages(player, Messages.EXP_WITHDRAW, String.valueOf(level));
        }

        @CommandCompletion("10")
        @Subcommand("%EXPERIENCE_DEPOSIT")
        @Syntax("%EXPERIENCE_DEPOSIT_SYNTAX")
        @Description("%EXPERIENCE_DEPOSIT_DESCRIPTION")
        @CommandPermission("%EXPERIENCE_DEPOSIT_PERM")
        public void onDepositLevel(Player player, @Conditions("limits:min=1,max=9999999") Integer xpla) {
            final UUID uuid = player.getUniqueId();
            final PlayerProfile playerProfile = plugin.userManager().get(uuid);
            final int actualLevel = player.getLevel();

            if (actualLevel < xpla) {
                plugin.sendMiniMessages(player, Messages.EXP_INSUFFICIENT);
                return;
            }

            final int amountToDeposit = experienceService.getXPForLevel(xpla);
            final int playerActualXp = playerActualXp(player);

            playerProfile.setExp(playerProfile.getExp() + amountToDeposit);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(playerActualXp - amountToDeposit);
            experienceService.setDatabaseExp(uuid, playerProfile.getExp());

            plugin.sendMiniMessages(player, Messages.EXP_DEPOSIT, String.valueOf(xpla));
        }

        private String senderDisplay(CommandSender sender) {
            return sender instanceof Player player ? player.getDisplayName() : sender.getName();
        }

        private int playerActualXp(Player player) {
            return experienceService.getXPForLevel(player.getLevel()) + (int) (player.getExpToLevel() * player.getExp());
        }

    }

}
