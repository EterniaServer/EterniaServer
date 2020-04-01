package com.eterniaserver.commands.experience;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.api.ExpAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositLevel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.depositlvl")) {
                if (args.length == 1) {
                    int xp_atual = player.getLevel();
                    try {
                        int xpla = Integer.parseInt(args[0]);
                        if (xp_atual >= xpla) {
                            int xp = xp_atual;
                            if (xp >= 1 && xp <= 15) {
                                xp = (xp * xp) + (6 * xp);
                            } else if (xp >= 16 && xp <= 30) {
                                xp = (int) ((2.5 * (xp * xp)) - (40.5 * xp) + 360);
                            } else {
                                xp = (int) ((4.5 * (xp * xp)) - (162.5 * xp) + 2220);
                            }
                            ExpAPI.addExp(player.getName(), xp);
                            MVar.playerReplaceMessage("xp.deposit", xpla, player);
                            player.setLevel(Math.max(player.getLevel() - xpla, 0));
                        } else {
                            MVar.playerMessage("xp.noxp", player);
                        }
                    } catch (NumberFormatException e) {
                        MVar.playerMessage("server.no-number", player);
                    }
                } else {
                    MVar.playerMessage("xp.use", player);
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