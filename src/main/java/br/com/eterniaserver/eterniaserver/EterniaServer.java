package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Files;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;
import br.com.eterniaserver.eterniaserver.generics.EventEntityDamage;
import br.com.eterniaserver.eterniaserver.generics.EventEntityDamageByEntity;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerJump;
import br.com.eterniaserver.eterniaserver.generics.EventEntityInventoryClick;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerBlockBreak;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerBlockPlace;
import br.com.eterniaserver.eterniaserver.generics.EventAsyncPlayerChat;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerCommandPreProcess;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerDeath;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerInteract;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerLeave;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerJoin;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerMove;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerRespawn;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerSignChange;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerTeleport;
import br.com.eterniaserver.eterniaserver.generics.EventPlayerToggleSneak;
import br.com.eterniaserver.eterniaserver.generics.EventServerListPing;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.List;

public class EterniaServer extends JavaPlugin {

    public static final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);
    public static final DecimalFormat df2 = new DecimalFormat(".##");

    public static final List<String> arrData = List.of("tblack", "tdarkblue", "tdarkgreen", "tdarkaqua", "tdarkred",
            "tdarkpurple", "tgold", "tlightgray", "tdarkgray", "tblue", "tgreen", "taqua", "tred", "tpurple", "tyellow",
            "twhite");

    public static final List<String> entityList = List.of("BEE", "BLAZE", "CAT", "CAVE_SPIDER", "CHICKEN", "COD",
            "COW", "CREEPER", "DOLPHIN", "DONKEY", "DROWNED", "ELDER_GUARDIAN", "ENDER_DRAGON", "ENDERMAN", "ENDERMITE",
            "EVOKER", "FOX", "GHAST", "GIANT", "GUARDIAN", "HOGLIN", "HORSE", "HUSK", "ILLUSIONER", "IRON_GOLEM",
            "MAGMA_CUBE", "MULE", "PANDA", "PARROT", "PHANTOM", "PIG", "PIGLIN", "PILLAGER", "POLAR_BEAR", "PUFFERFISH",
            "RABBIT", "RAVAGER", "SALMON", "SHEEP", "SILVERFISH", "SKELETON", "SKELETON_HORSE", "SLIME", "SNOW_GOLEM",
            "SPIDER", "SQUID", "STRAY", "STRIDER", "TURTLE", "VEX", "VILLAGER", "VINDICATOR", "WITCH", "WITHER",
            "WITHER_SKELETON", "WOLF", "ZOGLIN", "ZOMBIE", "ZOMBIE_HORSE", "ZOMBIFIED_PIGLIN", "ZOMBIE_VILLAGER");

    public static final List<ChatColor> colors = List.of(ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY,
            ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW, ChatColor.WHITE);

    public static final FileConfiguration serverConfig = new YamlConfiguration();
    public static final FileConfiguration msgConfig = new YamlConfiguration();
    public static final FileConfiguration cmdConfig = new YamlConfiguration();
    public static final FileConfiguration rewardsConfig = new YamlConfiguration();
    public static final FileConfiguration blockConfig = new YamlConfiguration();
    public static final FileConfiguration kitConfig = new YamlConfiguration();
    public static final FileConfiguration chatConfig = new YamlConfiguration();
    public static final FileConfiguration placeholderConfig = new YamlConfiguration();
    public static final FileConfiguration groupConfig = new YamlConfiguration();
    public static final FileConfiguration cashConfig = new YamlConfiguration();

    private final Files files = new Files(this);

    @Override
    public void onEnable() {

        files.loadConfigs();
        files.loadMessages();
        files.loadDatabase();
        files.loadPlaceHolders();

        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new EventPlayerJump(), this);
        this.getServer().getPluginManager().registerEvents(new EventEntityDamage(), this);
        this.getServer().getPluginManager().registerEvents(new EventEntityDamageByEntity(), this);
        this.getServer().getPluginManager().registerEvents(new EventEntityInventoryClick(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerBlockBreak(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new EventAsyncPlayerChat(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerCommandPreProcess(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerInteract(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerMove(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerRespawn(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerSignChange(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new EventPlayerToggleSneak(), this);
        this.getServer().getPluginManager().registerEvents(new EventServerListPing(), this);

    }

    public Files getFiles() {
        return files;
    }

}
