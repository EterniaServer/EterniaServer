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
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class Commands {

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
            final PlayerProfile playerProfile = plugin.userManager().get(player.getUniqueId());
            final AfkStatusEvent event = new AfkStatusEvent(player, !playerProfile.getAfk(), AfkStatusEvent.Cause.COMMAND);
            plugin.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) return;

            if (playerProfile.getAfk()) {
                Bukkit.broadcast(plugin.getMiniMessage(Messages.AFK_LEAVE, true, playerProfile.getName(), playerProfile.getDisplayName()));
                playerProfile.setAfk(false);
                return;
            }

            Bukkit.broadcast(plugin.getMiniMessage(Messages.AFK_ENTER, true, playerProfile.getName(), playerProfile.getDisplayName()));
            playerProfile.setLocation(player.getLocation());
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
                final Player target = onlineTarget.getPlayer();
                final String typeName = getType(type);

                target.setGameMode(gameMode);
                plugin.sendMiniMessages(target, Messages.GAMEMODE_SETED, typeName);
                plugin.sendMiniMessages(sender, Messages.GAMEMODE_SET_FROM, typeName, target.getName(), target.getDisplayName());
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

}
