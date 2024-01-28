package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import java.util.*;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class HomeService {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        private final Map<UUID, List<Entities.HomeLocation>> homeMap = new HashMap<>();

        public HomeService(EterniaServer plugin) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
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
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> databaseInterface.update(Entities.HomeLocation.class, home));
        }

        public void addHome(Entities.HomeLocation home) {
            List<Entities.HomeLocation> userHomes = computeIfAbsent(home.getUuid());
            userHomes.add(home);

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> databaseInterface.insert(Entities.HomeLocation.class, home));
        }

        public void removeHome(Entities.HomeLocation home) {
            List<Entities.HomeLocation> userHomes = computeIfAbsent(home.getUuid());
            userHomes.remove(home);

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> databaseInterface.delete(Entities.HomeLocation.class, home.getId()));
        }
    }

}
