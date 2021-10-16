package br.com.eterniaserver.eterniaserver.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AfkStatusEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    private final boolean afkStatus;
    private final Cause cause;
    private boolean isCancelled;

    public AfkStatusEvent(Player player, boolean afkStatus, Cause cause) {
        this.player = player;
        this.afkStatus = afkStatus;
        this.cause = cause;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Cause getCause() {
        return cause;
    }

    public boolean getStatus() {
        return afkStatus;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public enum Cause {
        ACTIVITY,
        MOVE,
        INTERACT,
        COMMAND,
        JOIN,
        CHAT,
        QUIT,
        KICK
    }

}
