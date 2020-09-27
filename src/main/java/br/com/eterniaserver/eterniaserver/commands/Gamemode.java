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

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
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
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            setGamemode(playerZin, GameMode.SURVIVAL, 0);
        } else if (target != null) {
            setGamemode(player, target.getPlayer(), GameMode.SURVIVAL, 0);
        }
    }

    @Subcommand("creative|c|1|criativo")
    @CommandCompletion("@players")
    @Description(" Define o modo de jogo de um jogador como criativo")
    @Syntax("<jogador>")
    public void onCreative(CommandSender player, @Optional OnlinePlayer target) {
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            setGamemode(playerZin, GameMode.CREATIVE, 1);
        } else if (target != null) {
            setGamemode(player, target.getPlayer(), GameMode.CREATIVE, 1);
        }
    }

    @Subcommand("adventure|a|2|aventura")
    @CommandCompletion("@players")
    @Description(" Define o modo de jogo de um jogador como aventura")
    @Syntax("<jogador>")
    public void onAdventure(CommandSender player, @Optional OnlinePlayer target) {
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            setGamemode(playerZin, GameMode.ADVENTURE, 2);
        } else if (target != null) {
            setGamemode(player, target.getPlayer(), GameMode.ADVENTURE, 2);
        }
    }

    @Subcommand("spectator|spect|3|espectador")
    @CommandCompletion("@players")
    @Description(" Define o modo de jogo de um jogador como espectador")
    @Syntax("<jogador>")
    public void onSpectator(CommandSender player, @Optional OnlinePlayer target) {
        final Player playerZin = (Player) player;
        if (target == null && playerZin != null) {
            setGamemode(playerZin, GameMode.SPECTATOR, 3);
        } else if (target != null) {
            setGamemode(player, target.getPlayer(), GameMode.SPECTATOR, 3);
        }
    }

    private void setGamemode(Player player, GameMode gameMode, int type) {
        final String typeName = getType(type);
        player.setGameMode(gameMode);
        EterniaServer.configs.sendMessage(player, Messages.GAMEMODE_SETED, typeName);
    }

    private void setGamemode(CommandSender player, Player target, GameMode gameMode, int type) {
        final String typeName = getType(type);
        target.setGameMode(gameMode);
        EterniaServer.configs.sendMessage(target, Messages.GAMEMODE_SETED, typeName);
        EterniaServer.configs.sendMessage(player, Messages.GAMEMODE_SET_FROM, typeName, target.getName(), target.getDisplayName());
    }

    private String getType(int type) {
        switch (type) {
            case 0:
                return EterniaServer.configs.gmSurvival;
            case 1:
                return EterniaServer.configs.gmCreative;
            case 2:
                return EterniaServer.configs.gmAdventure;
            default:
                return EterniaServer.configs.gmSpectator;
        }
    }

}
