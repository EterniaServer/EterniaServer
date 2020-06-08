package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.API.Money;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.player.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;
    private final Money moneyx;

    public TeleportSystem(EterniaServer plugin, Messages messages, Money moneyx, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.moneyx = moneyx;
        this.vars = vars;
    }

    @CommandAlias("tpall|teleportall")
    @CommandPermission("eternia.tpall")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) if (other != player) other.teleport(player);
        messages.sendMessage("teleport.tp.all", player);
    }

    @CommandAlias("tpaccept|teleportaccept")
    @CommandPermission("eternia.tpa")
    public void onTeleportAccept(Player player) {
        if (vars.tpa_requests.containsKey(player.getName())) {
            Player target = Bukkit.getPlayer(vars.tpa_requests.get(player.getName()));
            if (target != null) {
                messages.sendMessage("teleport.tpa.accept", "%target_name%", player.getName(), target);
                vars.teleports.put(target, new PlayerTeleport(target, player.getLocation(), "teleport.tpa.done", plugin));
            }
            vars.tpa_requests.remove(player.getName());
        } else {
            messages.sendMessage("teleport.tpa.no-request", player);
        }
    }

    @CommandAlias("tpdeny|teleportdeny")
    @CommandPermission("eternia.tpa")
    public void onTeleportDeny(Player player) {
        if (vars.tpa_requests.containsKey(player.getName())) {
            messages.sendMessage("teleport.tpa.deny", "%target_name%", vars.tpa_requests.get(player.getName()), player);
            Player target = Bukkit.getPlayer(vars.tpa_requests.get(player.getName()));
            vars.tpa_requests.remove(player.getName());
            if (target != null && target.isOnline()) messages.sendMessage("teleport.tpa.denied", target);
        } else {
            messages.sendMessage("teleport.tpa.no-request", player);
        }
    }

    @CommandAlias("tpa|teleportoplayer")
    @Syntax("<jogador>")
    @CommandPermission("eternia.tpa")
    public void onTeleportToPlayer(Player player, OnlinePlayer target) {
        if (vars.teleports.containsKey(player)) {
            messages.sendMessage("server.telep", player);
        } else {
            if (target.getPlayer() != player) {
                if (!vars.tpa_requests.containsKey(target.getPlayer().getName())) {
                    vars.tpa_requests.remove(target.getPlayer().getName());
                    vars.tpa_requests.put(target.getPlayer().getName(), player.getName());
                    messages.sendMessage("teleport.tpa.received", "%target_name%", player.getName(), target.getPlayer());
                    messages.sendMessage("teleport.tpa.sent", "%target_name%", target.getPlayer().getName(), player);
                } else {
                    messages.sendMessage("teleport.tpa.exists", player);
                }
            } else {
                messages.sendMessage("teleport.tpa.yourself", player);
            }
        }
    }

    @CommandAlias("back|dback")
    @CommandPermission("eternia.back")
    public void onBack(Player player) {
        if (vars.back.containsKey(player.getName())) {
            double money = moneyx.getMoney(player.getName());
            double valor = plugin.serverConfig.getInt("money.back");
            if (player.hasPermission("eternia.backfree") || !(plugin.serverConfig.getBoolean("modules.economy"))) {
                if (vars.teleports.containsKey(player)) messages.sendMessage("server.telep", player);
                else vars.teleports.put(player, new PlayerTeleport(player, vars.back.get(player.getName()), "teleport.back.free", plugin));
            } else {
                if (money >= valor) {
                    if (vars.teleports.containsKey(player)) messages.sendMessage("server.telep", player);
                    else vars.teleports.put(player, new PlayerTeleport(player, vars.back.get(player.getName()), "teleport.back.no-free", plugin));
                } else {
                    messages.sendMessage("teleport.back.no-money", "%money%", valor, player);
                }
            }
        } else {
            messages.sendMessage("teleport.back.no-tp", player);
        }
    }

}
