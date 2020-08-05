package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.objects.UUIDFetcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatCommands extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final int money;

    public ChatCommands(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.money = EterniaServer.serverConfig.getInt("money.nick");

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_NICK), Strings.PLAYER_NAME, Strings.PLAYER_DISPLAY);
        temp.forEach(Vars.nickname::put);
        messages.sendConsole(Strings.MSG_LOAD_DATA, Constants.MODULE, "Nicks", Constants.AMOUNT, temp.size());

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
        messages.broadcastMessage(Strings.MSG_LEAVE, Constants.PLAYER, player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(plugin, player);
        }
    }

    @CommandAlias("unvanish|chupadadimensionalreversa")
    @CommandPermission("eternia.vanish")
    public void onUnVanish(Player player) {
        messages.broadcastMessage(Strings.MSG_JOIN, Constants.PLAYER, player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(plugin, player);
        }
    }

    @CommandAlias("ignore")
    @CommandPermission("eternia.ignore")
    public void onIgnore(Player player, OnlinePlayer targetOnline) {
        final Player target = targetOnline.getPlayer();
        if (target != null && target.isOnline()) {
            final String targetName = target.getName();
            List<Player> ignoreds;
            if (Vars.ignoredPlayer.get(targetName) == null) {
                ignoreds = new ArrayList<>();
            } else {
                ignoreds = Vars.ignoredPlayer.get(targetName);
                if (ignoreds.contains(player)) {
                    messages.sendMessage(Strings.M_CHAT_DENY, Constants.TARGET, target.getDisplayName(), player);
                    ignoreds.remove(player);
                    return;
                }
            }
            ignoreds.add(player);
            Vars.ignoredPlayer.put(target.getName(), ignoreds);
            messages.sendMessage(Strings.M_CHAT_IGNORE, Constants.TARGET, target.getDisplayName(), player);
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
                messages.sendMessage(Strings.MSG_NO_PERM, player);
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
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (Vars.nick.containsKey(uuid)) {
            if (APIEconomy.hasMoney(uuid, money)) {
                APIEconomy.removeMoney(uuid, money);
                player.setDisplayName(Vars.nick.get(uuid));
                messages.sendMessage(Strings.M_CHAT_NEWNICK, Constants.PLAYER, player.getDisplayName(), player);
                saveToSQL(playerName);
                Vars.nickname.put(playerName, Vars.nick.get(uuid));
            } else {
                messages.sendMessage(Strings.MSG_NO_MONEY, player);
            }
            Vars.nick.remove(uuid);
        } else {
            messages.sendMessage(Strings.M_CHAT_NO_CHANGE, player);
        }
    }

    @CommandAlias("nickdeny")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (Vars.nick.containsKey(uuid)) {
            Vars.nick.remove(uuid);
            messages.sendMessage(Strings.M_CHAT_NICK_DENY, player);
        } else {
            messages.sendMessage(Strings.M_CHAT_NO_CHANGE, player);
        }
    }

    @CommandAlias("resp|r|w|reply")
    @Syntax("<mensagem>")
    @CommandPermission("eternia.tell")
    public void onResp(Player sender, String[] msg) {
        final String playerName = sender.getName();
        if (Vars.tell.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(Vars.tell.get(playerName));
            if (target != null && target.isOnline()) {
                if (Vars.ignoredPlayer.get(playerName) != null && Vars.ignoredPlayer.get(playerName).contains(target)) {
                    messages.sendMessage(Strings.M_CHAT_IGNORED, sender);
                    return;
                }
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
        Player targetPlayer = target.getPlayer();
        if (Vars.ignoredPlayer.get(player.getName()) != null && Vars.ignoredPlayer.get(player.getName()).contains(targetPlayer)) {
            messages.sendMessage(Strings.M_CHAT_IGNORED, player);
            return;
        }
        sendPrivate(player, targetPlayer, getMessage(msg));
    }

    private void playerNick(final Player player, final String string) {
        final String playerName = player.getName();
        if (string.equals(Strings.CLEAR)) {
            player.setDisplayName(playerName);
            messages.sendMessage(Strings.M_CHAT_REMOVE_NICK, player);
        } else {
            Vars.nick.put(UUIDFetcher.getUUIDOf(playerName), string);
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
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (Vars.nickname.containsKey(playerName)) {
            EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_NICK, Strings.PLAYER_DISPLAY, Vars.nick.get(uuid), Strings.UUID, uuid.toString()));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_NICK, Strings.UUID, uuid.toString(), Strings.PLAYER_DISPLAY, Vars.nick.get(uuid)));
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
