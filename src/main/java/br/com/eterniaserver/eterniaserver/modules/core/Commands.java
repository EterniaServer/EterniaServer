package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CatchUnknown;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Utils.RuntimeInfo;

import net.kyori.adventure.text.Component;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Generic extends BaseCommand {

        private final EterniaServer plugin;
        private final RuntimeInfo runtimeInfo;

        public Generic(EterniaServer plugin) {
            this.plugin = plugin;
            this.runtimeInfo = new RuntimeInfo();
        }

        @CommandAlias("%FLY")
        @CommandPermission("%FLY_PERM")
        @Syntax("%FLY_SYNTAX")
        @Description("%FLY_DESCRIPTION")
        public void onFly(Player player, @Optional OnlinePlayer target) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

            if (target != null) {
                Player targetPlayer = target.getPlayer();
                if (!player.hasPermission(plugin.getString(Strings.PERM_FLY_OTHER))) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.SERVER_NO_PERM);
                    return;
                }

                PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, targetPlayer.getUniqueId());
                MessageOptions playerOptions = new MessageOptions(targetProfile.getPlayerName(), targetProfile.getPlayerDisplay());
                MessageOptions targetOptions = new MessageOptions(playerProfile.getPlayerName(), playerProfile.getPlayerDisplay());

                if (targetPlayer.getAllowFlight()) {
                    targetPlayer.setAllowFlight(false);
                    targetPlayer.setFlying(false);
                    EterniaLib.getChatCommons().sendMessage(player, Messages.FLY_DISABLED_TO, playerOptions);
                    EterniaLib.getChatCommons().sendMessage(targetPlayer, Messages.FLY_DISABLED_BY, targetOptions);
                    return;
                }

                targetPlayer.setAllowFlight(true);
                targetPlayer.setFlying(true);

                EterniaLib.getChatCommons().sendMessage(player, Messages.FLY_ENABLED_TO, playerOptions);
                EterniaLib.getChatCommons().sendMessage(targetPlayer, Messages.FLY_ENABLED_BY, targetOptions);
                return;
            }

            if (playerProfile.isOnPvP() && !player.hasPermission(plugin.getString(Strings.PERM_FLY_BYPASS))) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.FLY_PVP_BLOCKED);
                return;
            }

            String worldName = player.getWorld().getName();
            if (plugin.getStringList(Lists.BLACKLISTED_WORLDS_FLY).contains(worldName) && !player.hasPermission(plugin.getString(Strings.PERM_FLY_BYPASS))) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.FLY_WORLD_BLOCKED);
                return;
            }

            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.setFlying(false);
                EterniaLib.getChatCommons().sendMessage(player, Messages.FLY_DISABLED);
                return;
            }

            player.setAllowFlight(true);
            player.setFlying(true);
            EterniaLib.getChatCommons().sendMessage(player, Messages.FLY_ENABLED);
        }

        @CommandAlias("%SPEED")
        @CommandPermission("%SPEED_PERM")
        @Syntax("%SPEED_SYNTAX")
        @Description("%SPEED_DESCRIPTION")
        public void onSpeed(Player player, @Default("1") Integer speed) {
            if (speed < 1 || speed > 10) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.SPEED_INVALID);
                return;
            }

            player.setFlySpeed(speed / 10F);
            player.setWalkSpeed(speed / 10F);
            MessageOptions options = new MessageOptions(String.valueOf(speed));
            EterniaLib.getChatCommons().sendMessage(player, Messages.SPEED_SETED, options);
        }

        @CommandCompletion("@players")
        @CommandAlias("%FEED")
        @CommandPermission("%FEED_PERM")
        @Syntax("%FEED_SYNTAX")
        @Description("%FEED_DESCRIPTION")
        public void onFeed(Player player, @Optional OnlinePlayer onlineTarget) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

            if (onlineTarget != null) {
                Player target = onlineTarget.getPlayer();
                PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

                target.setFoodLevel(20);

                MessageOptions playerOptions = new MessageOptions(targetProfile.getPlayerName(), targetProfile.getPlayerDisplay());
                EterniaLib.getChatCommons().sendMessage(player, Messages.FEED_SETED_TO, playerOptions);

                MessageOptions targetOptions = new MessageOptions(playerProfile.getPlayerName(), playerProfile.getPlayerDisplay());
                EterniaLib.getChatCommons().sendMessage(target, Messages.FEED_SETED_BY, targetOptions);
                return;
            }

            player.setFoodLevel(20);
            EterniaLib.getChatCommons().sendMessage(player, Messages.FEED_SETED);
        }

        @CommandAlias("%THOR")
        @CommandPermission("%THOR_PERM")
        @Syntax("%THOR_SYNTAX")
        @Description("%THOR_DESCRIPTION")
        public void onThor(CommandSender sender, @Optional OnlinePlayer onlineTarget, @Optional Boolean view) {
            if (onlineTarget != null && !Boolean.TRUE.equals(view)) {
                Player target = onlineTarget.getPlayer();
                PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

                target.getWorld().strikeLightning(target.getLocation());

                MessageOptions playerOptions = new MessageOptions(targetProfile.getPlayerName(), targetProfile.getPlayerDisplay());
                EterniaLib.getChatCommons().sendMessage(sender, Messages.THOR_SETED_TO, playerOptions);

                String[] nameAndDisplay = Utils.getNameAndDisplay(sender);
                MessageOptions targetOptions = new MessageOptions(nameAndDisplay[0], nameAndDisplay[1]);
                EterniaLib.getChatCommons().sendMessage(target, Messages.THOR_SETED_BY, targetOptions);
                return;
            }

            if (onlineTarget != null) {
                Player player = onlineTarget.getPlayer();
                player.getWorld().strikeLightning(player.getTargetBlock(null, 100).getLocation());
                return;
            }

            if (sender instanceof Player player) {
                player.getWorld().strikeLightning(player.getTargetBlock(null, 100).getLocation());
            }

            EterniaLib.getChatCommons().sendMessage(sender, Messages.SERVER_NO_PLAYER);
        }

        @CommandAlias("%BROADCAST")
        @Syntax("%BROADCAST_SYNTAX")
        @Description("%BROADCAST_DESCRIPTION")
        @CommandPermission("%BROADCAST_PERM")
        public void onBroadcast(@Default("true") Boolean prefix, String message) {
            Component component = EterniaLib.getChatCommons().parseColor(message);
            if (prefix) {
                MessageOptions options = new MessageOptions(false);
                component = EterniaLib.getChatCommons().parseMessage(Messages.SERVER_PREFIX, options).append(component);
            }
            plugin.getServer().broadcast(component);
        }

        @CommandAlias("%MEM")
        @CommandPermission("%MEM_PERM")
        @Description("%MEM_DESCRIPTION")
        public void onMem(CommandSender sender) {
            runtimeInfo.recalculateRuntime();
            MessageOptions memOptions = new MessageOptions(
                    String.valueOf(runtimeInfo.getFreemem()),
                    String.valueOf(runtimeInfo.getTotalmem())
            );
            MessageOptions hoursOptions = new MessageOptions(
                    String.valueOf(runtimeInfo.getDays()),
                    String.valueOf(runtimeInfo.getHours()),
                    String.valueOf(runtimeInfo.getMinutes()),
                    String.valueOf(runtimeInfo.getSeconds())
            );

            EterniaLib.getChatCommons().sendMessage(sender, Messages.STATS_MEM, memOptions);
            EterniaLib.getChatCommons().sendMessage(sender, Messages.STATS_HOURS, hoursOptions);
        }

        @CommandAlias("%MEM_ALL")
        @CommandPermission("%MEM_ALL_PERM")
        @Description("%MEM_ALL_DESCRIPTION")
        public void onMemAll(CommandSender sender) {
            runtimeInfo.recalculateRuntime();
            MessageOptions memOptions = new MessageOptions(
                    String.valueOf(runtimeInfo.getFreemem()),
                    String.valueOf(runtimeInfo.getTotalmem())
            );
            MessageOptions hoursOptions = new MessageOptions(
                    String.valueOf(runtimeInfo.getDays()),
                    String.valueOf(runtimeInfo.getHours()),
                    String.valueOf(runtimeInfo.getMinutes()),
                    String.valueOf(runtimeInfo.getSeconds())
            );

            plugin.getServer().broadcast(EterniaLib.getChatCommons().parseMessage(Messages.STATS_MEM, memOptions));
            plugin.getServer().broadcast(EterniaLib.getChatCommons().parseMessage(Messages.STATS_HOURS, hoursOptions));
        }

        @CommandAlias("%SUICIDE")
        @CommandPermission("%SUICIDE_PERM")
        @Syntax("%SUICIDE_SYNTAX")
        @Description("%SUICIDE_DESCRIPTION")
        public void onSuicide(Player player, String message) {
            player.setHealth(0);

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

            MessageOptions options = new MessageOptions(
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay(),
                    message
            );
            plugin.getServer().broadcast(EterniaLib.getChatCommons().parseMessage(Messages.SUICIDE_BROADCAST, options));
        }

        @CommandAlias("%PROFILE")
        @CommandPermission("%PROFILE_PERM")
        @Syntax("%PROFILE_SYNTAX")
        @Description("%PROFILE_DESCRIPTION")
        @CommandCompletion("@players")
        public void onProfile(Player player, @Optional OnlinePlayer onlinePlayer) {
            if (onlinePlayer == null) {
                sendProfile(player, player);
                return;
            }

            sendProfile(player, onlinePlayer.getPlayer());
        }

        private void sendProfile(Player player, Player target) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

                int minutesPlayed = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - targetProfile.getEnterMillis());

                targetProfile.setEnterMillis(System.currentTimeMillis());
                targetProfile.setPlayedMinutes(targetProfile.getPlayedMinutes() + minutesPlayed);

                int playedMinutes = targetProfile.getPlayedMinutes();

                Component dhm = EterniaLib.getChatCommons().parseColor(
                        String.format(
                                plugin.getString(Strings.PROFILE_PLAYED_TIME),
                                TimeUnit.MINUTES.toDays(playedMinutes),
                                TimeUnit.MINUTES.toHours(playedMinutes) - TimeUnit.DAYS.toHours(TimeUnit.MINUTES.toDays(playedMinutes)),
                                playedMinutes - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(playedMinutes))
                        )
                );

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

                MessageOptions noPrefixOptions = new MessageOptions(false);
                EterniaLib.getChatCommons().sendMessage(player, Messages.PROFILE_TITLE, noPrefixOptions);
                for (String msg : plugin.getStringList(Lists.PROFILE_CUSTOM_MESSAGES)) {
                    player.sendMessage(EterniaLib.getChatCommons().parseColor(plugin.setPlaceholders(target, msg)));
                }
                player.sendMessage(EterniaLib.getChatCommons().parseMessage(Messages.PROFILE_ACCOUNT_PLAYED_TIME, noPrefixOptions).append(dhm));

                MessageOptions firstJoinOptions = new MessageOptions(false, simpleDateFormat.format(new Date(targetProfile.getFirstJoin().getTime())));
                EterniaLib.getChatCommons().sendMessage(player, Messages.PROFILE_REGISTER_DATA, firstJoinOptions);

                MessageOptions lastJoinOptions = new MessageOptions(false, simpleDateFormat.format(new Date(targetProfile.getLastJoin().getTime())));
                EterniaLib.getChatCommons().sendMessage(player, Messages.PROFILE_LAST_LOGIN, lastJoinOptions);

                EterniaLib.getChatCommons().sendMessage(player, Messages.PROFILE_TITLE, noPrefixOptions);

                EterniaLib.getDatabase().update(PlayerProfile.class, targetProfile);
            });
        }

        @CommandAlias("%CONDENSER")
        @CommandPermission("%CONDENSER_PERM")
        @Description("%CONDENSER_DESCRIPTION")
        public void onCondenser(Player player) {
            int[] amounts = new int[CondenserEnum.values().length];

            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    for (CondenserEnum condenser : CondenserEnum.values()) {
                        amounts[condenser.ordinal()] += condenser.checkSimilar(itemStack);
                    }
                }
            }

            for (CondenserEnum condenser : CondenserEnum.values()) {
                condenser.condenseItem(amounts[condenser.ordinal()], player);
            }

            EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_CONDENSER);
        }

        private enum CondenserEnum {

            COAL(Material.COAL, Material.COAL_BLOCK),
            LAPIS(Material.LAPIS_LAZULI, Material.LAPIS_BLOCK),
            COPPER(Material.COPPER_INGOT, Material.COPPER_BLOCK),
            REDSTONE(Material.REDSTONE, Material.REDSTONE_BLOCK),
            IRON(Material.IRON_INGOT, Material.IRON_BLOCK),
            GOLD(Material.GOLD_INGOT, Material.GOLD_BLOCK),
            EMERALD(Material.EMERALD, Material.EMERALD_BLOCK),
            DIAMOND(Material.DIAMOND, Material.DIAMOND_BLOCK),
            NETHERITE(Material.NETHERITE_INGOT, Material.NETHERITE_BLOCK);

            private final Material ingot;
            private final ItemStack ingotBase;
            private final Material block;

            CondenserEnum(Material ingot, Material block) {
                this.ingot = ingot;
                this.ingotBase = new ItemStack(ingot);
                this.block = block;
            }

            public int checkSimilar(ItemStack item) {
                if (!item.isSimilar(ingotBase)) {
                    return 0;
                }
                if (!Objects.equals(ingotBase.getItemMeta().displayName(), item.getItemMeta().displayName())) {
                    return 0;
                }

                return item.getAmount();
            }

            public void condenseItem(int amount, Player player) {
                int result = amount / 9;
                if (result != 0) {
                    player.getInventory().removeItem(new ItemStack(ingot, result * 9));
                    player.getInventory().addItem(new ItemStack(block, result));
                }
            }
        }
    }

    static class Inventory extends BaseCommand {

        private final EterniaServer plugin;

        public Inventory(EterniaServer plugin) {
            this.plugin = plugin;
        }

        @CommandAlias("%WORKBENCH")
        @Description("%WORKBENCH_DESCRIPTION")
        @CommandPermission("%WORKBENCH_PERM")
        public void onWorkbench(Player player) {
            player.openWorkbench(null, true);
        }

        @CommandAlias("%OPENINV")
        @CommandCompletion("@players")
        @Syntax("%OPENINV_SYNTAX")
        @Description("%OPENINV_DESCRIPTION")
        @CommandPermission("%OPENINV_PERM")
        public void onOpenInventory(Player player, OnlinePlayer target) {
            player.openInventory(target.getPlayer().getInventory());
        }

        @CommandAlias("%ENDERCHEST")
        @CommandCompletion("@players")
        @Syntax("%ENDERCHEST_SYNTAX")
        @Description("%ENDERCHEST_DESCRIPTION")
        @CommandPermission("%ENDERCHEST_PERM")
        public void onEnderChest(Player player, @Optional OnlinePlayer target) {
            if (target == null) {
                player.openInventory(player.getEnderChest());
                return;
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_EC_OTHER))) {
                player.openInventory(target.getPlayer().getEnderChest());
                return;
            }

            EterniaLib.getChatCommons().sendMessage(player, Messages.SERVER_NO_PERM);
        }

        @CommandAlias("%HAT")
        @Description("%HAT_DESCRIPTION")
        @CommandPermission("%HAT_PERM")
        public void onHat(Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_NOT_FOUND);
                return;
            }

            ItemStack hat = player.getInventory().getHelmet();
            if (hat != null) {
                player.getWorld().dropItem(player.getLocation().add(0, 1, 0), hat);
            }
            player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_HELMET);
        }

    }

    @CommandAlias("%AFK")
    static class Afk extends BaseCommand {

        private final EterniaServer plugin;

        public Afk(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        @Default
        @CatchUnknown
        @HelpCommand
        @Syntax("%AFK_SYNTAX")
        @Description("%AFK_DESCRIPTION")
        @CommandPermission("%AFK_PERM")
        public void onDefault(Player player) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());
            AfkStatusEvent event = new AfkStatusEvent(player, !playerProfile.isAfk(), AfkStatusEvent.Cause.COMMAND);
            plugin.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            MessageOptions options = new MessageOptions(playerProfile.getPlayerName(), playerProfile.getPlayerDisplay());
            if (playerProfile.isAfk()) {
                plugin.getServer().broadcast(EterniaLib.getChatCommons().parseMessage(Messages.AFK_LEAVE, options));
                playerProfile.setAfk(false);
                return;
            }

            plugin.getServer().broadcast(EterniaLib.getChatCommons().parseMessage(Messages.AFK_ENTER, options));
            playerProfile.setAfk(true);
        }
    }

    @CommandAlias("%GAMEMODE")
    static class EGameMode extends BaseCommand {

        private final EterniaServer plugin;

        public EGameMode(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        @Default
        @CatchUnknown
        @HelpCommand
        @Syntax("%GAMEMODE_SYNTAX")
        @Description("%GAMEMODE_DESCRIPTION")
        @CommandPermission("%GAMEMODE_PERM")
        public void onHelp(CommandHelp help) {
            help.showHelp();
        }

        @CommandCompletion("@players")
        @Subcommand("%GAMEMODE_SURVIVAL")
        @Syntax("%GAMEMODE_SURVIVAL_SYNTAX")
        @Description("%GAMEMODE_SURVIVAL_DESCRIPTION")
        @CommandPermission("%GAMEMODE_SURVIVAL_PERM")
        public void onSurvival(CommandSender sender, @Optional OnlinePlayer target) {
            setGameMode(sender, target, GameMode.SURVIVAL, 0);
        }

        @CommandCompletion("@players")
        @Subcommand("%GAMEMODE_CREATIVE")
        @Syntax("%GAMEMODE_CREATIVE_SYNTAX")
        @Description("%GAMEMODE_CREATIVE_DESCRIPTION")
        @CommandPermission("%GAMEMODE_CREATIVE_PERM")
        public void onCreative(CommandSender sender, @Optional OnlinePlayer target) {
            setGameMode(sender, target, GameMode.CREATIVE, 1);
        }

        @CommandCompletion("@players")
        @Subcommand("%GAMEMODE_ADVENTURE")
        @Syntax("%GAMEMODE_ADVENTURE_SYNTAX")
        @Description("%GAMEMODE_ADVENTURE_DESCRIPTION")
        @CommandPermission("%GAMEMODE_ADVENTURE_PERM")
        public void onAdventure(CommandSender sender, @Optional OnlinePlayer target) {
            setGameMode(sender, target, GameMode.ADVENTURE, 2);
        }

        @CommandCompletion("@players")
        @Subcommand("%GAMEMODE_SPECTATOR")
        @Syntax("%GAMEMODE_SPECTATOR_SYNTAX")
        @Description("%GAMEMODE_SPECTATOR_DESCRIPTION")
        @CommandPermission("%GAMEMODE_SPECTATOR_PERM")
        public void onSpectator(CommandSender sender, @Optional OnlinePlayer target) {
            setGameMode(sender, target, GameMode.SPECTATOR, 3);
        }

        private void setGameMode(CommandSender sender, OnlinePlayer onlineTarget, GameMode gameMode, int type) {
            if (onlineTarget != null) {
                Player target = onlineTarget.getPlayer();
                PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());
                String typeName = getType(type);

                target.setGameMode(gameMode);
                MessageOptions setOptions = new MessageOptions(typeName);
                EterniaLib.getChatCommons().sendMessage(target, Messages.GAMEMODE_SETED, setOptions);

                MessageOptions fromOptions = new MessageOptions(
                        typeName,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
                EterniaLib.getChatCommons().sendMessage(sender, Messages.GAMEMODE_SET_FROM, fromOptions);
                return;
            }

            if (sender instanceof Player player) {
                player.setGameMode(gameMode);
                MessageOptions options = new MessageOptions(getType(type));
                EterniaLib.getChatCommons().sendMessage(player, Messages.GAMEMODE_SETED, options);
                return;
            }

            EterniaLib.getChatCommons().sendMessage(sender, Messages.GAMEMODE_NOT_BY_CONSOLE);
        }

        private String getType(int type) {
            return switch (type) {
                case 0 -> plugin.getString(Strings.CONS_SURVIVAL);
                case 1 -> plugin.getString(Strings.CONS_CREATIVE);
                case 2 -> plugin.getString(Strings.CONS_ADVENTURE);
                default -> plugin.getString(Strings.CONS_SPECTATOR);
            };
        }
    }

    @CommandAlias("%GODMODE")
    static class GodMode extends BaseCommand {

        @Default
        @CatchUnknown
        @Syntax("%GODMODE_SYNTAX")
        @Description("%GODMODE_DESCRIPTION")
        @CommandPermission("%GODMODE_PERM")
        public void onGodMode(Player player, @Optional OnlinePlayer onlineTarget) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

            if (onlineTarget != null) {
                Player target = onlineTarget.getPlayer();
                PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

                MessageOptions playerOptions = new MessageOptions(targetProfile.getPlayerName(), targetProfile.getPlayerDisplay());
                MessageOptions targetOptions = new MessageOptions(playerProfile.getPlayerName(), playerProfile.getPlayerDisplay());

                targetProfile.setGod(!targetProfile.isGod());
                if (targetProfile.isGod()) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.GODMODE_ENABLED_TO, playerOptions);
                    EterniaLib.getChatCommons().sendMessage(target, Messages.GODMODE_ENABLED_BY, targetOptions);
                    return;
                }

                EterniaLib.getChatCommons().sendMessage(player, Messages.GODMODE_DISABLED_TO, playerOptions);
                EterniaLib.getChatCommons().sendMessage(target, Messages.GODMODE_DISABLED_BY, targetOptions);
                return;
            }

            playerProfile.setGod(!playerProfile.isGod());
            if (playerProfile.isGod()) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.GODMODE_ENABLED);
                return;
            }

            EterniaLib.getChatCommons().sendMessage(player, Messages.GODMODE_DISABLED);
        }

    }

}
