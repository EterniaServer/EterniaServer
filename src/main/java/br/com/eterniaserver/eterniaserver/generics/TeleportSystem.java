package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.utils.PlayerTeleport;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportSystem extends BaseCommand {

    private final EFiles messages;
    private final double backMoney = EterniaServer.serverConfig.getInt("money.back");

    public TeleportSystem(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
    }

    @CommandAlias("tpall|teleportall")
    @CommandPermission("eternia.tpall")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) if (other != player) other.teleport(player);
        messages.sendMessage(Strings.M_TPALL, player);
    }

    @CommandAlias("tpaccept|teleportaccept")
    @CommandPermission("eternia.tpa")
    public void onTeleportAccept(Player player) {
        final String playerName = player.getName();
        if (Vars.tpaRequests.containsKey(playerName)) {
            final Player target = Bukkit.getPlayer(Vars.tpaRequests.get(playerName));
            if (target != null) {
                messages.sendMessage(Strings.M_TPA_ACCEPT, Constants.TARGET, player.getDisplayName(), target);
                Vars.teleports.put(target, new PlayerTeleport(target, player.getLocation(), Strings.M_TPA_DONE));
            }
            Vars.tpaTime.remove(playerName);
            Vars.tpaRequests.remove(playerName);
        } else {
            messages.sendMessage(Strings.M_TPA_NO_REQUEST, player);
        }
    }

    @CommandAlias("tpdeny|teleportdeny")
    @CommandPermission("eternia.tpa")
    public void onTeleportDeny(Player player) {
        final String playerName = player.getName();
        if (Vars.tpaRequests.containsKey(playerName)) {
            messages.sendMessage(Strings.M_TPA_DENY, Constants.TARGET, Vars.tpaRequests.get(playerName), player);
            final Player target = Bukkit.getPlayer(Vars.tpaRequests.get(playerName));
            Vars.tpaRequests.remove(playerName);
            Vars.tpaTime.remove(playerName);
            if (target != null && target.isOnline()) messages.sendMessage(Strings.M_TPA_DENIED, target);
        } else {
            messages.sendMessage(Strings.M_TPA_NO_REQUEST, player);
        }
    }

    @CommandAlias("tpa|teleportoplayer")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.tpa")
    public void onTeleportToPlayer(Player player, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        if (Vars.teleports.containsKey(player)) {
            messages.sendMessage(Strings.M_TELEP, player);
        } else {
            if (targetP != player) {
                final String playerName = player.getName();
                final String targetName = targetP.getName();
                if (!Vars.tpaRequests.containsKey(targetName)) {
                    Vars.tpaRequests.remove(targetName);
                    Vars.tpaRequests.put(targetName, playerName);
                    Vars.tpaTime.put(targetName, System.currentTimeMillis());
                    messages.sendMessage(Strings.M_TPA_RECEIVED, Constants.TARGET, player.getDisplayName(), targetP);
                    messages.sendMessage(Strings.M_TPA_SENT, Constants.TARGET, targetP.getDisplayName(), player);
                } else {
                    messages.sendMessage(Strings.M_TPA_EXISTS, player);
                }
            } else {
                messages.sendMessage(Strings.M_TPA_YOU, player);
            }
        }
    }

    @CommandAlias("back|dback")
    @CommandPermission("eternia.back")
    public void onBack(Player player) {
        final String playerName = player.getName();
        if (Vars.back.containsKey(playerName)) {
            if ((player.hasPermission("eternia.backfree") && canBack(player)) || (!(EterniaServer.serverConfig.getBoolean("modules.economy")) && canBack(player))) {
                Vars.teleports.put(player, new PlayerTeleport(player, Vars.back.get(playerName), Strings.M_BACK_FREE));
            } else if (APIEconomy.getMoney(playerName) >= backMoney && canBack(player)) {
                APIEconomy.removeMoney(playerName, backMoney);
                Vars.teleports.put(player, new PlayerTeleport(player, Vars.back.get(playerName), Strings.M_BACK));
            } else if (canBack(player)){
                messages.sendMessage(Strings.M_NO_MONEY, Constants.VALUE, backMoney, player);
            }
        } else {
            messages.sendMessage(Strings.M_BACK_NO, player);
        }
    }

    private boolean canBack(final Player player) {
        if (Vars.teleports.containsKey(player)) {
            messages.sendMessage(Strings.M_TELEP, player);
            return false;
        }
        return true;
    }

}
