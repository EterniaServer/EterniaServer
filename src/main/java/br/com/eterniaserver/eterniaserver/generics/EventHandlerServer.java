package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.objects.ChatMessage;
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

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventHandlerServer implements Listener {

    public static boolean version116;
    private final UtilChatFormatter cf;
    private final UtilJsonSender js;
    private final UtilCustomPlaceholdersFilter cp;
    private final UtilColors c = new UtilColors();
    private final Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");
    private final boolean isNull;
    private String message;
    private String message2;

    public EventHandlerServer() {

        this.cp = new UtilCustomPlaceholdersFilter();
        this.js = new UtilJsonSender();
        this.cf = new UtilChatFormatter();
        version116 = Bukkit.getBukkitVersion().contains("1.16");

        final List<String> list = EterniaServer.msgConfig.getStringList("server.motd");
        if (list.get(0) != null && list.get(1) != null) {
            this.isNull = false;
            message = ChatColor.translateAlternateColorCodes('&', list.get(0));
            message2 = ChatColor.translateAlternateColorCodes('&', list.get(1));
            if (Bukkit.getBukkitVersion().contains("1.16")) {
                message = matcherMessage(message);
                message2 = matcherMessage(message2);
            }
        } else {
            this.isNull = true;
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (EterniaServer.serverConfig.getBoolean("modules.clear")) {
            int amount = 0;
            EntityType entity = event.getEntityType();
            for (Entity e : event.getEntity().getLocation().getChunk().getEntities()) {
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
        if (!isNull) event.setMotd(message + "\n" + message2);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            final Player player = e.getPlayer();
            final String playerName = player.getName();
            if (PluginVars.chatMuted && !player.hasPermission("eternia.mute.bypass")) {
                player.sendMessage(PluginMSGs.M_CHATMUTED);
                e.setCancelled(true);
            } else {
                final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
                final long time = PluginVars.playerProfile.get(uuid).muted;
                if (UtilInternMethods.stayMuted(time)) {
                    player.sendMessage(PluginMSGs.M_CHAT_MUTED.replace(PluginConstants.TIME, UtilInternMethods.getTimeLeft(time)));
                    e.setCancelled(true);
                } else {
                    e.setCancelled(getChannel(e, player, e.getMessage(), uuid));
                }
            }
        }
    }

    private boolean getChannel(AsyncPlayerChatEvent e, Player player, String message, UUID uuid) {
        message = canHex(player, message);
        switch (PluginVars.playerProfile.get(uuid).chatChannel) {
            case 0:
                UtilInternMethods.sendLocal(message, player, EterniaServer.chatConfig.getInt("local.range"));
                return true;
            case 2:
                UtilInternMethods.sendStaff(message, player);
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
        if (PluginVars.chatLocked.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(PluginVars.chatLocked.get(playerName));
            if (target != null && target.isOnline()) {
                if (PluginVars.ignoredPlayer.get(playerName) != null && PluginVars.ignoredPlayer.get(playerName).contains(target)) {
                    sender.sendMessage(PluginMSGs.M_CHAT_IGNORE);
                    return;
                }
                UtilInternMethods.sendPrivate(sender, target, msg);
                return;
            }
        }
        sender.sendMessage(PluginMSGs.M_CHAT_R_NO);
    }

    private String canHex(final Player player, String message) {
        if (version116 && player.hasPermission("eternia.chat.color.hex")) {
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
