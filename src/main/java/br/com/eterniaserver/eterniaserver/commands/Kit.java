package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.generics.APIServer;
import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.APIUnstable;
import org.bukkit.Bukkit;
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
        EterniaServer.configs.sendMessage(player, Messages.KIT_LIST, APIServer.getColor(EterniaServer.kitConfig.getString("nameofkits")));
    }

    @CommandAlias("kit")
    @Syntax("<kit>")
    @CommandPermission("eternia.kit")
    public void onKit(Player player, String kit) {
        final String kitString = "kits.";
        if (EterniaServer.kitConfig.contains(kitString + kit)) {
            if (player.hasPermission("eternia.kit." + kit)) {
                final long time = System.currentTimeMillis();
                final String kitName = kit + "." + player.getName();
                final int cooldown = EterniaServer.kitConfig.getInt(kitString + kit + ".delay");
                final long cd = APIServer.getKitCooldown(kitName);
                if (APIUnstable.hasCooldown(cd, cooldown)) {
                    giveKit(player, time, kitName, kit);
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

    private void giveKit(final Player player, final long time, final String kitName, final String kit) {
        final String kitString = "kits.";
        for (String line : EterniaServer.kitConfig.getStringList(kitString + kit + ".command")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), APIUnstable.setPlaceholders(player, line));
        }
        for (String line : EterniaServer.kitConfig.getStringList(kitString + kit + ".text")) {
            player.sendMessage(APIServer.getColor(APIUnstable.setPlaceholders(player, line)));
        }
        APIServer.putKitCooldown(kitName, time);
        EQueries.executeQuery(PluginConstants.getQueryUpdate(EterniaServer.configs.tableKits, PluginConstants.COOLDOWN_STR, time, PluginConstants.NAME_STR, kitName));
    }

}
