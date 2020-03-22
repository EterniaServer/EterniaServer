package eternia.commands.teleports;

import eternia.api.MoneyAPI;
import eternia.configs.CVar;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("NullableProblems")
public class Back implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verifica se quem está executando o comando é um jogador
        // caso seja o comando é executado, caso não seja é enviado
        // uma mensagem ao console.
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Verifica se o jogador tem a permissão para usar o /back, caso tenha
            // verifica se ele tem permissão para usar de graça o comando e depois
            // irá executar o comando.
            if (player.hasPermission("eternia.back")) {
                if (Vars.back.containsKey(player)) {
                    double money = MoneyAPI.getMoney(player.getUniqueId());
                    double valor = CVar.getInt("money.valor-do-back");
                    // Se ele tiver a permissão de executar de graça irá teleportar ele até
                    // a posição antiga dele, que está salva na váriavel back, caso contrário
                    // irá ser removido da conta dele o valor informado na configuração.
                    if (player.hasPermission("eternia.backfree")) {
                        player.teleport(Vars.back.get(player));
                        Vars.back.remove(player);
                        MVar.playerMessage("back-gratis", player);
                    } else {
                        if (money >= valor) {
                            player.teleport(Vars.back.get(player));
                            Vars.back.remove(player);
                            MoneyAPI.removeMoney(player.getUniqueId(), valor);
                            MVar.playerReplaceMessage("back-sucesso", valor, player);
                        } else {
                            MVar.playerReplaceMessage("back-sem-dinheiro", valor, player);
                        }
                    }
                } else {
                    MVar.playerMessage("back-nao-pode", player);
                }
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}