package commands.player;
import center.main;
import center.vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class depositlvl implements CommandExecutor
{
    private final main plugin = main.getPlugin(main.class);
    @EventHandler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if(args.length == 1)
            {
                try
                {
                    int xp_atual = player.getLevel();
                    if (xp_atual >= Integer.parseInt(args[0]))
                    {
                        xp_atual = Integer.parseInt(args[0]);
                        player.sendMessage(vars.replaceObject("xp-guardado", xp_atual));
                        try
                        {
                            int xp = xp_atual;
                            if (xp >= 1 && xp <= 15)
                            {
                                xp = (xp * xp) + (6 * xp);
                            }
                            else if (xp >= 16 && xp <= 30)
                            {
                                xp = (int) ((2.5 * (xp * xp)) - (40.5 * xp) + 360);
                            }
                            else
                            {
                                xp = (int) ((4.5 * (xp * xp)) - (162.5 * xp) + 2220);
                            }
                            Statement statement = plugin.getConnection().createStatement();
                            ResultSet results = statement.executeQuery("SELECT XP FROM " + plugin.table + " WHERE UUID = '"+uuid.toString()+"'");
                            String Vu = "";
                            while (results.next())
                            {
                                Vu = results.getString("XP");
                            }
                            int XP = Integer.parseInt(Vu);
                            xp = Math.max(xp + XP, 0);
                            results.close();
                            statement.executeUpdate("UPDATE " + plugin.table + " SET XP='"+xp+"' WHERE UUID='"+uuid.toString()+"'");
                            player.setLevel(Math.max(player.getLevel() - xp_atual, 0));
                            statement.close();
                        }
                        catch(SQLException e)
                        {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    else
                    {
                        player.sendMessage(vars.getString("xp-insuficiente"));
                        return true;
                    }
                }
                catch (Exception e)
                {
                    player.sendMessage(vars.getString("guarda-level-errou"));
                    return true;
                }
            } else {
                player.sendMessage(vars.getString("guarda-level"));
                return true;
            }
        }
        return false;
    }
}