package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.eternialib.SQL;

import br.com.eterniaserver.eternialib.core.queries.Delete;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.SecureRandom;


public class Reward extends BaseCommand {

    private final EterniaServer plugin;
    private final SecureRandom random = new SecureRandom();
    private final byte[] bytes = new byte[20];

    public Reward(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("%usekey")
    @Syntax("%usekey_syntax")
    @Description("%usekey_description")
    @CommandPermission("%usekey_perm")
    public void onUseKey(Player player, String key) {
        if (plugin.hasReward(key)) {
            giveReward(plugin.getReward(key), player);
            deleteKey(key);
            plugin.removeReward(key);
        } else {
            plugin.sendMessage(player, Messages.REWARD_INVALID_KEY, key);
        }
    }

    @CommandAlias("%genkey")
    @Syntax("%genkey_syntax")
    @Description("%genkey_description")
    @CommandPermission("%genkey_perm")
    public void onGenKey(CommandSender sender, String reward) {
        if (plugin.getChanceMap(ChanceMaps.REWARDS).containsKey(reward)) {
            random.nextBytes(bytes);
            final String key = Long.toHexString(random.nextLong());
            createKey(reward, key);
            plugin.addReward(key, reward);
            plugin.sendMessage(sender, Messages.REWARD_CREATED, key);
        } else {
            plugin.sendMessage(sender, Messages.REWARD_NOT_FOUND, reward);
        }
    }

    private void createKey(final String grupo, String key) {
        Insert insert = new Insert(plugin.getString(Strings.TABLE_REWARD));
        insert.columns.set("key_code", "group_name");
        insert.values.set(key, grupo);
        SQL.executeAsync(insert);
    }

    private void deleteKey(final String key) {
        Delete delete = new Delete(plugin.getString(Strings.TABLE_REWARD));
        delete.where.set("key_code", key);
        SQL.executeAsync(delete);
    }

    private void giveReward(String group, Player player) {
        plugin.getChanceMap(ChanceMaps.REWARDS).get(group).forEach((chance, lista) -> {
            random.nextBytes(bytes);
            if (random.nextDouble() <= chance) {
                for (String command : lista) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), plugin.setPlaceholders(player, command));
                }
            }
        });
    }

}
