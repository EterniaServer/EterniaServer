package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class APICash {

    private APICash() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the cash balance of the player
     * @param uuid of player
     * @return the cash balance
     */
    public static int getCash(UUID uuid) {
        if (PluginVars.playerProfile.containsKey(uuid)) {
            return PluginVars.playerProfile.get(uuid).cash;
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(PluginConfigs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance, cash)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "', '" + 0 +"')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = EterniaServer.serverConfig.getDouble("money.start");
            PluginVars.playerProfile.put(uuid, playerProfile);
            return 0;
        }
    }

    /**
     * Check if the player has cash enough
     * @param uuid of player
     * @param amount the amount of cash needed
     * @return if has or not
     */
    public static boolean hasCash(UUID uuid, int amount) {
        return getCash(uuid) >= amount;
    }

    /**
     * Defines the cash balance of the player
     * @param uuid of player
     * @param amount the amount of cash to set
     */
    public static void setCash(UUID uuid, int amount) {
        if (PluginVars.playerProfile.containsKey(uuid)) {
            PluginVars.playerProfile.get(uuid).cash = amount;
            EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_PLAYER, PluginConstants.CASH_STR, amount, PluginConstants.UUID_STR, uuid.toString()));
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(PluginConfigs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance, cash)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "', '" + 0 +"')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = EterniaServer.serverConfig.getDouble("money.start");
            PluginVars.playerProfile.put(uuid, playerProfile);
            setCash(uuid, amount);
        }
    }

    /**
     * Add cash to player account
     * @param uuid of player
     * @param amount the amount of cash to add
     */
    public static void addCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) + amount);
    }

    /**
     * Remove cash to player account
     * @param uuid of player
     * @param amount the amount of cash to remove
     */
    public static void removeCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) - amount);
    }

    public static void menuGui(final Player player, int slotInt) {
        switch (slotInt) {
            case 10:
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getPermGui().size(), "Perm");
                for (int i = 0; i < BaseCmdCash.cashGui.getPermGui().size(); i++) {
                    gui.setItem(i, BaseCmdCash.cashGui.getPermGui().get(i));
                }
                player.openInventory(gui);
                break;
            case 12:
                player.closeInventory();
                Inventory paco = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getPacoteGui().size(), "Pacotes");
                for (int i = 0; i < BaseCmdCash.cashGui.getPacoteGui().size(); i++) {
                    paco.setItem(i, BaseCmdCash.cashGui.getPacoteGui().get(i));
                }
                player.openInventory(paco);
                break;
            case 14:
                player.closeInventory();
                Inventory tag = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getTagGui().size(), "Tags");
                for (int i = 0; i < BaseCmdCash.cashGui.getTagGui().size(); i++) {
                    tag.setItem(i, BaseCmdCash.cashGui.getTagGui().get(i));
                }
                player.openInventory(tag);
                break;
            case 16:
                player.closeInventory();
                Inventory spawner = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getSpawnerGui().size(), "Spawners");
                for (int i = 0; i < BaseCmdCash.cashGui.getSpawnerGui().size(); i++) {
                    spawner.setItem(i, BaseCmdCash.cashGui.getSpawnerGui().get(i));
                }
                player.openInventory(spawner);
                break;
            default:
                break;
        }
    }

    public static void permGui(final Player player, final String permString) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (!PluginVars.cashItem.containsKey(uuid)) {
            if (EterniaServer.cashConfig.contains(permString)) {
                final int cost = EterniaServer.cashConfig.getInt(permString + ".cost");
                if (APICash.hasCash(uuid, cost)) {
                    player.sendMessage(PluginMSGs.M_CASH_COST.replace(PluginConstants.AMOUNT, String.valueOf(cost)));
                    player.sendMessage(PluginMSGs.M_CASH);
                    PluginVars.cashItem.put(uuid, permString);
                } else {
                    player.sendMessage(PluginMSGs.M_CASH_NO);
                }
            } else {
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getMenuGui().size(), "Cash");
                for (int i = 0; i < BaseCmdCash.cashGui.getMenuGui().size(); i++) {
                    gui.setItem(i, BaseCmdCash.cashGui.getMenuGui().get(i));
                }
                player.openInventory(gui);
            }
        } else {
            player.sendMessage(PluginMSGs.M_CASH_ALREADY);
            player.sendMessage(PluginMSGs.M_CASH);
        }
    }

}
