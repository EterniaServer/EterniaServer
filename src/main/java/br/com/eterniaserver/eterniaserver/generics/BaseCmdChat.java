package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BaseCmdChat extends BaseCommand {

    private final EterniaServer plugin;
    private final int money;

    public BaseCmdChat(EterniaServer plugin) {
        this.plugin = plugin;
        this.money = EterniaServer.serverConfig.getInt("money.nick");
    }

    @CommandAlias("limparchat|chatclear|clearchat")
    @CommandPermission("eternia.clearchat")
    public void onClearChat() {
        for (int i = 0; i < 150; i ++) Bukkit.broadcastMessage("");
    }

    @CommandAlias("broadcast|advice|aviso")
    @CommandPermission("eternia.advice")
    public void onBroadcast(String message) {
        Bukkit.broadcastMessage(PluginMSGs.M_CHAT_ADVICE.replace(PluginConstants.ADVICE, PluginMSGs.getColor(message)));
    }

    @CommandAlias("vanish|chupadadimensional")
    @CommandPermission("eternia.vanish")
    public void onVanish(Player player) {
        Bukkit.broadcastMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_LEAVE));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(plugin, player);
        }
    }

    @CommandAlias("unvanish|chupadadimensionalreversa")
    @CommandPermission("eternia.vanish")
    public void onUnVanish(Player player) {
        Bukkit.broadcastMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_JOIN));
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
            if (PluginVars.ignoredPlayer.get(targetName) == null) {
                ignoreds = new ArrayList<>();
            } else {
                ignoreds = PluginVars.ignoredPlayer.get(targetName);
                if (ignoreds.contains(player)) {
                    player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.M_CHAT_DENY));
                    ignoreds.remove(player);
                    return;
                }
            }
            ignoreds.add(player);
            PluginVars.ignoredPlayer.put(target.getName(), ignoreds);
            player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.M_CHAT_IGNORE));
        }
    }

    @CommandAlias("spy|socialspy")
    @CommandPermission("eternia.spy")
    public void onSpy(Player player) {
        final String playerName = player.getName();
        final Boolean b = PluginVars.spy.getOrDefault(playerName, false);
        if (Boolean.TRUE.equals(b)) {
            PluginVars.spy.put(playerName, false);
            player.sendMessage(PluginMSGs.M_CHAT_SPY_D);
        } else {
            PluginVars.spy.put(playerName, true);
            player.sendMessage(PluginMSGs.M_CHAT_SPY_E);
        }
    }

    @CommandAlias("nickname|nick|name|apelido")
    @Syntax("<novo_nome> <jogador> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickname(Player player, String string, @Optional OnlinePlayer target) {
        string = string.replaceAll("[^a-zA-Z0-9]", "");
        if (!player.hasPermission("eternia.nickname.others")) {
            if (target == null) {
                playerNick(player, string);
            } else {
                player.sendMessage(PluginMSGs.MSG_NO_PERM);
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
        final PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);

        if (playerProfile.nickRequest) {
            if (APIEconomy.hasMoney(uuid, money)) {
                APIEconomy.removeMoney(uuid, money);
                player.setDisplayName(playerProfile.tempNick);
                player.sendMessage(UtilInternMethods.putName(player, PluginMSGs.M_CHAT_NEWNICK));
                playerProfile.playerDisplayName = playerProfile.tempNick;
            } else {
                player.sendMessage(PluginMSGs.MSG_NO_MONEY);
            }
            playerProfile.tempNick = null;
            playerProfile.nickRequest = false;
            PluginVars.playerProfile.put(uuid, playerProfile);
            saveToSQL(uuid);
        } else {
            player.sendMessage(PluginMSGs.M_CHAT_NO_CHANGE);
        }
    }

    @CommandAlias("nickdeny")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        final PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);

        if (playerProfile.nickRequest) {
            playerProfile.tempNick = null;
            playerProfile.nickRequest = false;
            PluginVars.playerProfile.put(uuid, playerProfile);
            player.sendMessage(PluginMSGs.M_CHAT_NICK_DENY);
        } else {
            player.sendMessage(PluginMSGs.M_CHAT_NO_CHANGE);
        }
    }

    @CommandAlias("resp|r|w|reply")
    @Syntax("<mensagem>")
    @CommandPermission("eternia.tell")
    public void onResp(Player sender, String msg) {
        if (isMuted(sender)) return;

        final String playerName = sender.getName();
        if (PluginVars.tell.containsKey(playerName) && msg != null) {
            final Player target = Bukkit.getPlayer(PluginVars.tell.get(playerName));
            if (target != null && target.isOnline()) {
                if (PluginVars.ignoredPlayer.get(playerName) != null && PluginVars.ignoredPlayer.get(playerName).contains(target)) {
                    sender.sendMessage(PluginMSGs.M_CHAT_IGNORE);
                    return;
                }
                UtilInternMethods.sendPrivate(sender, target, msg);
                return;
            }
        }
        sender.sendMessage(PluginMSGs.M_CHAT_R_NO);
    }

    @CommandAlias("tell|msg|whisper|emsg")
    @Syntax("<jogador> <mensagem>")
    @CommandCompletion("@players Oi.")
    @CommandPermission("eternia.tell")
    public void onTell(Player player, @Optional OnlinePlayer targets, @Optional String msg) {
        if (isMuted(player)) return;

        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (targets == null) {
            PluginVars.playerProfile.get(uuid).chatChannel = 0;
            player.sendMessage(PluginMSGs.M_CHAT_C.replace(PluginConstants.CHANNEL_NAME, "Local"));
            return;
        }

        if (PluginVars.chatLocked.containsKey(playerName)) {
            PluginVars.chatLocked.remove(playerName);
            player.sendMessage(PluginMSGs.MSG_CHAT_UNLOCKED);
            return;
        }

        final Player target = targets.getPlayer();

        if (msg == null || msg.length() == 0) {
            PluginVars.chatLocked.put(playerName, target.getName());
            PluginVars.playerProfile.get(uuid).chatChannel = 3;
            player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.MSG_CHAT_LOCKED));
            return;
        }

        if (PluginVars.ignoredPlayer.get(playerName) != null && PluginVars.ignoredPlayer.get(player.getName()).contains(target)) {
            player.sendMessage(PluginMSGs.M_CHAT_IGNORED);
            return;
        }

        UtilInternMethods.sendPrivate(player, target, msg);
    }

    private boolean isMuted(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        final long time = PluginVars.playerProfile.get(uuid).muted;
        if (UtilInternMethods.stayMuted(time)) {
            player.sendMessage(PluginMSGs.M_CHAT_MUTED.replace(PluginConstants.TIME, UtilInternMethods.getTimeLeft(time)));
            return true;
        }
        return false;
    }

    private void playerNick(final Player player, final String string) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (string.equals(PluginConstants.CLEAR_STR)) {
            player.setDisplayName(playerName);
            player.sendMessage(PluginMSGs.M_CHAT_REMOVE_NICK);
            return;
        }

        if (player.hasPermission("eternia.chat.color.nick")) {
            player.sendMessage(PluginMSGs.M_CHAT_NICK_MONEY.replace(PluginConstants.NEW_NAME, PluginMSGs.getColor(string)).replace(PluginConstants.AMOUNT, String.valueOf(money)));
        } else {
            player.sendMessage(PluginMSGs.M_CHAT_NICK_MONEY.replace(PluginConstants.NEW_NAME, string).replace(PluginConstants.AMOUNT, String.valueOf(money)));
        }

        final PlayerProfile playerProfile = PluginVars.playerProfile.get(uuid);

        playerProfile.tempNick = string;
        playerProfile.nickRequest = true;
        PluginVars.playerProfile.put(uuid, playerProfile);
        player.sendMessage(PluginMSGs.M_CHAT_NICK_MONEY_2);
    }

    private void staffNick(final OnlinePlayer target, final Player player, final String string) {
        if (target != null) {
            changeNickName(target.getPlayer(), player, string);
            return;
        }

        if (string.equals(PluginConstants.CLEAR_STR)) {
            final String playerName = player.getName();
            final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
            player.setDisplayName(playerName);
            PluginVars.playerProfile.get(uuid).playerDisplayName = playerName;
            player.sendMessage(PluginMSGs.M_CHAT_REMOVE_NICK);
            saveToSQL(uuid);
            return;
        }

        player.setDisplayName(PluginMSGs.getColor(string));
    }

    private void changeNickName(final Player target, final Player player, final String string) {
        final String targetName = target.getName();
        if (string.equals(PluginConstants.CLEAR_STR)) {
            target.setDisplayName(targetName);
            target.sendMessage(PluginMSGs.M_CHAT_REMOVE_NICK);
            player.sendMessage(PluginMSGs.M_CHAT_REMOVE_NICK);
        } else {
            target.setDisplayName(string);
            player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.M_CHAT_NEWNICK));
            target.sendMessage(UtilInternMethods.putName(target, PluginMSGs.M_CHAT_NEWNICK));
        }
    }

    private void saveToSQL(UUID uuid) {
        EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_PLAYER, PluginConstants.PLAYER_DISPLAY_STR, PluginVars.playerProfile.get(uuid).getPlayerDisplayName(), PluginConstants.UUID_STR, uuid.toString()));
    }

}