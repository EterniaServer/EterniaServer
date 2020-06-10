package br.com.eterniaserver.eterniaserver.modules.rewardsmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.rewardsmanager.RewardsManager;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class RewardsSystem extends BaseCommand {

    private final RewardsManager rewardsManager;
    private final Messages messages;
    private final EterniaServer plugin;

    public RewardsSystem(RewardsManager rewardsManager, Messages messages, EterniaServer plugin) {
        this.rewardsManager = rewardsManager;
        this.messages = messages;
        this.plugin = plugin;
    }

    @CommandAlias("usekey|usarkey|usarchave")
    @Syntax("<chave>")
    @CommandPermission("eternia.usekey")
    public void onUseKey(Player player, String key) {
        final String grupo = rewardsManager.existKey(key);
        if (!grupo.equals("no")) {
            rewardsManager.giveReward(grupo, player);
            rewardsManager.deleteKey(key);
        } else {
            messages.sendMessage("reward.invalid", player);
        }
    }

    @CommandAlias("genkey|criarkey|criarchave")
    @Syntax("<reward>")
    @CommandPermission("eternia.genkey")
    public void onGenKey(CommandSender sender, String reward) {
        if (plugin.rewardsConfig.getConfigurationSection("rewards." + reward) != null) {
            Random rand = new Random();
            final String key = Long.toHexString(rand.nextLong());
            rewardsManager.createKey(reward, key);
            messages.sendMessage("reward.created", "%key%", key, sender);
        } else {
            messages.sendMessage("reward.no-exists", "%group%", reward, sender);
        }
    }

}