package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.ChatMessage;

import org.bukkit.Bukkit;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public class JsonSender {

	private final EterniaServer plugin;

	public JsonSender(EterniaServer plugin) {
		this.plugin = plugin;
	}

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		if(e.isCancelled()) return;
		if(!Vars.uufi.containsKey(e.getPlayer().getName())) return;
		TextMaker tm = new TextMaker(message, e.getPlayer(), plugin);
		tm.convertMessageToComponents();
		Bukkit.spigot().broadcast(tm.getText());
		e.getRecipients().clear();
	}


}