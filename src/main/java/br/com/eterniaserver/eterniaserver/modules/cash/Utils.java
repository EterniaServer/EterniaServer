package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @Getter
    public static class BuyingItem {

        private final List<String> messages;
        private final List<String> commands;
        private final int cost;

        public BuyingItem(final String messages, final String commands, int cost) {
            this.messages = Arrays.asList(messages.split(";"));
            this.commands = Arrays.asList(commands.split(";"));
            this.cost = cost;
        }

    }
}
