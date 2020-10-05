package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CatchUnknown;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIChat;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("ch|channels")
@CommandPermission("eternia.chat.channels")
public class Channel extends BaseCommand {

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("<página>")
    @Description(" Ajuda para o sistema de Canais")
    public void onChannels(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("local")
    @CommandAlias("l|local")
    @Syntax("<msg>")
    @Description(" Fale ou vá para o canal local")
    public void toLocal(Player player, @Optional String[] messages) {
        changeChannel(0, EterniaServer.configs.chLocal, player, messages);
    }

    @Subcommand("global")
    @CommandAlias("g|global")
    @Syntax("<msg>")
    @CommandCompletion("@players")
    @Description(" Fale ou vá para o canal global")
    public void toGlobal(Player player, @Optional String[] messages) {
        changeChannel(1, EterniaServer.configs.chGlobal, player, messages);
    }

    @Subcommand("staff")
    @CommandAlias("s|a|staff")
    @Syntax("<msg>")
    @CommandPermission("eternia.chat.staff")
    @Description(" Fale ou vá para o canal de staffs")
    public void toStaff(Player player, @Optional String[] messages) {
        changeChannel(2, EterniaServer.configs.chStaff, player, messages);
    }

    private void changeChannel(final int channel, final String channelName, final Player player, final String[] messages) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (messages != null && messages.length == 0) {
            APIChat.setChannel(uuid, channel);
            EterniaServer.msg.sendMessage(player, Messages.CHAT_CHANNEL_CHANGED, channelName);
        } else {
            int defaultChannel = APIChat.getChannel(uuid);
            APIChat.setChannel(uuid, channel);
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            APIChat.setChannel(uuid, defaultChannel);
        }
    }

}
