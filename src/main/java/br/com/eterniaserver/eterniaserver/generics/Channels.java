package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import org.bukkit.entity.Player;

@CommandAlias("ch|channels")
@CommandPermission("eternia.chat.channels")
public class Channels extends BaseCommand {

    private final EFiles messages;

    public Channels(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
    }

    @Subcommand("local")
    @CommandAlias("l|local")
    public void toLocal(Player player, @Optional String[] messages) {
        if (messages.length == 0) {
            Vars.global.put(player.getName(), 0);
            this.messages.sendMessage("chat.channelc", "%channel_name%", "Local", player);
        } else {
            int o = Vars.global.get(player.getName());
            Vars.global.put(player.getName(), 0);
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            Vars.global.put(player.getName(), o);
        }
    }

    @Subcommand("global")
    @CommandAlias("g|global")
    public void toGlobal(Player player, @Optional String[] messages) {
        if (messages.length == 0) {
            Vars.global.put(player.getName(), 1);
            this.messages.sendMessage("chat.channelc", "%channel_name%", "Global", player);
        } else {
            int o = Vars.global.get(player.getName());
            Vars.global.put(player.getName(), 1);
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            Vars.global.put(player.getName(), o);
        }
    }

    @Subcommand("staff")
    @CommandAlias("s|a|staff")
    @CommandPermission("eternia.chat.staff")
    public void toStaff(Player player, @Optional String[] messages) {
        if (messages.length == 0) {
            Vars.global.put(player.getName(), 2);
            this.messages.sendMessage("chat.channelc", "%channel_name%", "Staff", player);
        } else {
            int o = Vars.global.get(player.getName());
            Vars.global.put(player.getName(), 2);
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            Vars.global.put(player.getName(), o);
        }
    }

}
