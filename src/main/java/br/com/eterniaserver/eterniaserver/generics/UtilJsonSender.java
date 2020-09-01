package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.objects.ChatMessage;

import org.bukkit.Bukkit;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public class UtilJsonSender {

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		if(e.isCancelled()) return;
		if(!Vars.uufi.containsKey(e.getPlayer().getName())) return;
		UtilTextMaker tm = new UtilTextMaker(message, e.getPlayer());
		tm.convertMessageToComponents();
		Bukkit.spigot().broadcast(tm.getText());
		e.getRecipients().clear();
	}


}