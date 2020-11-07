package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ConfigDoubles;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Profile;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {

    private Player player = null;
    private OfflinePlayer offlinePlayer = null;
    private CommandSender commandSender = null;
    private PlayerProfile playerProfile = null;

    private String playerName;
    private String playerDisplayName;
    private UUID uuid;

    public User(String playerName) {
        OfflinePlayer offlinePlayerTemp = Bukkit.getOfflinePlayer(UUIDFetcher.getUUIDOf(playerName));
        if (offlinePlayerTemp.isOnline()) {
            this.player = offlinePlayerTemp.getPlayer();
            getInfo(this.player);
        } else {
            this.offlinePlayer = offlinePlayerTemp;
            getInfo(this.offlinePlayer);
        }
    }

    public User(UUID uuid) {
        OfflinePlayer offlinePlayerTemp = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayerTemp.isOnline()) {
            this.player = offlinePlayerTemp.getPlayer();
            getInfo(this.player);
        } else {
            this.offlinePlayer = offlinePlayerTemp;
            getInfo(this.offlinePlayer);
        }
    }

    public User(Player player) {
        if (player.isOnline()) {
            this.player = player;
            getInfo(this.player);
        } else {
            this.offlinePlayer = player;
            getInfo(this.offlinePlayer);
        }
    }

    public User(CommandSender sender) {
        if (sender instanceof Player) {
            this.player = (Player) sender;
            getInfo(this.player);
        } else {
            this.commandSender = sender;
            this.playerName = sender.getName();
            this.playerDisplayName = sender.getName();
        }
    }

    private void getInfo(Player player) {
        this.playerName = player.getName();
        this.playerDisplayName = player.getDisplayName();
        this.uuid = UUIDFetcher.getUUIDOf(this.playerName);
        if (!Vars.playerProfile.containsKey(this.uuid)) {
            createProfile();
        }
        this.playerProfile = Vars.playerProfile.get(this.uuid);
    }

    private void getInfo(OfflinePlayer offlinePlayer) {
        this.playerName = offlinePlayer.getName();
        this.playerDisplayName = offlinePlayer.getName();
        this.uuid = UUIDFetcher.getUUIDOf(this.playerName);
        if (!Vars.playerProfile.containsKey(this.uuid)) {
            createProfile();
        }
        this.playerProfile = Vars.playerProfile.get(this.uuid);
    }

    public boolean hasProfile() {
        return playerProfile != null;
    }

    public void createProfile() {
        final long time = System.currentTimeMillis();

        Insert insert = new Insert(EterniaServer.getString(ConfigStrings.TABLE_PLAYER));
        insert.columns.set("uuid", "player_name", "time", "last", "hours", "balance", "muted");
        insert.values.set(uuid.toString(), playerName, getFirstLogin(), time, 0, EterniaServer.getDouble(ConfigDoubles.START_MONEY), time);
        SQL.executeAsync(insert);

        final PlayerProfile playerProfileTemp = new PlayerProfile(
                playerName,
                getFirstLogin(),
                time,
                0
        );
        playerProfileTemp.setBalance(EterniaServer.getDouble(ConfigDoubles.START_MONEY));
        playerProfileTemp.setMuted(time);
        Vars.playerProfile.put(uuid, playerProfileTemp);
    }

    public void updateProfile() {
        long time = System.currentTimeMillis();
        if (playerProfile.getPlayerName() == null) {
            final PlayerProfile newPlayerProfile = new PlayerProfile(playerName, time, time, 0);
            newPlayerProfile.setCash(playerProfile.getCash());
            newPlayerProfile.setBalance(playerProfile.getBalance());
            newPlayerProfile.setXp(playerProfile.getXp());
            newPlayerProfile.setMuted(time);

            Profile profile = new Profile(EterniaServer.getString(ConfigStrings.TABLE_PLAYER));
            profile.setObjects(playerName, playerName, player.getFirstPlayed(), time, 0, time);
            profile.where.set("uuid", uuid.toString());
            SQL.executeAsync(profile);

            playerProfile = newPlayerProfile;
            Vars.playerProfile.put(uuid, newPlayerProfile);
        }
        playerProfile.setLastLogin(time);
        if (!playerProfile.getPlayerName().equals(playerName)) {
            playerProfile.setPlayerName(playerName);

            Update update = new Update(EterniaServer.getString(ConfigStrings.TABLE_PLAYER));
            update.set.set("player_name", playerName);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
        }
        Update update = new Update(EterniaServer.getString(ConfigStrings.TABLE_PLAYER));
        update.set.set("last", time);
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

    public UUID getTpaSender() {
        return Vars.tpaRequests.get(uuid);
    }

    public void removeTpaRequest() {
        Vars.tpaTime.remove(uuid);
        Vars.tpaRequests.remove(uuid);
    }

    public void putTpaRequest(UUID uuid) {
        Vars.tpaRequests.put(this.uuid, uuid);
        Vars.tpaTime.put(this.uuid, System.currentTimeMillis());
    }

    public boolean hasTpaRequest() {
        return Vars.tpaRequests.containsKey(uuid);
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
        Vars.afkTime.remove(uuid);
        Vars.onAfk.remove(uuid);
        Vars.godMode.remove(uuid);
        Vars.spy.remove(uuid);
        Vars.bedCooldown.remove(uuid);
        Vars.vanished.remove(player);
        removeFromTeleporting();
    }

    public void hidePlayer(EterniaServer plugin, Player target) {
        player.hidePlayer(plugin, target);
    }

    public void createKits() {
        final long time = System.currentTimeMillis();
        for (String kit : EterniaServer.kits.kitList.keySet()) {
            final String kitName = kit + "." + playerName;
            if (!Vars.kitsCooldown.containsKey(kitName)) {
                Insert insert = new Insert(EterniaServer.getString(ConfigStrings.TABLE_KITS));
                insert.columns.set("name", "cooldown");
                insert.values.set(kitName, time);
                SQL.executeAsync(insert);

                Vars.kitsCooldown.put(kitName, time);
            }
        }
    }

    public boolean isTeleporting() {
        return Vars.teleports.containsKey(uuid);
    }

    public void removeFromTeleporting() {
        Vars.teleports.remove(uuid);
    }

    public void putInTeleport(PlayerTeleport playerTeleport) {
        Vars.teleports.put(uuid, playerTeleport);
    }

    public List<String> getHomes() {
        return playerProfile.getHomes();
    }

    public void updateHome(String home) {
        if (!playerProfile.getHomes().contains(home)) {
            playerProfile.getHomes().add(home);
        }
    }

    public void putGlowing(String nameColor) {
        Vars.glowingColor.put(uuid, nameColor);
    }

    public String getGlowColor() {
        return Vars.glowingColor.getOrDefault(uuid, "");
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
        return Vars.tell.containsKey(uuid);
    }

    public String getTellSender() {
        return Vars.tell.get(uuid);
    }

    public boolean isTell() {
        return Vars.chatLocked.containsKey(uuid);
    }

    public void setTelling(UUID uuid) {
        Vars.chatLocked.put(this.uuid, uuid);
    }

    public UUID getTellingPlayerName() {
        return Vars.chatLocked.get(uuid);
    }

    public void removeTelling() {
        Vars.chatLocked.remove(uuid);
    }

    public void sendPrivate(Player target, String s) {
        User user = new User(target);

        Vars.tell.put(user.getUUID(), playerName);
        player.sendMessage(EterniaServer.getMessage(Messages.CHAT_TELL_TO, false, s, playerName, playerDisplayName, user.getName(), user.getDisplayName()));
        target.sendMessage(EterniaServer.getMessage(Messages.CHAT_TELL_FROM, false, s, user.getName(), user.getDisplayName(), playerName, playerDisplayName));

        for (UUID uuidTemp : Vars.spy.keySet()) {
            final Boolean b = Vars.spy.getOrDefault(uuidTemp, false);
            if (Boolean.TRUE.equals(b) && !uuidTemp.equals(this.uuid) && !uuidTemp.equals(user.getUUID())) {
                final Player spyPlayer = Bukkit.getPlayer(uuidTemp);
                if (spyPlayer != null && spyPlayer.isOnline()) {
                    spyPlayer.sendMessage(APIServer.getColor("&8[&7SPY-&6P&8] &8" + playerDisplayName + " -> " + user.getDisplayName() + ": " + s));
                    spyPlayer.sendMessage(APIServer.getColor(EterniaServer.getString(ConfigStrings.CONS_SPY)
                            .replace("{0}", playerName)
                            .replace("{1}", playerDisplayName)
                            .replace("{2}", user.getName())
                            .replace("{3}", user.getDisplayName())
                            .replace("{4}", s)));
                } else {
                    Vars.spy.remove(uuidTemp);
                }
            }
        }
    }

    public void sendStaffMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(EterniaServer.getString(ConfigStrings.PERM_CHAT_STAFF))) {
                String format = EterniaServer.chat.staffFormat;
                format = APIServer.setPlaceholders(player, format);
                format = APIServer.getColor(format.replace("%message%", message));
                p.sendMessage(format);
            }
        }
    }

    public void sendLocalMessage(String message, int radius) {
        int pes = 0;
        final String format = getLocalFormat(message);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Vars.ignoredPlayer.get(uuid) != null && Vars.ignoredPlayer.get(uuid).contains(p)) return;
            final Boolean b = Vars.spy.get(uuid);
            if ((player.getWorld() == p.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= Math.pow(radius, 2)) || radius <= 0) {
                pes += 1;
                p.sendMessage(format);
            } else if (p.hasPermission(EterniaServer.getString(ConfigStrings.PERM_SPY)) && Boolean.TRUE.equals(b)) {
                p.sendMessage(APIServer.getColor(EterniaServer.getString(ConfigStrings.CONS_SPY_LOCAL)
                                .replace("{0}", playerName)
                                .replace("{1}", playerDisplayName)
                                .replace("{2}", message)));
            }
        }
        if (pes <= 1) {
            sendMessage(Messages.CHAT_NO_ONE_NEAR);
        }
    }

    public boolean isSpying() {
        return Vars.spy.getOrDefault(uuid, false);
    }

    public void changeSpyState() {
        Vars.spy.put(uuid, !Vars.spy.getOrDefault(uuid, false));
    }

    public void giveExp(int amount) {
        player.giveExp(amount);
    }

    private String getLocalFormat(String message) {
        String format = APIServer.setPlaceholders(player, EterniaServer.chat.localFormat);
        if (player.hasPermission(EterniaServer.getString(ConfigStrings.PERM_CHAT_COLOR))) {
            return APIServer.getColor(format.replace("%message%", message));
        } else {
            return(format.replace("%message%", message));
        }
    }

    public void updateAfkTime() {
        Vars.afkTime.put(uuid, System.currentTimeMillis());
    }

    public long getAfkTime() {
        return Vars.afkTime.getOrDefault(uuid, System.currentTimeMillis());
    }

    public void changeAfkState() {
        Vars.onAfk.put(uuid, !Vars.onAfk.getOrDefault(uuid, false));
    }

    public boolean isAfk() {
        return Vars.onAfk.getOrDefault(uuid, false);
    }

    public void changeGodModeState() {
        Vars.godMode.put(uuid, !Vars.godMode.getOrDefault(uuid, false));
    }

    public boolean getGodMode() {
        return Vars.godMode.getOrDefault(uuid, false);
    }

    public String getName() {
        return playerName;
    }

    public String getDisplayName() {
        return playerProfile.getPlayerDisplayName();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public String getAfkPlaceholder() {
        return isAfk() ? EterniaServer.getString(ConfigStrings.AFK_PLACEHOLDER) : "";
    }

    public String getGodeModePlaceholder() {
        return getGodMode() ? EterniaServer.getString(ConfigStrings.GOD_PLACEHOLDER) : "";
    }

    public String getFirstLoginPlaceholder() {
        if (playerProfile != null) {
            return new SimpleDateFormat(EterniaServer.getString(ConfigStrings.DATA_FORMAT)).format(new Date(playerProfile.getFirstLogin()));
        }
        return EterniaServer.getString(ConfigStrings.NO_REGISTER);
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
        return Vars.vanished.getOrDefault(player, false);
    }

    public void changeVanishState() {
        Vars.vanished.put(player, !Vars.vanished.getOrDefault(player, false));
    }

    public void setItemInMainHand(ItemStack item) {
        player.getInventory().setItemInMainHand(item);
    }

    public ItemStack getItemInMainHand() {
        return player.getInventory().getItemInMainHand();
    }

    public void putBackLocation(Location location) {
        Vars.back.put(uuid, location);
    }

    public boolean hasBackLocation() {
        return Vars.back.containsKey(uuid);
    }

    public Location getBackLocation() {
        return Vars.back.get(uuid);
    }

    public void requestNickChange(String nick) {
        nick = APIServer.getColor(nick);

        if (nick.equals(EterniaServer.getString(ConfigStrings.CLEAR_STRING))) {
            sendMessage(Messages.CHAT_NICK_CLEAR);
            clearNickName();
            return;
        }

        if (!hasPermission(EterniaServer.getString(ConfigStrings.PERM_CHAT_COLOR_NICK))) {
            nick = ChatColor.stripColor(nick);
        }

        sendMessage(Messages.CHAT_NICK_CHANGE_REQUEST, nick, String.valueOf(EterniaServer.getDouble(ConfigDoubles.NICK_COST)));
        playerProfile.setTempNick(nick);
        playerProfile.setNickRequest(true);
        sendMessage(Messages.CHAT_NICK_USE);
    }

    public void setTempNickName(String nick) {
        playerProfile.setTempNick(nick);
        playerProfile.setNickRequest(true);
    }

    public void updateNickName() {
        playerProfile.setPlayerDisplayName(playerProfile.getTempNick());
        player.setDisplayName(playerProfile.getTempNick());
        playerProfile.setNickRequest(false);

        Update update = new Update(EterniaServer.getString(ConfigStrings.TABLE_PLAYER));
        update.set.set("player_display", playerProfile.getPlayerDisplayName());
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
    }

    public void setDisplayName() {
        playerProfile.setPlayerDisplayName(getDisplayName());
        player.setDisplayName(getDisplayName());
    }

    public void clearNickName() {
        playerProfile.setPlayerDisplayName(playerName);
        player.setDisplayName(playerName);

        Update update = new Update(EterniaServer.getString(ConfigStrings.TABLE_PLAYER));
        update.set.set("player_display", playerProfile.getPlayerDisplayName());
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
    }

    public void removeNickRequest() {
        playerProfile.setNickRequest(false);
    }

    public boolean hasNickRequest() {
        return playerProfile.isNickRequest();
    }

    public long getBedCooldown() {
        return Vars.bedCooldown.getOrDefault(uuid, 0L);
    }

    public void updateBedCooldown() {
        Vars.bedCooldown.put(uuid, System.currentTimeMillis());
    }

    public int getExp() {
        return playerProfile.getXp();
    }

    public void setExp(int amount) {
        playerProfile.setXp(amount);

        Update update = new Update(EterniaServer.getString(ConfigStrings.TABLE_PLAYER));
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


}
