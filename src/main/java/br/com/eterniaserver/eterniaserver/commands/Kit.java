package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.core.APIChat;
import br.com.eterniaserver.eterniaserver.objects.CustomKit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class Kit extends BaseCommand {

    public Kit() {

        final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(EterniaServer.configs.tableKits), Constants.NAME_STR, Constants.COOLDOWN_STR);
        temp.forEach((k, v) -> APIServer.putKitCooldown(k, Long.parseLong(v)));

        Bukkit.getConsoleSender().sendMessage(EterniaServer.msg.getMessage(Messages.SERVER_DATA_LOADED, true, "Kits", String.valueOf(temp.size())));

    }

    @CommandAlias("kits")
    @CommandPermission("eternia.kits")
    public void onKits(Player player) {
        StringBuilder str = new StringBuilder();
        for (String key : EterniaServer.kits.kitList.keySet()) {
            str.append(ChatColor.DARK_AQUA).append(key).append(ChatColor.DARK_GRAY).append(", ");
        }
        str.setLength(str.length() - 2);
        EterniaServer.msg.sendMessage(player, Messages.KIT_LIST, str.toString());
    }

    @CommandAlias("kit")
    @Syntax("<kit>")
    @CommandPermission("eternia.kit")
    public void onKit(Player player, String kit) {
        if (EterniaServer.kits.kitList.containsKey(kit)) {
            if (player.hasPermission("eternia.kit." + kit)) {
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

        if (APIChat.hasCooldown(cd, cooldown)) {
            for (String command : kitObject.getCommands()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), APIChat.setPlaceholders(player, command));
            }
            for (String text : kitObject.getMessages()) {
                player.sendMessage(APIServer.getColor(APIChat.setPlaceholders(player, text)));
            }
            APIServer.putKitCooldown(kitName, time);
            EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tableKits, Constants.COOLDOWN_STR, time, Constants.NAME_STR, kitName));
        } else {
            EterniaServer.msg.sendMessage(player, Messages.SERVER_TIMING, APIChat.getTimeLeft(cooldown, cd));
        }
    }

}
