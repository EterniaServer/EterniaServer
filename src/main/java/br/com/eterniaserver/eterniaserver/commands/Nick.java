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
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.generics.APIEconomy;
import br.com.eterniaserver.eterniaserver.generics.APIPlayer;

import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("nick|nickname")
@CommandPermission("eternia.nickname")
public class Nick extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    @Syntax("<novo nome> ou <novo nome> <jogador>")
    @Description(" Altera o seu apelido ou de um jogador")
    public void onNick(Player player, String string, @Optional OnlinePlayer target) {
        string = string.replaceAll("[^a-zA-Z0-9]", "");
        if (!player.hasPermission("eternia.nickname.others")) {
            if (target == null) {
                APIPlayer.playerNick(player, string);
            } else {
                EterniaServer.configs.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        } else {
            APIPlayer.staffNick(target, player, string);
        }
    }

    @Subcommand("deny")
    @Description(" Nega um pedido de mudança de apelido")
    public void onNickDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (APIPlayer.hasNickRequest(uuid)) {
            APIPlayer.removeNickRequest(uuid);
            EterniaServer.configs.sendMessage(player, Messages.CHAT_NICK_DENIED);
        } else {
            EterniaServer.configs.sendMessage(player, Messages.CHAT_NICK_NOT_REQUESTED);
        }
    }

    @Subcommand("accept")
    @Description(" Aceita um pedido de mudança de apelido")
    public void onNickAccept(Player player) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (APIPlayer.hasNickRequest(uuid)) {
            if (APIEconomy.hasMoney(uuid, EterniaServer.configs.nickCost)) {
                APIEconomy.removeMoney(uuid, EterniaServer.configs.nickCost);
                APIPlayer.updateNickName(player, uuid);
            } else {
                EterniaServer.configs.sendMessage(player, Messages.ECO_NO_MONEY);
            }
            APIPlayer.removeNickRequest(uuid);
        } else {
            EterniaServer.configs.sendMessage(player, Messages.CHAT_NICK_NOT_REQUESTED);
        }
    }

    @HelpCommand
    @Subcommand("help")
    @Syntax("<página>")
    @Description(" Ajuda para os comandos de apelido")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
