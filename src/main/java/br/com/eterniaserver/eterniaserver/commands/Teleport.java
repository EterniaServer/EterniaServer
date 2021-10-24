package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.CmdConfirmationManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.RunCommand;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Teleport extends BaseCommand {

    private final EterniaServer plugin;

    public Teleport(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("%tpall")
    @Description("%tpall_description")
    @CommandPermission("%tpall_perm")
    public void onTeleportAll(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) if (other != player) other.teleport(player);
        Bukkit.broadcastMessage(plugin.getMessage(Messages.TELEPORT_ALL_PLAYERS, true, player.getName(), player.getDisplayName()));
    }

    @CommandAlias("%tpa_accept")
    @Description("%tpa_accept_description")
    @CommandPermission("%tpa_accept_perm")
    public void onTeleportAccept(Player player) {
        User user = new User(player);

        if (!EterniaServer.getUserAPI().hasTpaRequest(user.getUUID())) {
            plugin.sendMessage(player, Messages.TELEPORT_NOT_REQUESTED);
            return;
        }

        Player targets = Bukkit.getPlayer(EterniaServer.getUserAPI().getTpaSender(user.getUUID()));

        if (targets == null) {
            plugin.sendMessage(player, Messages.TELEPORT_TARGET_OFFLINE);
            return;
        }

        User target = new User(targets);

        plugin.locationManager().putTeleport(target.getUUID(), new PlayerTeleport(plugin.getInteger(Integers.COOLDOWN), player.getLocation(), plugin.getMessage(Messages.TELEPORT_GOING_TO_PLAYER, true, user.getName(), user.getDisplayName())));
        plugin.sendMessage(target.getPlayer(), Messages.TELEPORT_TARGET_ACCEPT, user.getName(), user.getDisplayName());
        plugin.sendMessage(player, Messages.TELEPORT_ACCEPT, target.getName(), target.getDisplayName());
        EterniaServer.getUserAPI().removeTpaRequest(user.getUUID());
    }

    @CommandAlias("%tpa_deny")
    @Description("%tpa_deny_description")
    @CommandPermission("%tpa_deny_perm")
    public void onTeleportDeny(Player player) {
        User user = new User(player);

        if (!EterniaServer.getUserAPI().hasTpaRequest(user.getUUID())) {
            plugin.sendMessage(player, Messages.TELEPORT_NOT_REQUESTED);
            return;
        }

        Player target = Bukkit.getPlayer(EterniaServer.getUserAPI().getTpaSender(user.getUUID()));

        if (target == null) {
            plugin.sendMessage(player, Messages.TELEPORT_TARGET_OFFLINE);
            return;
        }

        plugin.sendMessage(target, Messages.TELEPORT_TARGET_DENIED, user.getName(), user.getDisplayName());
        plugin.sendMessage(player, Messages.TELEPORT_DENIED, target.getName(), target.getDisplayName());
        EterniaServer.getUserAPI().removeTpaRequest(user.getUUID());
    }

    @CommandAlias("%tpa")
    @Syntax("%tpa_syntax")
    @Description("%tpa_description")
    @CommandPermission("%tpa_perm")
    @CommandCompletion("@players")
    public void onTeleportToPlayer(Player player, OnlinePlayer targets) {
        User user = new User(player);

        if (plugin.locationManager().getTeleport(player.getUniqueId()) != null) {
            plugin.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            return;
        }

        User target = new User(targets.getPlayer());

        if (user.getName().equals(target.getName())) {
            plugin.sendMessage(player, Messages.TELEPORT_CANT_YOURSELF);
            return;
        }

        if (EterniaServer.getUserAPI().hasTpaRequest(target.getUUID())) {
            plugin.sendMessage(player, Messages.TELEPORT_ALREADY_REQUESTED, target.getName(), target.getDisplayName());
            return;
        }

        EterniaServer.getUserAPI().putTpaRequest(target.getUUID(), user.getUUID());
        plugin.sendMessage(target.getPlayer(), Messages.TELEPORT_RECEIVED, user.getName(), user.getDisplayName());
        plugin.sendMessage(player, Messages.TELEPORT_SENT, target.getName(), target.getDisplayName());
    }

    @CommandAlias("%back")
    @Description("%back_description")
    @CommandPermission("%back_perm")
    public void onBack(Player player) {
        User user = new User(player);

        if (!user.hasBackLocation()) {
            plugin.sendMessage(player, Messages.TELEPORT_BACK_NOT_TP);
            return;
        }

        if (plugin.locationManager().getTeleport(player.getUniqueId()) != null) {
            plugin.sendMessage(player, Messages.SERVER_IN_TELEPORT);
            return;
        }

        if (plugin.getStringList(Lists.BLACKLISTED_WORLDS_BACK).contains(user.getBackLocation().getWorld().getName())) {
            plugin.sendMessage(player, Messages.TELEPORT_BACK_WORLD_BLOCKED);
            return;
        }

        if (user.hasPermission(plugin.getString(Strings.PERM_BACK_FREE)) || !plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            plugin.locationManager().putTeleport(user.getUUID(), new PlayerTeleport(plugin.getInteger(Integers.COOLDOWN), user.getBackLocation(), plugin.getMessage(Messages.TELEPORT_BACK_WITHOUT_COST, true)));
            return;
        }

        if (EterniaServer.getEconomyAPI().hasMoney(user.getUUID(), plugin.getDouble(Doubles.BACK_COST))) {
            plugin.sendMessage(player, Messages.COMMAND_COST, String.valueOf(plugin.getDouble(Doubles.BACK_COST)));
            final RunCommand runCommand = new RunCommand(() -> {
                EterniaServer.getEconomyAPI().removeMoney(user.getUUID(), plugin.getDouble(Doubles.BACK_COST));
                plugin.locationManager().putTeleport(user.getUUID(), new PlayerTeleport(plugin.getInteger(Integers.COOLDOWN), user.getBackLocation(), plugin.getMessage(Messages.TELEPORT_BACK_WITH_COST, true, String.valueOf(plugin.getDouble(Doubles.BACK_COST)))));
            });
            CmdConfirmationManager.scheduleCommand(player, runCommand);
            return;
        }

        plugin.sendMessage(player, Messages.ECO_NO_MONEY);
    }

}
