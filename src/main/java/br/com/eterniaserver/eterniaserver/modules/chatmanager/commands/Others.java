package br.com.eterniaserver.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Others extends BaseCommand {

    private final Messages messages;
    private final Strings strings;
    private final Vars vars;

    public Others(Messages messages, Strings strings, Vars vars) {
        this.messages = messages;
        this.strings = strings;
        this.vars = vars;
    }

    @CommandAlias("limparchat|chatclear|clearchat")
    @CommandPermission("eternia.clearchat")
    public void onClearChat() {
        for (int i = 0; i < 150; i ++) Bukkit.broadcastMessage("");
    }

    @CommandAlias("broadcast|advice|aviso")
    @CommandPermission("eternia.advice")
    public void onBroadcast(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        messages.BroadcastMessage("chat.global-advice", "%advice%", strings.getColor(sb.substring(0, sb.length() - 1)));
    }

    @CommandAlias("spy|socialspy")
    @CommandPermission("eternia.spy")
    public void onSpy(Player player) {
        if (vars.spy.getOrDefault(player, false)) {
            vars.spy.put(player, false);
            messages.sendMessage("chat.spyd", player);
        } else {
            vars.spy.put(player, true);
            messages.sendMessage("chat.spye", player);
        }
    }

    @CommandAlias("nickname|nick|name|apelido")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickname(Player sender, @Optional OnlinePlayer target, String string) {
        if (target != null) {
            if (string.equalsIgnoreCase("clear")) {
                target.getPlayer().setDisplayName(target.getPlayer().getName());
                messages.sendMessage("chat.remove-nick", target.getPlayer());
                messages.sendMessage("chat.remove-nick", sender);
            } else {
                target.getPlayer().setDisplayName(strings.getColor(string));
                messages.sendMessage("chat.newnick", "%player_display_name%", strings.getColor(string), sender);
                messages.sendMessage("chat.newnick", "%player_display_name%", strings.getColor(string), target.getPlayer());
            }
        } else {
            if (string.equalsIgnoreCase("clear")) {
                sender.setDisplayName(sender.getName());
                messages.sendMessage("chat.remove-nick", sender);
            } else {
                sender.setDisplayName(strings.getColor(string));
                messages.sendMessage("chat.newnick", "%player_display_name%", strings.getColor(string), sender);
            }
        }
    }

    @CommandAlias("resp|r|w|reply")
    @Syntax("<mensagem>")
    @CommandPermission("eternia.tell")
    public void onResp(Player sender, String[] msg) {
        try {
            Player target = Bukkit.getPlayer(vars.tell.get(sender.getName()));
            if (target != null && target.isOnline()) {
                StringBuilder sb = new StringBuilder();
                for (String arg : msg) sb.append(arg).append(" ");
                sb.substring(0, sb.length() - 1);
                String s = sb.toString();
                vars.tell.put(target.getName(), sender.getName());
                messages.sendMessage("chat.toplayer", "%player_name%", sender.getName(), "%target_name%", target.getName(), "%message%", s, sender);
                messages.sendMessage("chat.fromplayer", "%player_name%", target.getName(), "%target_name%", sender.getName(), "%message%", s, target);
                for (Player p : vars.spy.keySet())
                    if (vars.spy.get(p) && p != sender && p != target)
                        p.sendMessage(strings.getColor("&8[&7SPY-&6P&8] &8" + sender.getName() + "->" + target.getName() + ": " + s));
            } else {
                messages.sendMessage("chat.rnaote", sender);
            }
        } catch (Exception e) {
            messages.sendMessage("chat.rnaote", sender);
        }
    }

    @CommandAlias("tell|msg|whisper|emsg")
    @Syntax("<jogador> <mensagem>")
    @CommandCompletion("@players Oi.")
    @CommandPermission("eternia.tell")
    public void onTell(CommandSender player, OnlinePlayer target, String[] msg) {
        StringBuilder sb = new StringBuilder();
        for (String arg : msg) {
            sb.append(arg).append(" ");
        }
        sb.substring(0, sb.length() - 1);
        String s = sb.toString();
        vars.tell.put(target.getPlayer().getName(), player.getName());
        messages.sendMessage("chat.toplayer", "%player_name%", player.getName(), "%target_name%", target.getPlayer().getName(), "%message%", s, player);
        messages.sendMessage("chat.fromplayer", "%player_name%", target.getPlayer().getName(), "%target_name%", player.getName(), "%message%", s, target.getPlayer());
        for (Player p : vars.spy.keySet()) {
            if (vars.spy.get(p) && p != player && p != target.getPlayer()) {
                p.sendMessage(strings.getColor("&8[&7SPY-&6P&8] &8" + player.getName() + "->" + target.getPlayer().getName() + ": " + s));
            }
        }
    }

}
