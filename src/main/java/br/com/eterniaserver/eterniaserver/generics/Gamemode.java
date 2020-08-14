package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm")
@CommandPermission("eternia.gamemode")
public class Gamemode extends BaseCommand implements Constants {

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
            player.sendMessage(Strings.M_GM_CHANGED.replace(GM, survivalString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SURVIVAL);
            targetP.sendMessage(Strings.M_GM_CHANGED.replace(GM, survivalString));
            player.sendMessage(Strings.M_GM_TARGET.replace(TARGET, targetP.getDisplayName()).replace(GM, survivalString));
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
            playerZin.sendMessage(Strings.M_GM_CHANGED.replace(GM, creativeString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.CREATIVE);
            targetP.sendMessage(Strings.M_GM_CHANGED.replace(GM, creativeString));
            player.sendMessage(Strings.M_GM_TARGET.replace(TARGET, targetP.getDisplayName()).replace(GM, creativeString));
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
            playerZin.sendMessage(Strings.M_GM_CHANGED.replace(GM, adventureString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.ADVENTURE);
            targetP.sendMessage(Strings.M_GM_CHANGED.replace(GM, adventureString));
            player.sendMessage(Strings.M_GM_TARGET.replace(TARGET, targetP.getDisplayName()).replace(GM, adventureString));
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
            player.sendMessage(Strings.M_GM_CHANGED.replace(GM, spectatorString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(Strings.M_GM_CHANGED.replace(GM, spectatorString));
            player.sendMessage(Strings.M_GM_TARGET.replace(TARGET, targetP.getDisplayName()).replace(GM, spectatorString));
        }
    }

}
