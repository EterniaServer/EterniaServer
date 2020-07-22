package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class KitSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;

    public KitSystem(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        String query = "SELECT * FROM " + EterniaServer.serverConfig.getString("sql.table-kits") + ";";
        HashMap<String, String> temp = EQueries.getMapString(query, "name", "cooldown");

        temp.forEach((k, v) -> Vars.kitsCooldown.put(k, Long.parseLong(v)));
        messages.sendConsole("server.load-data", Constants.MODULE.get(), "Kits", Constants.AMOUNT.get(), temp.size());

    }

    @CommandAlias("kits")
    @CommandPermission("eternia.kits")
    public void onKits(Player player) {
        messages.sendMessage("kit.list", "%kits%", messages.getColor(EterniaServer.kitConfig.getString("kits.nameofkits")), player);
    }

    @CommandAlias("kit")
    @Syntax("<kit>")
    @CommandPermission("eternia.kit")
    public void onKit(Player player, String kit) {
        if (EterniaServer.kitConfig.contains("kits." + kit)) {
            if (player.hasPermission("eternia.kit." + kit)) {
                final String playerName = player.getName();
                final long time = System.currentTimeMillis();
                final int cooldown = EterniaServer.kitConfig.getInt("kits." + kit + ".delay");
                final long timeInSec = TimeUnit.MILLISECONDS.toSeconds(time - Vars.kitsCooldown.get(kit + "." + playerName));
                if (timeInSec >= cooldown) {
                    for (String line : EterniaServer.kitConfig.getStringList("kits." + kit + ".command")) {
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line));
                    }
                    for (String line : EterniaServer.kitConfig.getStringList("kits." + kit + ".text")) {
                        player.sendMessage(messages.getColor(PlaceholderAPI.setPlaceholders(player, line)));
                    }
                    Vars.kitsCooldown.put(kit + "." + playerName, time);
                    EQueries.executeQuery("UPDATE " + EterniaServer.serverConfig.getString("sql.table-kits") + " SET cooldown='" + time + "' WHERE name='" + kit + "." + playerName + "';");
                } else {
                    messages.sendMessage("server.timing", "%cooldown%", cooldown - timeInSec, player);
                }
            } else {
                messages.sendMessage("server.no-perm", "%kit_name%", kit, player);
            }
        } else {
            messages.sendMessage("kit.no-exists", "%kit_name%", kit, player);
        }
    }

}
