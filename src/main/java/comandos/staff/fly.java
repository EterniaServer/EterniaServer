package comandos.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.comandos.staff.fly")) {
                if (args.length == 0) {
                    if(player.getAllowFlight()) {
                        player.setAllowFlight(false);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Voar desativado" +
                                ChatColor.DARK_GRAY + ".");
                        return true;
                    } else {
                        player.setAllowFlight(true);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Voar ativado" +
                                ChatColor.DARK_GRAY + ".");
                        return true;
                    }
                }
                else if (args.length == 1) {
                    if (player.hasPermission("eternia.comandos.staff.fly.other")) {
                        String targetS = args[0];
                        Player target = Bukkit.getPlayer(targetS);
                        assert target != null;
                        if(target.isOnline()) {
                            if (target.getAllowFlight()) {
                                target.setAllowFlight(false);
                                target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                                target.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Voar desativado por " +
                                        player.getName() + ChatColor.DARK_GRAY + ".");
                                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você desativou o voar de " +
                                        ChatColor.DARK_AQUA + target.getName() + ChatColor.DARK_GRAY + ".");
                                return true;
                            } else {
                                target.setAllowFlight(true);
                                target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                                target.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Voar ativado por " +
                                        player.getName() + ChatColor.DARK_GRAY + ".");
                                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você ativou o voar de " +
                                        ChatColor.DARK_AQUA + target.getName() + ChatColor.DARK_GRAY + ".");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "O jogador não está online" +
                                    ChatColor.DARK_GRAY + ".");
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem permissão para isso" +
                                ChatColor.DARK_GRAY + ".");
                        return true;
                    }
                }
            } else {
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem permissão para isso" +
                        ChatColor.DARK_GRAY + ".");
                return true;
            }
        }
        return false;
    }
}