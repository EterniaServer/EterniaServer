package teleports;

import center.Main;
import center.Vars;
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
                if (Vars.back.containsKey(player))
                {
                    double money = economy.getBalance(player);
                    double valor = Vars.getInt("valor-do-back");
                    // Se ele tiver a permissão de executar de graça irá teleportar ele até
                    // a posição antiga dele, que está salva na váriavel back, caso contrário
                    // irá ser removido da conta dele o valor informado na configuração.
                    if (player.hasPermission("eternia.backfree"))
                    {
                        player.teleport(Vars.back.get(player));
                        Vars.back.remove(player);
                        Vars.playerMessage("back-gratis", player);
                    }
                    else
                    {
                        if (money >= valor)
                        {
                            player.teleport(Vars.back.get(player));
                            Vars.back.remove(player);
                            economy.withdrawPlayer(player, valor);
                            Vars.playerReplaceMessage("back-sucesso", valor, player);
                        }
                        else
                        {
                            Vars.playerReplaceMessage("back-sem-dinheiro", valor, player);
                        }
                    }
                }
                else
                {
                    Vars.playerMessage("back-nao-pode", player);
                }
            }
            else
            {
                Vars.playerMessage("sem-permissao", player);
            }
        }
        else
        {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}