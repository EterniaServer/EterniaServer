package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
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
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUIDFetcher.getUUIDOf(playerName));
        if (offlinePlayer.isOnline()) {
            this.player = offlinePlayer.getPlayer();
            getInfo(this.player);
        } else {
            this.offlinePlayer = offlinePlayer;
            getInfo(this.offlinePlayer);
        }
    }

    public User(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayer.isOnline()) {
            this.player = offlinePlayer.getPlayer();
            getInfo(this.player);
        } else {
            this.offlinePlayer = offlinePlayer;
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
        EQueries.executeQuery(Constants.getQueryInsert(EterniaServer.configs.tablePlayer, "(uuid, player_name, time, last, hours, balance, muted)",
                "('" + uuid.toString() + "', '" + playerName + "', '" + getFirstLogin() + "', '" + time + "', '" + 0 + "', '" + EterniaServer.configs.startMoney + "', '" + time + "')"));
        final PlayerProfile playerProfile = new PlayerProfile(
                playerName,
                getFirstLogin(),
                time,
                0
        );
        playerProfile.setBalance(EterniaServer.configs.startMoney);
        playerProfile.setMuted(time);
        Vars.playerProfile.put(uuid, playerProfile);
    }

    public void updateProfile() {
        long time = System.currentTimeMillis();
        if (playerProfile.getPlayerName() == null) {
            final PlayerProfile newPlayerProfile = new PlayerProfile(playerName, time, time, 0);
            newPlayerProfile.setCash(playerProfile.getCash());
            newPlayerProfile.setBalance(playerProfile.getBalance());
            newPlayerProfile.setXp(playerProfile.getXp());
            newPlayerProfile.setMuted(time);
            EQueries.executeQuery(
                    "UPDATE " + EterniaServer.configs.tablePlayer +
                            " SET player_name='" + playerName +
                            "', player_display='" + playerName +
                            "', time='" + player.getFirstPlayed() +
                            "', last='" + time +
                            "', hours='" + 0 +
                            "', muted='" + time +
                            "' WHERE uuid='" + uuid.toString() + "'");
            playerProfile = newPlayerProfile;
            Vars.playerProfile.put(uuid, newPlayerProfile);
            this.playerProfile = newPlayerProfile;
        }
        playerProfile.setLastLogin(time);
        if (!playerProfile.getPlayerName().equals(playerName)) {
            playerProfile.setPlayerName(playerName);
            EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "player_name", playerName, "uuid", uuid.toString()));
        }
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "last", time, "uuid", uuid.toString()));
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
        Vars.tpaRequests.put(uuid, this.uuid);
        Vars.tpaTime.put(uuid, System.currentTimeMillis());
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
            EterniaServer.msg.sendMessage(commandSender, message, args);
            return;
        }

        EterniaServer.msg.sendMessage(player, message, args);
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
                EQueries.executeQuery(Constants.getQueryInsert(EterniaServer.configs.tableKits, "name", kitName, "cooldown", time));
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
        player.sendMessage(EterniaServer.msg.getMessage(Messages.CHAT_TELL_TO, false, s, playerName, playerDisplayName, user.getName(), user.getDisplayName()));
        target.sendMessage(EterniaServer.msg.getMessage(Messages.CHAT_TELL_FROM, false, s, user.getName(), user.getDisplayName(), playerName, playerDisplayName));

        for (UUID uuid : Vars.spy.keySet()) {
            final Boolean b = Vars.spy.getOrDefault(uuid, false);
            if (Boolean.TRUE.equals(b) && !uuid.equals(this.uuid) && !uuid.equals(user.getUUID())) {
                final Player spyPlayer = Bukkit.getPlayer(uuid);
                if (spyPlayer != null && spyPlayer.isOnline()) {
                    spyPlayer.sendMessage(APIServer.getColor("&8[&7SPY-&6P&8] &8" + playerDisplayName + " -> " + user.getDisplayName() + ": " + s));
                } else {
                    Vars.spy.remove(uuid);
                }
            }
        }
    }

    public void sendStaffMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
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
            } else if (p.hasPermission("eternia.spy") && Boolean.TRUE.equals(b)) {
                p.sendMessage(APIServer.getColor("&8[&7SPY&8-&eL&8] &8" + playerDisplayName + ": " + message));
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
        if (player.hasPermission("eternia.chat.color")) {
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
        return playerDisplayName;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public String getAfkPlaceholder() {
        return isAfk() ? EterniaServer.constants.afkPlaceholder : "";
    }

    public String getGodeModePlaceholder() {
        return getGodMode() ? EterniaServer.constants.godPlaceholder : "";
    }

    public String getFirstLoginPlaceholder() {
        if (playerProfile != null) {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(playerProfile.getFirstLogin()));
        }
        return "Sem registro";
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

    public boolean hasFreeSlot() {
        return player.getInventory().firstEmpty() != -1;
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

    public void setDisplayName(String displayName) {
        this.playerDisplayName = displayName;
        this.player.setDisplayName(displayName);
    }

    public long getBedCooldown() {
        return Vars.bedCooldown.getOrDefault(uuid, 0L);
    }

    public void updateBedCooldown() {
        Vars.bedCooldown.put(uuid, System.currentTimeMillis());
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public boolean hasNickRequest() {
        return playerProfile.isNickRequest();
    }

    public void updateNickName() {
        player.setDisplayName(playerProfile.getTempNick());
        EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CHANGED, player.getDisplayName());
        playerProfile.setPlayerDisplayName(playerProfile.getTempNick());
        saveToSQL(uuid);
    }

    public void removeNickRequest() {
        playerProfile.setTempNick(null);
        playerProfile.setNickRequest(false);
    }

    public void playerNick(String string) {
        if (string.equals("clear")) {
            player.setDisplayName(playerName);
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CLEAR);
            return;
        }

        if (player.hasPermission("eternia.chat.color.nick")) {
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CHANGE_REQUEST, APIServer.getColor(string), String.valueOf(EterniaServer.configs.nickCost));
            playerProfile.setTempNick(string);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CHANGE_REQUEST, string, String.valueOf(EterniaServer.configs.nickCost));
            playerProfile.setTempNick(ChatColor.stripColor(string));
        }

        playerProfile.setTempNick(string);
        playerProfile.setNickRequest(true);
        EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_USE);
    }

    public void staffNick(final OnlinePlayer target, final Player player, final String string) {
        if (target != null) {
            changeNickName(target.getPlayer(), player, string);
            return;
        }

        if (string.equals("clear")) {
            final String playerName = player.getName();
            final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
            player.setDisplayName(playerName);
            playerProfile.setPlayerDisplayName(playerName);
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CLEAR);
            saveToSQL(uuid);
            return;
        }

        player.setDisplayName(APIServer.getColor(string));
    }

    private void changeNickName(final Player target, final Player player, final String string) {
        final String targetName = target.getName();
        if (string.equals("clear")) {
            EterniaServer.msg.sendMessage(target, Messages.CHAT_NICK_CLEAR_BY, player.getName(), player.getDisplayName());
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CLEAR_FROM, targetName, target.getDisplayName());
            target.setDisplayName(targetName);
        } else {
            EterniaServer.msg.sendMessage(target, Messages.CHAT_NICK_CHANGED_BY, string, player.getName(), player.getDisplayName());
            EterniaServer.msg.sendMessage(player, Messages.CHAT_NICK_CHANGED_FROM, string, player.getName(), player.getDisplayName());
            target.setDisplayName(string);
        }
    }

    private void saveToSQL(UUID uuid) {
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "player_display", playerProfile.getPlayerDisplayName(), "uuid", uuid.toString()));
    }

    public int getExp() {
        return playerProfile.getXp();
    }

    public void setExp(int amount) {
        playerProfile.setXp(amount);
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "xp", amount, "uuid", uuid.toString()));
    }

    public void addExp(int amount) {
        setExp(getExp() + amount);
    }

    public void removeExp(int amount) {
        setExp(getExp() - amount);
    }


}
