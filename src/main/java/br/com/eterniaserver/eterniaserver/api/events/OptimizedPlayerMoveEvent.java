package br.com.eterniaserver.eterniaserver.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import org.jetbrains.annotations.NotNull;

public class OptimizedPlayerMoveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Location from;
    private final Location to;

    private final Player player;

    public OptimizedPlayerMoveEvent(Player player, Location from, Location to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean hasChangedBlock() {
        return from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
