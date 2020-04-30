package br.com.eterniaserver.modules.genericmanager.commands.replaces;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.configs.Vars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Strings strings;
    private final Vars vars;

    public Profile(EterniaServer plugin, Messages messages, Strings strings, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.profile")) {
                String string = vars.player_login.get(player.getName());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    Date date = sdf.parse(string);
                    messages.PlayerMessage("profile.data", "%player_register_data%", sdf.format(date), player);
                    for (String line : plugin.msgConfig.getStringList("profile.custom")) {
                        if (plugin.hasPlaceholderAPI) {
                            String modifiedText = messages.putPAPI(player, line);
                            player.sendMessage(strings.getColor(modifiedText));
                        } else {
                            String modifiedText = line.replace("%player_name%", player.getName());
                            player.sendMessage(strings.getColor(modifiedText));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
