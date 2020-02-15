package events;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NetherPortal extends org.bukkit.scheduler.BukkitRunnable
{
    // A classe NetherPortal ela funciona em loop, ela fica
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
                        Vars.playersInPortal.put(player, 7);
                    }
                    else if (Vars.playersInPortal.get(player) <= 1)
                    {
                        Location player_location = player.getLocation();
                        if (player_location.getBlock().getType() == Material.NETHER_PORTAL)
                        {
                            player.teleport(Vars.spawn);
                            Vars.playerMessage("spawn", player);
                        }
                    }
                    else
                    {
                        Vars.playersInPortal.put(player, Vars.playersInPortal.get(player) - 1);
                        if (Vars.playersInPortal.get(player) < 5)
                        {
                            Vars.playerReplaceMessage("trap-mensagem-tempo", Vars.playersInPortal.get(player), player);
                        }
                    }
                }
                else Vars.playersInPortal.remove(player);
            }
        }
    }
}