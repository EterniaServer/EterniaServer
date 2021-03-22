package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerHandler implements Listener {

    private static final String WARP_SPAWN = "warp.spawn";

    private final EterniaServer plugin;
    private final List<Material> list = List.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL);

    public PlayerHandler(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.getBoolean(Booleans.MODULE_HOMES) && !plugin.getBoolean(Booleans.MODULE_EXPERIENCE) && !plugin.getBoolean(Booleans.MODULE_SPAWNERS)) return;

        User user = new User(event.getPlayer());
        final Action action = event.getAction();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            final ItemStack itemStack = event.getItem();
            if (itemFunction(user, itemStack)) {
                event.setCancelled(true);
                return;
            }

            event.setCancelled(blockFunction(event.getClickedBlock(), itemStack, user.hasPermission(plugin.getString(Strings.PERM_SPAWNERS_CHANGE))));
        }

        if (plugin.getBoolean(Booleans.MODULE_SPAWNERS) && event.getClickedBlock() != null
                && action.equals(Action.RIGHT_CLICK_BLOCK) && event.getItem() != null
                && event.getClickedBlock().getType() == Material.SPAWNER
                && !user.hasPermission(plugin.getString(Strings.PERM_SPAWNERS_CHANGE))) {
            event.setCancelled(true);
        }
    }

    private boolean blockFunction(final Block block, final ItemStack itemStack, boolean hasBreakPerm) {
        if (block == null) {
            return false;
        }


        if (list.contains(block.getType())) {
            final Location location = block.getLocation();
            for (Entity entity : location.getNearbyEntities(2, 2, 2)) {
                if (entity instanceof Minecart) {
                    return true;
                }
            }
        }

        return itemStack != null && block.getType() == Material.SPAWNER && !hasBreakPerm;
    }


    private boolean itemFunction(final User user, final ItemStack itemStack) {
        if (!plugin.getBoolean(Booleans.ITEMS_FUNCTIONS) || itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }

        final PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (!container.has(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER)) {
            return false;
        }

        final int function = container.get(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER);
        switch (function) {
            case 0:
                return expFunction(user, container);
            case 1:
                return homesFunction(user, container);
            case 2:
                return customFunction(user, itemStack);
            default:
                return false;
        }
    }

    private boolean customFunction(final User user, final ItemStack itemStack) {
        final Player player = user.getPlayer();
        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta.getPersistentDataContainer().has(plugin.getKey(ItemsKeys.TAG_RUN_COMMAND), PersistentDataType.STRING)) {
            final String cmd = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.TAG_RUN_COMMAND), PersistentDataType.STRING);
            if (itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.TAG_RUN_IN_CONSOLE), PersistentDataType.INTEGER) == 1) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.setPlaceholders(player, cmd));
            } else {
                player.performCommand(plugin.setPlaceholders(player, cmd));
            }

            final int itemUsages = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.TAG_USAGES), PersistentDataType.INTEGER);
            if (itemUsages == 1) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            } else if (itemUsages > 1) {
                itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_USAGES), PersistentDataType.INTEGER, (itemUsages - 1));
                itemStack.setItemMeta(itemMeta);
                player.getInventory().setItemInMainHand(itemStack);
            }
            return true;
        }
        return false;
    }

    private boolean expFunction(final User user, final PersistentDataContainer container) {
        if (!plugin.getBoolean(Booleans.MODULE_EXPERIENCE)) {
            return false;
        }

        user.setItemInMainHand(new ItemStack(Material.AIR));
        user.giveExp(container.get(plugin.getKey(ItemsKeys.TAG_INT_VALUE), PersistentDataType.INTEGER));
        return true;
    }

    private boolean homesFunction(final User user, final PersistentDataContainer container) {
        if (!plugin.getBoolean(Booleans.MODULE_HOMES)) {
            return false;
        }

        if (user.isTeleporting()) {
            plugin.sendMessage(user.getPlayer(), Messages.SERVER_IN_TELEPORT);
            return false;
        }

        user.putInTeleport(new PlayerTeleport(plugin, user.getPlayer(), new Location(
                Bukkit.getWorld(container.get(plugin.getKey(ItemsKeys.TAG_WORLD), PersistentDataType.STRING)),
                container.get(plugin.getKey(ItemsKeys.TAG_COORD_X), PersistentDataType.DOUBLE),
                container.get(plugin.getKey(ItemsKeys.TAG_COORD_Y), PersistentDataType.DOUBLE),
                container.get(plugin.getKey(ItemsKeys.TAG_COORD_Z), PersistentDataType.DOUBLE),
                container.get(plugin.getKey(ItemsKeys.TAG_COORD_YAW), PersistentDataType.FLOAT),
                container.get(plugin.getKey(ItemsKeys.TAG_COORD_PITCH), PersistentDataType.FLOAT)
        ), plugin.getMessage(Messages.HOME_GOING, true, container.get(plugin.getKey(ItemsKeys.TAG_LOC_NAME), PersistentDataType.STRING))));
        return true;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (plugin.getBoolean(Booleans.MODULE_TELEPORTS)) {
            final Player player = event.getEntity();
            new User(player).putBackLocation(player.getLocation());
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (plugin.getBoolean(Booleans.MODULE_TELEPORTS)) {
            final Player player = event.getPlayer();
            new User(player).putBackLocation(player.getLocation());
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (plugin.getBoolean(Booleans.MODULE_TELEPORTS) && plugin.hasLocation(WARP_SPAWN)) {
            event.setRespawnLocation(plugin.getLocation(WARP_SPAWN));
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerLeave(PlayerQuitEvent event) {
        User user = new User(event.getPlayer());
        user.clear();

        Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
        update.set.set("hours", user.getAndUpdateTimePlayed());
        update.where.set("uuid", user.getUUID().toString());
        SQL.executeAsync(update);

        event.setQuitMessage(plugin.getMessage(Messages.SERVER_LOGOUT, true, user.getName(), user.getDisplayName()));

    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.equalsIgnoreCase("/tps")) {
            plugin.sendMessage(event.getPlayer(), Messages.SERVER_TPS, String.format("%.2f", Bukkit.getTPS()[0]));
            event.setCancelled(true);
            return;
        }
        for (String line : plugin.getStringList(Lists.BLACKLISTED_COMMANDS)) {
            if (message.startsWith(line)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (!plugin.getBoolean(Booleans.MODULE_BED)) {
            return;
        }

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            User user = new User(event.getPlayer());
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.getBedCooldown()) > 6) {
                user.updateBedCooldown();
                Bukkit.broadcastMessage(plugin.getMessage(Messages.NIGHT_PLAYER_SLEEPING, true, user.getName(), user.getDisplayName()));
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        if (!plugin.getBoolean(Booleans.MODULE_BED)) {
            return;
        }

        User user = new User(event.getPlayer());
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.getBedCooldown()) > 6) {
            user.updateBedCooldown();
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getBoolean(Booleans.MODULE_ELEVATOR) && player.hasPermission(plugin.getString(Strings.PERM_ELEVATOR)) && !player.isSneaking()) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            for (Material value : plugin.getElevatorMaterials()) {
                if (value == material) {
                    block = block.getRelative(BlockFace.DOWN, plugin.getInteger(Integers.ELEVATOR_MIN));
                    int i;
                    for (i = plugin.getInteger(Integers.ELEVATOR_MAX); i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.DOWN)) --i;
                    elevatorDown(player, i);
                    break;
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJump(PlayerJumpEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getBoolean(Booleans.MODULE_ELEVATOR) && player.hasPermission(plugin.getString(Strings.PERM_ELEVATOR))) {
            Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            for (Material value : plugin.getElevatorMaterials()) {
                if (value == material) {
                    block = block.getRelative(BlockFace.UP, plugin.getInteger(Integers.ELEVATOR_MIN));
                    int i;
                    for (i = plugin.getInteger(Integers.ELEVATOR_MAX); i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.UP)) -- i;
                    elevatorUp(player, i);
                    break;
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = new User(event.getPlayer());

        event.setJoinMessage(plugin.getMessage(Messages.SERVER_LOGIN, true, user.getName(), user.getDisplayName()));

        user.updateProfile();

        if (plugin.getBoolean(Booleans.MODULE_CHAT)) {
            if (user.hasPermission(plugin.getString(Strings.PERM_SPY))) {
                user.changeSpyState();
            }
            user.setDisplayName();
        }

        for (Player player : EterniaServer.getUserAPI().getVanishList()) {
            user.hidePlayer(plugin, player);
        }

        if (user.getChannel() == 0) {
            user.setChannel(plugin.getString(Strings.DEFAULT_CHANNEL).hashCode());
        }

        user.updateAfkTime();
        user.createKits();
        user.disableFly();

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, user::teleport, 10L);
    }

    private void elevatorUp(final Player player, final int i) {
        if (i > 0) {
            Location location = player.getLocation();
            location.setY((location.getY() + plugin.getInteger(Integers.ELEVATOR_MAX) + 3.0D - (double) i) - 1);
            PaperLib.teleportAsync(player, location);
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
        }
    }

    private void elevatorDown(final Player player, final int i) {
        if (i > 0) {
            Location location = player.getLocation();
            location.setY((location.getY() - plugin.getInteger(Integers.ELEVATOR_MAX) - 3.0D + (double) i) + 1);
            PaperLib.teleportAsync(player, location);
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.D));
        }
    }

}
