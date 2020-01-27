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
    // Toda vez que um jogador entrar no servidor irá chamar
    // a função CreatePlayer para criar um registro do usuário.
    @EventHandler
    public void OnJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        CreatePlayer(player.getUniqueId(), player);
    }
    // Procura na database pelo ID do usuário se encontrar,
    // retorna verdadeiro, caso contrário retorna falso.
    private boolean PlayerExist(UUID uuid)
    {
        try
        {
            String select_player = "SELECT * FROM eternia WHERE UUID = ?";
            PreparedStatement find_player = plugin.getConnection().prepareStatement(select_player);
            find_player.setString(1, uuid.toString());
            ResultSet player_list = find_player.executeQuery();
            while(player_list.next())
            {
                if (player_list.getObject("UUID") != null)
                {
                    find_player.close();
                    return true;
                }
            }
            find_player.close();
            return false;

        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    // Então irá chamar a função playerExist para verificar se
    // o jogador já existe no banco de dados.
    private void CreatePlayer(final UUID uuid, Player player)
    {
        try
        {
            if(!PlayerExist(uuid))
            {
                String create_player = "INSERT INTO eternia (UUID,NAME,XP) VALUES (?,?,?)";
                PreparedStatement save_player = plugin.getConnection().prepareStatement(create_player);
                save_player.setString(1, uuid.toString());
                save_player.setString(2, player.getName());
                save_player.setInt(3, 0);
                save_player.executeUpdate();
                save_player.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}