package br.com.eterniaserver.eterniaserver.api;


import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import java.util.UUID;


public interface ProfileManager {

    PlayerProfile create(final UUID uuid, final String playerName);

    PlayerProfile get(final UUID uuid);

}
