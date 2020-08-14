package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.utils.TimeEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatCommands extends BaseCommand {

    private final EterniaServer plugin;
    private final int money;

    public ChatCommands(EterniaServer plugin) {
        this.plugin = plugin;
        this.money = EterniaServer.serverConfig.getInt("money.nick");

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.TABLE_NICK), Constants.UUID_STR, Constants.PLAYER_DISPLAY_STR);
        temp.forEach((k, v) -> {
            final UUID uuid = UUID.fromString(k);
            final PlayerProfile playerProfile = Vars.playerProfile.get(uuid);
            playerProfile.setPlayerDisplayName(v);
            Vars.playerProfile.put(uuid, playerProfile);
        });
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Nicks").replace(Constants.AMOUNT, String.valueOf(temp.size())));

    }

    @CommandAlias("limparchat|chatclear|clearchat")
    @CommandPermission("eternia.clearchat")
    public void onClearChat() {
        for (int i = 0; i < 150; i ++) Bukkit.broadcastMessage("");
    }

    @CommandAlias("broadcast|advice|aviso")
    @CommandPermission("eternia.advice")
    public void onBroadcast(String[] message) {
        Bukkit.broadcastMessage(Strings.M_CHAT_ADVICE.replace(Constants.ADVICE, Strings.getColor(getMessage(message))));
    }

    @CommandAlias("vanish|chupadadimensional")
    @CommandPermission("eternia.vanish")
    public void onVanish(Player player) {
        Bukkit.broadcastMessage(Strings.MSG_LEAVE.replace(Constants.PLAYER, player.getDisplayName()));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(plugin, player);
        }
    }

    @CommandAlias("unvanish|chupadadimensionalreversa")
    @CommandPermission("eternia.vanish")
    public void onUnVanish(Player player) {
        Bukkit.broadcastMessage(Strings.MSG_JOIN.replace(Constants.PLAYER, player.getDisplayName()));
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
                    player.sendMessage(Strings.M_CHAT_DENY.replace(Constants.TARGET, target.getDisplayName()));
                    ignoreds.remove(player);
                    return;
                }
            }
            ignoreds.add(player);
            Vars.ignoredPlayer.put(target.getName(), ignoreds);
            player.sendMessage(Strings.M_CHAT_IGNORE.replace(Constants.TARGET, target.getDisplayName()));
        }
    }

    @CommandAlias("spy|socialspy")
    @CommandPermission("eternia.spy")
    public void onSpy(Player player) {
        final String playerName = player.getName();
        final Boolean b = Vars.spy.getOrDefault(playerName, false);
        if (Boolean.TRUE.equals(b)) {
            Vars.spy.put(playerName, false);
            player.sendMessage(Strings.M_CHAT_SPY_D);
        } else {
            Vars.spy.put(playerName, true);
            player.sendMessage(Strings.M_CHAT_SPY_E);
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
                player.sendMessage(Strings.MSG_NO_PERM);
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
                player.sendMessage(Strings.M_CHAT_NEWNICK.replace(Constants.PLAYER, player.getDisplayName()));
                saveToSQL(playerName);
                final PlayerProfile playerProfile = Vars.playerProfile.get(uuid);
                playerProfile.setPlayerDisplayName(Vars.nick.get(uuid));
                Vars.playerProfile.put(uuid, playerProfile);
            } else {
                player.sendMessage(Strings.MSG_NO_MONEY);
            }
            Vars.nick.remove(uuid);
        } else {
            player.sendMessage(Strings.M_CHAT_NO_CHANGE);
        }
    }

    @CommandAlias("nickdeny")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (Vars.nick.containsKey(uuid)) {
            Vars.nick.remove(uuid);
            player.sendMessage();
            player.sendMessage(Strings.M_CHAT_NICK_DENY);
        } else {
            player.sendMessage(Strings.M_CHAT_NO_CHANGE);
        }
    }

    @CommandAlias("resp|r|w|reply")
    @Syntax("<mensagem>")
    @CommandPermission("eternia.tell")
    public void onResp(Player sender, String[] msg) {
        if (isMuted(sender)) return;

        final String playerName = sender.getName();
        if (Vars.tell.containsKey(playerName) && msg != null) {
            final Player target = Bukkit.getPlayer(Vars.tell.get(playerName));
            if (target != null && target.isOnline()) {
                if (Vars.ignoredPlayer.get(playerName) != null && Vars.ignoredPlayer.get(playerName).contains(target)) {
                    sender.sendMessage(Strings.M_CHAT_IGNORE);
                    return;
                }
                InternMethods.sendPrivate(sender, target, getMessage(msg));
                return;
            }
        }
        sender.sendMessage(Strings.M_CHAT_R_NO);
    }

    @CommandAlias("tell|msg|whisper|emsg")
    @Syntax("<jogador> <mensagem>")
    @CommandCompletion("@players Oi.")
    @CommandPermission("eternia.tell")
    public void onTell(Player player, OnlinePlayer targets, @Optional String[] msg) {
        if (isMuted(player)) return;

        final String playerName = player.getName();

        if (Vars.chatLocked.containsKey(playerName)) {
            Vars.chatLocked.remove(playerName);
            player.sendMessage(Strings.MSG_CHAT_UNLOCKED);
            return;
        }

        final Player target = targets.getPlayer();

        if (msg == null || msg.length == 0 || msg.length == 1) {
            Vars.chatLocked.put(playerName, target.getName());
            Vars.global.put(playerName, 3);
            player.sendMessage(Strings.MSG_CHAT_LOCKED.replace(Constants.TARGET, target.getDisplayName()));
            return;
        }

        if (Vars.ignoredPlayer.get(playerName) != null && Vars.ignoredPlayer.get(player.getName()).contains(target)) {
            player.sendMessage(Strings.M_CHAT_IGNORED);
            return;
        }

        InternMethods.sendPrivate(player, target, getMessage(msg));
    }

    private boolean isMuted(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        final long time = Vars.playerMuted.get(uuid);
        if (TimeEnum.HASCOOLDOWN.stayMuted(time)) {
            player.sendMessage(Strings.M_CHAT_MUTED.replace(Constants.TIME, TimeEnum.HASCOOLDOWN.getTimeLeft(time)));
            return true;
        }
        return false;
    }

    private void playerNick(final Player player, final String string) {
        final String playerName = player.getName();

        if (string.equals(Constants.CLEAR_STR)) {
            player.setDisplayName(playerName);
            player.sendMessage(Strings.M_CHAT_REMOVE_NICK);
            return;
        }

        if (player.hasPermission("eternia.chat.color.nick")) {
            player.sendMessage(Strings.M_CHAT_NICK_MONEY.replace(Constants.NEW_NAME, Strings.getColor(string)).replace(Constants.AMOUNT, String.valueOf(money)));
        } else {
            player.sendMessage(Strings.M_CHAT_NICK_MONEY.replace(Constants.NEW_NAME, string).replace(Constants.AMOUNT, String.valueOf(money)));
        }

        Vars.nick.put(UUIDFetcher.getUUIDOf(playerName), string);
        player.sendMessage(Strings.M_CHAT_NICK_MONEY_2);
    }

    private void staffNick(final OnlinePlayer target, final Player player, final String string) {
        if (target != null) {
            changeNickName(target.getPlayer(), player, string);
            return;
        }

        final String playerName = player.getName();
        if (string.equals(Constants.CLEAR_STR)) {
            final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
            final PlayerProfile playerProfile = Vars.playerProfile.get(uuid);
            player.setDisplayName(playerName);
            playerProfile.setPlayerDisplayName(playerName);
            Vars.playerProfile.put(uuid, playerProfile);
            player.sendMessage(Strings.M_CHAT_REMOVE_NICK);
            saveToSQL(playerName);
            return;
        }

        player.setDisplayName(Strings.getColor(string));
    }

    private void changeNickName(final Player target, final Player player, final String string) {
        final String targetName = target.getName();
        if (string.equals(Constants.CLEAR_STR)) {
            target.setDisplayName(targetName);
            target.sendMessage(Strings.M_CHAT_REMOVE_NICK);
            player.sendMessage(Strings.M_CHAT_REMOVE_NICK);
        } else {
            target.setDisplayName(Strings.getColor(string));
            player.sendMessage(Strings.M_CHAT_NEWNICK.replace(Constants.PLAYER, Strings.getColor(string)));
            target.sendMessage(Strings.M_CHAT_NEWNICK.replace(Constants.PLAYER, Strings.getColor(string)));
        }
    }

    private void saveToSQL(final String playerName) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (Vars.playerProfile.containsKey(uuid) && Vars.playerProfile.get(uuid).getPlayerDisplayName() != null) {
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_NICK, Constants.PLAYER_DISPLAY_STR, Vars.nick.get(uuid), Constants.UUID_STR, uuid.toString()));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_NICK, Constants.UUID_STR, uuid.toString(), Constants.PLAYER_DISPLAY_STR, Vars.nick.get(uuid)));
        }
    }

    private String getMessage(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        return sb.substring(0, sb.length() - 1);
    }

}
