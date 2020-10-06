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

@CommandAlias("%channel")
public class Channel extends BaseCommand {

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("%channel_syntax")
    @CommandPermission("%channel_perm")
    @Description("%channel_description")
    public void onChannels(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("%channel_local")
    @CommandAlias("%channel_local_aliases")
    @Syntax("%channel_local_syntax")
    @CommandPermission("%channel_local_perm")
    @Description("%channel_local_description")
    public void toLocal(Player player, @Optional String messages) {
        changeChannel(0, EterniaServer.configs.chLocal, player, messages);
    }

    @Subcommand("%channel_global")
    @CommandAlias("%channel_global_aliases")
    @Syntax("%channel_global_syntax")
    @CommandCompletion("@players")
    @CommandPermission("%channel_global_perm")
    @Description("%channel_global_description")
    public void toGlobal(Player player, @Optional String messages) {
        changeChannel(1, EterniaServer.configs.chGlobal, player, messages);
    }

    @Subcommand("%channel_staff")
    @CommandAlias("%channel_staff_aliases")
    @Syntax("%channel_staff_syntax")
    @CommandCompletion("@players")
    @CommandPermission("%channel_staff_perm")
    @Description("%channel_staff_description")
    public void toStaff(Player player, @Optional String messages) {
        changeChannel(2, EterniaServer.configs.chStaff, player, messages);
    }

    private void changeChannel(int channel, String channelName, Player player, String messages) {
        UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (messages == null || messages.equals("")) {
            APIChat.setChannel(uuid, channel);
            EterniaServer.msg.sendMessage(player, Messages.CHAT_CHANNEL_CHANGED, channelName);
        } else {
            int defaultChannel = APIChat.getChannel(uuid);
            APIChat.setChannel(uuid, channel);
            player.chat(messages);
            APIChat.setChannel(uuid, defaultChannel);
        }
    }

}
