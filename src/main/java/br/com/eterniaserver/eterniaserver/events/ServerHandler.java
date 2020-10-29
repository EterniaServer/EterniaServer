package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.core.GlobalFormatter;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerHandler implements Listener {

    private final GlobalFormatter utilGlobalFormat = new GlobalFormatter();
    private final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");
    private String messageMotd;
    private String message2;

    public ServerHandler() {

        messageMotd = ChatColor.translateAlternateColorCodes('&', EterniaServer.msg.getMessage(Messages.SERVER_MOTD_1, false));
        message2 = ChatColor.translateAlternateColorCodes('&', EterniaServer.msg.getMessage(Messages.SERVER_MOTD_2, false));
        if (APIServer.getVersion() >= 116) {
            messageMotd = matcherMessage(messageMotd);
            message2 = matcherMessage(message2);
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(PreCreatureSpawnEvent event) {
        if (EterniaServer.configs.moduleClear) {
            int amount = 0;
            EntityType entity = event.getType();
            for (Entity e : event.getSpawnLocation().getChunk().getEntities()) {
                if (e instanceof Item) return;
                if (!e.getType().equals(entity)) return;
                if (amount > 15) {
                    event.setCancelled(true);
                    return;
                }
                amount++;
            }
        }
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(messageMotd + "\n" + message2);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (EterniaServer.configs.moduleChat) {
            User user = new User(e.getPlayer());

            if (APIServer.isChatMuted() && !user.hasPermission(EterniaServer.constants.permMuteBypass)) {
                user.sendMessage(Messages.CHAT_CHANNELS_MUTED);
                e.setCancelled(true);
            } else {
                long time = user.getMuteTime();
                if (APIServer.isInFutureCooldown(time)) {
                    user.sendMessage(Messages.CHAT_ARE_MUTED, APIServer.getTimeLeftOfCooldown(time));
                    e.setCancelled(true);
                } else {
                    e.setCancelled(getChannel(e, user, e.getMessage()));
                }
            }
        }
    }

    private boolean getChannel(AsyncPlayerChatEvent e, User user, String message) {
        message = canHex(user, message);
        switch (user.getChannel()) {
            case 0:
                user.sendLocalMessage(message, EterniaServer.chat.localRange);
                return true;
            case 2:
                user.sendStaffMessage(message);
                return true;
            case 3:
                sendTell(user, message);
                return true;
            default:
                e.getRecipients().clear();
                utilGlobalFormat.filter(user, message);
                return false;
        }
    }

    private void sendTell(User user, String msg) {
        if (user.isTell()) {
            final Player target = Bukkit.getPlayer(user.getTellingPlayerName());
            if (target != null && target.isOnline()) {
                if (APIServer.hasIgnores(user.getUUID()) && APIServer.areIgnored(user.getUUID(), target)) {
                    user.sendMessage(Messages.CHAT_ARE_IGNORED);
                    return;
                }
                user.sendPrivate(target, msg);
                return;
            }
        }
        user.sendMessage(Messages.CHAT_NO_ONE_TO_RESP);
    }

    private String canHex(User user, String message) {
        if (APIServer.getVersion() >= 116 && user.hasPermission(EterniaServer.constants.permChatColor)) {
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

    private String matcherMessage(String msg) {
        Matcher matcher = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))").matcher(msg);
        if (matcher.find()) {
            StringBuffer buffer = new StringBuffer();
            do {
                matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
            } while (matcher.find());
            matcher.appendTail(buffer);
            msg = buffer.toString();
        }
        return msg;
    }

}
