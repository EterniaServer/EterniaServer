package com.eterniaserver.modules.genericmanager.commands.messages;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
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
                        String regras = MVar.getMessage("text.rules");
                        String[] regralista = regras.split("/split/");
                        for (int i = inicio; i < fim; i++) {
                            try {
                                player.sendMessage(MVar.getColor(regralista[i]));
                                cont += 1;
                            } catch (Exception e) {
                                break;
                            }
                        }
                        if (cont == fim - inicio) {
                            new PlayerMessage("text.next", Integer.parseInt(args[0]) + 1, player);
                        }
                    } else {
                        new PlayerMessage("server.no-negative", player);
                    }
                } else {
                    int valor = 1;
                    int inicio = 5 * (valor - 1);
                    int fim = 6 * (valor);
                    int cont = 0;
                    String regras = MVar.getMessage("text.rules");
                    String[] regralista = regras.split("/split/");
                    for (int i = inicio; i < fim; i++) {
                        try {
                            player.sendMessage(MVar.getColor(regralista[i]));
                            cont += 1;
                        } catch (Exception e) {
                            break;
                        }
                    }
                    if (cont == fim - inicio) {
                        new PlayerMessage("text.next", Integer.parseInt(args[0]) + 1, player);
                    }
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 1) {
                int valor = Integer.parseInt(args[0]);
                if (valor > 0) {
                    int inicio = 5 * (valor - 1);
                    int fim = 6 * (valor);
                    int cont = 0;
                    String regras = MVar.getMessage("text.rules");
                    String[] regralista = regras.split("/split/");
                    for (int i = inicio; i < fim; i++) {
                        try {
                            Bukkit.getConsoleSender().sendMessage(MVar.getColor(regralista[i]));
                            cont += 1;
                        } catch (Exception e) {
                            break;
                        }
                    }
                    if (cont == fim - inicio) {
                        new ConsoleMessage("text.next", Integer.parseInt(args[0]) + 1);
                    }
                } else {
                    new ConsoleMessage("server.no-negative");
                }
            } else {
                new ConsoleMessage("server.no-number");
            }
        }
        return true;
    }
}