package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.dtos.SearchField;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.EcoBalance;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankBalance;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankMember;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class VaultEconomyManager implements Economy {

    private final EterniaServer plugin;

    private NumberFormat numberFormat;

    public VaultEconomyManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return this.plugin.getString(Strings.ECO_NAME);
    }

    @Override
    public boolean hasBankSupport() {
        return plugin.getBoolean(Booleans.ECONOMY_HAS_BANK);
    }

    @Override
    public int fractionalDigits() {
        return plugin.getInteger(Integers.ECONOMY_COIN_DIGITS);
    }

    @Override
    public String format(double v) {
        if (numberFormat == null) {
            Locale locale = new Locale(plugin.getString(Strings.ECO_LANGUAGE), plugin.getString(Strings.ECO_COUNTRY));
            numberFormat = NumberFormat.getInstance(locale);
        }

        return numberFormat.format(v);
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getString(Strings.ECO_COIN_PLURAL_NAME);
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getString(Strings.ECO_COIN_NAME);
    }

    private EcoBalance getEcoBalance(UUID uuid) {
        EcoBalance ecoBalance = EterniaLib.getDatabase().get(EcoBalance.class, uuid);

        if (ecoBalance != null) {
            return ecoBalance;
        }

        return new EcoBalance();
    }

    @Override
    @Deprecated
    public boolean hasAccount(String playerName) {
        UUID uuid = EterniaLib.getUUIDOf(playerName);
        if (uuid == null) {
            return false;
        }

        return hasAccount(uuid);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return hasAccount(offlinePlayer.getUniqueId());
    }

    @Override
    @Deprecated
    public boolean hasAccount(String playerName, String ignored) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String ignored) {
        return hasAccount(offlinePlayer);
    }

    private boolean hasAccount(UUID uuid) {
        EcoBalance ecoBalance = getEcoBalance(uuid);
        return ecoBalance.getUuid() != null;
    }

    @Override
    @Deprecated
    public double getBalance(String playerName) {
        UUID uuid = EterniaLib.getUUIDOf(playerName);
        if (uuid == null) {
            return 0;
        }

        return getBalance(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return getBalance(offlinePlayer.getUniqueId());
    }

    @Override
    @Deprecated
    public double getBalance(String playerName, String ignored) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String ignored) {
        return getBalance(offlinePlayer);
    }

    private double getBalance(UUID uuid) {
        EcoBalance ecoBalance = getEcoBalance(uuid);
        if (ecoBalance.getBalance() != null) {
            return ecoBalance.getBalance();
        }

        return 0;
    }

    @Override
    @Deprecated
    public boolean has(String playerName, double amount) {
        UUID uuid = EterniaLib.getUUIDOf(playerName);
        if (uuid == null) {
            return false;
        }

        return has(uuid, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return has(offlinePlayer.getUniqueId(), amount);
    }

    @Override
    @Deprecated
    public boolean has(String playerName, String ignored, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String ignored, double amount) {
        return has(offlinePlayer, amount);
    }

    private boolean has(UUID uuid, double amount) {
        EcoBalance ecoBalance = getEcoBalance(uuid);
        if (ecoBalance.getBalance() != null) {
            BigDecimal playerBalance = BigDecimal
                    .valueOf(ecoBalance.getBalance())
                    .setScale(fractionalDigits(), RoundingMode.UP);

            BigDecimal amountToCheck = BigDecimal
                    .valueOf(amount)
                    .setScale(fractionalDigits(), RoundingMode.UP);

            return playerBalance.compareTo(amountToCheck) >= 0;
        }

        return false;
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        UUID uuid = EterniaLib.getUUIDOf(playerName);

        return withdrawPlayer(uuid, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        return withdrawPlayer(offlinePlayer.getUniqueId(), amount);
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, String ignored, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String ignored, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    public EconomyResponse withdrawPlayer(UUID uuid, double amount) {
        if (uuid == null || !has(uuid, amount)) {
            return new EconomyResponse(amount, getBalance(uuid), EconomyResponse.ResponseType.FAILURE, null);
        }

        EcoBalance ecoBalance = getEcoBalance(uuid);
        ecoBalance.setBalance(ecoBalance.getBalance() - amount);

        plugin.getServer().getScheduler().runTaskAsynchronously(
                plugin,
                () -> EterniaLib.getDatabase().update(EcoBalance.class, ecoBalance)
        );

        return new EconomyResponse(amount, ecoBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String playerName, double amount) {
        UUID uuid = EterniaLib.getUUIDOf(playerName);

        return depositPlayer(uuid, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        return depositPlayer(offlinePlayer.getUniqueId(), amount);
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String playerName, String ignored, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String ignored, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    public EconomyResponse depositPlayer(UUID uuid, double amount) {
        if (uuid == null || !hasAccount(uuid)) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        EcoBalance ecoBalance = getEcoBalance(uuid);
        ecoBalance.setBalance(ecoBalance.getBalance() + amount);

        plugin.getServer().getScheduler().runTaskAsynchronously(
                plugin,
                () -> EterniaLib.getDatabase().update(EcoBalance.class, ecoBalance)
        );

        return new EconomyResponse(amount, ecoBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    private BankBalance getBankBalance(String bankName) {
        BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);

        if (bankBalance != null) {
            return bankBalance;
        }

        return new BankBalance();
    }

    @Override
    @Deprecated
    public EconomyResponse createBank(String bankName, String playerName) {
        UUID uuid = EterniaLib.getUUIDOf(bankName);

        return createBank(bankName, uuid);
    }

    @Override
    public EconomyResponse createBank(String bankName, OfflinePlayer offlinePlayer) {
        return createBank(bankName, offlinePlayer.getUniqueId());
    }

    private EconomyResponse createBank(String bankName, UUID uuid) {
        BankBalance bankBalance = getBankBalance(bankName);

        if (bankBalance.getName() != null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        bankBalance.setName(bankName);
        bankBalance.setBalance(0D);
        bankBalance.setTax(0D);

        EterniaLib.getDatabase().insert(BankBalance.class, bankBalance);

        BankMember bankMember = new BankMember();

        bankMember.setBankName(bankName);
        bankMember.setUuid(uuid);
        bankMember.setRole(Enums.BankRole.OWNER.name());

        EterniaLib.getDatabase().insert(BankMember.class, bankMember);

        plugin.bankListName().add(bankName);

        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse deleteBank(String bankName) {
        BankBalance bankBalance = getBankBalance(bankName);

        if (bankBalance.getName() == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        EterniaLib.getDatabase().delete(BankBalance.class, bankName);

        plugin.bankListName().remove(bankName);

        return new EconomyResponse(0, bankBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankBalance(String bankName) {
        BankBalance bankBalance = getBankBalance(bankName);

        if (bankBalance.getName() == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        return new EconomyResponse(0, bankBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankHas(String bankName, double amount) {
        BankBalance bankBalance = getBankBalance(bankName);

        if (bankBalance.getName() == null || bankBalance.getBalance() < amount) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        return new EconomyResponse(amount, bankBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String bankName, double amount) {
        BankBalance bankBalance = getBankBalance(bankName);

        if (bankBalance.getName() == null) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        bankBalance.setBalance(bankBalance.getBalance() - amount);

        plugin.getServer().getScheduler().runTaskAsynchronously(
                plugin,
                () -> EterniaLib.getDatabase().update(BankBalance.class, bankBalance)
        );

        return new EconomyResponse(amount, bankBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }


    @Override
    public EconomyResponse bankDeposit(String bankName, double amount) {
        BankBalance bankBalance = getBankBalance(bankName);

        if (bankBalance.getName() == null) {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        bankBalance.setBalance(bankBalance.getBalance() + amount);

        plugin.getServer().getScheduler().runTaskAsynchronously(
                plugin,
                () -> EterniaLib.getDatabase().update(BankBalance.class, bankBalance)
        );

        return new EconomyResponse(amount, bankBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    @Deprecated
    public EconomyResponse isBankOwner(String bankName, String playerName) {
        UUID uuid = EterniaLib.getUUIDOf(playerName);
        return isBankOwner(bankName, uuid);
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, OfflinePlayer offlinePlayer) {
        return isBankOwner(bankName, offlinePlayer.getUniqueId());
    }

    private EconomyResponse isBankOwner(String bankName, UUID uuid) {
        SearchField bankSearch = new SearchField("bankName", bankName);
        SearchField uuidSearch = new SearchField("uuid", uuid);

        BankMember bankMember = EterniaLib.getDatabase().findBy(BankMember.class, bankSearch, uuidSearch);

        if (bankMember == null || !Enums.BankRole.OWNER.name().equals(bankMember.getRole())) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);

        return new EconomyResponse(0, bankBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    @Deprecated
    public EconomyResponse isBankMember(String bankName, String playerName) {
        UUID uuid = EterniaLib.getUUIDOf(playerName);

        return isBankMember(bankName, uuid);
    }

    @Override
    public EconomyResponse isBankMember(String bankName, OfflinePlayer offlinePlayer) {
        return isBankMember(bankName, offlinePlayer.getUniqueId());
    }

    private EconomyResponse isBankMember(String bankName, UUID uuid) {
        SearchField bankSearch = new SearchField("bankName", bankName);
        SearchField uuidSearch = new SearchField("uuid", uuid);

        BankMember bankMember = EterniaLib.getDatabase().findBy(BankMember.class, bankSearch, uuidSearch);

        if (bankMember == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }

        BankBalance bankBalance = EterniaLib.getDatabase().get(BankBalance.class, bankName);

        return new EconomyResponse(0, bankBalance.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public List<String> getBanks() {
        List<BankBalance> bankBalances = EterniaLib.getDatabase().listAll(BankBalance.class);

        return bankBalances.stream().map(BankBalance::getName).toList();
    }

    @Override
    @Deprecated
    public boolean createPlayerAccount(String playerName) {
        UUID uuid = EterniaLib.getUUIDOf(playerName);

        return createPlayerAccount(uuid);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return createPlayerAccount(offlinePlayer.getUniqueId());
    }

    @Override
    @Deprecated
    public boolean createPlayerAccount(String playerName, String ignored) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String ignored) {
        return createPlayerAccount(offlinePlayer);
    }

    private boolean createPlayerAccount(UUID uuid) {
        EcoBalance ecoBalance = getEcoBalance(uuid);

        if (ecoBalance.getUuid() != null) {
            return false;
        }

        ecoBalance.setUuid(uuid);
        ecoBalance.setBalance(plugin.getDouble(Doubles.ECO_START_MONEY));

        plugin.getServer().getScheduler().runTaskAsynchronously(
                plugin,
                () -> EterniaLib.getDatabase().insert(EcoBalance.class, ecoBalance)
        );

        return true;
    }


}
