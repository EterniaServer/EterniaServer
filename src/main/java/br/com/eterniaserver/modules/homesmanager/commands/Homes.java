package br.com.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.API.HomesAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Homes implements CommandExecutor {

    private final EterniaServer plugin;

    public Homes(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.homes")) {
                if (args.length == 1) {
                    if (player.hasPermission("eternia.homes.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                StringBuilder accounts = new StringBuilder();
                                String[] values = HomesAPI.getHomes(target.getName());
                                for (String line : values) {
                                    accounts.append(line).append("&8, &3");
                                }
                                Messages.PlayerMessage("home.list", "%homes%", Strings.getColor(accounts.toString()), player);
                            });
                        } else {
                            Messages.PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        Messages.PlayerMessage("server.no-perm", player);
                    }
                } else {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        StringBuilder accounts = new StringBuilder();
                        String[] values = HomesAPI.getHomes(player.getName());
                        for (String line : values) {
                            accounts.append(line).append("&8, &3");
                        }
                        Messages.PlayerMessage("home.list", "%homes%", Strings.getColor(accounts.toString()), player);
                    });
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
