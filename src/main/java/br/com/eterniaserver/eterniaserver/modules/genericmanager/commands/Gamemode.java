package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm")
@CommandPermission("eternia.gamemode")
public class Gamemode extends BaseCommand {
    private final Messages messages;

    public Gamemode(Messages messages) {
        this.messages = messages;
    }

    @Default
    @Subcommand("help")
    public void showGamemode(CommandSender sender) {
        messages.sendMessage("simp.gmuse", sender);
    }

    @Subcommand("survival|s|0|sobrevivencia")
    @Syntax("<jogador>")
    public void onSurvival(Player player, @Optional OnlinePlayer target) {
        final String playerName = player.getName();

        if (target == null) {
            player.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage("simp.gm","%gamemode%", "Sobrevivência", player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.SURVIVAL);
            messages.sendMessage("simp.gm-other", "%gamemode%", "Sobrevivência", "%target_name%", playerName, targetP);
            messages.sendMessage("simp.other-gm", "%target_name%", targetName, "%gamemode%", "Sobrevivência", player);
        }
    }

    @Subcommand("creative|c|1|criativo")
    @Syntax("<jogador>")
    public void onCreative(Player player, @Optional OnlinePlayer target) {
        final String playerName = player.getName();

        if (target == null) {
            player.setGameMode(GameMode.CREATIVE);
            messages.sendMessage("simp.gm", "%gamemode%", "Criativo", player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.CREATIVE);
            messages.sendMessage("simp.gm-other", "%gamemode%", "Criativo", "%target_name%", playerName, targetP);
            messages.sendMessage("simp.other-gm", "%target_name%", targetName, "%gamemode%", "Criativo", player);
        }
    }

    @Subcommand("adventure|a|2|aventura")
    @Syntax("<jogador>")
    public void onAdventure(Player player, @Optional OnlinePlayer target) {
        final String playerName = player.getName();

        if (target == null) {
            player.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage("simp.gm", "%gamemode%", "Aventura", player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.ADVENTURE);
            messages.sendMessage("simp.gm-other", "%gamemode%", "Aventura", "%target_name%", playerName, targetP);
            messages.sendMessage("simp.other-gm", "%target_name%", targetName, "%gamemode%", "Aventura", player);
        }
    }

    @Subcommand("spectator|spect|3|espectador")
    @Syntax("<jogador>")
    public void onSpectator(Player player, @Optional OnlinePlayer target) {
        final String playerName = player.getName();

        if (target == null) {
            player.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage("simp.gm", "%gamemode%", "Espectador", player);
        } else {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            targetP.setGameMode(GameMode.SPECTATOR);
            messages.sendMessage("simp.gm-other", "%gamemode%", "Espectador", "%target_name%", playerName, targetP);
            messages.sendMessage("simp.other-gm", "%target_name%", targetName, "%gamemode%", "Espectador", player);
        }
    }

}
