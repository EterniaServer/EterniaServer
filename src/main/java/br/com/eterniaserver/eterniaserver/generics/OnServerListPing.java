package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnServerListPing implements Listener {

    private String message;
    private String message2;

    public OnServerListPing(EterniaServer plugin) {
        final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");
        final List<String> list =  plugin.msgConfig.getStringList("server.motd");
        Matcher matcher = colorPattern.matcher(ChatColor.translateAlternateColorCodes('&', list.get(0)));
        if (matcher.find()) {
            StringBuffer buffer = new StringBuffer();
            do {
                matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
            } while (matcher.find());
            matcher.appendTail(buffer);
            message = buffer.toString();
        }
        matcher = colorPattern.matcher(ChatColor.translateAlternateColorCodes('&', list.get(1)));
        if (matcher.find()) {
            StringBuffer buffer = new StringBuffer();
            do {
                matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
            } while (matcher.find());
            matcher.appendTail(buffer);
            message2 = buffer.toString();
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(message + "\n" + message2);
    }

}
