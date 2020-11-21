package br.com.eterniaserver.eterniaserver.objects;

public class CustomPlaceholder {

    private final String permission;
    private final String value;
    private final String hoverText;
    private final String suggestCmd;
    private final int priority;

    public CustomPlaceholder(String permission, String value, String hoverText, String suggestCmd, int priority) {
        this.permission = permission;
        this.value = value;
        this.hoverText = hoverText;
        this.suggestCmd = suggestCmd;
        this.priority = priority;
    }

    public String getValue() {
        return value;
    }

    public String getPermission() {
        return permission;
    }

    public String getHoverText() {
        return hoverText;
    }

    public String getSuggestCmd() {
        return suggestCmd;
    }

    public int getPriority() {
        return priority;
    }
}
