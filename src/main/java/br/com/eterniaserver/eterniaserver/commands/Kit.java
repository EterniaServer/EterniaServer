package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.PluginConstants;
import br.com.eterniaserver.eterniaserver.core.APIUnstable;
import br.com.eterniaserver.eterniaserver.objects.KitObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class Kit extends BaseCommand {

    public Kit() {

        final Map<String, String> temp = EQueries.getMapString(PluginConstants.getQuerySelectAll(EterniaServer.configs.tableKits), PluginConstants.NAME_STR, PluginConstants.COOLDOWN_STR);
        temp.forEach((k, v) -> APIServer.putKitCooldown(k, Long.parseLong(v)));

        Bukkit.getConsoleSender().sendMessage(EterniaServer.configs.getMessage(Messages.SERVER_DATA_LOADED, true, "Kits", String.valueOf(temp.size())));

    }

    @CommandAlias("kits")
    @CommandPermission("eternia.kits")
    public void onKits(Player player) {
        StringBuilder str = new StringBuilder();
        for (String key : EterniaServer.configs.kitList.keySet()) {
            str.append(ChatColor.DARK_AQUA).append(key).append(ChatColor.DARK_GRAY).append(", ");
        }
        str.setLength(str.length() - 2);
        EterniaServer.configs.sendMessage(player, Messages.KIT_LIST, str.toString());
    }

    @CommandAlias("kit")
    @Syntax("<kit>")
    @CommandPermission("eternia.kit")
    public void onKit(Player player, String kit) {
        final String kitString = "kits.";
        if (EterniaServer.configs.kitList.containsKey(kit)) {
            if (player.hasPermission("eternia.kit." + kit)) {
                final long time = System.currentTimeMillis();
                KitObject kitObject = EterniaServer.configs.kitList.get(kit);
                final String kitName = kit + "." + player.getName();
                int cooldown = kitObject.getDelay();
                final long cd = APIServer.getKitCooldown(kitName);

                if (APIUnstable.hasCooldown(cd, cooldown)) {
                    for (String command : kitObject.getCommands()) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), APIUnstable.setPlaceholders(player, command));
                    }
                    for (String text : kitObject.getMessages()) {
                        player.sendMessage(APIServer.getColor(APIUnstable.setPlaceholders(player, text)));
                    }
                    APIServer.putKitCooldown(kitName, time);
                    EQueries.executeQuery(PluginConstants.getQueryUpdate(EterniaServer.configs.tableKits, PluginConstants.COOLDOWN_STR, time, PluginConstants.NAME_STR, kitName));
                } else {
                    EterniaServer.configs.sendMessage(player, Messages.SERVER_TIMING, APIUnstable.getTimeLeft(cooldown, cd));
                }
            } else {
                EterniaServer.configs.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        } else {
            EterniaServer.configs.sendMessage(player, Messages.KIT_NOT_FOUND, kit);
        }
    }

}
