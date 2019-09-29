package principal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class mysql implements Listener {
    @EventHandler
    public void OnJoin (PlayerJoinEvent event){
        Player player = event.getPlayer();
        CreatePlayer(player.getUniqueId(),player);
    }
    private final main plugin = main.getPlugin(main.class);
    private boolean playerExist(UUID uuid){
        PreparedStatement statement;
        ResultSet results;
        try
        {
            statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID = ?");
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

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void CreatePlayer(final UUID uuid, Player player){
        try {
            if(!playerExist(uuid)){
                PreparedStatement insert = plugin.getConnection().prepareStatement("INSERT INTO " + plugin.table + " (UUID,NAME,XP) VALUE (?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setInt(3, 0);
                insert.executeUpdate();
                insert.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
