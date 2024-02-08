package br.com.eterniaserver.eterniaserver.api.dtos;

import java.util.UUID;

public record BankMemberDTO(String bankName, UUID playerUUID, String playerRole) {
}
