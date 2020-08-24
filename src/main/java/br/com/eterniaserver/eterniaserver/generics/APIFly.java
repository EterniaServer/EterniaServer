package br.com.eterniaserver.eterniaserver.generics;

import org.bukkit.entity.Player;

import java.util.UUID;

public class APIFly {

    private APIFly() {
        throw new IllegalStateException("Utility class");
    }

    public static void changeFlyState(Player player) {
        if (player.getAllowFlight() && player.isFlying()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            return;
        }

        player.setAllowFlight(true);
        player.setFlying(true);
    }

    public static boolean isOnPvP(UUID uuid) {
        return Vars.playerProfile.get(uuid).isOnPvP();
    }

    public static int getPvPCooldown(UUID uuid) {
        return Vars.playerProfile.get(uuid).getOnPvP();
    }

    public static void setIsOnPvP(UUID uuid) {
        Vars.playerProfile.get(uuid).setIsOnPvP();
    }

}
