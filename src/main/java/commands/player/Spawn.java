package commands.player;

import center.Main;
import center.NetherTrapCheck;
import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Spawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            World world = Bukkit.getWorld((String) Objects.requireNonNull(NetherTrapCheck.file.get("world")));
            Location spawn = new Location(world, NetherTrapCheck.file.getDouble("x"),
                    NetherTrapCheck.file.getDouble("y"), NetherTrapCheck.file.getDouble("z"),
                    Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("yaw"))),
                    Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("pitch"))));
            Player player = (Player) sender;
            if (args.length == 0)
            {
                if (player.hasPermission("eternia.spawn"))
                {
                    if (player.hasPermission("eternia.timing.bypass"))
                    {
                        player.teleport(spawn);
                        player.sendMessage(Vars.getString("spawn"));
                        return true;
                    }
                    else
                    {
                        int tempo = Vars.getInt("cooldown");
                        player.sendMessage(Vars.replaceObject("teleportando-em", tempo));
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getMain(), () ->
                        {
                            player.teleport(spawn);
                            player.sendMessage(Vars.getString("spawn"));
                        }, 20 * tempo);
                        return true;
                    }
                }
                else
                {
                    player.sendMessage(Vars.getString("sem-permissao"));
                    return true;
                }
            }
            else if (args.length == 1)
            {
                if (player.hasPermission("eternia.spawn.other"))
                {
                    String targetS = args[0];
                    Player target = Bukkit.getPlayer(targetS);
                    assert target != null;
                    if(target.isOnline())
                    {
                        player.teleport(spawn);
                        target.sendMessage(Vars.getString("spawn"));
                        player.sendMessage(Vars.replaceString("teleportou-ele", target.getName()));
                    }
                    else
                    {
                        player.sendMessage(Vars.getString("jogador-offline"));
                        return true;
                    }
                }
                else
                {
                    player.sendMessage(Vars.getString("sem-permissao"));
                    return true;
                }
            }
        }
        return false;
    }
}
