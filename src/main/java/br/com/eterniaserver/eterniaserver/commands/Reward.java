package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Delete;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eternialib.sql.queries.Select;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reward extends BaseCommand {

    private final SecureRandom random = new SecureRandom();
    private final byte[] bytes = new byte[20];

    public Reward() {
        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(EterniaServer.getString(Strings.TABLE_REWARD)).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ServerRelated.updateRewardMap(resultSet.getString("key_code"), resultSet.getString("group_name"));
            }
        } catch (SQLException ignored) {
            ServerRelated.logError("Erro ao pegar arquivos da database", 3);
        }
        Bukkit.getConsoleSender().sendMessage(EterniaServer.getMessage(Messages.SERVER_DATA_LOADED, true, "Keys", String.valueOf(ServerRelated.getRewardMapSize())));
    }

    @CommandAlias("%usekey")
    @Syntax("%usekey_syntax")
    @Description("%usekey_description")
    @CommandPermission("%usekey_perm")
    public void onUseKey(Player player, String key) {
        if (ServerRelated.hasReward(key)) {
            giveReward(ServerRelated.getReward(key), player);
            deleteKey(key);
            ServerRelated.removeReward(key);
        } else {
            EterniaServer.sendMessage(player, Messages.REWARD_INVALID_KEY, key);
        }
    }

    @CommandAlias("%genkey")
    @Syntax("%genkey_syntax")
    @Description("%genkey_description")
    @CommandPermission("%genkey_perm")
    public void onGenKey(CommandSender sender, String reward) {
        if (EterniaServer.getChanceMap(ChanceMaps.REWARDS).containsKey(reward)) {
            random.nextBytes(bytes);
            final String key = Long.toHexString(random.nextLong());
            createKey(reward, key);
            ServerRelated.addReward(key, reward);
            EterniaServer.sendMessage(sender, Messages.REWARD_CREATED, key);
        } else {
            EterniaServer.sendMessage(sender, Messages.REWARD_NOT_FOUND, reward);
        }
    }

    private void createKey(final String grupo, String key) {
        Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_LOCATIONS));
        insert.columns.set("key_code", "group_name");
        insert.values.set(key, grupo);
        SQL.executeAsync(insert);
    }

    private void deleteKey(final String key) {
        Delete delete = new Delete(EterniaServer.getString(Strings.TABLE_LOCATIONS));
        delete.where.set("key_code", key);
        SQL.executeAsync(delete);
    }

    private void giveReward(String group, Player player) {
        EterniaServer.getChanceMap(ChanceMaps.REWARDS).get(group).forEach((chance, lista) -> {
            random.nextBytes(bytes);
            if (random.nextDouble() <= chance) {
                for (String command : lista) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), ServerRelated.setPlaceholders(player, command));
                }
            }
        });
    }

}
