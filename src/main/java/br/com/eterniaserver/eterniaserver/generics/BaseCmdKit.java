package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.MSG;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class BaseCmdKit extends BaseCommand {

    public BaseCmdKit() {

        final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.TABLE_KITS), Constants.NAME_STR, Constants.COOLDOWN_STR);
        temp.forEach((k, v) -> Vars.kitsCooldown.put(k, Long.parseLong(v)));

        Bukkit.getConsoleSender().sendMessage(MSG.MSG_LOAD_DATA.replace(Constants.MODULE, "Kits").replace(Constants.AMOUNT, String.valueOf(temp.size())));

    }

    @CommandAlias("kits")
    @CommandPermission("eternia.kits")
    public void onKits(Player player) {
        player.sendMessage(MSG.M_KIT_LIST.replace(Constants.KITS, MSG.getColor(EterniaServer.kitConfig.getString("nameofkits"))));
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
                final long cd = Vars.kitsCooldown.get(kitName);
                if (UtilInternMethods.hasCooldown(cd, cooldown)) {
                    giveKit(player, time, kitName, kit);
                } else {
                    player.sendMessage(MSG.MSG_TIMING.replace(Constants.COOLDOWN, UtilInternMethods.getTimeLeft(cooldown, cd)));
                }
            } else {
                player.sendMessage(MSG.MSG_NO_PERM.replace(Constants.KIT_NAME, kit));
            }
        } else {
            player.sendMessage(MSG.M_KIT_NO_EXISTS.replace(Constants.KIT_NAME, kit));
        }
    }

    private void giveKit(final Player player, final long time, final String kitName, final String kit) {
        final String kitString = "kits.";
        for (String line : EterniaServer.kitConfig.getStringList(kitString + kit + ".command")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), UtilInternMethods.setPlaceholders(player, line));
        }
        for (String line : EterniaServer.kitConfig.getStringList(kitString + kit + ".text")) {
            player.sendMessage(MSG.getColor(UtilInternMethods.setPlaceholders(player, line)));
        }
        Vars.kitsCooldown.put(kitName, time);
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_KITS, Constants.COOLDOWN_STR, time, Constants.NAME_STR, kitName));
    }

}
