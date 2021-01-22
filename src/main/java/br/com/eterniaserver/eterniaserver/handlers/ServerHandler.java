package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.objects.EntityControl;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.core.ChatFormatter;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
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

        if (ServerRelated.getVersion() >= 116) {
            messageMOTD = canHex(messageMOTD);
            message2 = canHex(message2);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreCreatureSpawn(PreCreatureSpawnEvent event) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_ENTITY) || !EterniaServer.getBoolean(Booleans.ENTITY_LIMITER)) {
            return;
        }

        int amount = 0;
        EntityType entityType = event.getType();
        EntityControl entityControl = EterniaServer.getControl(entityType);

        if (entityControl.getSpawnLimit() == -1) {
            return;
        }

        for (Entity e : event.getSpawnLocation().getChunk().getEntities()) {
            if (e.getType().ordinal() == entityType.ordinal()) {
                if (amount > entityControl.getSpawnLimit()) {
                    event.setCancelled(true);
                    break;
                }
                amount++;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_ENTITY) || !EterniaServer.getBoolean(Booleans.ENTITY_EDITOR)) {
            return;
        }

        LivingEntity entity = event.getEntity();
        EntityControl entityControl = EterniaServer.getControl(entity.getType());

        if (!entityControl.getEditorState()) {
            return;
        }

        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(entityControl.getHealth());
        entity.setHealth(entityControl.getHealth());
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(entityControl.getSpeed());
        entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(entityControl.getAttackDamage());
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(messageMOTD + "\n" + message2);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_CHAT) || e.isCancelled()) {
            return;
        }

        User user = new User(e.getPlayer());

        if (ServerRelated.isChatMuted() && !user.hasPermission(EterniaServer.getString(Strings.PERM_MUTE_BYPASS))) {
            e.setCancelled(true);
            return;
        }

        long time = user.getMuteTime();

        if (ServerRelated.isInFutureCooldown(time)) {
            user.sendMessage(Messages.CHAT_ARE_MUTED, ServerRelated.getTimeLeftOfCooldown(time));
            e.setCancelled(true);
            return;
        }

        e.setCancelled(getChannel(e, user, e.getMessage()));
    }

    private boolean getChannel(AsyncPlayerChatEvent e, User user, String message) {
        if (user.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_COLOR))) {
            message = canHex(message);
        }

        if (!user.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_BYPASS_PROTECTION))) {
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
        if (ServerRelated.getVersion() < 116) {
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
