package br.com.eterniaserver.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import org.bukkit.entity.Player;

@CommandAlias("ch|channels")
@CommandPermission("eternia.chat.channels")
public class Channels extends BaseCommand {

    private final Vars vars;
    private final Messages messages;

    public Channels(Vars vars, Messages messages) {
        this.vars = vars;
        this.messages = messages;
    }

    @Subcommand("local")
    @CommandAlias("l|local")
    public void toLocal(Player player, @Optional String[] messages) {
        if (messages.length == 0) {
            vars.global.put(player.getName(), 0);
            this.messages.sendMessage("chat.channelc", "%channel_name%", "Local", player);
        } else {
            int o = vars.global.get(player.getName());
            vars.global.put(player.getName(), 0);
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            vars.global.put(player.getName(), o);
        }
    }

    @Subcommand("global")
    @CommandAlias("g|global")
    public void toGlobal(Player player, @Optional String[] messages) {
        if (messages.length == 0) {
            vars.global.put(player.getName(), 1);
            this.messages.sendMessage("chat.channelc", "%channel_name%", "Global", player);
        } else {
            int o = vars.global.get(player.getName());
            vars.global.put(player.getName(), 1);
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            vars.global.put(player.getName(), o);
        }
    }

    @Subcommand("staff")
    @CommandAlias("s|a|staff")
    @CommandPermission("eternia.chat.staff")
    public void toStaff(Player player, @Optional String[] messages) {
        if (messages.length == 0) {
            vars.global.put(player.getName(), 2);
            this.messages.sendMessage("chat.channelc", "%channel_name%", "Staff", player);
        } else {
            int o = vars.global.get(player.getName());
            vars.global.put(player.getName(), 2);
            StringBuilder sb = new StringBuilder();
            for (String arg : messages) sb.append(arg).append(" ");
            player.chat(sb.substring(0, sb.length() - 1));
            vars.global.put(player.getName(), o);
        }
    }

}
