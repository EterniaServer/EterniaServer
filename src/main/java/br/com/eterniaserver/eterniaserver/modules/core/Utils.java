package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import lombok.Getter;

import net.kyori.adventure.text.Component;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    public record CommandData(String description,
                              List<String> aliases,
                              List<String> commands,
                              List<String> text,
                              boolean console) { }

    public static class CustomCommand extends Command {

        private final EterniaServer plugin;

        private final List<String> messagesStrings;
        private final List<String> commandsStrings;
        private final boolean console;

        public CustomCommand(EterniaServer plugin,
                             String command,
                             String description,
                             List<String> aliases,
                             List<String> messages,
                             List<String> commands,
                             boolean console) {
            super(command);

            CommandMap commandMap = plugin.getServer().getCommandMap();

            if (aliases != null) {
                this.setAliases(aliases);
            }
            if (description != null) {
                this.setDescription(description);
            }

            commandMap.register("eterniaserver", this);

            this.messagesStrings = messages;
            this.commandsStrings = commands;
            this.plugin = plugin;
            this.console = console;
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
            if (!sender.hasPermission(plugin.getString(Strings.PERM_BASE_COMMAND) + commandLabel)) {
                return false;
            }

            if (!(sender instanceof Player) && !console) {
                return false;
            }

            for (String line : commandsStrings) {
                String command = (
                        sender instanceof Player player ? plugin.setPlaceholders(player, line) : line
                ) + " " + String.join(" ", args);

                if (console) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
                } else {
                    Player player = (Player) sender;
                    player.performCommand(command);
                }
            }

            for (String line : messagesStrings) {
                sender.sendMessage(
                        EterniaLib.getChatCommons().parseColor(sender instanceof Player player ? plugin.setPlaceholders(player, line) : line
                ));
            }

            return true;
        }
    }

    public static String[] getNameAndDisplay(CommandSender sender) {
        String senderName;
        String senderDisplay;
        if (sender instanceof Player player) {
            Entities.PlayerProfile playerProfile = EterniaLib.getDatabase().get(
                    Entities.PlayerProfile.class,
                    player.getUniqueId()
            );
            senderName = playerProfile.getPlayerName();
            senderDisplay = playerProfile.getPlayerDisplay();
        } else {
            senderName = sender.getName();
            senderDisplay = sender.getName();
        }

        return new String[] {senderName, senderDisplay};
    }

    @Getter
    public static class RuntimeInfo {

        private long freemem;
        private long totalmem;
        private int days;
        private int seconds;
        private int minutes;
        private int hours;

        public void recalculateRuntime() {
            Runtime runtime = Runtime.getRuntime();
            long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();

            days = (int) TimeUnit.MILLISECONDS.toDays(milliseconds);
            totalmem = runtime.totalMemory() / 1048576;
            freemem = totalmem - (runtime.freeMemory() / 1048576);
            seconds = (int) (milliseconds / 1000) % 60;
            minutes = (int) ((milliseconds / (1000*60)) % 60);
            hours = (int) ((milliseconds / (1000*60*60)) % 24);
        }

    }

    public static class GUIData {

        private final ItemStack[] items;
        @Getter
        private final Component guiTitle;
        @Getter
        private final int guiSize;

        public GUIData(Component guiTitle, ItemStack[] items) {
            this.items = items;
            this.guiTitle = guiTitle;
            this.guiSize = items.length;
        }

        public ItemStack getItem(int slot) {
            return items[slot];
        }

    }
}
