package principal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class mysql implements Listener {
    main plugin = main.getPlugin(main.class);
    @EventHandler
    public void OnJoin (PlayerJoinEvent event){
        Player player = event.getPlayer();
        CreatePlayer(player.getUniqueId(),player);
    }
    public boolean playerExist(UUID uuid){
        try {
            Statement statement = plugin.getConnection().createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + plugin.table + " WHERE UUID = '"+uuid.toString()+"'");
            while(results.next())
            {
                if (results.getObject("UUID") != null) {
                    statement.close();
                    return true;
                } else {
                    statement.close();
                    return false;
                }
            }
            return false;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void CreatePlayer(final UUID uuid, Player player){
        try {
            if(playerExist(uuid) != true){
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
