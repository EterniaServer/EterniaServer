package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.dependencies.papi.PlaceHolders;

import br.com.eterniaserver.eterniaserver.storages.Files;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadEternia implements CommandExecutor {

    private final Messages messages;
    private final Files files;
    private final PlaceHolders placeHolders;

    public ReloadEternia(Messages messages, Files files, PlaceHolders placeHolders) {
        this.messages = messages;
        this.files = files;
        this.placeHolders = placeHolders;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("eternia.reload")) {
            messages.sendMessage("server.no-perm", sender);
            return false;
        }

        messages.sendMessage("server.reload-s", sender);

        files.loadConfigs();
        files.loadMessages();
        files.loadChat();
        files.loadDatabase();

        PlaceholderAPI.unregisterPlaceholderHook("eterniaserver");
        PlaceholderAPI.registerPlaceholderHook("eterniaserver", placeHolders);

        messages.sendMessage("eternia.reload-f", sender);
        return true;
    }
}
