package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.PlayerRelated;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class User {

    private final String playerName;

    private Player player = null;
    private OfflinePlayer offlinePlayer = null;
    private CommandSender commandSender = null;
    private PlayerProfile playerProfile = null;

    private String playerDisplayName;
    private UUID uuid;
    private boolean firstLogin = false;

    public User(String playerName) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUIDFetcher.getUUIDOf(playerName));
        final Player player = offlinePlayer.getPlayer();
        if (player == null) {
            this.offlinePlayer = offlinePlayer;
            this.playerName = offlinePlayer.getName();
        } else {
            this.player = player;
            this.playerName = player.getName();
        }
        this.uuid = UUIDFetcher.getUUIDOf(this.playerName);
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
        this.uuid = UUIDFetcher.getUUIDOf(this.playerName);
        getInfo();
    }

    public User(Player player) {
        this.player = player;
        this.playerName = player.getName();
        this.uuid = UUIDFetcher.getUUIDOf(this.playerName);
        getInfo();
    }

    public User(OfflinePlayer offlinePlayer) {
        final Player player = offlinePlayer.getPlayer();
        if (player == null) {
            this.offlinePlayer = offlinePlayer;
            this.playerName = offlinePlayer.getName();
        } else {
            this.player = player;
            this.playerName = player.getName();
        }
        this.uuid = UUIDFetcher.getUUIDOf(this.playerName);
        getInfo();
    }

    public User(CommandSender sender) {
        if (sender instanceof Player) {
            this.player = (Player) sender;
            this.playerName = player.getName();
            this.uuid = UUIDFetcher.getUUIDOf(this.playerName);
            getInfo();
        } else {
            this.commandSender = sender;
            this.playerName = sender.getName();
            this.playerDisplayName = sender.getName();
        }
    }
    private void getInfo() {
        if (!PlayerRelated.hasProfile(this.uuid)) {
            PlayerRelated.createProfile(uuid, playerName);
            this.playerProfile = PlayerRelated.getProfile(this.uuid);
            this.playerDisplayName = this.playerProfile.getPlayerDisplayName();
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SERVER_FIRST_LOGIN, true, playerName, playerDisplayName));
            if (this.player != null) {
                this.firstLogin = true;
            }
            return;
        }
        this.playerProfile = PlayerRelated.getProfile(this.uuid);
        this.playerDisplayName = this.playerProfile.getPlayerDisplayName();
    }

    public void teleport() {
        if (!firstLogin) {
            return;
        }

        player.teleport(ServerRelated.getLocation("warp.spawn"));
    }

    public boolean hasProfile() {
        return playerProfile != null;
    }

    public void updateProfile() {
        if (playerProfile.getPlayerName() == null) {
            PlayerRelated.createProfile(uuid, playerName);
            this.playerProfile = PlayerRelated.getProfile(uuid);
        }
        
        playerProfile.setLastLogin(System.currentTimeMillis());
        if (!playerProfile.getPlayerName().equals(playerName)) {
            playerProfile.setPlayerName(playerName);

            Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
            update.set.set("player_name", playerName);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
        }

        Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
        update.set.set("last", System.currentTimeMillis());
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
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

    public void sendMessage(Messages message, String... args) {
        if (player == null) {
            EterniaServer.sendMessage(commandSender, message, args);
            return;
        }

        EterniaServer.sendMessage(player, message, args);
    }

    public void clear() {
        PlayerRelated.playerLogout(uuid);
        removeFromTeleporting();
    }

    public void hidePlayer(EterniaServer plugin, Player target) {
        player.hidePlayer(plugin, target);
    }

    public void createKits() {
        PlayerRelated.generatePlayerKits(playerName);
    }

    public boolean isTeleporting() {
        return PlayerRelated.areTeleporting(uuid);
    }

    public void removeFromTeleporting() {
        PlayerRelated.removeFromTeleport(uuid);
    }

    public void putInTeleport(PlayerTeleport playerTeleport) {
        PlayerRelated.putInTeleport(uuid, playerTeleport);
    }

    public Set<String> getHomes() {
        return playerProfile.getHomes();
    }

    public void updateHome(String home) {
        if (!playerProfile.getHomes().contains(home)) {
            playerProfile.getHomes().add(home);
        }
    }

    public void putGlowing(String nameColor) {
        PlayerRelated.putGlowing(uuid, nameColor);
    }

    public String getGlowColor() {
        return PlayerRelated.getGlowColor(uuid);
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
        return PlayerRelated.receivedTell(uuid);
    }

    public String getTellSender() {
        return PlayerRelated.getTellSender(uuid);
    }

    public boolean isTell() {
        return PlayerRelated.isTell(uuid);
    }

    public void setTelling(UUID uuid) {
        PlayerRelated.setTelling(this.uuid, uuid);
    }

    public UUID getTellingPlayerName() {
        return PlayerRelated.getTellingPlayerName(uuid);
    }

    public void removeTelling() {
        PlayerRelated.removeTelling(uuid);
    }

    public void sendPrivate(Player target, String s) {
        User user = new User(target);

        PlayerRelated.putInTell(user.getUUID(), playerName);
        player.sendMessage(EterniaServer.getMessage(Messages.CHAT_TELL_TO, false, s, playerName, playerDisplayName, user.getName(), user.getDisplayName()));
        target.sendMessage(EterniaServer.getMessage(Messages.CHAT_TELL_FROM, false, s, user.getName(), user.getDisplayName(), playerName, playerDisplayName));

        for (UUID uuidTemp : PlayerRelated.getSpyKeySet()) {
            if (PlayerRelated.isSpying(uuidTemp) && !uuidTemp.equals(this.uuid) && !uuidTemp.equals(user.getUUID())) {
                Player spyPlayer = Bukkit.getPlayer(uuidTemp);
                if (spyPlayer != null && spyPlayer.isOnline()) {
                    spyPlayer.sendMessage(ServerRelated.getColor(EterniaServer.getString(Strings.CONS_SPY)
                            .replace("{0}", playerName)
                            .replace("{1}", playerDisplayName)
                            .replace("{2}", user.getName())
                            .replace("{3}", user.getDisplayName())
                            .replace("{4}", s)));
                } else {
                    PlayerRelated.removeFromSpy(uuidTemp);
                }
            }
        }
    }

    public boolean isSpying() {
        return PlayerRelated.isSpying(uuid);
    }

    public void changeSpyState() {
        PlayerRelated.changeSpyState(uuid);
    }

    public void giveExp(int amount) {
        player.giveExp(amount);
    }

    public void updateAfkTime() {
        PlayerRelated.updateAFKTime(uuid);
    }

    public long getAfkTime() {
        return PlayerRelated.getAFKTime(uuid);
    }

    public void changeAfkState() {
        PlayerRelated.changeAFKState(uuid);
    }

    public boolean isAfk() {
        return PlayerRelated.areAFK(uuid);
    }

    public void changeGodModeState() {
        PlayerRelated.changeGodModeState(uuid);
    }

    public boolean getGodMode() {
        return PlayerRelated.getGodMode(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public String getAfkPlaceholder() {
        return isAfk() ? EterniaServer.getString(Strings.AFK_PLACEHOLDER) : "";
    }

    public String getGodeModePlaceholder() {
        return getGodMode() ? EterniaServer.getString(Strings.GOD_PLACEHOLDER) : "";
    }

    public String getFirstLoginPlaceholder() {
        if (playerProfile != null) {
            return new SimpleDateFormat(EterniaServer.getString(Strings.DATA_FORMAT)).format(new Date(playerProfile.getFirstLogin()));
        }
        return EterniaServer.getString(Strings.NO_REGISTER);
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
        return PlayerRelated.isVanished(player);
    }

    public void changeVanishState() {
        PlayerRelated.changeVanishState(player);
    }

    public void setItemInMainHand(ItemStack item) {
        player.getInventory().setItemInMainHand(item);
    }

    public ItemStack getItemInMainHand() {
        return player.getInventory().getItemInMainHand();
    }

    public void putBackLocation(Location location) {
        PlayerRelated.putBackLocation(uuid, location);
    }

    public boolean hasBackLocation() {
        return PlayerRelated.hasBackLocation(uuid);
    }

    public Location getBackLocation() {
        return PlayerRelated.getBackLocation(uuid);
    }

    public void changeNick(String nick) {
        if (nick.equals(EterniaServer.getString(Strings.CLEAR_STRING))) {
            sendMessage(Messages.CHAT_NICK_CLEAR);
            playerProfile.setPlayerDisplayName(playerName);
            player.setDisplayName(playerName);
    
            Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
            update.set.set("player_display", playerProfile.getPlayerDisplayName());
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
            return;
        }

        if (!hasPermission(EterniaServer.getString(Strings.PERM_CHAT_COLOR_NICK))) {
            nick = ChatColor.stripColor(nick);
        }

        playerProfile.setPlayerDisplayName(nick);
        player.setDisplayName(nick);
        sendMessage(Messages.CHAT_NICK_CHANGE, nick);

        Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
        update.set.set("player_display", playerProfile.getPlayerDisplayName());
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
    }

    public void setDisplayName() {
        player.setDisplayName(getDisplayName());
    }

    public long getBedCooldown() {
        return ServerRelated.getBedCooldown(uuid);
    }

    public void updateBedCooldown() {
        ServerRelated.updateBedCooldown(uuid);
    }

    public int getExp() {
        return playerProfile.getXp();
    }

    public void disableFly() {
        player.setFlying(false);
        player.setAllowFlight(false);
    }

    public void setExp(int amount) {
        playerProfile.setXp(amount);

        Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
        update.set.set("xp", amount);
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
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

}
