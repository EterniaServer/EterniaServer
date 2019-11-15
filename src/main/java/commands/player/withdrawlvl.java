package commands.player;
import center.sql.xpmanager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import java.util.UUID;

public class withdrawlvl implements CommandExecutor
{
    final private xpmanager xpm = new xpmanager();
    @EventHandler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            try
            {
                int xp1 = player.getLevel();
                xpm.withdraw_xp(uuid, player);
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Pronto seu level agora é " + ChatColor.DARK_AQUA
                        + xp1 + ChatColor.DARK_GRAY + ".");
                return true;
            }
            catch (Exception e)
            {
                return true;
            }
        }
        return false;
    }
}