package br.com.eterniaserver.modules.chatmanager.act.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class ChatMessage {

	final List<ChatObject> chatObjects = new ArrayList<>();
	public String messageSent;
	
	public ChatMessage(String msg, Player p) {
		messageSent = msg;
		chatObjects.add(new ChatObject(msg));
	}

	public int size() {
		return chatObjects.size();
	}
	
	public ChatObject get(int i) {
		return chatObjects.get(i);
	}
	
	public List<ChatObject> getChatObjects() {
		return chatObjects;
	}
	
}
