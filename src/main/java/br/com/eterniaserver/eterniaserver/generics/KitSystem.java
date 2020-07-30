package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class KitSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;

    public KitSystem(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_KITS), Strings.NAME, Strings.COOLDOWN);
        temp.forEach((k, v) -> Vars.kitsCooldown.put(k, Long.parseLong(v)));
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Kits", Constants.AMOUNT, temp.size());

    }

    @CommandAlias("kits")
    @CommandPermission("eternia.kits")
    public void onKits(Player player) {
        messages.sendMessage(Strings.M_KIT_LIST, Constants.KITS, messages.getColor(EterniaServer.kitConfig.getString("kits.nameofkits")), player);
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
                final long timeInSec = TimeUnit.MILLISECONDS.toSeconds(time - Vars.kitsCooldown.get(kitName));
                if (timeInSec >= cooldown) {
                    giveKit(player, time, kitName, kit);
                } else {
                    messages.sendMessage(Strings.M_TIMING, Constants.COOLDOWN, cooldown - timeInSec, player);
                }
            } else {
                messages.sendMessage(Strings.M_NO_PERM, Constants.KIT_NAME, kit, player);
            }
        } else {
            messages.sendMessage(Strings.M_KIT_NO_EXISTS, Constants.KIT_NAME, kit, player);
        }
    }

    private void giveKit(final Player player, final long time, final String kitName, final String kit) {
        final String kitString = "kits.";
        for (String line : PlaceholderAPI.setPlaceholders((OfflinePlayer) player, EterniaServer.kitConfig.getStringList(kitString + kit + ".command"))) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), line);
        }
        for (String line : PlaceholderAPI.setPlaceholders((OfflinePlayer) player, EterniaServer.kitConfig.getStringList(kitString + kit + ".text"))) {
            player.sendMessage(messages.getColor(line));
        }
        Vars.kitsCooldown.put(kitName, time);
        EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_KITS, Strings.COOLDOWN, time, Strings.NAME, kitName));
    }

}
