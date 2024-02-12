package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.reward.Entities.RewardGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Rewards {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        private final List<Map<String, Map<Double, List<String>>>> chanceMap;
        private final Map<String, String> rewardMap = new HashMap<>();

        protected Rewards(EterniaServer plugin) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
            this.chanceMap = plugin.chanceMaps();

            List<RewardGroup> rewardGroups = databaseInterface.listAll(RewardGroup.class);
            for (RewardGroup rewardGroup : rewardGroups) {
                rewardMap.put(rewardGroup.getKeyCode(), rewardGroup.getGroupName());
            }
        }

        protected boolean addReward(String key, String reward) {
            if (!chanceMap.get(ChanceMaps.REWARDS.ordinal()).containsKey(reward)) {
                return false;
            }

            rewardMap.put(key, reward);

            Runnable insertRunnable = () -> databaseInterface.insert(RewardGroup.class, new RewardGroup(key, reward));
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, insertRunnable);

            return true;
        }

        protected void removeReward(String key) {
            rewardMap.remove(key);

            Runnable getAndDelete = () -> {
                RewardGroup rewardGroup = databaseInterface.findBy(RewardGroup.class, "keyCode", key);
                if (rewardGroup != null) {
                    databaseInterface.delete(RewardGroup.class, rewardGroup);
                }
            };
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, getAndDelete);
        }

        protected String getReward(String key) {
            return rewardMap.get(key);
        }

        protected Map<Double, List<String>> getRewards(String group) {
            return chanceMap.get(ChanceMaps.REWARDS.ordinal()).get(group);
        }
    }

}
