package br.com.eterniaserver.modules.chatmanager.act.filter;

import br.com.eterniaserver.modules.chatmanager.act.utils.ChatMessage;
import br.com.eterniaserver.modules.chatmanager.act.utils.TextMaker;

import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class JsonSender {

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		TextMaker tm = new TextMaker(message, e.getPlayer());
		Bukkit.spigot().broadcast(tm.getText());
	}
	
}
