package br.com.eterniaserver.modules.chatmanager.act.filter;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.chatmanager.act.utils.ChatMessage;
import br.com.eterniaserver.modules.chatmanager.act.utils.TextMaker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class JsonSender {

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		if(e.isCancelled()) return;
		if(!Vars.uufi.containsKey(e.getPlayer().getUniqueId())) return;
		TextMaker tm = new TextMaker(message, e.getPlayer());
		boolean relationalPlaceholders = EterniaServer.chat.getBoolean("chat.enableRelationalPlaceholders");
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