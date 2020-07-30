package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.HashMap;

@SuppressWarnings("squid:S2245")
public class RewardsSystem extends BaseCommand {

    private final EFiles messages;
    private final EterniaServer plugin;

    private final SecureRandom random = new SecureRandom();
    private final byte[] bytes = new byte[20];

    public RewardsSystem(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
        this.plugin = plugin;

        HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_REWARD), Strings.CODE, Strings.CODE_GROUP);
        temp.forEach(Vars.rewards::put);
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Keys", Constants.AMOUNT, temp.size());

    }

    @CommandAlias("usekey|usarkey|usarchave")
    @Syntax("<chave>")
    @CommandPermission("eternia.usekey")
    public void onUseKey(Player player, String key) {
        if (Vars.rewards.containsKey(key)) {
            giveReward(Vars.rewards.get(key), player);
            deleteKey(key);
        } else {
            messages.sendMessage(Strings.M_REWARD_INVALID, player);
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
            messages.sendMessage(Strings.M_REWARD_CREATE, Constants.KEY, key, sender);
        } else {
            messages.sendMessage(Strings.M_REWARD_NO, Constants.GROUP, reward, sender);
        }
    }

    private void createKey(final String grupo, String key) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_REWARD, Strings.CODE, key, Strings.CODE_GROUP, grupo));
    }

    private void deleteKey(final String key) {
        EQueries.executeQuery(Constants.getQueryDelete(Constants.TABLE_REWARD, Strings.CODE, key));
    }

    private void giveReward(String group, Player player) {
        final String rewardsConfig = "rewards.";
        for (String percentage : EterniaServer.rewardsConfig.getConfigurationSection(rewardsConfig + group + ".commands").getKeys(true)) {
            if (Math.random() <= Double.parseDouble(percentage)) {
                for (String command : EterniaServer.rewardsConfig.getStringList(rewardsConfig + group + ".commands." + percentage)) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders((OfflinePlayer) player, command));
                }
            }
        }
    }

}
