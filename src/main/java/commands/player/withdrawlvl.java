package commands.player;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class withdrawlvl implements CommandExecutor
{
    private final Main plugin = Main.getPlugin(Main.class);
    @EventHandler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.withdrawlvl"))
            {
                UUID uuid = player.getUniqueId();
                try
                {
                    try
                    {
                        Statement statement = plugin.getConnection().createStatement();
                        ResultSet results = statement.executeQuery("SELECT XP FROM " + plugin.table + " WHERE UUID = '" + uuid.toString() + "'");
                        String Vu = "";
                        while (results.next())
                        {
                            Vu = results.getString("XP");
                        }
                        results.close();
                        player.giveExp(Integer.parseInt(Vu));
                        int v = 0;
                        statement.executeUpdate("UPDATE " + plugin.table + " SET XP='" + v + "' WHERE UUID='" + uuid.toString() + "'");
                        statement.close();
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                    player.sendMessage(Vars.replaceObject("xp-tirar", player.getLevel()));
                    return true;
                } catch (Exception e)
                {
                    return true;
                }
            }
            else
            {
                player.sendMessage(Vars.getString("sem-permissao"));
            }
        }
        return false;
    }
}