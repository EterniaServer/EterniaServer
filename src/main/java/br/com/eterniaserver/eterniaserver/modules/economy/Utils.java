package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.commands.AdvancedCommand;
import br.com.eterniaserver.eternialib.commands.enums.AdvancedCategory;
import br.com.eterniaserver.eternialib.commands.enums.AdvancedRules;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankMember;

import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public final class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    public static class AffiliateCommand extends AdvancedCommand {

        private final EterniaServer plugin;

        private final String bankName;
        private final Player sender;
        private final Player target;

        private boolean aborted = false;
        private int commandTicks = 0;

        public AffiliateCommand(EterniaServer plugin, String bankName, Player sender, Player target) {
            this.plugin = plugin;
            this.bankName = bankName;
            this.sender = sender;
            this.target = target;
        }

        @Override
        public Player sender() {
            return sender;
        }

        @Override
        public void execute() {
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
            return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                BankMember bankMember = new BankMember();

                bankMember.setBankName(bankName);
                bankMember.setRole(Enums.BankRole.MEMBER.name());
                bankMember.setUuid(target.getUniqueId());

                EterniaLib.getDatabase().insert(BankMember.class, bankMember);

                plugin.sendMiniMessages(target, Messages.ECO_BANK_AFILIATE_SUCCESS, bankName);
            });
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

}
