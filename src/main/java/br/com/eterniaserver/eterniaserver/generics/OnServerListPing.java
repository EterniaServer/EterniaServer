package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnServerListPing implements Listener {

    private final boolean isNull;
    private String message;
    private String message2;

    public OnServerListPing() {
        final List<String> list = EterniaServer.msgConfig.getStringList("server.motd");
        if (list.get(0) != null && list.get(1) != null) {
            this.isNull = false;

            message = ChatColor.translateAlternateColorCodes('&', list.get(0));
            message2 = ChatColor.translateAlternateColorCodes('&', list.get(1));

            if (Bukkit.getBukkitVersion().contains("1.16")) {
                final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");
                Matcher matcher = colorPattern.matcher(message);
                if (matcher.find()) {
                    StringBuffer buffer = new StringBuffer();
                    do {
                        matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
                    } while (matcher.find());
                    matcher.appendTail(buffer);
                    message = buffer.toString();
                }
                matcher = colorPattern.matcher(message2);
                if (matcher.find()) {
                    StringBuffer buffer = new StringBuffer();
                    do {
                        matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
                    } while (matcher.find());
                    matcher.appendTail(buffer);
                    message2 = buffer.toString();
                }
            }
        } else {
            this.isNull = true;
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (!isNull) event.setMotd(message + "\n" + message2);
    }

}
