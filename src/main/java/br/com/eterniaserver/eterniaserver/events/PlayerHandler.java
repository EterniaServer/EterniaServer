package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.ConfigBooleans;
import br.com.eterniaserver.eterniaserver.enums.ConfigIntegers;
import br.com.eterniaserver.eterniaserver.enums.ConfigLists;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerHandler implements Listener {

    private final EterniaServer plugin;
    private final List<Material> list = List.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL);

    public PlayerHandler(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!EterniaServer.getBoolean(ConfigBooleans.MODULE_HOMES) && !EterniaServer.getBoolean(ConfigBooleans.MODULE_EXPERIENCE) && !EterniaServer.getBoolean(ConfigBooleans.MODULE_SPAWNERS)) return;

        User user = new User(event.getPlayer());
        final Action action = event.getAction();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            final ItemStack is = user.getItemInMainHand();
            final List<String> lore = is.getLore();
            event.setCancelled(loadHomes(is, lore, user));
            loadModuleExp(is, lore, user);
            if (event.getClickedBlock() != null && list.contains(event.getClickedBlock().getType())) {
                final Location location = event.getClickedBlock().getLocation();
                location.getNearbyEntities(2, 2, 2).forEach(k -> {
                    if (k instanceof Minecart) {
                        event.setCancelled(true);
                    }
                });
            }
        }
        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_SPAWNERS) && event.getClickedBlock() != null
                && action.equals(Action.RIGHT_CLICK_BLOCK) && event.getItem() != null
                && event.getClickedBlock().getType() == Material.SPAWNER
                && !user.hasPermission(EterniaServer.getString(ConfigStrings.PERM_SPAWNERS_CHANGE))) {
            event.setCancelled(true);
        }
    }

    private void loadModuleExp(ItemStack is, List<String> lore, User user) {
        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_EXPERIENCE) && is.getType().equals(Material.EXPERIENCE_BOTTLE)
                && lore != null) {
            user.setItemInMainHand(new ItemStack(Material.AIR));
            user.giveExp(Integer.parseInt(lore.get(0)));
        }
    }

    private boolean loadHomes(ItemStack is, List<String> lore, User user) {
        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_HOMES) && is.getType().equals(Material.COMPASS)
                && lore != null) {
            final String[] isso = lore.get(0).split(":");
            final Location location = new Location(Bukkit.getWorld(isso[0]), Double.parseDouble(isso[1]) + 1, Double.parseDouble(isso[2]), Double.parseDouble(isso[3]), Float.parseFloat(isso[4]), Float.parseFloat(isso[5]));
            String nome = is.getItemMeta().getDisplayName();
            nome = nome.replace("[", "").replace("]", "");
            nome = ChatColor.stripColor(nome);
            if (user.isTeleporting()) {
                user.sendMessage(Messages.SERVER_IN_TELEPORT);
            } else {
                user.putInTeleport( new PlayerTeleport(user.getPlayer(), location, EterniaServer.getMessage(Messages.HOME_GOING, true, nome)));
            }
            return true;
        }
        return false;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_TELEPORTS)) {
            final Player player = event.getEntity();
            new User(player).putBackLocation(player.getLocation());
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) return;

        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_TELEPORTS)) {
            new User(event.getPlayer()).putBackLocation(event.getFrom());
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_TELEPORTS) && APIServer.hasLocation("warp.spawn") && (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - event.getPlayer().getFirstPlayed()) < 10)) {
            event.setSpawnLocation(APIServer.getLocation("warp.spawn"));
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_TELEPORTS) && APIServer.hasLocation("warp.spawn")) {
            event.setRespawnLocation(APIServer.getLocation("warp.spawn"));
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event) {
        User user = new User(event.getPlayer());
        user.clear();

        Update update = new Update(EterniaServer.getString(ConfigStrings.TABLE_PLAYER));
        update.set.set("hours", user.getAndUpdateTimePlayed());
        update.where.set("uuid", user.getUUID().toString());
        SQL.executeAsync(update);

        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SERVER_LOGOUT, true, user.getName(), user.getDisplayName()));
        event.setQuitMessage(null);

    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.equalsIgnoreCase("/tps")) {
            EterniaServer.sendMessage(event.getPlayer(), Messages.SERVER_TPS, String.format("%.2f", Bukkit.getTPS()[0]));
            event.setCancelled(true);
            return;
        }
        for (String line : EterniaServer.getStringList(ConfigLists.BLACKLISTED_COMMANDS)) {
            if (message.startsWith(line)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (!EterniaServer.getBoolean(ConfigBooleans.MODULE_BED)) return;

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            User user = new User(event.getPlayer());
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.getBedCooldown()) > 6) {
                user.updateBedCooldown();
                Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.NIGHT_PLAYER_SLEEPING, true, user.getName(), user.getDisplayName()));
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        if (!EterniaServer.getBoolean(ConfigBooleans.MODULE_BED)) return;

        User user = new User(event.getPlayer());
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.getBedCooldown()) > 6) {
            user.updateBedCooldown();
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_ELEVATOR) && player.hasPermission(EterniaServer.getString(ConfigStrings.PERM_ELEVATOR)) && !player.isSneaking()) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            for (Object valueObject : EterniaServer.getElevatorMaterials()) {
                Material value = (Material) valueObject;
                if (value == material) {
                    block = block.getRelative(BlockFace.DOWN, EterniaServer.getInteger(ConfigIntegers.ELEVATOR_MIN));
                    int i;
                    for (i = EterniaServer.getInteger(ConfigIntegers.ELEVATOR_MAX); i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.DOWN)) --i;
                    elevatorDown(player, i);
                    break;
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJump(PlayerJumpEvent event) {
        final Player player = event.getPlayer();
        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_ELEVATOR) && player.hasPermission(EterniaServer.getString(ConfigStrings.PERM_ELEVATOR))) {
            Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            for (Object valueObject : EterniaServer.getElevatorMaterials()) {
                Material value = (Material) valueObject;
                if (value == material) {
                    block = block.getRelative(BlockFace.UP, EterniaServer.getInteger(ConfigIntegers.ELEVATOR_MIN));
                    int i;
                    for (i = EterniaServer.getInteger(ConfigIntegers.ELEVATOR_MAX); i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.UP)) -- i;
                    elevatorUp(player, i);
                    break;
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = new User(event.getPlayer());

        event.setJoinMessage(null);

        if (!user.hasProfile()) {
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SERVER_FIRST_LOGIN, true, user.getName(), user.getDisplayName()));
            user.createProfile();
        } else {
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SERVER_LOGIN, true, user.getName(), user.getDisplayName()));
            user.updateProfile();
        }

        if (EterniaServer.getBoolean(ConfigBooleans.MODULE_CHAT)) {
            if (user.hasPermission(EterniaServer.getString(ConfigStrings.PERM_SPY))) {
                user.changeSpyState();
            }
            user.setDisplayName();
        }

        for (Player player : APIServer.getVanishList()) {
            user.hidePlayer(plugin, player);
        }

        if (user.getChannel() == 0) {
            user.setChannel(EterniaServer.getInteger(ConfigIntegers.DEFAULT_CHANNEL));
        }

        user.updateAfkTime();
        user.createKits();
    }

    private void elevatorUp(final Player player, final int i) {
        if (i > 0) {
            Location location = player.getLocation();
            location.setY((location.getY() + EterniaServer.getInteger(ConfigIntegers.ELEVATOR_MAX) + 3.0D - (double) i) - 1);
            PaperLib.teleportAsync(player, location);
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
        }
    }

    private void elevatorDown(final Player player, final int i) {
        if (i > 0) {
            Location location = player.getLocation();
            location.setY((location.getY() - EterniaServer.getInteger(ConfigIntegers.ELEVATOR_MAX) - 3.0D + (double) i) + 1);
            PaperLib.teleportAsync(player, location);
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.D));
        }
    }

}
