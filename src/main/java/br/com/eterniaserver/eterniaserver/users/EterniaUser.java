package br.com.eterniaserver.eterniaserver.users;

import net.minecraft.server.level.ServerPlayer;

import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;

public class EterniaUser extends CraftPlayer {

    public EterniaUser(CraftServer server, ServerPlayer entity) {
        super(server, entity);
    }
}
