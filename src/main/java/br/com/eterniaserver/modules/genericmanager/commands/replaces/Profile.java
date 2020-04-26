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
import java.util.List;

public class Profile implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.profile")) {
                String string = Vars.player_login.get(player.getName());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    Date date = sdf.parse(string);
                    Messages.PlayerMessage("profile.data", "%player_register_data%", sdf.format(date), player);
                    List<String> textList = EterniaServer.messages.getStringList("profile.custom");
                    for (String line : textList) {
                        if (Strings.papi) {
                            String modifiedText = Messages.putPAPI(player, line);
                            player.sendMessage(Strings.getColor(modifiedText));
                        } else {
                            String modifiedText = line.replace("%player_name%", player.getName());
                            player.sendMessage(Strings.getColor(modifiedText));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
