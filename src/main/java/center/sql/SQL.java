package center.sql;

import center.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQL implements Listener
{
    private final Main plugin = Main.getPlugin(Main.class);
    @EventHandler
    public void OnJoin (PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        CreatePlayer(player.getUniqueId(), player);
    }

    private boolean playerExist(UUID uuid)
    {
        PreparedStatement statement;
        ResultSet results;
        try
        {
            String select_player = String.format("SELECT * FROM %s WHERE UUID = ?", plugin.table);
            statement = plugin.getConnection().prepareStatement(select_player);
            statement.setString(1, uuid.toString());
            results = statement.executeQuery();
            while(results.next())
            {
                if (results.getObject("UUID") != null)
                {
                    statement.close();
                    return true;
                }
            }
            statement.close();
            return false;

        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private void CreatePlayer(final UUID uuid, Player player)
    {
        try
        {
            if(!playerExist(uuid))
            {
                String create_player = "INSERT INTO eternia (UUID,NAME,XP) VALUES (?,?,?)";
                PreparedStatement insert = plugin.getConnection().prepareStatement(create_player);
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setInt(3, 0);
                insert.executeUpdate();
                insert.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}