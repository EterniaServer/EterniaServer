package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.utils.ChatMessage;

import br.com.eterniaserver.eterniaserver.utils.TimeEnum;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnAsyncPlayerChat implements Listener, Constants {

    private final ChatFormatter cf;
    private final JsonSender js;
    private final CustomPlaceholdersFilter cp;
    private final Local local;
    private final Staff staff;
    private final Colors c = new Colors();
    private final boolean hexSupport;
    private final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");

    public OnAsyncPlayerChat() {
        this.cp = new CustomPlaceholdersFilter();
        this.js = new JsonSender();
        this.cf = new ChatFormatter();
        this.local = new Local();
        this.staff = new Staff();
        hexSupport = Bukkit.getBukkitVersion().contains("1.16");
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            final Player player = e.getPlayer();
            final String playerName = player.getName();
            if (Vars.chatMuted && !player.hasPermission("eternia.mute.bypass")) {
                player.sendMessage(Strings.M_CHATMUTED);
                e.setCancelled(true);
            } else {
                final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
                final long time = Vars.playerMuted.get(uuid);
                if (TimeEnum.HASCOOLDOWN.stayMuted(time)) {
                    player.sendMessage(Strings.M_CHAT_MUTED.replace(TIME, TimeEnum.HASCOOLDOWN.getTimeLeft(time)));
                    e.setCancelled(true);
                } else {
                    e.setCancelled(getChannel(e, player, e.getMessage(), playerName));
                }
            }
        }
    }

    private boolean getChannel(AsyncPlayerChatEvent e, Player player, String message, String playerName) {
        message = canHex(player, message);
        switch (Vars.global.getOrDefault(playerName, 0)) {
            case 0:
                local.sendChatMessage(message, player, EterniaServer.chatConfig.getInt("local.range"));
                return true;
            case 2:
                staff.sendChatMessage(message, player);
                return true;
            case 3:
                sendTell(player, message);
                return true;
            default:
                ChatMessage messagex = new ChatMessage(message);
                cf.filter(e, messagex);
                c.filter(e, messagex);
                cp.filter(e, messagex);
                js.filter(e, messagex);
                return false;
        }
    }

    private void sendTell(Player sender, final String msg) {
        final String playerName = sender.getName();
        if (Vars.chatLocked.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(Vars.chatLocked.get(playerName));
            if (target != null && target.isOnline()) {
                if (Vars.ignoredPlayer.get(playerName) != null && Vars.ignoredPlayer.get(playerName).contains(target)) {
                    sender.sendMessage(Strings.M_CHAT_IGNORE);
                    return;
                }
                InternMethods.sendPrivate(sender, target, msg);
                return;
            }
        }
        sender.sendMessage(Strings.M_CHAT_R_NO);
    }

    private String canHex(final Player player, String message) {
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
        return message;
    }

}