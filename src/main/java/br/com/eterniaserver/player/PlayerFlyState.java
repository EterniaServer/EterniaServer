package br.com.eterniaserver.player;

import br.com.eterniaserver.configs.methods.PlayerMessage;

import org.bukkit.entity.Player;

public class PlayerFlyState {

    public PlayerFlyState(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            new PlayerMessage("fly.disable", player);
        } else {
            player.setAllowFlight(true);
            new PlayerMessage("fly.enable", player);
        }
    }

}