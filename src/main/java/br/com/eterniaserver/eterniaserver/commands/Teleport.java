package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.*;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Teleport extends BaseCommand {

    @CommandAlias("tpall|teleportall")
    @CommandPermission("eternia.tpall")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) if (other != player) other.teleport(player);
        Bukkit.broadcastMessage(EterniaServer.msg.getMessage(Messages.TELEPORT_ALL_PLAYERS, true, player.getName(), player.getDisplayName()));
    }

    @CommandAlias("tpaccept|teleportaccept")
    @CommandPermission("eternia.tpa")
    public void onTeleportAccept(Player player) {
        final String playerName = player.getName();
        if (APIPlayer.hasTpaRequest(playerName)) {
            final Player target = Bukkit.getPlayer(APIPlayer.getTpaSender(playerName));
            if (target != null) {
                APIServer.putInTeleport(target, new PlayerTeleport(target, player.getLocation(), EterniaServer.msg.getMessage(Messages.TELEPORT_GOING_TO_PLAYER, true, playerName, player.getDisplayName())));
                EterniaServer.msg.sendMessage(target, Messages.TELEPORT_TARGET_ACCEPT, playerName, player.getDisplayName());
                EterniaServer.msg.sendMessage(player, Messages.TELEPORT_ACCEPT, target.getName(), target.getDisplayName());
            } else {
                EterniaServer.msg.sendMessage(player, Messages.TELEPORT_TARGET_OFFLINE);
            }
            APIPlayer.removeTpaRequest(playerName);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.TELEPORT_NOT_REQUESTED);
        }
    }

    @CommandAlias("tpdeny|teleportdeny")
    @CommandPermission("eternia.tpa")
    public void onTeleportDeny(Player player) {
        final String playerName = player.getName();
        final Player target = Bukkit.getPlayer(UUIDFetcher.getUUIDOf(APIPlayer.getTpaSender(playerName)));
        if (APIPlayer.hasTpaRequest(playerName)) {
            if (target != null && target.isOnline()) {
                EterniaServer.msg.sendMessage(target, Messages.TELEPORT_TARGET_DENIED, playerName, player.getDisplayName());
                EterniaServer.msg.sendMessage(player, Messages.TELEPORT_DENIED, target.getName(), target.getDisplayName());
            } else {
                EterniaServer.msg.sendMessage(player, Messages.TELEPORT_TARGET_OFFLINE);
            }
            APIPlayer.removeTpaRequest(playerName);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.TELEPORT_NOT_REQUESTED);
        }
    }

    @CommandAlias("tpa|teleportoplayer")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.tpa")
    public void onTeleportToPlayer(Player player, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        if (APIPlayer.isTeleporting(player)) {
            EterniaServer.msg.sendMessage(player, Messages.SERVER_IN_TELEPORT);
        } else {
            if (targetP != player) {
                final String playerName = player.getName();
                final String targetName = targetP.getName();
                if (!APIPlayer.hasTpaRequest(targetName)) {
                    APIPlayer.putTpaRequest(targetName, playerName);
                    EterniaServer.msg.sendMessage(targetP, Messages.TELEPORT_RECEIVED, playerName, player.getDisplayName());
                    EterniaServer.msg.sendMessage(player, Messages.TELEPORT_SENT, targetName, targetP.getDisplayName());
                } else {
                    EterniaServer.msg.sendMessage(player, Messages.TELEPORT_ALREADY_REQUESTED, targetName, targetP.getDisplayName());
                }
            } else {
                EterniaServer.msg.sendMessage(player, Messages.TELEPORT_CANT_YOURSELF);
            }
        }
    }

    @CommandAlias("back|dback")
    @CommandPermission("eternia.back")
    public void onBack(Player player) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (APIServer.hasBackLocation(playerName)) {
            if ((player.hasPermission("eternia.backfree") && canBack(player)) || (!EterniaServer.configs.moduleEconomy && canBack(player))) {
                APIServer.putInTeleport(player, new PlayerTeleport(player, APIServer.getBackLocation(playerName), EterniaServer.msg.getMessage(Messages.TELEPORT_BACK_WITHOUT_COST, true)));
            } else if (APIEconomy.getMoney(uuid) >= EterniaServer.configs.backCost && canBack(player) && !player.hasPermission("eternia.backfree")) {
                APIEconomy.removeMoney(uuid, EterniaServer.configs.backCost);
                APIServer.putInTeleport(player, new PlayerTeleport(player, APIServer.getBackLocation(playerName), EterniaServer.msg.getMessage(Messages.TELEPORT_BACK_WITH_COST, true, String.valueOf(EterniaServer.configs.backCost))));
            } else if (canBack(player)){
                EterniaServer.msg.sendMessage(player, Messages.ECO_NO_MONEY);
            }
        } else {
            EterniaServer.msg.sendMessage(player, Messages.TELEPORT_BACK_NOT_DIED);
        }
    }

    private boolean canBack(final Player player) {
        if (APIPlayer.isTeleporting(player)) {
            EterniaServer.msg.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            return false;
        }
        return true;
    }

}
