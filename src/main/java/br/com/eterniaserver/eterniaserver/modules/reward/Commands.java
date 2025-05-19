package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Rewards extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.Rewards rewardsService;

        private final SecureRandom random = new SecureRandom();
        private final byte[] bytes = new byte[20];


        public Rewards(final EterniaServer plugin, Services.Rewards rewardsService) {
            this.plugin = plugin;
            this.rewardsService = rewardsService;
        }

        @CommandAlias("%USE_KEY")
        @Syntax("%USE_KEY_SYNTAX")
        @Description("%USE_KEY_DESCRIPTION")
        @CommandPermission("%USE_KEY_PERM")
        public void onUseKey(Player player, String key) {
            String reward = rewardsService.getReward(key);
            if (reward == null) {
                MessageOptions options = new MessageOptions(key);
                EterniaLib.getChatCommons().sendMessage(player, Messages.REWARD_INVALID_KEY, options);
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
            String key = Long.toHexString(random.nextLong());

            if (!rewardsService.addReward(key, reward)) {
                EterniaLib.getChatCommons().sendMessage(sender, Messages.REWARD_NOT_FOUND);
                return;
            }

            MessageOptions options = new MessageOptions(key);
            EterniaLib.getChatCommons().sendMessage(sender, Messages.REWARD_CREATED, options);
        }

        private void giveReward(String key, Player player) {
            for (Map.Entry<Double, List<String>> entry : rewardsService.getRewards(rewardsService.getReward(key)).entrySet()) {
                random.nextBytes(bytes);

                if (random.nextDouble() > entry.getKey()) {
                    continue;
                }

                for (String command : entry.getValue()) {
                    plugin.getServer().dispatchCommand(
                            Bukkit.getServer().getConsoleSender(),
                            plugin.setPlaceholders(player, command)
                    );
                }
            }

            rewardsService.removeReward(key);
        }
    }

}
