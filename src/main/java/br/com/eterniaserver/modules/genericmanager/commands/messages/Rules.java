package br.com.eterniaserver.modules.genericmanager.commands.messages;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rules implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.rules")) {
                if (args.length == 1) {
                    int valor = Integer.parseInt(args[0]);
                    if (valor > 0) {
                        int inicio = 5 * (valor - 1);
                        int fim = 6 * (valor);
                        int cont = 0;
                        java.lang.String regras = Strings.getMessage("text.rules");
                        java.lang.String[] regralista = regras.split("/split/");
                        for (int i = inicio; i < fim; i++) {
                            try {
                                player.sendMessage(Strings.getColor(regralista[i]));
                                cont += 1;
                            } catch (Exception e) {
                                break;
                            }
                        }
                        if (cont == fim - inicio) {
                            Messages.PlayerMessage("text.next", Integer.parseInt(args[0]) + 1, player);
                        }
                    } else {
                        Messages.PlayerMessage("server.no-negative", player);
                    }
                } else {
                    args[0] = "1";
                    int valor = 1;
                    int inicio = 5 * (valor - 1);
                    int fim = 6 * (valor);
                    int cont = 0;
                    java.lang.String regras = Strings.getMessage("text.rules");
                    java.lang.String[] regralista = regras.split("/split/");
                    for (int i = inicio; i < fim; i++) {
                        try {
                            player.sendMessage(Strings.getColor(regralista[i]));
                            cont += 1;
                        } catch (Exception e) {
                            break;
                        }
                    }
                    if (cont == fim - inicio) {
                        Messages.PlayerMessage("text.next", Integer.parseInt(args[0]) + 1, player);
                    }
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 1) {
                int valor = Integer.parseInt(args[0]);
                if (valor > 0) {
                    int inicio = 5 * (valor - 1);
                    int fim = 6 * (valor);
                    int cont = 0;
                    java.lang.String regras = Strings.getMessage("text.rules");
                    java.lang.String[] regralista = regras.split("/split/");
                    for (int i = inicio; i < fim; i++) {
                        try {
                            Bukkit.getConsoleSender().sendMessage(Strings.getColor(regralista[i]));
                            cont += 1;
                        } catch (Exception e) {
                            break;
                        }
                    }
                    if (cont == fim - inicio) {
                        Messages.ConsoleMessage("text.next", Integer.parseInt(args[0]) + 1);
                    }
                } else {
                    Messages.ConsoleMessage("server.no-negative");
                }
            } else {
                Messages.ConsoleMessage("server.no-number");
            }
        }
        return true;
    }
}