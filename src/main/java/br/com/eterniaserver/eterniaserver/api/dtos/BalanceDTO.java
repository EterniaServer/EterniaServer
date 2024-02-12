package br.com.eterniaserver.eterniaserver.api.dtos;

import java.util.UUID;

public record BalanceDTO(UUID playerUUID, Double balance) {
}
