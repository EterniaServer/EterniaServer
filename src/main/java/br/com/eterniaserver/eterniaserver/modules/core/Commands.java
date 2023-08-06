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
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

final class Commands {

    private Commands() {
        throw new IllegalStateException("Utility class");
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

            plugin.sendMiniMessages(player, Messages.SERVER_NO_PERM);
        }

        @CommandAlias("%HAT")
        @Description("%HAT_DESCRIPTION")
        @CommandPermission("%HAT_PERM")
        public void onHat(Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                plugin.sendMiniMessages(player, Messages.ITEM_NOT_FOUND);
                return;
            }

            ItemStack hat = player.getInventory().getHelmet();
            if (hat != null) {
                player.getWorld().dropItem(player.getLocation().add(0, 1, 0), hat);
            }
            player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            plugin.sendMiniMessages(player, Messages.ITEM_HELMET);
        }

    }

    @CommandAlias("%AFK")
    static class Afk extends BaseCommand {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        public Afk(final EterniaServer plugin) {
            this.databaseInterface = EterniaLib.getDatabase();
            this.plugin = plugin;
        }

        @Default
        @CatchUnknown
        @HelpCommand
        @Syntax("%AFK_SYNTAX")
        @Description("%AFK_DESCRIPTION")
        @CommandPermission("%AFK_PERM")
        public void onDefault(Player player) {
            PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());
            AfkStatusEvent event = new AfkStatusEvent(player, !playerProfile.isAfk(), AfkStatusEvent.Cause.COMMAND);
            plugin.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            if (playerProfile.isAfk()) {
                Component afkLeaveMessage = plugin.getMiniMessage(
                        Messages.AFK_LEAVE,
                        true,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
                plugin.getServer().broadcast(afkLeaveMessage);
                playerProfile.setAfk(false);
                return;
            }

            Component afkEnterMessage = plugin.getMiniMessage(
                    Messages.AFK_ENTER,
                    true,
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay()
            );
            plugin.getServer().broadcast(afkEnterMessage);
            playerProfile.setAfk(true);
        }
    }

    @CommandAlias("%GAMEMODE")
    static class EGameMode extends BaseCommand {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        public EGameMode(final EterniaServer plugin) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
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
                PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());
                String typeName = getType(type);

                target.setGameMode(gameMode);
                plugin.sendMiniMessages(target, Messages.GAMEMODE_SETED, typeName);
                plugin.sendMiniMessages(
                        sender,
                        Messages.GAMEMODE_SET_FROM,
                        typeName,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
                return;
            }

            if (sender instanceof Player player) {
                player.setGameMode(gameMode);
                plugin.sendMiniMessages(player, Messages.GAMEMODE_SETED, getType(type));
                return;
            }

            plugin.sendMiniMessages(sender, Messages.GAMEMODE_NOT_BY_CONSOLE);
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

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        public GodMode(final EterniaServer plugin) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
        }

        @Default
        @CatchUnknown
        @Syntax("%GODMODE_SYNTAX")
        @Description("%GODMODE_DESCRIPTION")
        @CommandPermission("%GODMODE_PERM")
        public void onGodMode(Player player, @Optional OnlinePlayer onlineTarget) {
            PlayerProfile playerProfile = databaseInterface.get(PlayerProfile.class, player.getUniqueId());

            if (onlineTarget != null) {
                Player target = onlineTarget.getPlayer();
                PlayerProfile targetProfile = databaseInterface.get(PlayerProfile.class, target.getUniqueId());

                targetProfile.setGod(!targetProfile.isGod());
                if (targetProfile.isGod()) {
                    plugin.sendMiniMessages(
                            player,
                            Messages.GODMODE_ENABLED_TO,
                            targetProfile.getPlayerName(),
                            targetProfile.getPlayerDisplay()
                    );
                    plugin.sendMiniMessages(
                            target,
                            Messages.GODMODE_ENABLED_BY,
                            playerProfile.getPlayerName(),
                            playerProfile.getPlayerDisplay()
                    );
                    return;
                }

                plugin.sendMiniMessages(
                        player,
                        Messages.GODMODE_DISABLED_TO,
                        targetProfile.getPlayerName(),
                        targetProfile.getPlayerDisplay()
                );
                plugin.sendMiniMessages(
                        target,
                        Messages.GODMODE_DISABLED_BY,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
                return;
            }

            playerProfile.setGod(!playerProfile.isGod());
            if (playerProfile.isGod()) {
                plugin.sendMiniMessages(player, Messages.GODMODE_ENABLED);
                return;
            }

            plugin.sendMiniMessages(player, Messages.GODMODE_DISABLED);
        }

    }

}
