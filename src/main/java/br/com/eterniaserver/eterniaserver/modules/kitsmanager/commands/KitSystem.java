package br.com.eterniaserver.eterniaserver.modules.kitsmanager.commands;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.modules.kitsmanager.KitsManager;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KitSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final KitsManager kitsManager;

    public KitSystem(EterniaServer plugin, KitsManager kitsManager) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.kitsManager = kitsManager;
    }

    @CommandAlias("kits")
    @CommandPermission("eternia.kits")
    public void onKits(Player player) {
        messages.sendMessage("kit.list", "%kits%", messages.getColor(plugin.kitConfig.getString("kits.nameofkits")), player);
    }

    @CommandAlias("kit")
    @Syntax("<kit>")
    @CommandPermission("eternia.kit")
    public void onKit(Player player, String kit) {
        if (plugin.kitConfig.contains(Strings.KITS_GET + kit)) {
            if (player.hasPermission(Strings.KITS_GET + kit)) {
                String data = kitsManager.getKitCooldown(player.getName(), kit);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                Date date;
                try {
                    date = sdf.parse(data);
                    executeKit(player, date, kit);
                } catch (ParseException e) {
                    // todo
                }
            } else {
                messages.sendMessage("server.no-perm", "%kit_name%", kit, player);
            }
        } else {
            messages.sendMessage("kit.no-exists", "%kit_name%", kit, player);
        }
    }

    private void executeKit(Player player, Date date, String kit) {
        long millis = date.getTime();
        if ((((millis / 1000) + plugin.kitConfig.getInt(Strings.KITS_GET + kit + ".delay")) - (System.currentTimeMillis() / 1000)) <= 0) {
            for (String line : plugin.kitConfig.getStringList(Strings.KITS_GET + kit + ".command")) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line));
            }
            for (String line : plugin.kitConfig.getStringList(Strings.KITS_GET + kit + ".text")) {
                player.sendMessage(messages.getColor(PlaceholderAPI.setPlaceholders(player, line)));
            }
            kitsManager.setKitCooldown(player.getName(), kit);
        } else {
            messages.sendMessage("server.timing", "%cooldown%", (((millis / 1000) + plugin.kitConfig.getInt(Strings.KITS_GET + kit + ".delay")) - (System.currentTimeMillis() / 1000)), player);
        }
    }

}
