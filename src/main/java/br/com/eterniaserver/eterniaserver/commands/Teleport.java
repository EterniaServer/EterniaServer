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
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Strings;
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
        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.TELEPORT_ALL_PLAYERS, true, player.getName(), player.getDisplayName()));
    }

    @CommandAlias("%tpa_accept")
    @Description("%tpa_accept_description")
    @CommandPermission("%tpa_accept_perm")
    public void onTeleportAccept(Player player) {
        User user = new User(player);

        if (!APIServer.hasTpaRequest(user.getUUID())) {
            user.sendMessage(Messages.TELEPORT_NOT_REQUESTED);
            return;
        }

        Player targets = Bukkit.getPlayer(APIServer.getTpaSender(user.getUUID()));

        if (targets == null) {
            user.sendMessage(Messages.TELEPORT_TARGET_OFFLINE);
            return;
        }

        User target = new User(targets);

        target.putInTeleport(new PlayerTeleport(targets, player.getLocation(), EterniaServer.getMessage(Messages.TELEPORT_GOING_TO_PLAYER, true, user.getName(), user.getDisplayName())));
        EterniaServer.sendMessage(targets, Messages.TELEPORT_TARGET_ACCEPT, user.getName(), user.getDisplayName());
        user.sendMessage(Messages.TELEPORT_ACCEPT, target.getName(), target.getDisplayName());
        APIServer.removeTpaRequest(user.getUUID());
    }

    @CommandAlias("%tpa_deny")
    @Description("%tpa_deny_description")
    @CommandPermission("%tpa_deny_perm")
    public void onTeleportDeny(Player player) {
        User user = new User(player);

        if (!APIServer.hasTpaRequest(user.getUUID())) {
            user.sendMessage(Messages.TELEPORT_NOT_REQUESTED);
            return;
        }

        Player target = Bukkit.getPlayer(APIServer.getTpaSender(user.getUUID()));

        if (target == null) {
            user.sendMessage(Messages.TELEPORT_TARGET_OFFLINE);
            return;
        }

        EterniaServer.sendMessage(target, Messages.TELEPORT_TARGET_DENIED, user.getName(), user.getDisplayName());
        user.sendMessage(Messages.TELEPORT_DENIED, target.getName(), target.getDisplayName());
        APIServer.removeTpaRequest(user.getUUID());
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

        if (APIServer.hasTpaRequest(target.getUUID())) {
            user.sendMessage(Messages.TELEPORT_ALREADY_REQUESTED, target.getName(), target.getDisplayName());
            return;
        }

        APIServer.putTpaRequest(target.getUUID(), user.getUUID());
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

        if (user.hasPermission(EterniaServer.getString(Strings.PERM_BACK_FREE)) || !EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            user.putInTeleport(new PlayerTeleport(player, user.getBackLocation(), EterniaServer.getMessage(Messages.TELEPORT_BACK_WITHOUT_COST, true)));
            return;
        }

        if (APIEconomy.hasMoney(user.getUUID(), EterniaServer.getDouble(Doubles.BACK_COST))) {
            APIEconomy.removeMoney(user.getUUID(), EterniaServer.getDouble(Doubles.BACK_COST));
            user.putInTeleport(new PlayerTeleport(player, user.getBackLocation(), EterniaServer.getMessage(Messages.TELEPORT_BACK_WITH_COST, true, String.valueOf(EterniaServer.getDouble(Doubles.BACK_COST)))));
            return;
        }

        user.sendMessage(Messages.ECO_NO_MONEY);
    }

}
