package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.modules.economymanager.EconomyManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.Vars;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles eFiles;
    private final Vars vars;
    private final EconomyManager moneyx;

    public TeleportSystem(EterniaServer plugin) {
        this.plugin = plugin;
        this.eFiles = plugin.getEFiles();
        this.moneyx = plugin.getMoney();
        this.vars = plugin.getVars();
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
        if (vars.tpa_requests.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(vars.tpa_requests.get(playerName));
            if (target != null) {
                eFiles.sendMessage("teleport.tpa.accept", "%target_name%", playerName, target);
                vars.teleports.put(target, new PlayerTeleport(target, player.getLocation(), "teleport.tpa.done", plugin));
            }
            vars.tpa_time.remove(playerName);
            vars.tpa_requests.remove(playerName);
        } else {
            eFiles.sendMessage("teleport.tpa.no-request", player);
        }
    }

    @CommandAlias("tpdeny|teleportdeny")
    @CommandPermission("eternia.tpa")
    public void onTeleportDeny(Player player) {
        final String playerName = player.getName();
        if (vars.tpa_requests.containsKey(playerName)) {
            eFiles.sendMessage("teleport.tpa.deny", "%target_name%", vars.tpa_requests.get(playerName), player);
            final Player target = Bukkit.getPlayer(vars.tpa_requests.get(playerName));
            vars.tpa_requests.remove(playerName);
            vars.tpa_time.remove(playerName);
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
        if (vars.teleports.containsKey(player)) {
            eFiles.sendMessage("server.telep", player);
        } else {
            if (targetP != player) {
                final String playerName = player.getName();
                final String targetName = targetP.getName();
                if (!vars.tpa_requests.containsKey(targetName)) {
                    vars.tpa_requests.remove(targetName);
                    vars.tpa_requests.put(targetName, playerName);
                    vars.tpa_time.put(targetName, System.currentTimeMillis());
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
        if (vars.back.containsKey(playerName)) {
            double money = moneyx.getMoney(playerName);
            double valor = plugin.serverConfig.getInt("money.back");
            if (player.hasPermission("eternia.backfree") || !(plugin.serverConfig.getBoolean("modules.economy"))) {
                if (vars.teleports.containsKey(player)) eFiles.sendMessage("server.telep", player);
                else vars.teleports.put(player, new PlayerTeleport(player, vars.back.get(playerName), "teleport.back.free", plugin));
            } else {
                if (money >= valor) {
                    if (vars.teleports.containsKey(player)) eFiles.sendMessage("server.telep", player);
                    else vars.teleports.put(player, new PlayerTeleport(player, vars.back.get(playerName), "teleport.back.no-free", plugin));
                } else {
                    eFiles.sendMessage("teleport.back.no-money", "%money%", valor, player);
                }
            }
        } else {
            eFiles.sendMessage("teleport.back.no-tp", player);
        }
    }

}
