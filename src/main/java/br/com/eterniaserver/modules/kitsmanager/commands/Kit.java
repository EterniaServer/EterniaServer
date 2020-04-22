package br.com.eterniaserver.modules.kitsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.modules.kitsmanager.sql.KitsAPI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Kit implements CommandExecutor {

    private final EterniaServer plugin;

    public Kit(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.kit")) {
                if (args.length == 1) {
                    final String kit = args[0].toLowerCase();
                    if (EterniaServer.kits.contains("kits." + kit)) {
                        if (player.hasPermission("kits." + kit)) {
                            String data = KitsAPI.getKitCooldown(player.getName(), kit);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            Date date;
                            try {
                                date = sdf.parse(data);
                                long millis = date.getTime();
                                if ((((millis / 1000) + EterniaServer.kits.getInt("kits." + kit + ".delay")) - (System.currentTimeMillis() / 1000)) <= 0) {
                                    List<String> commandList = EterniaServer.kits.getStringList("kits." + kit + ".command");
                                    for (String line : commandList) {
                                        if (Strings.papi) {
                                            String modifiedCommand = Messages.putPAPI(player, line);
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                                        } else {
                                            String modifiedCommand = line.replace("%player_name%", player.getName());
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                                        }
                                    }
                                    List<String> textList = EterniaServer.kits.getStringList("kits." + kit + ".text");
                                    for (String line : textList) {
                                        if (Strings.papi) {
                                            String modifiedText = Messages.putPAPI(player, line);
                                            player.sendMessage(Strings.getColor(modifiedText));
                                        } else {
                                            String modifiedText = line.replace("%player_name%", player.getName());
                                            player.sendMessage(Strings.getColor(modifiedText));
                                        }
                                    }
                                    KitsAPI.setKitCooldown(player.getName(), kit);
                                } else {
                                    Messages.PlayerMessage("kits.cooldown", "%cooldown%", (((millis / 1000) + EterniaServer.kits.getInt("kits." + kit + ".delay")) - (System.currentTimeMillis() / 1000)), player);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Messages.PlayerMessage("kits.no-perm", "%kit_name%", kit, player);
                        }
                    } else {
                        Messages.PlayerMessage("kits.noexist", "%kit_name%", kit, player);
                    }
                } else {
                    Messages.PlayerMessage("kits.use", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
