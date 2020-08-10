package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeleportSystem extends BaseCommand {

    private final double backMoney = EterniaServer.serverConfig.getInt("money.back");


    @CommandAlias("tpall|teleportall")
    @CommandPermission("eternia.tpall")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) if (other != player) other.teleport(player);
        player.sendMessage(Strings.MSG_TELEPORT_ALL);
    }

    @CommandAlias("tpaccept|teleportaccept")
    @CommandPermission("eternia.tpa")
    public void onTeleportAccept(Player player) {
        final String playerName = player.getName();
        if (Vars.tpaRequests.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(Vars.tpaRequests.get(playerName));
            if (target != null) {
                target.sendMessage(Strings.MSG_TELEPORT_ACCEPT.replace(Constants.TARGET, player.getDisplayName()));
                Vars.teleports.put(target, new PlayerTeleport(target, player.getLocation(), Strings.MSG_TELEPORT_DONE));
            }
            Vars.tpaTime.remove(playerName);
            Vars.tpaRequests.remove(playerName);
        } else {
            player.sendMessage(Strings.MSG_TELEPORT_NO_REQUEST);
        }
    }

    @CommandAlias("tpdeny|teleportdeny")
    @CommandPermission("eternia.tpa")
    public void onTeleportDeny(Player player) {
        final String playerName = player.getName();
        if (Vars.tpaRequests.containsKey(playerName)) {
            player.sendMessage(Strings.MSG_TELEPORT_DENY.replace(Constants.TARGET, Vars.tpaRequests.get(playerName)));
            final Player target = Bukkit.getPlayer(Vars.tpaRequests.get(playerName));
            Vars.tpaRequests.remove(playerName);
            Vars.tpaTime.remove(playerName);
            if (target != null && target.isOnline()) target.sendMessage(Strings.MSG_TELEPORT_DENIED);
        } else {
            player.sendMessage(Strings.MSG_TELEPORT_NO_REQUEST);
        }
    }

    @CommandAlias("tpa|teleportoplayer")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.tpa")
    public void onTeleportToPlayer(Player player, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        if (Vars.teleports.containsKey(player)) {
            player.sendMessage(Strings.MSG_IN_TELEPORT);
        } else {
            if (targetP != player) {
                final String playerName = player.getName();
                final String targetName = targetP.getName();
                if (!Vars.tpaRequests.containsKey(targetName)) {
                    Vars.tpaRequests.remove(targetName);
                    Vars.tpaRequests.put(targetName, playerName);
                    Vars.tpaTime.put(targetName, System.currentTimeMillis());
                    targetP.sendMessage(Strings.MSG_TELEPORT_ACCEPT.replace(Constants.TARGET, player.getDisplayName()));
                    player.sendMessage(Strings.MSG_TELEPORT_SENT.replace(Constants.TARGET, targetP.getDisplayName()));
                } else {
                    player.sendMessage(Strings.MSG_TELEPORT_EXISTS);
                }
            } else {
                player.sendMessage(Strings.MSG_TELEPORT_YOURSELF);
            }
        }
    }

    @CommandAlias("back|dback")
    @CommandPermission("eternia.back")
    public void onBack(Player player) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (Vars.back.containsKey(playerName)) {
            if ((player.hasPermission("eternia.backfree") && canBack(player)) || (!(EterniaServer.serverConfig.getBoolean("modules.economy")) && canBack(player))) {
                Vars.teleports.put(player, new PlayerTeleport(player, Vars.back.get(playerName), Strings.MSG_BACK_FREE));
            } else if (APIEconomy.getMoney(uuid) >= backMoney && canBack(player)) {
                APIEconomy.removeMoney(uuid, backMoney);
                Vars.teleports.put(player, new PlayerTeleport(player, Vars.back.get(playerName), Strings.MSG_BACK_COST));
            } else if (canBack(player)){
                player.sendMessage(Strings.MSG_NO_MONEY.replace(Constants.VALUE, String.valueOf(backMoney)));
            }
        } else {
            player.sendMessage(Strings.MSG_BACK_NO_TELEPORT);
        }
    }

    private boolean canBack(final Player player) {
        if (Vars.teleports.containsKey(player)) {
            player.sendMessage(Strings.MSG_IN_TELEPORT);
            return false;
        }
        return true;
    }

}
