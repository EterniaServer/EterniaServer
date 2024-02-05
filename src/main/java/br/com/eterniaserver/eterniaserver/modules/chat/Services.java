package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eterniaserver.api.interfaces.ChatAPI;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import net.kyori.adventure.text.TextReplacementConfig;

import java.util.UUID;
import java.util.regex.Pattern;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Chat implements ChatAPI {

        @Override
        public TextReplacementConfig getFilter() {
            return null;
        }

        @Override
        public void setFilter(Pattern filter) {

        }

        @Override
        public void setTellLink(UUID sender, UUID target) {

        }

        @Override
        public void removeTellLink(UUID uuid) {

        }

        @Override
        public UUID getTellLink(UUID uuid) {
            return null;
        }

        @Override
        public void tempMuteAllChannels(long time) {

        }

        @Override
        public void muteAllChannels() {

        }

        @Override
        public void unMuteAllChannels() {

        }

        @Override
        public boolean isChannelsMute() {
            return false;
        }

        @Override
        public boolean isMuted(UUID uuid) {
            return false;
        }

        @Override
        public int secondsMutedLeft(UUID uuid) {
            return 0;
        }

        @Override
        public void mute(UUID uuid, long time) {

        }
    }

}
