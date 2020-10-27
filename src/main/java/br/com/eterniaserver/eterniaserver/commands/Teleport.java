package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIEconomy;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Teleport extends BaseCommand {

    @CommandAlias("%tpall")
    @Description("%tpall_description")
    @CommandPermission("%tpall_perm")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) if (other != player) other.teleport(player);
        Bukkit.broadcastMessage(EterniaServer.msg.getMessage(Messages.TELEPORT_ALL_PLAYERS, true, player.getName(), player.getDisplayName()));
    }

    @CommandAlias("%tpa_accept")
    @Description("%tpa_accept_description")
    @CommandPermission("%tpa_accept_perm")
    public void onTeleportAccept(Player player) {
        User user = new User(player);

        if (!user.hasTpaRequest()) {
            user.sendMessage(Messages.TELEPORT_NOT_REQUESTED);
            return;
        }

        Player target = Bukkit.getPlayer(user.getTpaSender());

        if (target == null) {
            user.sendMessage(Messages.TELEPORT_TARGET_OFFLINE);
            return;
        }

        new User(target).putInTeleport(new PlayerTeleport(target, player.getLocation(), EterniaServer.msg.getMessage(Messages.TELEPORT_GOING_TO_PLAYER, true, user.getName(), user.getDisplayName())));
        EterniaServer.msg.sendMessage(target, Messages.TELEPORT_TARGET_ACCEPT, user.getName(), user.getDisplayName());
        user.sendMessage(Messages.TELEPORT_ACCEPT, target.getName(), target.getDisplayName());
        user.removeTpaRequest();
    }

    @CommandAlias("%tpa_deny")
    @Description("%tpa_deny_description")
    @CommandPermission("%tpa_deny_perm")
    public void onTeleportDeny(Player player) {
        User user = new User(player);

        if (!user.hasTpaRequest()) {
            user.sendMessage(Messages.TELEPORT_NOT_REQUESTED);
            return;
        }

        Player target = Bukkit.getPlayer(user.getTpaSender());

        if (target == null) {
            user.sendMessage(Messages.TELEPORT_TARGET_OFFLINE);
            return;
        }

        EterniaServer.msg.sendMessage(target, Messages.TELEPORT_TARGET_DENIED, user.getName(), user.getDisplayName());
        user.sendMessage(Messages.TELEPORT_DENIED, target.getName(), target.getDisplayName());
        user.removeTpaRequest();
    }

    @CommandAlias("%tpa")
    @Syntax("%tpa_syntax")
    @Description("%tpa_description")
    @CommandPermission("%tpa_perm")
    @CommandCompletion("@players")
    public void onTeleportToPlayer(Player player, OnlinePlayer targets) {
        User user = new User(player);

        if (user.isTeleporting()) {
            user.sendMessage(Messages.SERVER_IN_TELEPORT);
            return;
        }

        User target = new User(targets.getPlayer());

        if (user.getName().equals(target.getName())) {
            user.sendMessage(Messages.TELEPORT_CANT_YOURSELF);
            return;
        }

        if (target.hasTpaRequest()) {
            user.sendMessage(Messages.TELEPORT_ALREADY_REQUESTED, target.getName(), target.getDisplayName());
            return;
        }

        target.putTpaRequest(user.getUUID());
        target.sendMessage(Messages.TELEPORT_RECEIVED, user.getName(), user.getDisplayName());
        user.sendMessage(Messages.TELEPORT_SENT, target.getName(), target.getDisplayName());
    }

    @CommandAlias("%back")
    @Description("%back_description")
    @CommandPermission("%back_perm")
    public void onBack(Player player) {
        User user = new User(player);

        if (!user.hasBackLocation()) {
            user.sendMessage(Messages.TELEPORT_BACK_NOT_TP);
            return;
        }

        if (user.isTeleporting()) {
            user.sendMessage(Messages.SERVER_IN_TELEPORT);
            return;
        }

        if (user.hasPermission(EterniaServer.constants.permBackFree) || !EterniaServer.configs.moduleEconomy) {
            user.putInTeleport(new PlayerTeleport(player, user.getBackLocation(), EterniaServer.msg.getMessage(Messages.TELEPORT_BACK_WITHOUT_COST, true)));
            return;
        }

        if (APIEconomy.hasMoney(user.getUUID(), EterniaServer.configs.backCost)) {
            APIEconomy.removeMoney(user.getUUID(), EterniaServer.configs.backCost);
            user.putInTeleport(new PlayerTeleport(player, user.getBackLocation(), EterniaServer.msg.getMessage(Messages.TELEPORT_BACK_WITH_COST, true, String.valueOf(EterniaServer.configs.backCost))));
            return;
        }

        user.sendMessage(Messages.ECO_NO_MONEY);
    }

}
