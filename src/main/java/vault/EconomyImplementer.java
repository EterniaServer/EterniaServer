package vault;

import center.Main;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import java.text.DecimalFormat;
import java.util.List;

public class EconomyImplementer implements Economy {

    private final Main plugin;
    DecimalFormat format = new DecimalFormat(".##");
    public EconomyImplementer(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return plugin.getName();
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
        return format.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getConfig().getString("money.plural");
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getConfig().getString("money.singular");
    }

    @Override
    public boolean hasAccount(String player) {

        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String player, String moeda) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String moeda) {
        return false;
    }

    @Override
    public double getBalance(String player) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return 0;
    }

    @Override
    public double getBalance(String player, String moeda) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String moeda) {
        return 0;
    }

    @Override
    public boolean has(String player, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return false;
    }

    @Override
    public boolean has(String player, String moeda, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String moeda, double amount) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount)
    {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, String moeda, double amount)
    {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String moeda, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String player, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String player, String moeda, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String moeda, double amount) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String bank, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String bank, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String bank) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String bank) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String bank, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String bank, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String bank, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String bank, String player) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String bank, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String bank, String player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String bank, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String player, String moeda) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String moeda) {
        return false;
    }
}
