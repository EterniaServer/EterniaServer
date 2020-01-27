package commands.player;

import center.Main;
import center.PlayerListener;
import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;

public class Back implements CommandExecutor
{
    private final Economy economy = Main.getEconomy();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // Verifica se quem está executando o comando é um jogador
        // caso seja o comando é executado, caso não seja é enviado
        // uma mensagem ao console.
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            // Verifica se o jogador tem a permissão para usar o /back, caso tenha
            // verifica se ele tem permissão para usar de graça o comando e depois
            // irá executar o comando.
            if (player.hasPermission("eternia.back"))
            {
                if (PlayerListener.back.containsKey(player))
                {
                    double money = economy.getBalance(player);
                    double valor = Vars.getInt("valor-do-back");
                    // Se ele tiver a permissão de executar de graça irá teleportar ele até
                    // a posição antiga dele, que está salva na váriavel back, caso contrário
                    // irá ser removido da conta dele o valor informado na configuração.
                    if (player.hasPermission("eternia.backfree"))
                    {
                        player.teleport(PlayerListener.back.get(player));
                        PlayerListener.back.remove(player);
                        player.sendMessage(Vars.getString("back-gratis"));
                        return true;
                    }
                    else
                    {
                        if (money >= valor)
                        {
                            player.teleport(PlayerListener.back.get(player));
                            PlayerListener.back.remove(player);
                            player.sendMessage(Vars.replaceObject("back-sucesso", valor));
                            economy.withdrawPlayer(player, valor);
                            return true;
                        }
                        else
                        {
                            player.sendMessage(Vars.replaceObject("back-sem-dinheiro", valor));
                            return true;
                        }
                    }
                }
                else
                {
                    player.sendMessage(Vars.getString("back-nao-pode"));
                    return true;
                }
            }
            else
            {
                player.sendMessage(Vars.getString("sem-permissao"));
                return true;
            }
        }
        else
        {
            Bukkit.getConsoleSender().sendMessage(Vars.getString("somente-jogador"));
            return true;
        }
    }
}