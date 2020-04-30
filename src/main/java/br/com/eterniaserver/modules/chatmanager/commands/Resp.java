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

import java.util.Set;

public class Resp implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Strings strings;
    private final Vars vars;

    public Resp(EterniaServer plugin, Messages messages, Strings strings, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tell")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayer(vars.tell.get(player.getName()));
                    if (target != null && target.isOnline()) {
                        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                            StringBuilder sb = new StringBuilder();
                            for (String arg : args) {
                                sb.append(arg).append(" ");
                            }
                            sb.substring(0, sb.length() - 1);
                            String s = sb.toString();
                            vars.tell.put(target.getName(), player.getName());
                            messages.PlayerMessage("chat.toplayer", "%player_name%", player.getName(), "%target_name%", target.getName(), "%message%", s, player, false);
                            messages.PlayerMessage("chat.fromplayer", "%player_name%", target.getName(), "%target_name%", player.getName(), "%message%", s, target, false);
                            for (Player p : vars.spy.keySet()) {
                                if (vars.spy.get(p) && p != player && p != target) {
                                    p.sendMessage(strings.getColor("&8[&7SPY-&6P&8] &7" + player.getName() + "->" + target.getName() + ": " + s));
                                }
                            }
                        });
                    } else {
                        messages.PlayerMessage("chat.rnaote", player);
                    }
                } else {
                    messages.PlayerMessage("chat.ruse", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}