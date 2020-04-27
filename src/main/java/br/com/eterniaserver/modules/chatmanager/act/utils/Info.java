package br.com.eterniaserver.modules.chatmanager.act.utils;

public class Info {

	final int p;
	final String name;

	public Info(int priority, String name) {
		this.p = priority;
		this.name = name;
	}

	public int getPri() {
		return p;
	}

	public String getName() {
		return StringHelper.cc(name);
	}
	
}