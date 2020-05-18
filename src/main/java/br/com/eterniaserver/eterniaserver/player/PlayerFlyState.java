package br.com.eterniaserver.eterniaserver.player;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.entity.Player;

public class PlayerFlyState {

    private final Messages messages;

    public PlayerFlyState(Messages messages) {
        this.messages = messages;
    }

    public void changeFlyState(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            messages.sendMessage("fly.disable", player);
        } else {
            player.setAllowFlight(true);
            messages.sendMessage("fly.enable", player);
        }
    }

}