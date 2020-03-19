package eternia.commands.others;

import eternia.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.fly")) {
                if (args.length == 0) {
                    if (!(player.getWorld() == Bukkit.getWorld("evento"))) {
                        if (player.hasPermission("eternia.fly.evento")) {
                            if (player.getAllowFlight()) {
                                player.setAllowFlight(false);
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                                MVar.playerMessage("desativar-voar", player);
                            } else {
                                player.setAllowFlight(true);
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                                MVar.playerMessage("ativar-voar", player);
                            }
                        } else {
                            MVar.playerMessage("sem-permissao", player);
                        }
                    } else {
                        if (player.getAllowFlight()) {
                            player.setAllowFlight(false);
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                            MVar.playerMessage("desativar-voar", player);
                        } else {
                            player.setAllowFlight(true);
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                            MVar.playerMessage("ativar-voar", player);
                        }
                    }
                    return true;
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.fly.other")) {
                        String targetS = args[0];
                        Player target = Bukkit.getPlayer(targetS);
                        assert target != null;
                        if (target.isOnline()) {
                            if (target.getAllowFlight()) {
                                target.setAllowFlight(false);
                                target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                                MVar.playerReplaceMessage("desativaram-voar", "console", target);
                                MVar.consoleReplaceMessage("desativar-voar-de", target.getName());
                            } else {
                                target.setAllowFlight(true);
                                target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                                MVar.playerReplaceMessage("ativaram-voar", "console", target);
                                MVar.consoleReplaceMessage("ativar-voar-de", target.getName());
                            }
                        } else {
                            MVar.playerMessage("jogador-offline", player);
                        }
                    } else {
                        MVar.playerMessage("sem-permissao", player);
                    }
                } else {
                    MVar.playerMessage("fly-use", player);
                }
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else if (args.length == 1) {
            String targetS = args[0];
            Player target = Bukkit.getPlayer(targetS);
            assert target != null;
            if (target.isOnline()) {
                if (target.getAllowFlight()) {
                    target.setAllowFlight(false);
                    target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                    MVar.playerReplaceMessage("desativaram-voar", "console", target);
                    MVar.consoleReplaceMessage("desativar-voar-de", target.getName());
                } else {
                    target.setAllowFlight(true);
                    target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                    MVar.playerReplaceMessage("ativaram-voar", "console", target);
                    MVar.consoleReplaceMessage("ativar-voar-de", target.getName());
                }
            } else {
                MVar.consoleMessage("jogador-offline");
            }
        }
        return true;
    }
}