package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils;

public class FormatInfo {

    private final int priority;
    private final String name;

    public FormatInfo(int p, String name) {
        this.priority = p;
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

}