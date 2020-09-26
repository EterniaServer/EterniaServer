package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Configs;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.generics.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Chat extends BaseCommand {

    private final EterniaServer plugin;

    public Chat(EterniaServer plugin) {
        this.plugin = plugin;
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
            if (!APIPlayer.hasIgnoreds(targetName)) {
                ignoreds = new ArrayList<>();
            } else {
                ignoreds = APIPlayer.getIgnoreds(targetName);
                if (ignoreds.contains(player)) {
                    player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.M_CHAT_DENY));
                    ignoreds.remove(player);
                    return;
                }
            }
            ignoreds.add(player);
            APIPlayer.putIgnored(targetName, ignoreds);
            player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.M_CHAT_IGNORE));
        }
    }

    @CommandAlias("spy|socialspy")
    @CommandPermission("eternia.spy")
    public void onSpy(Player player) {
        final String playerName = player.getName();
        if (APIServer.isSpying(playerName)) {
            APIServer.disableSpy(playerName);
            player.sendMessage(PluginMSGs.M_CHAT_SPY_D);
        } else {
            APIServer.putSpy(playerName);
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
                APIPlayer.playerNick(player, string);
            } else {
                player.sendMessage(PluginMSGs.MSG_NO_PERM);
            }
        } else {
            APIPlayer.staffNick(target, player, string);
        }
    }

    @CommandAlias("nickaccept")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickAccept(Player player) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (APIPlayer.hasNickRequest(uuid)) {
            if (APIEconomy.hasMoney(uuid, Configs.instance.nickCost)) {
                APIEconomy.removeMoney(uuid, Configs.instance.nickCost);
                APIPlayer.updateNickName(player, uuid);
            } else {
                player.sendMessage(PluginMSGs.MSG_NO_MONEY);
            }
            APIPlayer.removeNickRequest(uuid);
        } else {
            player.sendMessage(PluginMSGs.M_CHAT_NO_CHANGE);
        }
    }

    @CommandAlias("nickdeny")
    @Syntax("<jogador> <novo_nome> ou <novo_nome>")
    @CommandPermission("eternia.nickname")
    public void onNickDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (APIPlayer.hasNickRequest(uuid)) {
            APIPlayer.removeNickRequest(uuid);
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
        if (APIPlayer.receivedTell(playerName) && msg != null) {
            final Player target = Bukkit.getPlayer(APIPlayer.getTellSender(playerName));
            if (target != null && target.isOnline()) {
                if (APIPlayer.hasIgnoreds(playerName) && APIPlayer.areIgnored(playerName, target)) {
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
            APIPlayer.setChannel(uuid, 0);
            player.sendMessage(PluginMSGs.M_CHAT_C.replace(PluginConstants.CHANNEL_NAME, "Local"));
            return;
        }

        if (APIPlayer.isTell(playerName)) {
            APIPlayer.removeTelling(playerName);
            player.sendMessage(PluginMSGs.MSG_CHAT_UNLOCKED);
            return;
        }

        final Player target = targets.getPlayer();

        if (msg == null || msg.length() == 0) {
            APIPlayer.setTelling(playerName, target.getName());
            APIPlayer.setChannel(uuid, 3);
            player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.MSG_CHAT_LOCKED));
            return;
        }

        if (APIPlayer.hasIgnoreds(playerName) && APIPlayer.areIgnored(player.getName(), target)) {
            player.sendMessage(PluginMSGs.M_CHAT_IGNORED);
            return;
        }

        UtilInternMethods.sendPrivate(player, target, msg);
    }

    private boolean isMuted(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        final long time = APIPlayer.getMutedTime(uuid);
        if (UtilInternMethods.stayMuted(time)) {
            player.sendMessage(PluginMSGs.M_CHAT_MUTED.replace(PluginConstants.TIME, UtilInternMethods.getTimeLeft(time)));
            return true;
        }
        return false;
    }

}
