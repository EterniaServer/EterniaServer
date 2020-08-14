package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import org.bukkit.entity.Player;

@CommandAlias("ch|channels")
@CommandPermission("eternia.chat.channels")
public class Channels extends BaseCommand {

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
        if (messages != null && messages.length == 0) {
            Vars.global.put(player.getName(), channel);
            player.sendMessage(Strings.M_CHAT_C.replace(Constants.CHANNEL_NAME, channelName));
        } else {
            int o = Vars.global.get(player.getName());
            Vars.global.put(player.getName(), channel);
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            Vars.global.put(player.getName(), o);
        }
    }

}
