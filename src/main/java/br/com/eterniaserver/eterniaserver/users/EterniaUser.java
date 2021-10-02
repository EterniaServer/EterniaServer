package br.com.eterniaserver.eterniaserver.users;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class EterniaUser extends ServerPlayer {

    public EterniaUser(MinecraftServer server, ServerLevel world, GameProfile profile) {
        super(server, world, profile);
    }

}
