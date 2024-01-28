package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eternialib.commands.AdvancedCommand;
import br.com.eterniaserver.eternialib.commands.enums.AdvancedCategory;
import br.com.eterniaserver.eternialib.commands.enums.AdvancedRules;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public final class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
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

            this.message = plugin.getMessage(
                    Messages.TELEPORT_ON_GOING,
                    false,
                    locationName
            );
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
