package br.com.eterniaserver.eterniaserver.configurations.dependencies;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VaultInterface implements Economy {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "EterniaServer";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return EterniaServer.getEconomyAPI().format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return EterniaServer.getEconomyAPI().pluralName();
    }

    @Override
    public String currencyNameSingular() {
        return EterniaServer.getEconomyAPI().singularName();
    }

    @Override
    public boolean hasAccount(String playerName) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (uuid == null) {
            return false;
        }
        return EterniaServer.getEconomyAPI().hasAccount(uuid);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return EterniaServer.getEconomyAPI().hasAccount(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (uuid == null) {
            return false;
        }
        return EterniaServer.getEconomyAPI().hasAccount(uuid);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return EterniaServer.getEconomyAPI().hasAccount(player.getUniqueId());
    }

    @Override
    public double getBalance(String playerName) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (uuid == null) {
            return 0.0;
        }
        return EterniaServer.getEconomyAPI().getMoney(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return EterniaServer.getEconomyAPI().getMoney(player.getUniqueId());
    }

    @Override
    public double getBalance(String playerName, String world) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (uuid == null) {
            return 0.0;
        }
        return EterniaServer.getEconomyAPI().getMoney(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return EterniaServer.getEconomyAPI().getMoney(player.getUniqueId());
    }

    @Override
    public boolean has(String playerName, double amount) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (uuid == null) {
            return false;
        }
        return EterniaServer.getEconomyAPI().hasMoney(uuid, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return EterniaServer.getEconomyAPI().hasMoney(player.getUniqueId(), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (uuid == null) {
            return false;
        }
        return EterniaServer.getEconomyAPI().hasMoney(uuid, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return EterniaServer.getEconomyAPI().hasMoney(player.getUniqueId(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdraw(UUIDFetcher.getUUIDOf(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdraw(player.getUniqueId(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdraw(UUIDFetcher.getUUIDOf(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdraw(player.getUniqueId(), amount);
    }

    private EconomyResponse withdraw(final UUID uuid, double amount) {
        if (uuid != null && EterniaServer.getEconomyAPI().hasMoney(uuid, amount)) {
            EterniaServer.getEconomyAPI().removeMoney(uuid, amount);
            return new EconomyResponse(amount, EterniaServer.getEconomyAPI().getMoney(uuid), EconomyResponse.ResponseType.SUCCESS, null);
        }
        final double balance = uuid != null ? EterniaServer.getEconomyAPI().getMoney(uuid) : 0.0D;
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return deposit(UUIDFetcher.getUUIDOf(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return deposit(player.getUniqueId(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return deposit(UUIDFetcher.getUUIDOf(playerName), amount);

    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return deposit(player.getUniqueId(), amount);
    }

    private EconomyResponse deposit(final UUID uuid, final double amount) {
        if (uuid != null) {
            EterniaServer.getEconomyAPI().addMoney(uuid, amount);
            return new EconomyResponse(amount, EterniaServer.getEconomyAPI().getMoney(uuid), EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(amount, 0.0D, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse createBank(String playerName, String paramString2) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse deleteBank(String paramString) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse bankBalance(String paramString) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse bankHas(String paramString, double paramDouble) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse bankWithdraw(String paramString, double paramDouble) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse bankDeposit(String paramString, double paramDouble) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse isBankOwner(String paramString1, String paramString2) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse isBankMember(String paramString1, String paramString2) {
        return createUnsupportedResponse();
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return createUnsupportedResponse();
    }

    private EconomyResponse createUnsupportedResponse() {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, EterniaServer.getEconomyAPI().notSupported());
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createAccount(UUIDFetcher.getUUIDOf(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return createAccount(player.getUniqueId());
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createAccount(UUIDFetcher.getUUIDOf(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createAccount(player.getUniqueId());
    }

    private boolean createAccount(final UUID uuid) {
        if (uuid == null) {
            return false;
        }
        if (EterniaServer.getEconomyAPI().hasAccount(uuid)) {
            return false;
        }
        EterniaServer.getEconomyAPI().createAccount(uuid);
        return true;
    }

}