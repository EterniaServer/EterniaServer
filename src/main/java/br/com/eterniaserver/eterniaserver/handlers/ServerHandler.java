package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.core.ChatFormatter;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerHandler implements Listener {

    private final ChatFormatter chatFormatter = new ChatFormatter();
    private static final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");

    private String messageMOTD;
    private String message2;

    public ServerHandler() {

        messageMOTD = ChatColor.translateAlternateColorCodes('&', EterniaServer.getMessage(Messages.SERVER_MOTD_1, false));
        message2 = ChatColor.translateAlternateColorCodes('&', EterniaServer.getMessage(Messages.SERVER_MOTD_2, false));

        if (APIServer.getVersion() >= 116) {
            messageMOTD = canHex(messageMOTD);
            message2 = canHex(message2);
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(PreCreatureSpawnEvent event) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_CLEAR)) {
            return;
        }

        int amount = 0;
        EntityType entityType = event.getType();
        for (Entity e : event.getSpawnLocation().getChunk().getEntities()) {
            if (!(e instanceof Creature)) {
                continue;
            }

            if (!e.getType().equals(entityType)) {
                continue;
            }

            if (amount > 15) {
                event.setCancelled(true);
                return;
            }
            amount++;
        }
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(messageMOTD + "\n" + message2);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_CHAT)) {
            return;
        }

        User user = new User(e.getPlayer());

        if (APIServer.isChatMuted() && !user.hasPermission(EterniaServer.getString(Strings.PERM_MUTE_BYPASS))) {
            e.setCancelled(true);
            return;
        }

        long time = user.getMuteTime();

        if (APIServer.isInFutureCooldown(time)) {
            user.sendMessage(Messages.CHAT_ARE_MUTED, APIServer.getTimeLeftOfCooldown(time));
            e.setCancelled(true);
            return;
        }

        e.setCancelled(getChannel(e, user, e.getMessage()));
    }

    private boolean getChannel(AsyncPlayerChatEvent e, User user, String message) {
        if (user.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_COLOR))) {
            message = canHex(message);
        }

        if (user.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_BYPASS_PROTECTION))) {
            message = EterniaServer.getFilter().matcher(message).replaceAll("");
        }

        Set<Player> players = e.getRecipients();

        if (user.getChannel() == EterniaServer.getString(Strings.DISCORD_SRV).hashCode()) {
            chatFormatter.filter(user, message, EterniaServer.channelObject(user.getChannel()), players);
            return false;
        }

        if (user.getChannel() == "tell".hashCode()) {
            sendTell(user, message);
            return true;
        }

        chatFormatter.filter(user, message, EterniaServer.channelObject(user.getChannel()), players);
        return true;
    }

    private void sendTell(User user, String msg) {
        if (!user.isTell() || user.getTellingPlayerName() == null) {
            user.sendMessage(Messages.CHAT_NO_ONE_TO_RESP);
            return;
        }

        Player target = Bukkit.getPlayer(user.getTellingPlayerName());

        if (target == null || !target.isOnline()) {
            user.sendMessage(Messages.CHAT_NO_ONE_TO_RESP);
            return;
        }

        user.sendPrivate(target, msg);
    }

    private String canHex(String message) {
        if (APIServer.getVersion() < 116) {
            return message;
        }

        Matcher matcher = colorPattern.matcher(message);

        if (!matcher.find()) {
            return message;
        }

        StringBuffer buffer = new StringBuffer();

        do {
            matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
        } while (matcher.find());
        matcher.appendTail(buffer);

        return buffer.toString();
    }

}
