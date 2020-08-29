package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.strings.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("ch|channels")
@CommandPermission("eternia.chat.channels")
public class BaseCmdChannels extends BaseCommand {

    @Subcommand("local")
    @CommandAlias("l|local")
    public void toLocal(Player player, @Optional String[] messages) {
        changeChannel(0, "Local", player, messages);
    }

    @Subcommand("global")
    @CommandAlias("g|global")
    public void toGlobal(Player player, @Optional String[] messages) {
        changeChannel(1, "Global", player, messages);
    }

    @Subcommand("staff")
    @CommandAlias("s|a|staff")
    @CommandPermission("eternia.chat.staff")
    public void toStaff(Player player, @Optional String[] messages) {
        changeChannel(2, "Staff", player, messages);
    }

    private void changeChannel(final int channel, final String channelName, final Player player, final String[] messages) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (messages != null && messages.length == 0) {
            Vars.playerProfile.get(uuid).chatChannel = channel;
            player.sendMessage(Strings.M_CHAT_C.replace(Constants.CHANNEL_NAME, channelName));
        } else {
            int o = Vars.playerProfile.get(uuid).chatChannel;
            Vars.playerProfile.get(uuid).chatChannel = channel;
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            Vars.playerProfile.get(uuid).chatChannel = o;
        }
    }

}
