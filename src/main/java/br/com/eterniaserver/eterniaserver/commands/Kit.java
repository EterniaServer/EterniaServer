package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.objects.CustomKit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class Kit extends BaseCommand {

    public Kit() {

        final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(EterniaServer.getString(ConfigStrings.TABLE_KITS)), "name", "cooldown");
        temp.forEach((k, v) -> APIServer.putKitCooldown(k, Long.parseLong(v)));

        Bukkit.getConsoleSender().sendMessage(EterniaServer.msg.getMessage(Messages.SERVER_DATA_LOADED, true, "Kits", String.valueOf(temp.size())));

    }

    @CommandAlias("%kits")
    @Description("%kits_description")
    @CommandPermission("%kits_perm")
    public void onKits(Player player) {
        StringBuilder str = new StringBuilder();
        for (String key : EterniaServer.kits.kitList.keySet()) {
            str.append(ChatColor.DARK_AQUA).append(key).append(ChatColor.DARK_GRAY).append(", ");
        }
        str.setLength(str.length() - 2);
        EterniaServer.msg.sendMessage(player, Messages.KIT_LIST, str.toString());
    }

    @CommandAlias("%kit")
    @Syntax("%kit_syntax")
    @Description("%kit_description")
    @CommandPermission("%kit_perm")
    public void onKit(Player player, String kit) {
        if (EterniaServer.kits.kitList.containsKey(kit)) {
            if (player.hasPermission(EterniaServer.constants.permKitPrefix + kit)) {
                giveKit(player, kit);
            } else {
                EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
            }
        } else {
            EterniaServer.msg.sendMessage(player, Messages.KIT_NOT_FOUND, kit);
        }
    }

    private void giveKit(Player player, String kit) {
        final long time = System.currentTimeMillis();
        CustomKit kitObject = EterniaServer.kits.kitList.get(kit);
        final String kitName = kit + "." + player.getName();
        int cooldown = kitObject.getDelay();
        final long cd = APIServer.getKitCooldown(kitName);

        if (APIServer.hasCooldown(cd, cooldown)) {
            for (String command : kitObject.getCommands()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), APIServer.setPlaceholders(player, command));
            }
            for (String text : kitObject.getMessages()) {
                player.sendMessage(APIServer.getColor(APIServer.setPlaceholders(player, text)));
            }
            APIServer.putKitCooldown(kitName, time);
            EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.getString(ConfigStrings.TABLE_KITS), "cooldown", time, "name", kitName));
        } else {
            EterniaServer.msg.sendMessage(player, Messages.SERVER_TIMING, APIServer.getTimeLeftOfCooldown(cooldown, cd));
        }
    }

}
