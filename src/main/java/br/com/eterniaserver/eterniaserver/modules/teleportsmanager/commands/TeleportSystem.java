package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.modules.economymanager.EconomyManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class TeleportSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles eFiles;
    private final EconomyManager moneyx;

    private final Map<String, String> tpaRequests;
    private final Map<String, Long> tpaTime;

    public TeleportSystem(EterniaServer plugin) {
        this.plugin = plugin;
        this.eFiles = plugin.getEFiles();
        this.moneyx = plugin.getMoney();
        this.tpaRequests = plugin.getTpa_requests();
        this.tpaTime = plugin.getTpa_time();
    }

    @CommandAlias("tpall|teleportall")
    @CommandPermission("eternia.tpall")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) if (other != player) other.teleport(player);
        eFiles.sendMessage("teleport.tp.all", player);
    }

    @CommandAlias("tpaccept|teleportaccept")
    @CommandPermission("eternia.tpa")
    public void onTeleportAccept(Player player) {
        final String playerName = player.getName();
        if (tpaRequests.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(tpaRequests.get(playerName));
            if (target != null) {
                eFiles.sendMessage("teleport.tpa.accept", "%target_name%", playerName, target);
                EterniaServer.teleports.put(target, new PlayerTeleport(target, player.getLocation(), "teleport.tpa.done", plugin));
            }
            tpaTime.remove(playerName);
            tpaRequests.remove(playerName);
        } else {
            eFiles.sendMessage("teleport.tpa.no-request", player);
        }
    }

    @CommandAlias("tpdeny|teleportdeny")
    @CommandPermission("eternia.tpa")
    public void onTeleportDeny(Player player) {
        final String playerName = player.getName();
        if (tpaRequests.containsKey(playerName)) {
            eFiles.sendMessage("teleport.tpa.deny", "%target_name%", tpaRequests.get(playerName), player);
            final Player target = Bukkit.getPlayer(tpaRequests.get(playerName));
            tpaRequests.remove(playerName);
            tpaTime.remove(playerName);
            if (target != null && target.isOnline()) eFiles.sendMessage("teleport.tpa.denied", target);
        } else {
            eFiles.sendMessage("teleport.tpa.no-request", player);
        }
    }

    @CommandAlias("tpa|teleportoplayer")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.tpa")
    public void onTeleportToPlayer(Player player, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        if (EterniaServer.teleports.containsKey(player)) {
            eFiles.sendMessage("server.telep", player);
        } else {
            if (targetP != player) {
                final String playerName = player.getName();
                final String targetName = targetP.getName();
                if (!tpaRequests.containsKey(targetName)) {
                    tpaRequests.remove(targetName);
                    tpaRequests.put(targetName, playerName);
                    tpaTime.put(targetName, System.currentTimeMillis());
                    eFiles.sendMessage("teleport.tpa.received", "%target_name%", playerName, targetP);
                    eFiles.sendMessage("teleport.tpa.sent", "%target_name%", targetName, player);
                } else {
                    eFiles.sendMessage("teleport.tpa.exists", player);
                }
            } else {
                eFiles.sendMessage("teleport.tpa.yourself", player);
            }
        }
    }

    @CommandAlias("back|dback")
    @CommandPermission("eternia.back")
    public void onBack(Player player) {
        final String playerName = player.getName();
        if (EterniaServer.back.containsKey(playerName)) {
            double money = moneyx.getMoney(playerName);
            double valor = plugin.serverConfig.getInt("money.back");
            if (player.hasPermission("eternia.backfree") || !(plugin.serverConfig.getBoolean("modules.economy"))) {
                if (EterniaServer.teleports.containsKey(player)) eFiles.sendMessage("server.telep", player);
                else EterniaServer.teleports.put(player, new PlayerTeleport(player, EterniaServer.back.get(playerName), "teleport.back.free", plugin));
            } else {
                if (money >= valor) {
                    if (EterniaServer.teleports.containsKey(player)) {
                        eFiles.sendMessage("server.telep", player);
                    }
                    else {
                        moneyx.removeMoney(playerName, valor);
                        EterniaServer.teleports.put(player, new PlayerTeleport(player, EterniaServer.back.get(playerName), "teleport.back.no-free", plugin));
                    }
                } else {
                    eFiles.sendMessage("teleport.back.no-money", "%money%", valor, player);
                }
            }
        } else {
            eFiles.sendMessage("teleport.back.no-tp", player);
        }
    }

}
