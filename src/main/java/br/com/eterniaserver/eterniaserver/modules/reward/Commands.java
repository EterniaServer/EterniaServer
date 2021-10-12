package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

final class Commands {

    static class Rewards extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.Rewards rewardsService;

        private final SecureRandom random = new SecureRandom();
        private final byte[] bytes = new byte[20];


        public Rewards(final EterniaServer plugin) {
            this.plugin = plugin;
            this.rewardsService = new Services.Rewards(plugin);
        }

        @CommandAlias("%USE_KEY")
        @Syntax("%USE_KEY_SYNTAX")
        @Description("%USE_KEY_DESCRIPTION")
        @CommandPermission("%USE_KEY_PERM")
        public void onUseKey(Player player, String key) {
            final String reward = rewardsService.getReward(key);
            if (reward == null) {
                plugin.sendMiniMessages(player, Messages.REWARD_INVALID_KEY, key);
                return;
            }

            giveReward(rewardsService.getReward(key), player);
        }

        @CommandAlias("%GEN_KEY")
        @Syntax("%GEN_KEY_SYNTAX")
        @Description("%GEN_KEY_DESCRIPTION")
        @CommandPermission("%GEN_KEY_PERM")
        public void onGenKey(CommandSender sender, String reward) {
            random.nextBytes(bytes);
            final String key = Long.toHexString(random.nextLong());

            if (!rewardsService.addReward(key, reward)) {
                plugin.sendMiniMessages(sender, Messages.REWARD_NOT_FOUND);
                return;
            }

            plugin.sendMiniMessages(sender, Messages.REWARD_CREATED, key);
        }

        private void giveReward(String key, Player player) {
            for (Map.Entry<Double, List<String>> entry : rewardsService.getRewards(rewardsService.getReward(key)).entrySet()) {
                random.nextBytes(bytes);

                if (random.nextDouble() > entry.getKey()) continue;

                for (String command : entry.getValue()) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), plugin.setPlaceholders(player, command));
                }
            }

            rewardsService.removeReward(key);
        }
    }

}
