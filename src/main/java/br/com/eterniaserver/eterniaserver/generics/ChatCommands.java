package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ChatCommands extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final int money;

    public ChatCommands(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.money = EterniaServer.serverConfig.getInt("money.nick");

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_NICK), Strings.PNAME, Strings.PDISPLAY);
        temp.forEach(Vars.nickname::put);
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Nicks", Constants.AMOUNT, temp.size());

    }

    @CommandAlias("limparchat|chatclear|clearchat")
    @CommandPermission("eternia.clearchat")
    public void onClearChat() {
        for (int i = 0; i < 150; i ++) Bukkit.broadcastMessage("");
    }

    @CommandAlias("broadcast|advice|aviso")
    @CommandPermission("eternia.advice")
    public void onBroadcast(String[] message) {
        messages.broadcastMessage(Strings.M_CHAT_ADVICE, Constants.ADVICE, messages.getColor(getMessage(message)));
    }

    @CommandAlias("vanish|chupadadimensional")
    @CommandPermission("eternia.vanish")
    public void onVanish(Player player) {
        messages.broadcastMessage(Strings.M_LEAVE, Constants.PLAYER, player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(plugin, player);
        }
    }

    @CommandAlias("unvanish|chupadadimensionalreversa")
    @CommandPermission("eternia.vanish")
    public void onUnVanish(Player player) {
        messages.broadcastMessage(Strings.M_JOIN, Constants.PLAYER, player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(plugin, player);
        }
    }

    @CommandAlias("spy|socialspy")
    @CommandPermission("eternia.spy")
    public void onSpy(Player player) {
        final String playerName = player.getName();
        final Boolean b = Vars.spy.getOrDefault(playerName, false);
        if (Boolean.TRUE.equals(b)) {
            Vars.spy.put(playerName, false);
            messages.sendMessage(Strings.M_CHAT_SPY_D, player);
        } else {
            Vars.spy.put(playerName, true);
            messages.sendMessage(Strings.M_CHAT_SPY_E, player);
        }
    }

    @CommandAlias("nickname|nick|name|apelido")
    @Syntax("<novo_nome> <jogador> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickname(Player player, String string, @Optional OnlinePlayer target) {
        if (!player.hasPermission("eternia.nickname.others")) {
            if (target == null) {
                playerNick(player, string);
            } else {
                messages.sendMessage(Strings.M_NO_PERM, player);
            }
        } else {
            staffNick(target, player, string);
        }
    }

    @CommandAlias("nickaccept")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickAccept(Player player) {
        final String playerName = player.getName();
        if (Vars.nick.containsKey(playerName)) {
            if (APIEconomy.hasMoney(playerName, money)) {
                APIEconomy.removeMoney(playerName, money);
                player.setDisplayName(Vars.nick.get(playerName));
                messages.sendMessage(Strings.M_CHAT_NEWNICK, Constants.PLAYER, player.getDisplayName(), player);
                saveToSQL(playerName);
                Vars.nickname.put(playerName, Vars.nick.get(playerName));
            } else {
                messages.sendMessage(Strings.M_NO_MONEY, player);
            }
            Vars.nick.remove(playerName);
        } else {
            messages.sendMessage(Strings.M_CHAT_NO_CHANGE, player);
        }
    }

    @CommandAlias("nickdeny")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickDeny(Player player) {
        final String playerName = player.getName();
        if (Vars.nick.containsKey(playerName)) {
            Vars.nick.remove(playerName);
            messages.sendMessage(Strings.M_CHAT_NICK_DENY, player);
        } else {
            messages.sendMessage(Strings.M_CHAT_NO_CHANGE, player);
        }
    }

    @CommandAlias("resp|r|w|reply")
    @Syntax("<mensagem>")
    @CommandPermission("eternia.tell")
    public void onResp(Player sender, String[] msg) {
        if (Vars.tell.containsKey(sender.getName())) {
            final Player target = Bukkit.getPlayer(Vars.tell.get(sender.getName()));
            if (target != null && target.isOnline()) {
                sendPrivate(sender, target, getMessage(msg));
                return;
            }
        }
        messages.sendMessage(Strings.M_CHAT_R_NO, sender);
    }

    @CommandAlias("tell|msg|whisper|emsg")
    @Syntax("<jogador> <mensagem>")
    @CommandCompletion("@players Oi.")
    @CommandPermission("eternia.tell")
    public void onTell(Player player, OnlinePlayer target, String[] msg) {
        if (msg == null) msg = "Oi".split("x");
        sendPrivate(player, target.getPlayer(), getMessage(msg));
    }

    private void playerNick(final Player player, final String string) {
        final String playerName = player.getName();
        if (string.equals(Strings.CLEAR)) {
            player.setDisplayName(playerName);
            messages.sendMessage(Strings.M_CHAT_REMOVE_NICK, player);
        } else {
            Vars.nick.put(playerName, string);
            if (player.hasPermission("eternia.chat.color.nick")) {
                messages.sendMessage(Strings.M_CHAT_NICK_MONEY, Constants.NEW_NAME, ChatColor.translateAlternateColorCodes('&', string), player);
            } else {
                messages.sendMessage(Strings.M_CHAT_NICK_MONEY, Constants.NEW_NAME, string, player);
            }
            messages.sendMessage(Strings.M_CHAT_NICK_MONEY_2, player);
        }
    }

    private void staffNick(final OnlinePlayer target, final Player player, final String string) {
        if (target == null) {
            final String playerName = player.getName();
            if (string.equals(Strings.CLEAR)) {
                player.setDisplayName(playerName);
                saveToSQL(playerName);
                Vars.nickname.put(playerName, playerName);
                messages.sendMessage(Strings.M_CHAT_REMOVE_NICK, player);
            } else {
                player.setDisplayName(messages.getColor(string));
            }
        } else {
            changeNickName(target.getPlayer(), player, string);
        }
    }

    private void changeNickName(final Player target, final Player player, final String string) {
        final String targetName = target.getName();
        if (string.equals(Strings.CLEAR)) {
            target.setDisplayName(targetName);
            messages.sendMessage(Strings.M_CHAT_REMOVE_NICK, target);
            messages.sendMessage(Strings.M_CHAT_REMOVE_NICK, player);
        } else {
            target.setDisplayName(messages.getColor(string));
            messages.sendMessage(Strings.M_CHAT_NEWNICK, Constants.PLAYER, messages.getColor(string), player);
            messages.sendMessage(Strings.M_CHAT_NEWNICK, Constants.PLAYER, messages.getColor(string), target);
        }
    }

    private void saveToSQL(final String playerName) {
        if (Vars.nickname.containsKey(playerName)) {
            EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_NICK, Strings.PDISPLAY, Vars.nick.get(playerName), Strings.PNAME, playerName));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_NICK, Strings.PNAME, playerName, Strings.PDISPLAY, Vars.nick.get(playerName)));
        }
    }

    private void sendPrivate(final Player player, final Player target, final String s) {
        final String playerDisplay = player.getDisplayName();
        final String targetDisplay = target.getDisplayName();
        Vars.tell.put(target.getName(), player.getName());
        player.sendMessage(messages.getMessage(Strings.M_CHAT_TO).
                replace(Constants.PLAYER, playerDisplay).
                replace(Constants.TARGET, targetDisplay).
                replace(Constants.MESSAGE, s));
        target.sendMessage(messages.getMessage(Strings.M_CHAT_FROM).
                replace(Constants.PLAYER, targetDisplay).
                replace(Constants.TARGET, playerDisplay).
                replace(Constants.MESSAGE, s));
        for (String p : Vars.spy.keySet()) {
            final Boolean b = Vars.spy.getOrDefault(p, false);
            if (Boolean.TRUE.equals(b) && !p.equals(player.getName()) && !p.equals(target.getName())) {
                final Player spyPlayer = Bukkit.getPlayerExact(p);
                if (spyPlayer != null && spyPlayer.isOnline()) {
                    spyPlayer.sendMessage(messages.getColor("&8[&7SPY-&6P&8] &8" + playerDisplay + " -> " + targetDisplay + ": " + s));
                } else {
                    Vars.spy.remove(p);
                }
            }
        }
    }

    private String getMessage(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        return sb.substring(0, sb.length() - 1);
    }

}
