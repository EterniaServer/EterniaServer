package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.Strings;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm")
@CommandPermission("eternia.gamemode")
public class Gamemode extends BaseCommand {
    private final EFiles messages;

    public Gamemode(EFiles messages) {
        this.messages = messages;
    }

    @Default
    @Subcommand("help")
    public void showGamemode(CommandSender sender) {
        messages.sendMessage(Strings.M_GM_USE, sender);
    }

    @Subcommand("survival|s|0|sobrevivencia")
    @Syntax("<jogador>")
    public void onSurvival(Player player, @Optional OnlinePlayer target) {
        final String survivalString = "Sobrevivência";
        if (target == null) {
            player.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage(Strings.M_GM_CHANGED, Constants.GM, survivalString, player);
        } else {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage(Strings.M_GM_CHANGED, Constants.GM, survivalString, targetP);
            messages.sendMessage(Strings.M_GM_TARGET, Constants.TARGET, targetP.getDisplayName(), Constants.GM, survivalString, player);
        }
    }

    @Subcommand("creative|c|1|criativo")
    @Syntax("<jogador>")
    public void onCreative(Player player, @Optional OnlinePlayer target) {
        final String creativeString = "Criativo";
        if (target == null) {
            player.setGameMode(GameMode.CREATIVE);
            messages.sendMessage(Strings.M_GM_CHANGED, Constants.GM, creativeString, player);
        } else {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.CREATIVE);
            messages.sendMessage(Strings.M_GM_CHANGED, Constants.GM, creativeString, targetP);
            messages.sendMessage(Strings.M_GM_TARGET, Constants.TARGET, targetP.getDisplayName(), Constants.GM, creativeString, player);
        }
    }

    @Subcommand("adventure|a|2|aventura")
    @Syntax("<jogador>")
    public void onAdventure(Player player, @Optional OnlinePlayer target) {
        final String adventureString = "Aventura";
        if (target == null) {
            player.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage(Strings.M_GM_CHANGED, Constants.GM, adventureString, player);
        } else {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage(Strings.M_GM_CHANGED, Constants.GM, adventureString, targetP);
            messages.sendMessage(Strings.M_GM_TARGET, Constants.TARGET, targetP.getDisplayName(), Constants.GM, adventureString, player);
        }
    }

    @Subcommand("spectator|spect|3|espectador")
    @Syntax("<jogador>")
    public void onSpectator(Player player, @Optional OnlinePlayer target) {
        final String spectatorString = "Espectador";
        if (target == null) {
            player.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage(Strings.M_GM_CHANGED, Constants.GM, spectatorString, player);
        } else {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage(Strings.M_GM_CHANGED, Constants.GM, spectatorString, targetP);
            messages.sendMessage(Strings.M_GM_TARGET, Constants.TARGET, targetP.getDisplayName(), Constants.GM, spectatorString, player);
        }
    }

}
