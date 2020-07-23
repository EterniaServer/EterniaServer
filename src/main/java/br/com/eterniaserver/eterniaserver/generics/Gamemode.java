package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
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
        if (target == null) {
            player.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage("generic.gm.changed","%gamemode%", "Sobrevivência", player);
        } else {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage("generic.gm.changed", "%gamemode%", "Sobrevivência", targetP);
            messages.sendMessage("generic.gm.changed-target", Constants.TARGET, targetP.getDisplayName(), "%gamemode%", "Sobrevivência", player);
        }
    }

    @Subcommand("creative|c|1|criativo")
    @Syntax("<jogador>")
    public void onCreative(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setGameMode(GameMode.CREATIVE);
            messages.sendMessage("generic.gm.changed", "%gamemode%", "Criativo", player);
        } else {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.CREATIVE);
            messages.sendMessage("generic.gm.changed", "%gamemode%", "Criativo", targetP);
            messages.sendMessage("generic.gm.changed-target", Constants.TARGET, targetP.getDisplayName(), "%gamemode%", "Criativo", player);
        }
    }

    @Subcommand("adventure|a|2|aventura")
    @Syntax("<jogador>")
    public void onAdventure(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage("generic.gm.changed", "%gamemode%", "Aventura", player);
        } else {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage("generic.gm.changed", "%gamemode%", "Aventura", targetP);
            messages.sendMessage("generic.gm.changed-target", Constants.TARGET, targetP.getDisplayName(), "%gamemode%", "Aventura", player);
        }
    }

    @Subcommand("spectator|spect|3|espectador")
    @Syntax("<jogador>")
    public void onSpectator(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage("generic.gm.changed", "%gamemode%", "Espectador", player);
        } else {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage("generic.gm.changed", "%gamemode%", "Espectador", targetP);
            messages.sendMessage("generic.gm.changed-target", Constants.TARGET, targetP.getDisplayName(), "%gamemode%", "Espectador", player);
        }
    }

}
