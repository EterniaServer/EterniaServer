package br.com.eterniaserver.eterniaserver.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BreakRewardEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final List<String> rewards;
    private boolean isCancelled;

    public BreakRewardEvent(Player player, List<String> rewards) {
        this.player = player;
        this.rewards = rewards;
        this.isCancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public List<String> getRewards() {
        return rewards;
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
