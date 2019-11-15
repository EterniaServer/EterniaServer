package center.sql;

import center.main;
import org.bukkit.entity.Player;
import java.lang.Math;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class xpmanager {
    private final main plugin = main.getPlugin(main.class);
    private int xp = 0;
    private int lvl = 0;
    private String Vu = "";
    private void xp_player(Player player)
    {
        xp = player.getLevel();
        if (xp >= 1 && xp <= 16)
        {
            xp = xp * (6 + xp);
        }
        else if (xp >= 17 && xp <= 25)
        {
            xp = xp * (8 + xp);
        }
        else if (xp >= 26 && xp <= 30)
        {
            xp = xp * (16 + xp);
        }
        else if (xp >= 31)
        {
            xp = xp * (33 + xp);
        }
    }

    private void lvl_player()
    {
        if (xp >= 1 && xp <= 20)
        {
            lvl = (int) Math.ceil(xp / 25.0);
            xp = 0;
        }
        else if (xp >= 21 && xp <= 30)
        {
            lvl = (int) Math.ceil(xp / 46.0);
            xp = 0;
        }
        else if (xp >= 40)
        {
            lvl = (int) Math.ceil(xp / 74.0);
            xp = 0;
        }
    }

    private void connect(UUID uuid, Player player)
    {
        try
        {
            xp_player(player);
            PreparedStatement statement;
            ResultSet results;
            statement = plugin.getConnection().prepareStatement(String.format("SELECT XP FROM %s WHERE UUID = ?", plugin.table));
            statement.setString(1, uuid.toString());
            results = statement.executeQuery();
            while (results.next()) {
                Vu = results.getString("XP");
            }
            int XP = Integer.parseInt(Vu);
            xp = Math.max(xp + XP, 0);
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void deposit_xp(UUID uuid, Player player)
    {
        try
        {
            connect(uuid, player);
            PreparedStatement insert;
            insert = plugin.getConnection().prepareStatement(String.format("UPDATE %s SET XP = %d WHERE UUID = %s", plugin.table, xp, uuid.toString()));
            player.setLevel(0);
            player.setExp(0);
            insert.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void withdraw_xp(UUID uuid, Player player)
    {
        try
        {
            connect(uuid, player);
            lvl_player();
            PreparedStatement insert;
            insert = plugin.getConnection().prepareStatement(String.format("UPDATE %s SET XP = 0 WHERE UUID = %s", plugin.table, uuid.toString()));
            player.setLevel(lvl);
            insert.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
