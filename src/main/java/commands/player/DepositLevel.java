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

public class DepositLevel implements CommandExecutor
{
    private final Main plugin = Main.getPlugin(Main.class);
    @EventHandler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.depositlvl"))
            {
                UUID uuid = player.getUniqueId();
                if (args.length == 1)
                {
                    try
                    {
                        int xp_atual = player.getLevel();
                        if (xp_atual >= Integer.parseInt(args[0]))
                        {
                            xp_atual = Integer.parseInt(args[0]);
                            player.sendMessage(Vars.replaceObject("xp-guardado", xp_atual));
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
                                String get_xp = String.format("SELECT XP FROM eternia WHERE UUID=%s", uuid.toString());
                                ResultSet results = statement.executeQuery(get_xp);
                                String Vu = "";
                                while (results.next())
                                {
                                    Vu = results.getString("XP");
                                }
                                int XP = Integer.parseInt(Vu);
                                xp = Math.max(xp + XP, 0);
                                results.close();
                                String att_xp = String.format("UPDATE eternia SET XP=%s WHERE UUID=%s", String.valueOf(xp), uuid.toString());
                                statement.executeUpdate(att_xp);
                                player.setLevel(Math.max(player.getLevel() - xp_atual, 0));
                                statement.close();
                            }
                            catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                            return true;
                        }
                        else
                        {
                            player.sendMessage(Vars.getString("xp-insuficiente"));
                            return true;
                        }
                    }
                    catch (Exception e)
                    {
                        player.sendMessage(Vars.getString("guarda-level-errou"));
                        return true;
                    }
                }
                else
                {
                    player.sendMessage(Vars.getString("guarda-level"));
                    return true;
                }
            }
            else
            {
                player.sendMessage(Vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}