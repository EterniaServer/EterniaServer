package br.com.eterniaserver.eterniaserver.api.interfaces;

import net.kyori.adventure.text.TextReplacementConfig;

import java.util.UUID;
import java.util.regex.Pattern;


public interface ChatAPI {

    TextReplacementConfig getFilter();

    void setFilter(Pattern filter);

    void setTellLink(UUID sender, UUID target);

    void removeTellLink(UUID uuid);

    UUID getTellLink(UUID uuid);

    void tempMuteAllChannels(long time);

    void muteAllChannels();

    void unMuteAllChannels();

    boolean isChannelsMute();

    boolean isMuted(UUID uuid);

    int secondsMutedLeft(UUID uuid);

    void mute(UUID uuid, long time);

}
