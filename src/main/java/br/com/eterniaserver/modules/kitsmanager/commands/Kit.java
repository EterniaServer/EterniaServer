package br.com.eterniaserver.modules.kitsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.modules.kitsmanager.KitsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Kit implements CommandExecutor {

    private final EterniaServer plugin;
    private final KitsManager kitsManager;
    private final Messages messages;
    private final Strings strings;

    public Kit(EterniaServer plugin, KitsManager kitsManager, Messages messages, Strings strings) {
        this.plugin = plugin;
        this.kitsManager = kitsManager;
        this.messages = messages;
        this.strings = strings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.kit")) {
                if (args.length == 1) {
                    final String kit = args[0].toLowerCase();
                    if (plugin.kitConfig.contains("kits." + kit)) {
                        if (player.hasPermission("kits." + kit)) {
                            String data = kitsManager.getKitCooldown(player.getName(), kit);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            Date date;
                            try {
                                date = sdf.parse(data);
                                long millis = date.getTime();
                                if ((((millis / 1000) + plugin.kitConfig.getInt("kits." + kit + ".delay")) - (System.currentTimeMillis() / 1000)) <= 0) {
                                    for (String line : plugin.kitConfig.getStringList("kits." + kit + ".command")) {
                                        if (plugin.hasPlaceholderAPI) {
                                            String modifiedCommand = messages.putPAPI(player, line);
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                                        } else {
                                            String modifiedCommand = line.replace("%player_name%", player.getName());
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                                        }
                                    }
                                    for (String line : plugin.kitConfig.getStringList("kits." + kit + ".text")) {
                                        if (plugin.hasPlaceholderAPI) {
                                            String modifiedText = messages.putPAPI(player, line);
                                            player.sendMessage(strings.getColor(modifiedText));
                                        } else {
                                            String modifiedText = line.replace("%player_name%", player.getName());
                                            player.sendMessage(strings.getColor(modifiedText));
                                        }
                                    }
                                    kitsManager.setKitCooldown(player.getName(), kit);
                                } else {
                                    messages.PlayerMessage("kits.cooldown", "%cooldown%", (((millis / 1000) + plugin.kitConfig.getInt("kits." + kit + ".delay")) - (System.currentTimeMillis() / 1000)), player);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            messages.PlayerMessage("kits.no-perm", "%kit_name%", kit, player);
                        }
                    } else {
                        messages.PlayerMessage("kits.noexist", "%kit_name%", kit, player);
                    }
                } else {
                    messages.PlayerMessage("kits.use", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
