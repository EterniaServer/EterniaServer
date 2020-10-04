package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.UUIDFetcher;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIPlayer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.APIChat;
import br.com.eterniaserver.eterniaserver.core.UtilGlobalFormat;
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
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerHandler implements Listener {

    private final UtilGlobalFormat utilGlobalFormat = new UtilGlobalFormat();
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
            final Player player = e.getPlayer();
            final String playerName = player.getName();
            if (APIServer.isChatMuted() && !player.hasPermission("eternia.mute.bypass")) {
                EterniaServer.msg.sendMessage(player, Messages.CHAT_CHANNELS_MUTED);
                e.setCancelled(true);
            } else {
                final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
                final long time = APIPlayer.getMutedTime(uuid);
                if (APIChat.stayMuted(time)) {
                    EterniaServer.msg.sendMessage(player, Messages.CHAT_ARE_MUTED, APIChat.getTimeLeft(time));
                    e.setCancelled(true);
                } else {
                    e.setCancelled(getChannel(e, player, e.getMessage(), uuid));
                }
            }
        }
    }

    private boolean getChannel(AsyncPlayerChatEvent e, Player player, String message, UUID uuid) {
        message = canHex(player, message);
        switch (APIPlayer.getChannel(uuid)) {
            case 0:
                APIChat.sendLocal(message, player, EterniaServer.chat.localRange);
                return true;
            case 2:
                APIChat.sendStaff(message, player);
                return true;
            case 3:
                sendTell(player, message);
                return true;
            default:
                e.getRecipients().clear();
                utilGlobalFormat.filter(player, message);
                return false;
        }
    }

    private void sendTell(Player sender, final String msg) {
        final String playerName = sender.getName();
        if (APIPlayer.isTell(playerName)) {
            final Player target = Bukkit.getPlayer(APIPlayer.getTellingPlayerName(playerName));
            if (target != null && target.isOnline()) {
                if (APIPlayer.hasIgnoreds(playerName) && APIPlayer.areIgnored(playerName, target)) {
                    EterniaServer.msg.sendMessage(sender, Messages.CHAT_ARE_IGNORED);
                    return;
                }
                APIChat.sendPrivate(sender, target, msg);
                return;
            }
        }
        EterniaServer.msg.sendMessage(sender, Messages.CHAT_NO_ONE_TO_RESP);
    }

    private String canHex(final Player player, String message) {
        if (APIServer.getVersion() >= 116 && player.hasPermission("eternia.chat.color.hex")) {
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
