package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @Getter
    static class WarpService {

        private final EterniaServer plugin;

        private final List<String> warpNames = new ArrayList<>();

        private Entities.WarpLocation spawnLocation = null;

        public WarpService(EterniaServer plugin) {
            this.plugin = plugin;
        }

        protected void updateSpawnLocation() {
            spawnLocation = EterniaLib.getDatabase().get(Entities.WarpLocation.class, "spawn");
        }

        public Optional<Location> getSpawnLocation() {
            if (spawnLocation == null || spawnLocation.getName() == null) {
                return Optional.empty();
            }

            return Optional.of(spawnLocation.getLocation(plugin));
        }

        public boolean teleportTo(Player player, String warpName) {
            Entities.WarpLocation warpLocation = EterniaLib.getDatabase().get(Entities.WarpLocation.class, warpName);
            if (warpLocation == null || warpLocation.getName() == null) {
                return false;
            }

            Utils.TeleportCommand.addTeleport(
                    plugin,
                    player,
                    warpLocation.getLocation(plugin),
                    warpName
            );
            return true;
        }

        public boolean deleteWarp(String warpName) {
            Entities.WarpLocation warpLocation = EterniaLib.getDatabase().get(Entities.WarpLocation.class, warpName);
            if (warpLocation == null || warpLocation.getName() == null) {
                return false;
            }

            warpNames.remove(warpName);
            EterniaLib.getDatabase().delete(Entities.WarpLocation.class, warpLocation.getName());
            return true;
        }

        public boolean createWarp(String warpName, Location location) {
            Entities.WarpLocation warpLocation = EterniaLib.getDatabase().get(Entities.WarpLocation.class, warpName);
            if (warpLocation == null) {
                warpLocation = new Entities.WarpLocation();
            }

            if (warpName.equals("spawn")) {
                spawnLocation = warpLocation;
            }

            if (warpLocation.getName() != null) {
                updateWarp(warpLocation, location, warpName);
                EterniaLib.getDatabase().update(Entities.WarpLocation.class, warpLocation);
                return false;
            }

            warpNames.add(warpName);
            updateWarp(warpLocation, location, warpName);
            EterniaLib.getDatabase().insert(Entities.WarpLocation.class, warpLocation);
            return true;
        }

        private void updateWarp(Entities.WarpLocation warpLocation, org.bukkit.Location location, String warpName) {
            warpLocation.setName(warpName);
            warpLocation.setCoordX(location.getX());
            warpLocation.setCoordY(location.getY());
            warpLocation.setCoordZ(location.getZ());
            warpLocation.setWorldName(location.getWorld().getName());
            warpLocation.setCoordYaw((double) location.getYaw());
            warpLocation.setCoordPitch((double) location.getPitch());
        }
    }

    static class HomeService {

        private final EterniaServer plugin;

        private final Map<UUID, List<Entities.HomeLocation>> homeMap = new HashMap<>();

        public HomeService(EterniaServer plugin) {
            this.plugin = plugin;
        }

        private List<Entities.HomeLocation> computeIfAbsent(UUID uuid) {
            return homeMap.computeIfAbsent(uuid, k -> new ArrayList<>());
        }

        public void setHomes(List<Entities.HomeLocation> homes) {
            for (Entities.HomeLocation home : homes) {
                List<Entities.HomeLocation> userHomes = computeIfAbsent(home.getUuid());
                userHomes.add(home);
            }
        }

        public List<String> getHomeNames(UUID uuid) {
            List<String> homeNames = new ArrayList<>();
            for (Entities.HomeLocation home : getHomes(uuid)) {
                homeNames.add(home.getName());
            }
            return homeNames;
        }

        public List<Entities.HomeLocation> getHomes(UUID uuid) {
            return homeMap.getOrDefault(uuid, new ArrayList<>());
        }

        public void updateHome(Entities.HomeLocation home) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> EterniaLib.getDatabase().update(Entities.HomeLocation.class, home));
        }

        public void addHome(Entities.HomeLocation home) {
            List<Entities.HomeLocation> userHomes = computeIfAbsent(home.getUuid());
            userHomes.add(home);

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> EterniaLib.getDatabase().insert(Entities.HomeLocation.class, home));
        }

        public void removeHome(Entities.HomeLocation home) {
            List<Entities.HomeLocation> userHomes = computeIfAbsent(home.getUuid());
            userHomes.remove(home);

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> EterniaLib.getDatabase().delete(Entities.HomeLocation.class, home.getId()));
        }
    }

}
