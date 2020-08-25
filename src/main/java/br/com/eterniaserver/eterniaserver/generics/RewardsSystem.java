package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.Map;

@SuppressWarnings("squid:S2245")
public class RewardsSystem extends BaseCommand {

    private final SecureRandom random = new SecureRandom();
    private final byte[] bytes = new byte[20];

    public RewardsSystem() {
        Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.TABLE_REWARD), Constants.CODE_STR, Constants.CODE_GROUP_STR);
        temp.forEach(Vars.rewards::put);
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Keys").replace(Constants.AMOUNT, String.valueOf(temp.size())));
    }

    @CommandAlias("usekey|usarkey|usarchave")
    @Syntax("<chave>")
    @CommandPermission("eternia.usekey")
    public void onUseKey(Player player, String key) {
        if (Vars.rewards.containsKey(key)) {
            giveReward(Vars.rewards.get(key), player);
            deleteKey(key);
        } else {
            player.sendMessage(Strings.MSG_REWARD_INVALID);
        }
    }

    @CommandAlias("genkey|criarkey|criarchave")
    @Syntax("<reward>")
    @CommandPermission("eternia.genkey")
    public void onGenKey(CommandSender sender, String reward) {
        if (EterniaServer.rewardsConfig.getConfigurationSection("rewards." + reward) != null) {
            random.nextBytes(bytes);
            final String key = Long.toHexString(random.nextLong());
            createKey(reward, key);
            sender.sendMessage(Strings.MSG_REWARD_CREATE.replace(Constants.KEY, key));
        } else {
            sender.sendMessage(Strings.MSG_REWARD_NO.replace(Constants.GROUP, reward));
        }
    }

    private void createKey(final String grupo, String key) {
        EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_REWARD, Constants.CODE_STR, key, Constants.CODE_GROUP_STR, grupo));
    }

    private void deleteKey(final String key) {
        EQueries.executeQuery(Constants.getQueryDelete(Configs.TABLE_REWARD, Constants.CODE_STR, key));
    }

    private void giveReward(String group, Player player) {
        final String rewardsConfig = "rewards.";
        for (String percentage : EterniaServer.rewardsConfig.getConfigurationSection(rewardsConfig + group + ".commands").getKeys(false)) {
            if (Math.random() <= Double.parseDouble(percentage)) {
                for (String command : EterniaServer.rewardsConfig.getStringList(rewardsConfig + group + ".commands." + percentage)) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), InternMethods.setPlaceholders(player, command));
                }
            }
        }
    }

}
