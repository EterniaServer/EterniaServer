package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Select;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.PlayerRelated;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.User;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

@CommandAlias("%title")
public class Titles extends BaseCommand {

    public Titles() {
        try (Connection connection = SQL.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(new Select(EterniaServer.getString(Strings.TABLE_TITLES)).queryString()); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                PlayerProfile playerProfile = PlayerRelated.getProfile(UUID.fromString(resultSet.getString("uuid")));
                playerProfile.getTitles().addAll(Arrays.asList(resultSet.getString("titles_array").split(":")));
                playerProfile.setActiveTitle(resultSet.getString("default_title"));
            }
        } catch (SQLException exception) {
            ServerRelated.logError("Erro ao carregar database", 3);
        }
    }

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("%title_syntax")
    @CommandPermission("%title_perm")
    @Description("%title_description")
    public void onDefault(CommandHelp help) {
        help.showHelp();
    }

    public void seeTitles(Player player) {
        User user = new User(player);
    }

}
