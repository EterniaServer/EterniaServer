package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommands extends BaseCommand {

    private final EFiles messages;
    private final EconomyManager moneyx;
    private final int money;

    public ChatCommands(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
        this.moneyx = plugin.getMoney();
        this.money = plugin.serverConfig.getInt("money.nick");
    }

    @CommandAlias("limparchat|chatclear|clearchat")
    @CommandPermission("eternia.clearchat")
    public void onClearChat() {
        for (int i = 0; i < 150; i ++) Bukkit.broadcastMessage("");
    }

    @CommandAlias("broadcast|advice|aviso")
    @CommandPermission("eternia.advice")
    public void onBroadcast(String[] message) {
        messages.broadcastMessage("chat.global-advice", "%advice%", messages.getColor(getMessage(message)));
    }

    @CommandAlias("spy|socialspy")
    @CommandPermission("eternia.spy")
    public void onSpy(Player player) {
        if (Vars.spy.getOrDefault(player, false)) {
            Vars.spy.put(player, false);
            messages.sendMessage("chat.spyd", player);
        } else {
            Vars.spy.put(player, true);
            messages.sendMessage("chat.spye", player);
        }
    }

    @CommandAlias("nickname|nick|name|apelido")
    @Syntax("<novo_nome> <jogador> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickname(Player player, String string, @Optional OnlinePlayer target) {
        if (!player.hasPermission("eternia.nickname.others")) {
            if (target == null) {
                final String playerName = player.getName();
                if (string.equals("clear")) {
                    player.setDisplayName(playerName);
                    messages.sendMessage("chat.remove-nick", player);
                } else {
                    Vars.nick.put(playerName, string);
                    messages.sendMessage("chat.nick-money", "%new_name%", string, player);
                    messages.sendMessage("chat.nick-money-2", player);
                }
            } else {
                messages.sendMessage("server.no-perm", player);
            }
        } else {
            if (target == null) {
                final String playerName = player.getName();
                if (string.equals("clear")) {
                    player.setDisplayName(playerName);
                    messages.sendMessage("chat.remove-nick", player);
                } else {
                    player.setDisplayName(messages.getColor(string));
                }
            } else {
                final Player targetP = target.getPlayer();
                final String targetName = targetP.getName();
                if (string.equals("clear")) {
                    targetP.setDisplayName(targetName);
                    messages.sendMessage("chat.remove-nick", targetP);
                    messages.sendMessage("chat.remove-nick", player);
                } else {
                    targetP.setDisplayName(messages.getColor(string));
                    messages.sendMessage("chat.newnick", "%player_display_name%", messages.getColor(string), player);
                    messages.sendMessage("chat.newnick", "%player_display_name%", messages.getColor(string), targetP);
                }
            }
        }
    }

    @CommandAlias("nickaccept")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickAccept(Player player) {
        final String playerName = player.getName();
        if (Vars.nick.containsKey(playerName)) {
            if (moneyx.hasMoney(playerName, money)) {
                moneyx.removeMoney(playerName, money);
                player.setDisplayName(Vars.nick.get(playerName));
                messages.sendMessage("chat.newnick", "%player_display_name%", player.getDisplayName(), player);
            } else {
                messages.sendMessage("no-money", player);
            }
            Vars.nick.remove(playerName);
        } else {
            messages.sendMessage("chat.no-change", player);
        }
    }

    @CommandAlias("nickdeny")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickDeny(Player player) {
        final String playerName = player.getName();
        if (Vars.nick.containsKey(playerName)) {
            Vars.nick.remove(playerName);
            messages.sendMessage("chat.nick-deny", player);
        } else {
            messages.sendMessage("chat.no-change", player);
        }
    }

    @CommandAlias("resp|r|w|reply")
    @Syntax("<mensagem>")
    @CommandPermission("eternia.tell")
    public void onResp(Player sender, String[] msg) {
        final Player target = Bukkit.getPlayer(Vars.tell.get(sender.getName()));
        if (target != null && target.isOnline()) {
            sendPrivate(sender, target.getPlayer(), getMessage(msg));
        } else {
            messages.sendMessage("chat.rnaote", sender);
        }
    }

    @CommandAlias("tell|msg|whisper|emsg")
    @Syntax("<jogador> <mensagem>")
    @CommandCompletion("@players Oi.")
    @CommandPermission("eternia.tell")
    public void onTell(CommandSender player, OnlinePlayer target, String[] msg) {
        sendPrivate(player, target.getPlayer(), getMessage(msg));
    }

    private void sendPrivate(final CommandSender player, final Player target, final String s) {
        final String targetName = target.getName();
        final String playerName = player.getName();
        Vars.tell.put(targetName, playerName);
        messages.sendMessage("chat.toplayer", "%player_name%", playerName, "%target_name%", targetName, "%message%", s, player);
        messages.sendMessage("chat.fromplayer", "%player_name%", targetName, "%target_name%", playerName, "%message%", s, target);
        for (Player p : Vars.spy.keySet()) {
            if (Vars.spy.get(p) && p != player && p != target) {
                p.sendMessage(messages.getColor("&8[&7SPY-&6P&8] &8" + playerName + "->" + targetName + ": " + s));
            }
        }
    }

    private String getMessage(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        return sb.substring(0, sb.length() - 1);
    }

}
