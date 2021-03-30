package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eterniaserver.EterniaServer;
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

public class ServerHandler implements Listener {

    private final EterniaServer plugin;

    private final ChatFormatter chatFormatter;
    private static final int tellHashCode = "tell".hashCode();

    private String messageMOTD;
    private String message2;

    public ServerHandler(final EterniaServer plugin) {
        this.plugin = plugin;
        this.chatFormatter= new ChatFormatter(plugin);
        messageMOTD = ChatColor.translateAlternateColorCodes('&', plugin.getMessage(Messages.SERVER_MOTD_1, false));
        message2 = ChatColor.translateAlternateColorCodes('&', plugin.getMessage(Messages.SERVER_MOTD_2, false));
        if (plugin.getVersion() >= 116) {
            messageMOTD = plugin.translateHex(messageMOTD);
            message2 = plugin.translateHex(message2);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreCreatureSpawn(PreCreatureSpawnEvent event) {
        if (!plugin.getBoolean(Booleans.MODULE_ENTITY) || !plugin.getBoolean(Booleans.ENTITY_LIMITER)) {
            return;
        }

        int amount = 0;
        EntityType entityType = event.getType();
        EntityControl entityControl = plugin.getControl(entityType);

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
        if (!plugin.getBoolean(Booleans.MODULE_ENTITY) || !plugin.getBoolean(Booleans.ENTITY_EDITOR)) {
            return;
        }

        LivingEntity entity = event.getEntity();
        EntityControl entityControl = plugin.getControl(entity.getType());

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
        if (!plugin.getBoolean(Booleans.MODULE_CHAT) || e.isCancelled()) {
            return;
        }

        User user = new User(e.getPlayer());

        if (plugin.isChatMuted() && !user.hasPermission(plugin.getString(Strings.PERM_MUTE_BYPASS))) {
            e.setCancelled(true);
            return;
        }

        long time = user.getMuteTime();

        if (plugin.isInFutureCooldown(time)) {
            plugin.sendMessage(user.getPlayer(), Messages.CHAT_ARE_MUTED, plugin.getTimeLeftOfCooldown(time));
            e.setCancelled(true);
            return;
        }

        e.setCancelled(getChannel(e, user));
    }

    private boolean getChannel(AsyncPlayerChatEvent e, User user) {
        String message = e.getMessage();
        if (!user.hasPermission(plugin.getString(Strings.PERM_CHAT_BYPASS_PROTECTION))) {
            message = plugin.getFilter().matcher(message).replaceAll("");
        }

        Set<Player> players = e.getRecipients();

        if (user.getChannel() == plugin.getString(Strings.DISCORD_SRV).hashCode()) {
            chatFormatter.filter(user, message, plugin.channelObject(user.getChannel()), players);
            return false;
        }

        if (user.getChannel() == tellHashCode) {
            sendTell(user, message);
            return true;
        }

        chatFormatter.filter(user, message, plugin.channelObject(user.getChannel()), players);
        return true;
    }

    private void sendTell(User user, String msg) {
        if (!user.isTell() || user.getTellingPlayerName() == null) {
            plugin.sendMessage(user.getPlayer(), Messages.CHAT_NO_ONE_TO_RESP);
            return;
        }

        Player target = Bukkit.getPlayer(user.getTellingPlayerName());

        if (target == null || !target.isOnline()) {
            plugin.sendMessage(user.getPlayer(), Messages.CHAT_NO_ONE_TO_RESP);
            return;
        }

        user.sendPrivate(target, msg);
    }

}
