package com.eterniaserver.modules.genericmanager.commands.others;

import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import com.eterniaserver.modules.genericmanager.Reload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadEternia implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.reload")) {
                new Reload();
                new PlayerMessage("server.reload", player);
            }
        } else {
            new Reload();
            new ConsoleMessage("server.reload");
        }
        return true;
    }
}
