package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.ChatMessage;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.TextMaker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class JsonSender {

	private final EterniaServer plugin;

	public JsonSender(EterniaServer plugin) {
		this.plugin = plugin;
	}

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		if(e.isCancelled()) return;
		if(!EterniaServer.uufi.containsKey(e.getPlayer().getName())) return;
		TextMaker tm = new TextMaker(message, e.getPlayer(), plugin);
		boolean relationalPlaceholders = plugin.chatConfig.getBoolean("chat.enableRelationalPlaceholders");
		if(!relationalPlaceholders) {
			tm.convertMessageToComponents();
		}
		if(relationalPlaceholders) {
			for (Player p : e.getRecipients()) {
				p.spigot().sendMessage(tm.getRelationalText(p));
			}
		} else {
			Bukkit.spigot().broadcast(tm.getText());
		}
		e.getRecipients().clear();
	}


}