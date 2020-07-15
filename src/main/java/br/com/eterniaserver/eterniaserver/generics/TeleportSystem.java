package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportSystem extends BaseCommand {

    private final EFiles eFiles;

    public TeleportSystem(EterniaServer plugin) {
        this.eFiles = plugin.getEFiles();
    }

    @CommandAlias("tpall|teleportall")
    @CommandPermission("eternia.tpall")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other != player) {
                other.teleport(player);
            }
        }
        eFiles.sendMessage("teleport.tp.all", player);
    }

    @CommandAlias("tpaccept|teleportaccept")
    @CommandPermission("eternia.tpa")
    public void onTeleportAccept(Player player) {
        final String playerName = player.getName();
        if (Vars.tpaRequests.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(Vars.tpaRequests.get(playerName));
            if (target != null) {
                eFiles.sendMessage("teleport.tpa.accept", Constants.TARGET.get(), player.getDisplayName(), target);
                Vars.teleports.put(target, new PlayerTeleport(target, player.getLocation(), "teleport.tpa.done"));
            }
            Vars.tpaTime.remove(playerName);
            Vars.tpaRequests.remove(playerName);
        } else {
            eFiles.sendMessage("teleport.tpa.no-request", player);
        }
    }

    @CommandAlias("tpdeny|teleportdeny")
    @CommandPermission("eternia.tpa")
    public void onTeleportDeny(Player player) {
        final String playerName = player.getName();
        if (Vars.tpaRequests.containsKey(playerName)) {
            eFiles.sendMessage("teleport.tpa.deny", Constants.TARGET.get(), Vars.tpaRequests.get(playerName), player);
            final Player target = Bukkit.getPlayer(Vars.tpaRequests.get(playerName));
            Vars.tpaRequests.remove(playerName);
            Vars.tpaTime.remove(playerName);
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
        if (Vars.teleports.containsKey(player)) {
            eFiles.sendMessage("server.telep", player);
        } else {
            if (targetP != player) {
                final String playerName = player.getName();
                final String targetName = targetP.getName();
                if (!Vars.tpaRequests.containsKey(targetName)) {
                    Vars.tpaRequests.remove(targetName);
                    Vars.tpaRequests.put(targetName, playerName);
                    Vars.tpaTime.put(targetName, System.currentTimeMillis());
                    eFiles.sendMessage("teleport.tpa.received", Constants.TARGET.get(), player.getDisplayName(), targetP);
                    eFiles.sendMessage("teleport.tpa.sent", Constants.TARGET.get(), targetP.getDisplayName(), player);
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
        if (Vars.back.containsKey(playerName)) {
            double money = APIEconomy.getMoney(playerName);
            double valor = EterniaServer.serverConfig.getInt("money.back");
            if (player.hasPermission("eternia.backfree") || !(EterniaServer.serverConfig.getBoolean("modules.economy"))) {
                if (Vars.teleports.containsKey(player)) eFiles.sendMessage("server.telep", player);
                else Vars.teleports.put(player, new PlayerTeleport(player, Vars.back.get(playerName), "teleport.back.free"));
            } else {
                if (money >= valor) {
                    if (Vars.teleports.containsKey(player)) {
                        eFiles.sendMessage("server.telep", player);
                    }
                    else {
                        APIEconomy.removeMoney(playerName, valor);
                        Vars.teleports.put(player, new PlayerTeleport(player, Vars.back.get(playerName), "teleport.back.no-free"));
                    }
                } else {
                    eFiles.sendMessage("teleport.back.no-money", Constants.VALUE.get(), valor, player);
                }
            }
        } else {
            eFiles.sendMessage("teleport.back.no-tp", player);
        }
    }

}
