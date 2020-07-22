package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.ChatMessage;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnAsyncPlayerChat implements Listener {

    private final EterniaServer plugin;
    private final ChatFormatter cf;
    private final JsonSender js;
    private final CustomPlaceholdersFilter cp;
    private final EFiles messages;
    private final Local local;
    private final Staff staff;
    private final Colors c = new Colors();
    private final boolean hexSupport;
    private final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");

    public OnAsyncPlayerChat(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.cp = new CustomPlaceholdersFilter(plugin);
        this.js = new JsonSender(plugin);
        this.cf = new ChatFormatter(plugin);
        this.local = new Local(plugin);
        this.staff = new Staff(plugin);
        hexSupport = Bukkit.getBukkitVersion().contains("1.16");
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;

        final Player player = e.getPlayer();
        final String playerName = player.getName();
        String message = e.getMessage();

        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            if (plugin.isChatMuted() && !player.hasPermission("eternia.mute.bypass")) {
                messages.sendMessage("chat.chatmuted", player);
                e.setCancelled(true);
                return;
            }
            final long time = System.currentTimeMillis();
            if (Vars.playerMuted.get(playerName) - time > 0) {
                messages.sendMessage("chat.muted", "%time%", TimeUnit.MILLISECONDS.toSeconds(Vars.playerMuted.get(playerName) - time), player);
                e.setCancelled(true);
                return;
            }

            if (hexSupport && player.hasPermission("eternia.chat.color.hex")) {
                Matcher matcher = colorPattern.matcher(message);
                if (matcher.find()) {
                    StringBuffer buffer = new StringBuffer();
                    do {
                        matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
                    } while (matcher.find());
                    matcher.appendTail(buffer);
                    message = buffer.toString();
                }
            }

            switch (Vars.global.getOrDefault(playerName, 0)) {
                case 0:
                    local.sendChatMessage(message, player, EterniaServer.chatConfig.getInt("local.range"));
                    e.setCancelled(true);
                    break;
                case 2:
                    staff.sendChatMessage(message, player);
                    e.setCancelled(true);
                    break;
                default:
                    break;
            }

            if (e.isCancelled()) return;

            ChatMessage messagex = new ChatMessage(message);
            cf.filter(e, messagex);
            c.filter(e, messagex);
            cp.filter(e, messagex);
            js.filter(e, messagex);
        }
    }

}