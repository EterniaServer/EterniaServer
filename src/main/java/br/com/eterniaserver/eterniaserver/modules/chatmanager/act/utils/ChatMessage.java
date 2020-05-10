package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils;

import java.util.ArrayList;
import java.util.List;

public class ChatMessage {

	final List<ChatObject> chatObjects = new ArrayList<>();
	public String messageSent;
	
	public ChatMessage(String msg) {
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
