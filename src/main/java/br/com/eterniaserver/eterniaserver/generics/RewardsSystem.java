package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.security.SecureRandom;

public class RewardsSystem extends BaseCommand {

    private final EFiles messages;
    private final EterniaServer plugin;

    private final SecureRandom random = new SecureRandom();
    private final byte[] bytes = new byte[20];

    public RewardsSystem(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
        this.plugin = plugin;
    }

    @CommandAlias("usekey|usarkey|usarchave")
    @Syntax("<chave>")
    @CommandPermission("eternia.usekey")
    public void onUseKey(Player player, String key) {
        final String grupo = existKey(key);
        if (!grupo.equals("no")) {
            giveReward(grupo, player);
            deleteKey(key);
        } else {
            messages.sendMessage("reward.invalid", player);
        }
    }

    @CommandAlias("genkey|criarkey|criarchave")
    @Syntax("<reward>")
    @CommandPermission("eternia.genkey")
    public void onGenKey(CommandSender sender, String reward) {
        if (plugin.rewardsConfig.getConfigurationSection("rewards." + reward) != null) {
            random.nextBytes(bytes);
            final String key = Long.toHexString(random.nextLong());
            createKey(reward, key);
            messages.sendMessage("reward.created", "%key%", key, sender);
        } else {
            messages.sendMessage("reward.no-exists", "%group%", reward, sender);
        }
    }

    private void createKey(final String grupo, String key) {
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-rewards") + " (code, lalalala) VALUES('" + key + "', '" + grupo + "');");
    }

    private void deleteKey(final String key) {
        EQueries.executeQuery("DELETE FROM " + plugin.serverConfig.getString("sql.table-rewards") + " WHERE code='" + key + "';");
    }

    private String existKey(final String key) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-rewards")+ " WHERE code='" + key + "';";
        return EQueries.queryString(querie, "code", "lalalala");
    }

    private void giveReward(String group, Player player) {
        ConfigurationSection cs = plugin.rewardsConfig.getConfigurationSection("rewards." + group + ".commands");
        if (cs != null) {
            for (String percentage : cs.getKeys(true)) {
                if (Math.random() <= Double.parseDouble(percentage)) {
                    for (String command : plugin.rewardsConfig.getStringList("rewards." + group + ".commands." + percentage)) {
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), putPAPI(player, command));
                    }
                }
            }
        }
    }

    private String putPAPI(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

}
