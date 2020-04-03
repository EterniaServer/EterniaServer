package com.eterniaserver.modules.genericmanager.commands.simplifications;

import com.eterniaserver.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {
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
                                MVar.playerReplaceMessage("simp.gm", "Sobrevivência", player);
                            } else {
                                MVar.playerMessage("server.no-perm", player);
                            }
                            break;
                        case "1":
                        case "c":
                        case "creative":
                        case "criativo":
                            if (player.hasPermission("eternia.gamemode.1")) {
                                player.setGameMode(GameMode.CREATIVE);
                                MVar.playerReplaceMessage("simp.gm", "Criativo", player);
                            } else {
                                MVar.playerMessage("server.no-perm", player);
                            }
                            break;
                        case "2":
                        case "a":
                        case "adventure":
                        case "aventura":
                            if (player.hasPermission("eternia.gamemode.2")) {
                                player.setGameMode(GameMode.ADVENTURE);
                                MVar.playerReplaceMessage("simp.gm", "Aventura", player);
                            } else {
                                MVar.playerMessage("server.no-perm", player);
                            }
                            break;
                        case "3":
                        case "sp":
                        case "spectator":
                        case "spect":
                            if (player.hasPermission("eternia.gamemode.3")) {
                                player.setGameMode(GameMode.SPECTATOR);
                                MVar.playerReplaceMessage("simp.gm", "Espectador", player);
                            } else {
                                MVar.playerMessage("server.no-perm", player);
                            }
                            break;
                        default:
                            MVar.playerMessage("simp.gmuse", player);
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
                                    MVar.playerReplaceMessage("simp.gm-other", "Sobrevivência", player.getName(), target);
                                    MVar.playerReplaceMessage("simp.other-gm", target.getName(), "Sobrevivência", player);
                                } else {
                                    MVar.playerMessage("server.no-perm", player);
                                }
                                break;
                            case "1":
                            case "c":
                            case "creative":
                            case "criativo":
                                if (player.hasPermission("eternia.gamemode.1")) {
                                    target.setGameMode(GameMode.CREATIVE);
                                    MVar.playerReplaceMessage("simp.gm-other", "Criativo", player.getName(), target);
                                    MVar.playerReplaceMessage("simp.other-gm", target.getName(), "Criativo", player);
                                } else {
                                    MVar.playerMessage("server.no-perm", player);
                                }
                                break;
                            case "2":
                            case "a":
                            case "adventure":
                            case "aventura":
                                if (player.hasPermission("eternia.gamemode.2")) {
                                    target.setGameMode(GameMode.ADVENTURE);
                                    MVar.playerReplaceMessage("simp.gm-other", "Aventura", player.getName(), target);
                                    MVar.playerReplaceMessage("simp.other-gm", target.getName(), "Aventura", player);
                                } else {
                                    MVar.playerMessage("server.no-perm", player);
                                }
                                break;
                            case "3":
                            case "sp":
                            case "spectator":
                            case "spect":
                                if (player.hasPermission("eternia.gamemode.3")) {
                                    target.setGameMode(GameMode.SPECTATOR);
                                    MVar.playerReplaceMessage("simp.gm-other", "Espectador", player.getName(), target);
                                    MVar.playerReplaceMessage("simp.other-gm", target.getName(), "Espectador", player);
                                } else {
                                    MVar.playerMessage("server.no-perm", player);
                                }
                                break;
                            default:
                                MVar.playerMessage("simp.gmuse", player);
                                break;
                        }
                    } else {
                        MVar.playerMessage("server.player-offline", player);
                    }
                } else {
                    MVar.playerMessage("simp.gmuse", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
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
                            MVar.playerReplaceMessage("simp.gm-other", "Sobrevivência", "Console", target);
                            MVar.consoleReplaceMessage("simp.other-gm", target.getName(), "Sobrevivência");
                            break;
                        case "1":
                        case "c":
                        case "creative":
                        case "criativo":
                            target.setGameMode(GameMode.CREATIVE);
                            MVar.playerReplaceMessage("simp.gm-other", "Criativo", "Console", target);
                            MVar.consoleReplaceMessage("simp.other-gm", target.getName(), "Criativo");
                            break;
                        case "2":
                        case "a":
                        case "adventure":
                        case "aventura":
                            target.setGameMode(GameMode.ADVENTURE);
                            MVar.playerReplaceMessage("simp.gm-other", "Aventura", "Console", target);
                            MVar.consoleReplaceMessage("simp.other-gm", target.getName(), "Aventura");
                            break;
                        case "3":
                        case "sp":
                        case "spectator":
                        case "spect":
                            target.setGameMode(GameMode.SPECTATOR);
                            MVar.playerReplaceMessage("simp.gm-other", "Espectador", "Console", target);
                            MVar.consoleReplaceMessage("simp.other-gm", target.getName(), "Espectador");
                            break;
                        default:
                            MVar.consoleMessage("simp.gmuse");
                            break;
                    }
                } else {
                    MVar.consoleMessage("server.player-offline");
                }
            } else {
                MVar.consoleMessage("simp.gmuse");
            }
        }
        return true;
    }
}