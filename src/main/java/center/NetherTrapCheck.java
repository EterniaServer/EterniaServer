package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class NetherTrapCheck extends org.bukkit.scheduler.BukkitRunnable
{
    // Recebe a configuração informada pela classe principal e define
    // ela como inalterável.
    public static FileConfiguration file;
    NetherTrapCheck(FileConfiguration file)
    {
        NetherTrapCheck.file = file;
    }
    // A classe NetherTrapCheck ela funciona em loop, ela fica
    // sendo rodada com o intervalo escolhido nas configurações
    // para verificar se existe algum jogador preso em uma Trap
    // do Nether.
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void run()
    {
        // Irá fazer isso para cada jogador no servidor.
        for (Player player : Bukkit.getOnlinePlayers())
        {
            synchronized (player)
            {
                // Se o jogador estiver dentro de um bloco de portal do Nether ele irá
                // começar uma contagem e se o jogador permanecer lá por mais de 7 segundos
                // ele irá teleportar o jogador ao spawn.
                if (player.getLocation().getBlock().getType() == Material.NETHER_PORTAL)
                {
                    if (!Vars.playersInPortal.containsKey(player))
                    {
                        //noinspection unchecked
                        Vars.playersInPortal.put(player, 7);
                    }
                    else if ((Integer) Vars.playersInPortal.get(player) <= 1)
                    {
                        Location player_location = player.getLocation();
                        if (player_location.getBlock().getType() == Material.NETHER_PORTAL)
                        {
                            World world = Bukkit.getWorld((String) Objects.requireNonNull(NetherTrapCheck.file.get("world")));
                            Location spawn = new Location(world, NetherTrapCheck.file.getDouble("x"),
                                    NetherTrapCheck.file.getDouble("y"), NetherTrapCheck.file.getDouble("z"),
                                    Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("yaw"))),
                                    Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("pitch"))));
                            player.teleport(spawn);
                            player.sendMessage(Vars.getString("spawn"));
                        }
                    }
                    else
                    {
                        //noinspection unchecked
                        Vars.playersInPortal.put(player, (Integer) Vars.playersInPortal.get(player) - 1);
                        if ((Integer) Vars.playersInPortal.get(player) < 5)
                        {
                            player.sendMessage(Vars.replaceObject("trap-mensagem-tempo", Vars.playersInPortal.get(player)));
                        }
                    }
                }
                else Vars.playersInPortal.remove(player);
            }
        }
    }
}