package br.com.eterniaserver.eterniaserver.objects;

public class CustomPlaceholder {

    private final String permission;
    private final String value;
    private final String hoverText;
    private final String suggestCmd;
    private final int priority;
    private final boolean isStatic;

    public CustomPlaceholder(String permission, String value, String hoverText, String suggestCmd, int priority, boolean isStatic) {
        this.permission = permission;
        this.value = value;
        this.hoverText = hoverText;
        this.suggestCmd = suggestCmd;
        this.priority = priority;
        this.isStatic = isStatic;
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

    public boolean getIsStatic() {
        return isStatic;
    }
}
