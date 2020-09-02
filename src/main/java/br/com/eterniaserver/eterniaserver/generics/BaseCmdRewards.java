package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.Map;

@SuppressWarnings("squid:S2245")
public class BaseCmdRewards extends BaseCommand {

    private final SecureRandom random = new SecureRandom();
    private final byte[] bytes = new byte[20];

    public BaseCmdRewards() {
        Map<String, String> temp = EQueries.getMapString(PluginConstants.getQuerySelectAll(PluginConfigs.TABLE_REWARD), PluginConstants.CODE_STR, PluginConstants.CODE_GROUP_STR);
        temp.forEach(PluginVars.rewards::put);
        Bukkit.getConsoleSender().sendMessage(PluginMSGs.MSG_LOAD_DATA.replace(PluginConstants.MODULE, "Keys").replace(PluginConstants.AMOUNT, String.valueOf(temp.size())));
    }

    @CommandAlias("usekey|usarkey|usarchave")
    @Syntax("<chave>")
    @CommandPermission("eternia.usekey")
    public void onUseKey(Player player, String key) {
        if (PluginVars.rewards.containsKey(key)) {
            giveReward(PluginVars.rewards.get(key), player);
            deleteKey(key);
        } else {
            player.sendMessage(PluginMSGs.MSG_REWARD_INVALID);
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
            sender.sendMessage(PluginMSGs.MSG_REWARD_CREATE.replace(PluginConstants.KEY, key));
        } else {
            sender.sendMessage(PluginMSGs.MSG_REWARD_NO.replace(PluginConstants.GROUP, reward));
        }
    }

    private void createKey(final String grupo, String key) {
        EQueries.executeQuery(PluginConstants.getQueryInsert(PluginConfigs.TABLE_REWARD, PluginConstants.CODE_STR, key, PluginConstants.CODE_GROUP_STR, grupo));
    }

    private void deleteKey(final String key) {
        EQueries.executeQuery(PluginConstants.getQueryDelete(PluginConfigs.TABLE_REWARD, PluginConstants.CODE_STR, key));
    }

    private void giveReward(String group, Player player) {
        final String rewardsConfig = "rewards.";
        for (String percentage : EterniaServer.rewardsConfig.getConfigurationSection(rewardsConfig + group + ".commands").getKeys(false)) {
            if (Math.random() <= Double.parseDouble(percentage)) {
                for (String command : EterniaServer.rewardsConfig.getStringList(rewardsConfig + group + ".commands." + percentage)) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), UtilInternMethods.setPlaceholders(player, command));
                }
            }
        }
    }

}
