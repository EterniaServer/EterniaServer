package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eterniaserver.modules.Constants;

public class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    public record CustomPlaceholder(String permission,
                                    String value,
                                    String hoverText,
                                    String suggestCmd,
                                    int priority,
                                    boolean isStatic) {
    }

    public record ChannelObject(String format,
                                String name,
                                String perm,
                                String channelColor,
                                boolean hasRange,
                                Integer range) {
    }

}
