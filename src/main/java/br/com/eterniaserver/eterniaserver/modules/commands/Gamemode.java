package br.com.eterniaserver.eterniaserver.modules.commands;

import br.com.eterniaserver.eternialib.EFiles;
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

    private final String gmChanged = "generic.gm.changed";
    private final String gmChangedT = "generic.gm.changed-target";

    private final String gamemodeString = "%gamemode%";
    private final String targetString = "%target_name%";

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
            messages.sendMessage(gmChanged, gamemodeString, sobString, player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage(gmChanged, gamemodeString, sobString, targetP);
            messages.sendMessage(gmChangedT, targetString, targetName, gamemodeString, sobString, player);
        }
    }

    @Subcommand("creative|c|1|criativo")
    @Syntax("<jogador>")
    public void onCreative(Player player, @Optional OnlinePlayer target) {
        String creString = "Criativo";
        if (target == null) {
            player.setGameMode(GameMode.CREATIVE);
            messages.sendMessage(gmChanged, gamemodeString, creString, player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.CREATIVE);
            messages.sendMessage(gmChanged, gamemodeString, creString, targetP);
            messages.sendMessage(gmChangedT, targetString, targetName, gamemodeString, creString, player);
        }
    }

    @Subcommand("adventure|a|2|aventura")
    @Syntax("<jogador>")
    public void onAdventure(Player player, @Optional OnlinePlayer target) {
        String aveString = "Aventura";
        if (target == null) {
            player.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage(gmChanged, gamemodeString, aveString, player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage(gmChanged, gamemodeString, aveString, targetP);
            messages.sendMessage(gmChangedT, targetString, targetName, gamemodeString, aveString, player);
        }
    }

    @Subcommand("spectator|spect|3|espectador")
    @Syntax("<jogador>")
    public void onSpectator(Player player, @Optional OnlinePlayer target) {
        String espString = "Espectador";
        if (target == null) {
            player.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage(gmChanged, gamemodeString, espString, player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage(gmChanged, gamemodeString, espString, targetP);
            messages.sendMessage(gmChangedT, targetString, targetName, gamemodeString, espString, player);
        }
    }

}
