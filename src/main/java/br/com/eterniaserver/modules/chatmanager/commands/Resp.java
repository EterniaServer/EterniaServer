package br.com.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.configs.Vars;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Resp implements CommandExecutor {

    private final EterniaServer plugin;

    public Resp(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tell")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayer(Vars.tell.get(player.getName()));
                    if (target != null && target.isOnline()) {
                        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                            StringBuilder sb = new StringBuilder();
                            for (String arg : args) {
                                sb.append(arg).append(" ");
                            }
                            sb.substring(0, sb.length() - 1);
                            String s = sb.toString();
                            Vars.tell.put(target.getName(), player.getName());
                            Messages.PlayerMessage("chat.toplayer", "%player_name%", player.getName(), "%target_name%", target.getName(), "%message%", s, player, false);
                            Messages.PlayerMessage("chat.fromplayer", "%player_name%", target.getName(), "%target_name%", player.getName(), "%message%", s, target, false);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.hasPermission("eternia.spy") && p != player && p != target) {
                                    p.sendMessage(Strings.getColor("&8[&7SPY-&6P&8] &7" + player.getName() + "->" + target.getName() + ": " + s));
                                }
                            }
                        });
                    } else {
                        Messages.PlayerMessage("chat.rnaote", player);
                    }
                } else {
                    Messages.PlayerMessage("chat.ruse", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}