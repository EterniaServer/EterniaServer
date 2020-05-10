package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.simplifications;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {

    private final Messages messages;

    public Gamemode(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.gamemode")) {
                if (args.length == 1) {
                    String gm = args[0].toLowerCase();
                    switch (gm) {
                        case "0":
                        case "s":
                        case "survival":
                        case "sobrevivencia":
                            if (player.hasPermission("eternia.gamemode.0")) {
                                player.setGameMode(GameMode.SURVIVAL);
                                messages.PlayerMessage("simp.gm","%gamemode%", "Sobrevivência", player);
                            } else {
                                messages.PlayerMessage("server.no-perm", player);
                            }
                            break;
                        case "1":
                        case "c":
                        case "creative":
                        case "criativo":
                            if (player.hasPermission("eternia.gamemode.1")) {
                                player.setGameMode(GameMode.CREATIVE);
                                messages.PlayerMessage("simp.gm", "%gamemode%", "Criativo", player);
                            } else {
                                messages.PlayerMessage("server.no-perm", player);
                            }
                            break;
                        case "2":
                        case "a":
                        case "adventure":
                        case "aventura":
                            if (player.hasPermission("eternia.gamemode.2")) {
                                player.setGameMode(GameMode.ADVENTURE);
                                messages.PlayerMessage("simp.gm", "%gamemode%", "Aventura", player);
                            } else {
                                messages.PlayerMessage("server.no-perm", player);
                            }
                            break;
                        case "3":
                        case "sp":
                        case "spectator":
                        case "spect":
                            if (player.hasPermission("eternia.gamemode.3")) {
                                player.setGameMode(GameMode.SPECTATOR);
                                messages.PlayerMessage("simp.gm", "%gamemode%", "Espectador", player);
                            } else {
                                messages.PlayerMessage("server.no-perm", player);
                            }
                            break;
                        default:
                            messages.PlayerMessage("simp.gmuse", player);
                            break;
                    }
                } else if (args.length == 2) {
                    String gm = args[0].toLowerCase();
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null && target.isOnline()) {
                        switch (gm) {
                            case "0":
                            case "s":
                            case "survival":
                            case "sobrevivencia":
                                if (player.hasPermission("eternia.gamemode.0")) {
                                    target.setGameMode(GameMode.SURVIVAL);
                                    messages.PlayerMessage("simp.gm-other", "%gamemode%", "Sobrevivência", "%target_name%", player.getName(), target);
                                    messages.PlayerMessage("simp.other-gm", "%target_name%", target.getName(), "%gamemode%", "Sobrevivência", player);
                                } else {
                                    messages.PlayerMessage("server.no-perm", player);
                                }
                                break;
                            case "1":
                            case "c":
                            case "creative":
                            case "criativo":
                                if (player.hasPermission("eternia.gamemode.1")) {
                                    target.setGameMode(GameMode.CREATIVE);
                                    messages.PlayerMessage("simp.gm-other", "%gamemode%", "Criativo", "%target_name%", player.getName(), target);
                                    messages.PlayerMessage("simp.other-gm", "%target_name%", target.getName(), "%gamemode%", "Criativo", player);
                                } else {
                                    messages.PlayerMessage("server.no-perm", player);
                                }
                                break;
                            case "2":
                            case "a":
                            case "adventure":
                            case "aventura":
                                if (player.hasPermission("eternia.gamemode.2")) {
                                    target.setGameMode(GameMode.ADVENTURE);
                                    messages.PlayerMessage("simp.gm-other", "%gamemode%", "Aventura", "%target_name%", player.getName(), target);
                                    messages.PlayerMessage("simp.other-gm", "%target_name%", target.getName(), "%gamemode%", "Aventura", player);
                                } else {
                                    messages.PlayerMessage("server.no-perm", player);
                                }
                                break;
                            case "3":
                            case "sp":
                            case "spectator":
                            case "spect":
                                if (player.hasPermission("eternia.gamemode.3")) {
                                    target.setGameMode(GameMode.SPECTATOR);
                                    messages.PlayerMessage("simp.gm-other", "%gamemode%", "Espectador", "%target_name%", player.getName(), target);
                                    messages.PlayerMessage("simp.other-gm", "%target_name%", target.getName(), "%gamemode%", "Espectador", player);
                                } else {
                                    messages.PlayerMessage("server.no-perm", player);
                                }
                                break;
                            default:
                                messages.PlayerMessage("simp.gmuse", player);
                                break;
                        }
                    } else {
                        messages.PlayerMessage("server.player-offline", player);
                    }
                } else {
                    messages.PlayerMessage("simp.gmuse", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 2) {
                String gm = args[0].toLowerCase();
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null && target.isOnline()) {
                    switch (gm) {
                        case "0":
                        case "s":
                        case "survival":
                        case "sobrevivencia":
                            target.setGameMode(GameMode.SURVIVAL);
                            messages.PlayerMessage("simp.gm-other", "%gamemode%", "Sobrevivência", "%target_name%", "Console", target);
                            messages.ConsoleMessage("simp.other-gm", "%target_name%", target.getName(), "%gamemode%", "Sobrevivência");
                            break;
                        case "1":
                        case "c":
                        case "creative":
                        case "criativo":
                            target.setGameMode(GameMode.CREATIVE);
                            messages.PlayerMessage("simp.gm-other", "%gamemode%", "Criativo", "%target_name%", "Console", target);
                            messages.ConsoleMessage("simp.other-gm", "%target_name%", target.getName(), "%gamemode%", "Criativo");
                            break;
                        case "2":
                        case "a":
                        case "adventure":
                        case "aventura":
                            target.setGameMode(GameMode.ADVENTURE);
                            messages.PlayerMessage("simp.gm-other", "%gamemode%", "Aventura", "%target_name%", "Console", target);
                            messages.ConsoleMessage("simp.other-gm", "%target_name%", target.getName(), "%gamemode%", "Aventura");
                            break;
                        case "3":
                        case "sp":
                        case "spectator":
                        case "spect":
                            target.setGameMode(GameMode.SPECTATOR);
                            messages.PlayerMessage("simp.gm-other", "%gamemode%", "Espectador", "%target_name%", "Console", target);
                            messages.ConsoleMessage("simp.other-gm", "%target_name%", target.getName(), "%gamemode%", "Espectador");
                            break;
                        default:
                            messages.ConsoleMessage("simp.gmuse");
                            break;
                    }
                } else {
                    messages.ConsoleMessage("server.player-offline");
                }
            } else {
                messages.ConsoleMessage("simp.gmuse");
            }
        }
        return true;
    }
}