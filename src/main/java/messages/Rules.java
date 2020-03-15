package messages;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rules implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.rules")) {
                if (args.length == 1) {
                    int valor = Integer.parseInt(args[0]);
                    if (valor > 0) {
                        int inicio = 5 * (valor - 1);
                        int fim = 6 * (valor);
                        int cont = 0;
                        String regras = Vars.getString("regras");
                        assert regras != null;
                        String[] regralista = regras.split("/split/");
                        for (int i = inicio; i < fim; i++) {
                            try {
                                player.sendMessage(Vars.getColor(regralista[i]));
                                cont += 1;
                            } catch (Exception e) {
                                break;
                            }
                        }
                        if (cont == fim - inicio) {
                            Vars.playerReplaceMessage("proxima-pagina", Integer.parseInt(args[0]) + 1, player);
                        }
                    } else {
                        Vars.playerMessage("pagina-negativa", player);
                    }
                } else {
                    Vars.playerMessage("precisa-numero", player);
                }
            } else {
                Vars.playerMessage("sem-permissao", player);
            }
        } else {
            if (args.length == 1) {
                int valor = Integer.parseInt(args[0]);
                if (valor > 0) {
                    int inicio = 5 * (valor - 1);
                    int fim = 6 * (valor);
                    int cont = 0;
                    String regras = Vars.getString("regras");
                    assert regras != null;
                    String[] regralista = regras.split("/split/");
                    for (int i = inicio; i < fim; i++) {
                        try {
                            Bukkit.getConsoleSender().sendMessage(Vars.getColor(regralista[i]));
                            cont += 1;
                        } catch (Exception e) {
                            break;
                        }
                    }
                    if (cont == fim - inicio) {
                        Vars.consoleReplaceMessage("proxima-pagina", Integer.parseInt(args[0]) + 1);
                    }
                } else {
                    sender.sendMessage(Vars.getMessage("pagina-negativa"));
                }
            } else {
                sender.sendMessage(Vars.getMessage("precisa-numero"));
            }
        }
        return true;
    }
}