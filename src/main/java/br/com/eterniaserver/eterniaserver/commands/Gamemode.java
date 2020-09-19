package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
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

import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;
import br.com.eterniaserver.eterniaserver.generics.UtilInternMethods;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm")
@CommandPermission("eternia.gamemode")
@Description(" Altera o modo de jogo de um jogador")
public class Gamemode extends BaseCommand {

    @Default
    @HelpCommand
    @Syntax("<página>")
    @Description(" Ajuda para o comando Gamemode")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("survival|s|0|sobrevivencia")
    @CommandCompletion("@players")
    @Description(" Define o modo de jogo de um jogador como sobrevivência")
    @Syntax("<jogador>")
    public void onSurvival(CommandSender player, @Optional OnlinePlayer target) {
        final String survivalString = "Sobrevivência";
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            playerZin.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(PluginMSGs.M_GM_CHANGED.replace(PluginConstants.GM, survivalString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SURVIVAL);
            targetP.sendMessage(PluginMSGs.M_GM_CHANGED.replace(PluginConstants.GM, survivalString));
            player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.M_GM_TARGET.replace(PluginConstants.GM, survivalString)));
        }
    }

    @Subcommand("creative|c|1|criativo")
    @CommandCompletion("@players")
    @Description(" Define o modo de jogo de um jogador como criativo")
    @Syntax("<jogador>")
    public void onCreative(CommandSender player, @Optional OnlinePlayer target) {
        final String creativeString = "Criativo";
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            playerZin.setGameMode(GameMode.CREATIVE);
            playerZin.sendMessage(PluginMSGs.M_GM_CHANGED.replace(PluginConstants.GM, creativeString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.CREATIVE);
            targetP.sendMessage(PluginMSGs.M_GM_CHANGED.replace(PluginConstants.GM, creativeString));
            player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.M_GM_TARGET.replace(PluginConstants.GM, creativeString)));
        }
    }

    @Subcommand("adventure|a|2|aventura")
    @CommandCompletion("@players")
    @Description(" Define o modo de jogo de um jogador como aventura")
    @Syntax("<jogador>")
    public void onAdventure(CommandSender player, @Optional OnlinePlayer target) {
        final String adventureString = "Aventura";
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            playerZin.setGameMode(GameMode.ADVENTURE);
            playerZin.sendMessage(PluginMSGs.M_GM_CHANGED.replace(PluginConstants.GM, adventureString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.ADVENTURE);
            targetP.sendMessage(PluginMSGs.M_GM_CHANGED.replace(PluginConstants.GM, adventureString));
            player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.M_GM_TARGET.replace(PluginConstants.GM, adventureString)));
        }
    }

    @Subcommand("spectator|spect|3|espectador")
    @CommandCompletion("@players")
    @Description(" Define o modo de jogo de um jogador como espectador")
    @Syntax("<jogador>")
    public void onSpectator(CommandSender player, @Optional OnlinePlayer target) {
        final String spectatorString = "Espectador";
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            playerZin.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(PluginMSGs.M_GM_CHANGED.replace(PluginConstants.GM, spectatorString));
        } else if (target != null) {
            final Player targetP = target.getPlayer();
            targetP.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(PluginMSGs.M_GM_CHANGED.replace(PluginConstants.GM, spectatorString));
            player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.M_GM_TARGET.replace(PluginConstants.GM, spectatorString)));
        }
    }

}
