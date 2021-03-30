package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.CustomKit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Kit extends BaseCommand {

    private final EterniaServer plugin;

    public Kit(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("%kits")
    @Description("%kits_description")
    @CommandPermission("%kits_perm")
    public void onKits(Player player) {
        StringBuilder str = new StringBuilder();
        for (String key : plugin.getKitList().keySet()) {
            if (player.hasPermission(plugin.getString(Strings.PERM_KIT_PREFIX) + key)) {
                str.append(ChatColor.DARK_AQUA).append(key).append(ChatColor.DARK_GRAY).append(", ");
            }
        }
        str.setLength(str.length() - 2);
        plugin.sendMessage(player, Messages.KIT_LIST, str.toString());
    }

    @CommandAlias("%kit")
    @Syntax("%kit_syntax")
    @Description("%kit_description")
    @CommandPermission("%kit_perm")
    public void onKit(Player player, String kit) {
        if (plugin.getKitList().containsKey(kit)) {
            if (player.hasPermission(plugin.getString(Strings.PERM_KIT_PREFIX) + kit)) {
                giveKit(player, kit);
            } else {
                plugin.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        } else {
            plugin.sendMessage(player, Messages.KIT_NOT_FOUND, kit);
        }
    }

    private void giveKit(Player player, String kit) {
        final long time = System.currentTimeMillis();
        CustomKit kitObject = plugin.getKitList().get(kit);
        final String kitName = kit + "." + player.getName();
        int cooldown = kitObject.getDelay();
        final long cd = EterniaServer.getUserAPI().getKitCooldown(kitName);

        if (plugin.hasCooldown(cd, cooldown)) {
            for (String command : kitObject.getCommands()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), plugin.setPlaceholders(player, command));
            }
            for (String text : kitObject.getMessages()) {
                player.sendMessage(plugin.getColor(plugin.setPlaceholders(player, text)));
            }
            EterniaServer.getUserAPI().putKitCooldown(kitName, time);

            Update update = new Update(plugin.getString(Strings.TABLE_KITS));
            update.set.set("cooldown", time);
            update.where.set("name", kitName);
            SQL.executeAsync(update);
        } else {
            plugin.sendMessage(player, Messages.SERVER_TIMING, plugin.getTimeLeftOfCooldown(cooldown, cd));
        }
    }

}
