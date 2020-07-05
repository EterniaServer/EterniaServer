package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnServerListPing implements Listener {

    private String message;

    public OnServerListPing(EterniaServer plugin) {
        final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");
        Matcher matcher = colorPattern.matcher(plugin.msgConfig.getString("server.motd"));
        if (matcher.find()) {
            StringBuffer buffer = new StringBuffer();
            do {
                matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
            } while (matcher.find());
            matcher.appendTail(buffer);
            message = buffer.toString();
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(message);
    }

}
