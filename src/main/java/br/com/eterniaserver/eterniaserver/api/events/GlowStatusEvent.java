package br.com.eterniaserver.eterniaserver.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import org.jetbrains.annotations.NotNull;


public class GlowStatusEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final String color;

    private boolean glowing;
    private boolean isCancelled;

    public GlowStatusEvent(Player player, String color, boolean glowing) {
        this.player = player;
        this.color = color;
        this.glowing = glowing;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public String getColor() {
        return color;
    }

    public void setGlowingStatus(boolean glowing) {
        this.glowing = glowing;
    }

    public boolean getGlowingStatus() {
        return glowing;
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

}
