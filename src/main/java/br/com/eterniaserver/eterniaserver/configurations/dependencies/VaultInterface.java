package br.com.eterniaserver.eterniaserver.configurations.dependencies;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.EconomyRelated;
import br.com.eterniaserver.eterniaserver.enums.Strings;

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
        return EconomyRelated.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return EconomyRelated.pluralName();
    }

    @Override
    public String currencyNameSingular() {
        return EconomyRelated.singularName();
    }

    @Override
    public boolean hasAccount(String playerName) {
        return EconomyRelated.hasAccount(UUIDFetcher.getUUIDOf(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return EconomyRelated.hasAccount(UUIDFetcher.getUUIDOf(player.getName()));
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return EconomyRelated.hasAccount(UUIDFetcher.getUUIDOf(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return EconomyRelated.hasAccount(UUIDFetcher.getUUIDOf(player.getName()));
    }

    @Override
    public double getBalance(String playerName) {
        return EconomyRelated.getMoney(UUIDFetcher.getUUIDOf(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return EconomyRelated.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
    }

    @Override
    public double getBalance(String playerName, String world) {
        return EconomyRelated.getMoney(UUIDFetcher.getUUIDOf(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return EconomyRelated.getMoney(UUIDFetcher.getUUIDOf(player.getName()));
    }

    @Override
    public boolean has(String playerName, double amount) {
        return EconomyRelated.hasMoney(UUIDFetcher.getUUIDOf(playerName), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return EconomyRelated.hasMoney(UUIDFetcher.getUUIDOf(player.getName()), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return EconomyRelated.hasMoney(UUIDFetcher.getUUIDOf(playerName), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return EconomyRelated.hasMoney(UUIDFetcher.getUUIDOf(player.getName()), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdraw(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdraw(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdraw(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdraw(player.getName(), amount);
    }

    private EconomyResponse withdraw(final String playerName, double amount) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (EconomyRelated.hasMoney(uuid, amount)) {
            EconomyRelated.removeMoney(uuid, amount);
            return new EconomyResponse(amount, EconomyRelated.getMoney(uuid), EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(amount, EconomyRelated.getMoney(uuid), EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        EconomyRelated.addMoney(uuid, amount);
        return new EconomyResponse(amount, EconomyRelated.getMoney(uuid), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        EconomyRelated.addMoney(uuid, amount);
        return new EconomyResponse(amount, EconomyRelated.getMoney(uuid), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        EconomyRelated.addMoney(uuid, amount);
        return new EconomyResponse(amount, EconomyRelated.getMoney(uuid), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        EconomyRelated.addMoney(uuid, amount);
        return new EconomyResponse(amount, EconomyRelated.getMoney(uuid), EconomyResponse.ResponseType.SUCCESS, null);
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
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, EterniaServer.getString(Strings.NOT_SUPPORTED));
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (EconomyRelated.hasAccount(uuid)) return false;
        EconomyRelated.createAccount(uuid);
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (EconomyRelated.hasAccount(uuid)) return false;
        EconomyRelated.createAccount(uuid);
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (EconomyRelated.hasAccount(uuid)) return false;
        EconomyRelated.createAccount(uuid);
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (EconomyRelated.hasAccount(uuid)) return false;
        EconomyRelated.createAccount(uuid);
        return true;
    }



}