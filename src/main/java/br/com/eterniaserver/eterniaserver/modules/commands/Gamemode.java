package br.com.eterniaserver.eterniaserver.modules.commands;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Strings;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

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
        messages.sendMessage("generic.gm.use", sender);
    }

    @Subcommand("survival|s|0|sobrevivencia")
    @Syntax("<jogador>")
    public void onSurvival(Player player, @Optional OnlinePlayer target) {
        String sobString = "Sobrevivência";
        if (target == null) {
            player.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage(Strings.GM_CHANGED, Strings.GAMEMODE, sobString, player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage(Strings.GM_CHANGED, Strings.GAMEMODE, sobString, targetP);
            messages.sendMessage(Strings.GM_CHANGED_TARGET, Strings.TARGET_NAME, targetName, Strings.GAMEMODE, sobString, player);
        }
    }

    @Subcommand("creative|c|1|criativo")
    @Syntax("<jogador>")
    public void onCreative(Player player, @Optional OnlinePlayer target) {
        String creString = "Criativo";
        if (target == null) {
            player.setGameMode(GameMode.CREATIVE);
            messages.sendMessage(Strings.GM_CHANGED, Strings.GAMEMODE, creString, player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.CREATIVE);
            messages.sendMessage(Strings.GM_CHANGED, Strings.GAMEMODE, creString, targetP);
            messages.sendMessage(Strings.GM_CHANGED_TARGET, Strings.TARGET_NAME, targetName, Strings.GAMEMODE, creString, player);
        }
    }

    @Subcommand("adventure|a|2|aventura")
    @Syntax("<jogador>")
    public void onAdventure(Player player, @Optional OnlinePlayer target) {
        String aveString = "Aventura";
        if (target == null) {
            player.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage(Strings.GM_CHANGED, Strings.GAMEMODE, aveString, player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage(Strings.GM_CHANGED, Strings.GAMEMODE, aveString, targetP);
            messages.sendMessage(Strings.GM_CHANGED_TARGET, Strings.TARGET_NAME, targetName, Strings.GAMEMODE, aveString, player);
        }
    }

    @Subcommand("spectator|spect|3|espectador")
    @Syntax("<jogador>")
    public void onSpectator(Player player, @Optional OnlinePlayer target) {
        String espString = "Espectador";
        if (target == null) {
            player.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage(Strings.GM_CHANGED, Strings.GAMEMODE, espString, player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage(Strings.GM_CHANGED, Strings.GAMEMODE, espString, targetP);
            messages.sendMessage(Strings.GM_CHANGED_TARGET, Strings.TARGET_NAME, targetName, Strings.GAMEMODE, espString, player);
        }
    }

}
