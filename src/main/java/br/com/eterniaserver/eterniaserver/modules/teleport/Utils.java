package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eternialib.commands.AdvancedCommand;
import br.com.eterniaserver.eternialib.commands.enums.AdvancedCategory;
import br.com.eterniaserver.eternialib.commands.enums.AdvancedRules;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public final class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    public static class TpaCommand extends AdvancedCommand {

        private final Player sender;
        private final Player target;
        private final Runnable runnable;

        private boolean aborted = false;
        private int commandTicks = 0;

        public TpaCommand(Player sender, Player target, Runnable runnable) {
            this.sender = sender;
            this.target = target;
            this.runnable = runnable;
        }

        @Override
        public Player sender() {
            return sender;
        }

        @Override
        public void execute() {
            runnable.run();
        }

        @Override
        public String getTimeMessage() {
            return "";
        }

        @Override
        public void abort(Component component) {
            this.aborted = true;
            target.sendMessage(component);
        }

        @Override
        public boolean isAborted() {
            return aborted;
        }

        @Override
        public BukkitTask executeAsynchronously() {
            return null;
        }

        @Override
        public AdvancedCategory getCategory() {
            return AdvancedCategory.CONFIRMATION;
        }

        @Override
        public int neededTimeInSeconds() {
            return 15;
        }

        @Override
        public int getCommandTicks() {
            return commandTicks;
        }

        @Override
        public void addCommandTicks(int i) {
            commandTicks += i;
        }

        @Override
        public AdvancedRules[] getAdvancedRules() {
            return new AdvancedRules[0];
        }
    }

    public static class TeleportCommand extends AdvancedCommand {

        private final EterniaServer plugin;

        private final String message;
        private final Player sender;
        private final Location location;

        private boolean aborted = false;
        private int commandTicks = 0;

        public TeleportCommand(EterniaServer plugin, String locationName, Player sender, Location location) {
            this.plugin = plugin;
            this.sender = sender;
            this.location = location;

            this.message = EterniaLib
                    .getChatCommons()
                    .getMessage(Messages.TELEPORT_ON_GOING, new MessageOptions(locationName));
        }

        public static void addTeleport(EterniaServer plugin, Player player, Location location, String name) {
            if (player.hasPermission(plugin.getString(Strings.PERM_TELEPORT_TIME_BYPASS))) {
                player.teleportAsync(location);
            } else {
                Utils.TeleportCommand teleportCommand = new Utils.TeleportCommand(
                        plugin,
                        name,
                        player,
                        location
                );
                boolean result = EterniaLib.getAdvancedCmdManager().addTimedCommand(teleportCommand);
                if (!result) {
                    MessageOptions options = new MessageOptions(name);
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ALREADY_IN_TIMING, options);
                }
            }
        }

        @Override
        public Player sender() {
            return sender;
        }

        @Override
        public void execute() {
            sender.teleportAsync(location);
        }

        @Override
        public String getTimeMessage() {
            return message;
        }

        @Override
        public void abort(Component component) {
            this.aborted = true;
            sender.sendMessage(component);
        }

        @Override
        public boolean isAborted() {
            return aborted;
        }

        @Override
        public BukkitTask executeAsynchronously() {
            return null;
        }

        @Override
        public AdvancedCategory getCategory() {
            return AdvancedCategory.TIMED;
        }

        @Override
        public int neededTimeInSeconds() {
            return plugin.getInteger(Integers.TELEPORT_TIMER);
        }

        @Override
        public int getCommandTicks() {
            return commandTicks;
        }

        @Override
        public void addCommandTicks(int i) {
            commandTicks += i;
        }

        @Override
        public AdvancedRules[] getAdvancedRules() {
            return new AdvancedRules[] {
                    AdvancedRules.NOT_ATTACK,
                    AdvancedRules.NOT_MOVE,
                    AdvancedRules.NOT_JUMP,
                    AdvancedRules.NOT_BREAK_BLOCK
            };
        }
    }

}
