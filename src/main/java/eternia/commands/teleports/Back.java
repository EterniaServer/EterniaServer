package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.api.MoneyAPI;
import eternia.configs.CVar;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                    double money = MoneyAPI.getMoney(player.getName());
                    double valor = CVar.getInt("money.back");
                    // Se ele tiver a permissão de executar de graça irá teleportar ele até
                    // a posição antiga dele, que está salva na váriavel back, caso contrário
                    // irá ser removido da conta dele o valor informado na configuração.
                    if (player.hasPermission("eternia.backfree")) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(Vars.back.get(player));
                            Vars.back.remove(player);
                            MVar.playerMessage("back.free", player);
                        } else {
                            MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                player.teleport(Vars.back.get(player));
                                Vars.back.remove(player);
                                MVar.playerMessage("back.free", player);
                            }, 20 * Vars.cooldown);
                        }
                    } else {
                        if (money >= valor) {
                            if (player.hasPermission("eternia.timing.bypass")) {
                                player.teleport(Vars.back.get(player));
                                Vars.back.remove(player);
                                MoneyAPI.removeMoney(player.getName(), valor);
                                MVar.playerReplaceMessage("back.nofree", valor, player);
                            } else {
                                MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                                {
                                    player.teleport(Vars.back.get(player));
                                    Vars.back.remove(player);
                                    MoneyAPI.removeMoney(player.getName(), valor);
                                    MVar.playerReplaceMessage("back.nofree", valor, player);
                                }, 20 * Vars.cooldown);
                            }
                        } else {
                            MVar.playerReplaceMessage("back.nomoney", valor, player);
                        }
                    }
                } else {
                    MVar.playerMessage("back.notp", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}