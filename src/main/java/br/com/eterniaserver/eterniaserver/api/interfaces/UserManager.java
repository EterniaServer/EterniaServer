package br.com.eterniaserver.eterniaserver.api.interfaces;


import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import java.util.Set;
import java.util.UUID;


public interface UserManager {

    PlayerProfile create(final UUID uuid, final String playerName);

    PlayerProfile get(final UUID uuid);

    Set<String> getPlayersNames();

}
