package br.com.eterniaserver.eterniaserver.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;

public class ServerRelated {
    
    private static long nightMessageTime = System.currentTimeMillis();
    private static final List<World> skippingWorlds = new ArrayList<>();
    private static final Map<UUID, Long> bedCooldown = new HashMap<>();

    private ServerRelated() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Update the cooldown of Night Message Time
     */
    public static void updateNightMessageTime() {
        nightMessageTime = System.currentTimeMillis();
    }

    /**
     * Get the Night Message Time cooldown
     * @return the time
     */
    public static long getNightMessageTime() {
        return nightMessageTime;
    }

    /**
     * Start to skipping world
     * @param world
     */
    public static void putInSkipping(World world) {
        skippingWorlds.add(world);
    }

    /**
     * Stop the world skipping
     * @param world
     */
    public static void removeFromSkipping(World world) {
        skippingWorlds.remove(world);
    }

    /**
     * Checks if the world are skipping
     * @param world
     * @return if is
     */
    public static boolean isSkipping(World world) {
        return skippingWorlds.contains(world);
    }

    /**
     * Get the Bed Cooldown the user by searching from uuid
     * @param uuid of user
     * @return the time
     */
    public static long getBedCooldown(UUID uuid) {
        return bedCooldown.getOrDefault(uuid, 0L);
    }

    /**
     * Update the Bed Cooldown of user
     * @param uuid of user
     */
    public static void updateBedCooldown(UUID uuid) {
        bedCooldown.put(uuid, System.currentTimeMillis());
    }

}
