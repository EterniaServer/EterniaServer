package events;

import center.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerJoin implements Listener {
    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CreatePlayer(player.getUniqueId(), player);
    }
    private boolean PlayerExist(UUID uuid) {
        try {
            String select_player = "SELECT * FROM eternia WHERE UUID = ?";
            PreparedStatement find_player = Main.getMain().getConnection().prepareStatement(select_player);
            find_player.setString(1, uuid.toString());
            ResultSet player_list = find_player.executeQuery();
            while (player_list.next()) {
                if (player_list.getObject("UUID") != null) {
                    find_player.close();
                    player_list.close();
                    return true;
                }
            }
            find_player.close();
            player_list.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void CreatePlayer(final UUID uuid, Player player) {
        try {
            if (!PlayerExist(uuid)) {
                String create_player = "INSERT INTO eternia (UUID,NAME,XP) VALUES (?,?,?)";
                PreparedStatement save_player = Main.getMain().getConnection().prepareStatement(create_player);
                save_player.setString(1, uuid.toString());
                save_player.setString(2, player.getName());
                save_player.setInt(3, 0);
                save_player.executeUpdate();
                save_player.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}