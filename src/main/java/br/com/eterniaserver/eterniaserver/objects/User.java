package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

public class User {

    private final String playerName;

    private Player player = null;
    private OfflinePlayer offlinePlayer = null;
    private PlayerProfile playerProfile = null;

    private String playerDisplayName;
    private UUID uuid;
    private boolean firstLogin = false;

    public User(final String playerName) {
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (uuid == null) {
            this.playerName = playerName;
            this.uuid = null;
            return;
        }

        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        final Player player = offlinePlayer.getPlayer();
        if (player == null) {
            this.offlinePlayer = offlinePlayer;
            this.playerName = offlinePlayer.getName();
        } else {
            this.player = player;
            this.playerName = player.getName();
        }
        this.uuid = uuid;
        getInfo();
    }

    public User(UUID uuid) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        final Player player = offlinePlayer.getPlayer();
        if (player == null) {
            this.offlinePlayer = offlinePlayer;
            this.playerName = offlinePlayer.getName();
        } else {
            this.player = player;
            this.playerName = player.getName();
        }
        this.uuid = uuid;
        getInfo();
    }

    public User(Player player) {
        this.player = player;
        this.playerName = player.getName();
        this.uuid = player.getUniqueId();
        getInfo();
    }

    public User(CommandSender sender) {
        if (sender instanceof Player) {
            this.player = (Player) sender;
            this.playerName = player.getName();
            this.uuid = player.getUniqueId();
            getInfo();
        } else {
            this.playerName = sender.getName();
            this.playerDisplayName = sender.getName();
        }
    }
    private void getInfo() {
        if (!EterniaServer.getUserAPI().hasProfile(this.uuid)) {
            EterniaServer.getUserAPI().createProfile(uuid, playerName);
            this.playerProfile = EterniaServer.getUserAPI().getProfile(this.uuid);
            this.playerDisplayName = this.playerProfile.getPlayerDisplayName();
            EterniaServer.getUserAPI().firstLoginMessage(playerName, playerDisplayName);
            if (this.player != null) {
                this.firstLogin = true;
            }
            return;
        }
        this.playerProfile = EterniaServer.getUserAPI().getProfile(this.uuid);
        this.playerDisplayName = this.playerProfile.getPlayerDisplayName();
    }

    // API

    public void teleport() {
        if (!firstLogin) {
            return;
        }
        EterniaServer.getUserAPI().teleportToSpawn(player);
    }

    public boolean hasProfile() {
        return playerProfile != null;
    }

    public void updateProfile() {
        if (playerProfile.getPlayerName() == null) {
            EterniaServer.getUserAPI().createProfile(uuid, playerName);
            this.playerProfile = EterniaServer.getUserAPI().getProfile(uuid);
        }
        EterniaServer.getUserAPI().updateProfile(playerProfile, uuid, playerName);
    }

    public void changeFlyState() {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            return;
        }

        player.setAllowFlight(true);
        player.setFlying(true);
    }

    public boolean isPlayer() {
        return player != null;
    }

    public void setLevel(int level) {
        player.setLevel(level);
    }

    public int getLevel() {
        return player.getLevel();
    }

    public float getGameExp() {
        return player.getExp();
    }

    public void giveGameExp(int amount) {
        player.giveExp(amount);
    }

    public void setGameExp(float amount) {
        player.setExp(amount);
    }

    public boolean isOnPvP() {
        return playerProfile.isOnPvP();
    }

    public int getPvPCooldown() {
        return playerProfile.getOnPvP();
    }

    public void setIsOnPvP() {
        playerProfile.setIsOnPvP();
    }

    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    public void kick(String message) {
        player.kickPlayer(message);
    }

    public Player getPlayer() {
        return player;
    }

    public void clear() {
        EterniaServer.getUserAPI().playerLogout(uuid);
        removeFromTeleporting();
    }

    public void hidePlayer(EterniaServer plugin, Player target) {
        player.hidePlayer(plugin, target);
    }

    public void createKits() {
        EterniaServer.getUserAPI().generatePlayerKits(playerName);
    }

    public boolean isTeleporting() {
        return EterniaServer.getUserAPI().areTeleporting(uuid);
    }

    public void removeFromTeleporting() {
        EterniaServer.getUserAPI().removeFromTeleport(uuid);
    }

    public void putInTeleport(PlayerTeleport playerTeleport) {
        EterniaServer.getUserAPI().putInTeleport(uuid, playerTeleport);
    }

    public Set<String> getHomes() {
        return playerProfile.getHomes();
    }

    public void putGlowing(String nameColor) {
        EterniaServer.getUserAPI().putGlowing(uuid, nameColor);
    }

    public String getGlowColor() {
        return EterniaServer.getUserAPI().getGlowColor(uuid);
    }

    public void putMutedTime(long time) {
        playerProfile.setMuted(time);
    }

    public long getMuteTime() {
        return playerProfile.getMuted();
    }

    public int getChannel() {
        return playerProfile.getChatChannel();
    }

    public void setChannel(int channel) {
        playerProfile.setChatChannel(channel);
    }

    public boolean receivedTell() {
        return EterniaServer.getUserAPI().receivedTell(uuid);
    }

    public String getTellSender() {
        return EterniaServer.getUserAPI().getTellSender(uuid);
    }

    public boolean isTell() {
        return EterniaServer.getUserAPI().isTell(uuid);
    }

    public void setTelling(UUID uuid) {
        EterniaServer.getUserAPI().setTelling(this.uuid, uuid);
    }

    public UUID getTellingPlayerName() {
        return EterniaServer.getUserAPI().getTellingPlayerName(uuid);
    }

    public void removeTelling() {
        EterniaServer.getUserAPI().removeTelling(uuid);
    }

    public void sendPrivate(Player target, String s) {
        EterniaServer.getUserAPI().sendPrivateMessage(target, s, player, uuid, playerName, playerDisplayName);
    }

    public boolean isSpying() {
        return EterniaServer.getUserAPI().isSpying(uuid);
    }

    public void changeSpyState() {
        EterniaServer.getUserAPI().changeSpyState(uuid);
    }

    public void giveExp(int amount) {
        player.giveExp(amount);
    }

    public void updateAfkTime() {
        EterniaServer.getUserAPI().updateAFKTime(uuid);
    }

    public long getAfkTime() {
        return EterniaServer.getUserAPI().getAFKTime(uuid);
    }

    public void changeAfkState() {
        EterniaServer.getUserAPI().changeAFKState(uuid);
    }

    public boolean isAfk() {
        return EterniaServer.getUserAPI().areAFK(uuid);
    }

    public void changeGodModeState() {
        EterniaServer.getUserAPI().changeGodModeState(uuid);
    }

    public boolean getGodMode() {
        return EterniaServer.getUserAPI().getGodMode(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public String getAfkPlaceholder() {
        return EterniaServer.getUserAPI().getAfkPlaceholder(uuid);
    }

    public String getGodeModePlaceholder() {
        return EterniaServer.getUserAPI().getGodeModePlaceholder(uuid);
    }

    public String getFirstLoginPlaceholder() {
        return EterniaServer.getUserAPI().getFirstLoginPlaceholder(playerProfile);
    }

    public long getAndUpdateTimePlayed() {
        return playerProfile.updateTimePlayed();
    }

    public long getFirstLogin() {
        if (player == null) {
            return offlinePlayer.getFirstPlayed();
        }
        return player.getFirstPlayed();
    }

    public long getLastLogin() {
        if (player == null) {
            return offlinePlayer.getLastLogin();
        }
        return player.getLastLogin();
    }

    public boolean isVanished() {
        return EterniaServer.getUserAPI().isVanished(player);
    }

    public void changeVanishState() {
        EterniaServer.getUserAPI().changeVanishState(player);
    }

    public void setItemInMainHand(ItemStack item) {
        player.getInventory().setItemInMainHand(item);
    }

    public ItemStack getItemInMainHand() {
        return player.getInventory().getItemInMainHand();
    }

    public void putBackLocation(Location location) {
        EterniaServer.getUserAPI().putBackLocation(uuid, location);
    }

    public boolean hasBackLocation() {
        return EterniaServer.getUserAPI().hasBackLocation(uuid);
    }

    public Location getBackLocation() {
        return EterniaServer.getUserAPI().getBackLocation(uuid);
    }

    public void changeNick(String nick) {
        EterniaServer.getUserAPI().changeNick(nick, player, playerName, uuid, playerProfile);
    }

    public void setDisplayName() {
        player.setDisplayName(getDisplayName());
    }

    public long getBedCooldown() {
        return EterniaServer.getUserAPI().getBedCooldown(uuid);
    }

    public void updateBedCooldown() {
        EterniaServer.getUserAPI().updateBedCooldown(uuid);
    }

    public int getExp() {
        return playerProfile.getXp();
    }

    public void disableFly() {
        player.setFlying(false);
        player.setAllowFlight(false);
    }

    public void setExp(int amount) {
        EterniaServer.getUserAPI().setExp(playerProfile, amount, uuid);
    }

    public void addExp(int amount) {
        setExp(getExp() + amount);
    }

    public void removeExp(int amount) {
        setExp(getExp() - amount);
    }

    public String getName() {
        return playerName;
    }

    public String getDisplayName() {
        return playerDisplayName;
    }

    public boolean isBalanceTop() {
        return EterniaServer.getEconomyAPI().isBalanceTop(this.uuid);
    }

}
