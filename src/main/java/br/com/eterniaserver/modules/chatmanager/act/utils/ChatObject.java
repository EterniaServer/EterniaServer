package br.com.eterniaserver.modules.chatmanager.act.utils;

import org.bukkit.ChatColor;


public class ChatObject {

	String message;
	String hover;
	String suggest;

	String run;
	ChatColor color;
	boolean isText;

	public ChatObject(String message) {
		this.message = message;
		this.isText = true;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public ChatColor getColor() {
		return color;
	}

	public ChatObject(String message, String hover, String suggest, String run) {
		this.message = message;
		this.hover = hover;
		this.suggest = suggest;
		this.run = run;
		this.isText = false;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHover() {
		return hover;
	}

	public void setIsText(boolean isText) {
		this.isText = isText;
	}

	public String getSuggest() {
		return suggest;
	}

	public String getRun() {
		return run;
	}

	public boolean isText() {
		return this.isText;
	}

}
