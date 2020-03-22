package eternia.vault;

import eternia.api.MoneyAPI;
import eternia.api.UUIDFetcher;
import eternia.player.PlayerManager;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import java.text.DecimalFormat;

public class SystemEconomy extends AbstractEconomy implements UnsupportedBankEconomy {
    private static final DecimalFormat df2 = new DecimalFormat(".##");

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "EterniaServer";
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return df2.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "Eternias";
    }

    @Override
    public String currencyNameSingular() {
        return "Eternia";
    }

    @Override
    public boolean hasAccount(String playerName) {
        UUIDFetcher.getUUID(playerName);
        return PlayerManager.PlayerExist(UUIDFetcher.getUUID(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return PlayerManager.PlayerExist(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return PlayerManager.PlayerExist(UUIDFetcher.getUUID(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return PlayerManager.PlayerExist(player.getUniqueId());
    }

    @Override
    public double getBalance(String playerName) {
        return MoneyAPI.getMoney(UUIDFetcher.getUUID(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return MoneyAPI.getMoney(player.getUniqueId());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return MoneyAPI.getMoney(UUIDFetcher.getUUID(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return MoneyAPI.getMoney(player.getUniqueId());
    }

    @Override
    public boolean has(String playerName, double amount) {
        return amount >= MoneyAPI.getMoney(UUIDFetcher.getUUID(playerName));
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return amount >= MoneyAPI.getMoney(player.getUniqueId());
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return amount >= MoneyAPI.getMoney(UUIDFetcher.getUUID(playerName));
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return amount >= MoneyAPI.getMoney(player.getUniqueId());
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        MoneyAPI.removeMoney(UUIDFetcher.getUUID(playerName), amount);
        return new EconomyResponse(amount, MoneyAPI.getMoney(UUIDFetcher.getUUID(playerName)), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        MoneyAPI.removeMoney(player.getUniqueId(), amount);
        return new EconomyResponse(amount, MoneyAPI.getMoney(player.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        MoneyAPI.removeMoney(UUIDFetcher.getUUID(playerName), amount);
        return new EconomyResponse(amount, MoneyAPI.getMoney(UUIDFetcher.getUUID(playerName)), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        MoneyAPI.removeMoney(player.getUniqueId(), amount);
        return new EconomyResponse(amount, MoneyAPI.getMoney(player.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        MoneyAPI.addMoney(UUIDFetcher.getUUID(playerName), amount);
        return new EconomyResponse(amount, MoneyAPI.getMoney(UUIDFetcher.getUUID(playerName)), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        MoneyAPI.addMoney(player.getUniqueId(), amount);
        return new EconomyResponse(amount, MoneyAPI.getMoney(player.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        MoneyAPI.addMoney(UUIDFetcher.getUUID(playerName), amount);
        return new EconomyResponse(amount, MoneyAPI.getMoney(UUIDFetcher.getUUID(playerName)), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        MoneyAPI.addMoney(player.getUniqueId(), amount);
        return new EconomyResponse(amount, MoneyAPI.getMoney(player.getUniqueId()), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        if (this.hasAccount(playerName)) {
            return false;
        }
        PlayerManager.CreatePlayer(UUIDFetcher.getUUID(playerName));
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (this.hasAccount(player)) {
            return false;
        }
        PlayerManager.CreatePlayer(player.getUniqueId());
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        if (this.hasAccount(playerName)) {
            return false;
        }
        PlayerManager.CreatePlayer(UUIDFetcher.getUUID(playerName));
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        if (this.hasAccount(player)) {
            return false;
        }
        PlayerManager.CreatePlayer(player.getUniqueId());
        return true;
    }
}
