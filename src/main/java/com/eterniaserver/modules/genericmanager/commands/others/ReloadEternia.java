package com.eterniaserver.modules.genericmanager.commands.others;

import com.eterniaserver.configs.MVar;
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
                MVar.playerMessage("server.reload", player);
            }
        } else {
            new Reload();
            MVar.consoleMessage("server.reload");
        }
        return true;
    }
}
