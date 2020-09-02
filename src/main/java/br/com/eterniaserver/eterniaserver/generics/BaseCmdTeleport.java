package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BaseCmdTeleport extends BaseCommand {

    private final double backMoney = EterniaServer.serverConfig.getInt("money.back");

    @CommandAlias("tpall|teleportall")
    @CommandPermission("eternia.tpall")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) if (other != player) other.teleport(player);
        player.sendMessage(PluginMSGs.MSG_TELEPORT_ALL);
    }

    @CommandAlias("tpaccept|teleportaccept")
    @CommandPermission("eternia.tpa")
    public void onTeleportAccept(Player player) {
        final String playerName = player.getName();
        if (PluginVars.tpaRequests.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(PluginVars.tpaRequests.get(playerName));
            if (target != null) {
                target.sendMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_TELEPORT_ACCEPT));
                PluginVars.teleports.put(target, new PlayerTeleport(target, player.getLocation(), PluginMSGs.MSG_TELEPORT_DONE));
            }
            PluginVars.tpaTime.remove(playerName);
            PluginVars.tpaRequests.remove(playerName);
        } else {
            player.sendMessage(PluginMSGs.MSG_TELEPORT_NO_REQUEST);
        }
    }

    @CommandAlias("tpdeny|teleportdeny")
    @CommandPermission("eternia.tpa")
    public void onTeleportDeny(Player player) {
        final String playerName = player.getName();
        final Player target = Bukkit.getPlayer(UUIDFetcher.getUUIDOf(PluginVars.tpaRequests.get(playerName)));
        if (target != null && target.isOnline()) {
            player.sendMessage(UtilInternMethods.putName(target, PluginMSGs.MSG_TELEPORT_DENY));
            PluginVars.tpaRequests.remove(playerName);
            PluginVars.tpaTime.remove(playerName);
            target.sendMessage(PluginMSGs.MSG_TELEPORT_DENIED);
        } else {
            PluginVars.tpaRequests.remove(playerName);
            PluginVars.tpaTime.remove(playerName);
            player.sendMessage(PluginMSGs.MSG_TELEPORT_NO_REQUEST);
        }
    }

    @CommandAlias("tpa|teleportoplayer")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.tpa")
    public void onTeleportToPlayer(Player player, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        if (PluginVars.teleports.containsKey(player)) {
            player.sendMessage(PluginMSGs.MSG_IN_TELEPORT);
        } else {
            if (targetP != player) {
                final String playerName = player.getName();
                final String targetName = targetP.getName();
                if (!PluginVars.tpaRequests.containsKey(targetName)) {
                    PluginVars.tpaRequests.remove(targetName);
                    PluginVars.tpaRequests.put(targetName, playerName);
                    PluginVars.tpaTime.put(targetName, System.currentTimeMillis());
                    targetP.sendMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_TELEPORT_RECEIVED));
                    player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.MSG_TELEPORT_SENT));
                } else {
                    player.sendMessage(PluginMSGs.MSG_TELEPORT_EXISTS);
                }
            } else {
                player.sendMessage(PluginMSGs.MSG_TELEPORT_YOURSELF);
            }
        }
    }

    @CommandAlias("back|dback")
    @CommandPermission("eternia.back")
    public void onBack(Player player) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (PluginVars.back.containsKey(playerName)) {
            if ((player.hasPermission("eternia.backfree") && canBack(player)) || (!(EterniaServer.serverConfig.getBoolean("modules.economy")) && canBack(player))) {
                PluginVars.teleports.put(player, new PlayerTeleport(player, PluginVars.back.get(playerName), PluginMSGs.MSG_BACK_FREE));
            } else if (APIEconomy.getMoney(uuid) >= backMoney && canBack(player)) {
                APIEconomy.removeMoney(uuid, backMoney);
                PluginVars.teleports.put(player, new PlayerTeleport(player, PluginVars.back.get(playerName), PluginMSGs.MSG_BACK_COST));
            } else if (canBack(player)){
                player.sendMessage(PluginMSGs.MSG_NO_MONEY.replace(PluginConstants.VALUE, String.valueOf(backMoney)));
            }
        } else {
            player.sendMessage(PluginMSGs.MSG_BACK_NO_TELEPORT);
        }
    }

    private boolean canBack(final Player player) {
        if (PluginVars.teleports.containsKey(player)) {
            player.sendMessage(PluginMSGs.MSG_IN_TELEPORT);
            return false;
        }
        return true;
    }

}
