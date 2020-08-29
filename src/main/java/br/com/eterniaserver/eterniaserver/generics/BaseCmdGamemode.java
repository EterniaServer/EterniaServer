package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.strings.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm")
@CommandPermission("eternia.gamemode")
public class BaseCmdGamemode extends BaseCommand {

    @Default
    @Subcommand("help")
    public void showGamemode(CommandSender sender) {
        sender.sendMessage(Strings.M_GM_USE);
    }

    @Subcommand("survival|s|0|sobrevivencia")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    public void onSurvival(CommandSender player, @Optional OnlinePlayer target) {
        final String survivalString = "Sobrevivência";
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            playerZin.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(Strings.M_GM_CHANGED.replace(Constants.GM, survivalString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SURVIVAL);
            targetP.sendMessage(Strings.M_GM_CHANGED.replace(Constants.GM, survivalString));
            player.sendMessage(InternMethods.putName(targetP, Strings.M_GM_TARGET.replace(Constants.GM, survivalString)));
        }
    }

    @Subcommand("creative|c|1|criativo")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    public void onCreative(CommandSender player, @Optional OnlinePlayer target) {
        final String creativeString = "Criativo";
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            playerZin.setGameMode(GameMode.CREATIVE);
            playerZin.sendMessage(Strings.M_GM_CHANGED.replace(Constants.GM, creativeString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.CREATIVE);
            targetP.sendMessage(Strings.M_GM_CHANGED.replace(Constants.GM, creativeString));
            player.sendMessage(InternMethods.putName(targetP, Strings.M_GM_TARGET.replace(Constants.GM, creativeString)));
        }
    }

    @Subcommand("adventure|a|2|aventura")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    public void onAdventure(CommandSender player, @Optional OnlinePlayer target) {
        final String adventureString = "Aventura";
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            playerZin.setGameMode(GameMode.ADVENTURE);
            playerZin.sendMessage(Strings.M_GM_CHANGED.replace(Constants.GM, adventureString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.ADVENTURE);
            targetP.sendMessage(Strings.M_GM_CHANGED.replace(Constants.GM, adventureString));
            player.sendMessage(InternMethods.putName(targetP, Strings.M_GM_TARGET.replace(Constants.GM, adventureString)));
        }
    }

    @Subcommand("spectator|spect|3|espectador")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    public void onSpectator(CommandSender player, @Optional OnlinePlayer target) {
        final String spectatorString = "Espectador";
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            playerZin.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(Strings.M_GM_CHANGED.replace(Constants.GM, spectatorString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(Strings.M_GM_CHANGED.replace(Constants.GM, spectatorString));
            player.sendMessage(InternMethods.putName(targetP, Strings.M_GM_TARGET.replace(Constants.GM, spectatorString)));
        }
    }

}
