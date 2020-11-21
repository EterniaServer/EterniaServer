package br.com.eterniaserver.eterniaserver.commands;

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
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("%gamemode")
public class Gamemode extends BaseCommand {

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("%gamemode_syntax")
    @Description("%gamemode_description")
    @CommandPermission("%gamemode_perm")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @CommandCompletion("@players")
    @Syntax("%gamemode_survival_syntax")
    @Subcommand("%gamemode_survival")
    @Description("%gamemode_survival_description")
    @CommandPermission("%gamemode_survival_perm")
    public void onSurvival(CommandSender player, @Optional OnlinePlayer target) {
        User user = new User(player);

        if (target == null && user.isPlayer()) {
            setGamemode(user, GameMode.SURVIVAL, 0);
            return;
        }

        if (target != null) {
            setGamemode(user, target.getPlayer(), GameMode.SURVIVAL, 0);
        }
    }

    @CommandCompletion("@players")
    @Syntax("%gamemode_creative_syntax")
    @Subcommand("%gamemode_creative")
    @Description("%gamemode_creative_description")
    @CommandPermission("%gamemode_creative_perm")
    public void onCreative(CommandSender player, @Optional OnlinePlayer target) {
        User user = new User(player);

        if (target == null && user.isPlayer()) {
            setGamemode(user, GameMode.CREATIVE, 1);
            return;
        }

        if (target != null) {
            setGamemode(user, target.getPlayer(), GameMode.CREATIVE, 1);
        }
    }

    @CommandCompletion("@players")
    @Syntax("%gamemode_adventure_syntax")
    @Subcommand("%gamemode_adventure")
    @Description("%gamemode_adventure_description")
    @CommandPermission("%gamemode_adventure_perm")
    public void onAdventure(CommandSender player, @Optional OnlinePlayer target) {
        User user = new User(player);

        if (target == null && user.isPlayer()) {
            setGamemode(user, GameMode.ADVENTURE, 2);
            return;
        }

        if (target != null) {
            setGamemode(user, target.getPlayer(), GameMode.ADVENTURE, 2);
        }
    }

    @CommandCompletion("@players")
    @Syntax("%gamemode_spectator_syntax")
    @Subcommand("%gamemode_spectator")
    @Description("%gamemode_spectator_description")
    @CommandPermission("%gamemode_spectator_perm")
    public void onSpectator(CommandSender player, @Optional OnlinePlayer target) {
        User user = new User(player);

        if (target == null && user.isPlayer()) {
            setGamemode(user, GameMode.SPECTATOR, 3);
            return;
        }

        if (target != null) {
            setGamemode(user, target.getPlayer(), GameMode.SPECTATOR, 3);
        }
    }

    private void setGamemode(User user, GameMode gameMode, int type) {
        String typeName = getType(type);

        user.getPlayer().setGameMode(gameMode);
        user.sendMessage(Messages.GAMEMODE_SETED, typeName);
    }

    private void setGamemode(User user, Player targets, GameMode gameMode, int type) {
        String typeName = getType(type);
        User target = new User(targets);

        target.getPlayer().setGameMode(gameMode);
        target.sendMessage(Messages.GAMEMODE_SETED, typeName);
        user.sendMessage(Messages.GAMEMODE_SET_FROM, typeName, target.getName(), target.getDisplayName());
    }

    private String getType(int type) {
        switch (type) {
            case 0:
                return EterniaServer.getString(Strings.CONS_SURVIVAL);
            case 1:
                return EterniaServer.getString(Strings.CONS_CREATIVE);
            case 2:
                return EterniaServer.getString(Strings.CONS_ADVENTURE);
            default:
                return EterniaServer.getString(Strings.CONS_SPECTATOR);
        }
    }

}
