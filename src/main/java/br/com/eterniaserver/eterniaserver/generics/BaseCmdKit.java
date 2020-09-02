package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class BaseCmdKit extends BaseCommand {

    public BaseCmdKit() {

        final Map<String, String> temp = EQueries.getMapString(PluginConstants.getQuerySelectAll(PluginConfigs.TABLE_KITS), PluginConstants.NAME_STR, PluginConstants.COOLDOWN_STR);
        temp.forEach((k, v) -> PluginVars.kitsCooldown.put(k, Long.parseLong(v)));

        Bukkit.getConsoleSender().sendMessage(PluginMSGs.MSG_LOAD_DATA.replace(PluginConstants.MODULE, "Kits").replace(PluginConstants.AMOUNT, String.valueOf(temp.size())));

    }

    @CommandAlias("kits")
    @CommandPermission("eternia.kits")
    public void onKits(Player player) {
        player.sendMessage(PluginMSGs.M_KIT_LIST.replace(PluginConstants.KITS, PluginMSGs.getColor(EterniaServer.kitConfig.getString("nameofkits"))));
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
                final long cd = PluginVars.kitsCooldown.get(kitName);
                if (UtilInternMethods.hasCooldown(cd, cooldown)) {
                    giveKit(player, time, kitName, kit);
                } else {
                    player.sendMessage(PluginMSGs.MSG_TIMING.replace(PluginConstants.COOLDOWN, UtilInternMethods.getTimeLeft(cooldown, cd)));
                }
            } else {
                player.sendMessage(PluginMSGs.MSG_NO_PERM.replace(PluginConstants.KIT_NAME, kit));
            }
        } else {
            player.sendMessage(PluginMSGs.M_KIT_NO_EXISTS.replace(PluginConstants.KIT_NAME, kit));
        }
    }

    private void giveKit(final Player player, final long time, final String kitName, final String kit) {
        final String kitString = "kits.";
        for (String line : EterniaServer.kitConfig.getStringList(kitString + kit + ".command")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), UtilInternMethods.setPlaceholders(player, line));
        }
        for (String line : EterniaServer.kitConfig.getStringList(kitString + kit + ".text")) {
            player.sendMessage(PluginMSGs.getColor(UtilInternMethods.setPlaceholders(player, line)));
        }
        PluginVars.kitsCooldown.put(kitName, time);
        EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_KITS, PluginConstants.COOLDOWN_STR, time, PluginConstants.NAME_STR, kitName));
    }

}
