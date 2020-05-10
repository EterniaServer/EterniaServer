package br.com.eterniaserver.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.HomesManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Homes implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final HomesManager homesManager;
    private final Strings strings;

    public Homes(EterniaServer plugin, Messages messages, HomesManager homesManager, Strings strings) {
        this.plugin = plugin;
        this.messages = messages;
        this.homesManager = homesManager;
        this.strings = strings;
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
                                String[] values = homesManager.getHomes(target.getName());
                                for (String line : values) {
                                    accounts.append(line).append("&8, &3");
                                }
                                messages.PlayerMessage("home.list", "%homes%", strings.getColor(accounts.toString()), player);
                            });
                        } else {
                            messages.PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        messages.PlayerMessage("server.no-perm", player);
                    }
                } else {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        StringBuilder accounts = new StringBuilder();
                        String[] values = homesManager.getHomes(player.getName());
                        for (String line : values) {
                            accounts.append(line).append("&8, &3");
                        }
                        messages.PlayerMessage("home.list", "%homes%", strings.getColor(accounts.toString()), player);
                    });
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
