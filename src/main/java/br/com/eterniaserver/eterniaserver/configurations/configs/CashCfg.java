package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.CashItem;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashCfg implements ReloadableConfiguration {

    private final EterniaServer plugin;

    public CashCfg(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.WARNING_ADVICE;
    }

    @Override
    public void executeConfig() {
        plugin.menuGui.clear();
        plugin.guis.clear();
        plugin.guisInvert.clear();
        plugin.othersGui.clear();

        final FileConfiguration cashGui = YamlConfiguration.loadConfiguration(new File(Constants.CASHGUI_FILE_PATH));
        final FileConfiguration outCash = new YamlConfiguration();

        loadDefaultValues();

        List<ItemStack> menuGuiTemp = new ArrayList<>();
        Map<Integer, String> guisTemp = new HashMap<>();
        Map<String, Integer> guisTempInvert = new HashMap<>();
        Map<Integer, List<CashItem>> othersGuiTemp = new HashMap<>();

        loadMapFromArchive(cashGui, othersGuiTemp, menuGuiTemp, guisTemp, guisTempInvert);

        if (othersGuiTemp.isEmpty()) {
            menuGuiTemp = new ArrayList<>(plugin.menuGui);
            guisTemp = new HashMap<>(plugin.guis);
            guisTempInvert = new HashMap<>(plugin.guisInvert);
            othersGuiTemp = new HashMap<>(plugin.othersGui);
        }

        plugin.menuGui.clear();
        plugin.menuGui.addAll(menuGuiTemp);
        plugin.guis.clear();
        guisTemp.forEach(plugin.guis::put);
        plugin.guisInvert.clear();
        guisTempInvert.forEach(plugin.guisInvert::put);
        plugin.othersGui.clear();
        othersGuiTemp.forEach(plugin.othersGui::put);

        outCash.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");
        outCash.set("menu.size", menuGuiTemp.size());
        for (int i = 0; i < menuGuiTemp.size(); i++) {
            ItemStack itemStack = menuGuiTemp.get(i);
            if (!itemStack.isSimilar(getGlass())) {
                outCash.set("menu." + i + ".material", itemStack.getType().name());
                outCash.set("menu." + i + ".name", itemStack.getItemMeta().getDisplayName());
                List<String> listando = itemStack.getLore();
                outCash.set("menu." + i + ".lore", listando);
                String guiName = guisTemp.get(i);
                outCash.set("menu." + i + ".gui", guiName);
                List<CashItem> cashItems = othersGuiTemp.get(i);
                outCash.set("guis." + guiName + ".size", cashItems.size());
                for (int j = 0; j < cashItems.size(); j++) {
                    CashItem cashItem = cashItems.get(j);
                    if (!cashItem.isGlass()) {
                        outCash.set("guis." + guiName + "." + j + ".cost", cashItem.getCost());
                        outCash.set("guis." + guiName + "." + j + ".material", cashItem.getItemStack().getType().name());
                        outCash.set("guis." + guiName + "." + j + ".name", cashItem.getItemStack().getItemMeta().getDisplayName());
                        outCash.set("guis." + guiName + "." + j + ".commands", cashItem.getCommands());
                        List<String> lore = cashItem.getItemStack().getLore();
                        outCash.set("guis." + guiName + "." + j + ".lore", lore);
                        List<String> msgs = cashItem.getMessages();
                        outCash.set("guis." + guiName + "." + j + ".messages", msgs);
                    }
                }
            }
        }

        try {
            outCash.save(Constants.CASHGUI_FILE_PATH);
        } catch (IOException exception) {
            plugin.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }
    }

    @Override
    public void executeCritical() {

    }

    private void loadDefaultValues() {
        for (int i = 0; i < 27; i++) {
            if (i == 10) {
                ItemStack itemStack = new ItemStack(Material.OAK_SIGN);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("$7Permissões$8.".replace('$', (char) 0x00A7));
                itemMeta.setLore(List.of( "$7Compre permissões".replace('$', (char) 0x00A7), "$7para lhe ajudar".replace('$', (char) 0x00A7), "$7nessa jornada$8.".replace('$', (char) 0x00A7)));
                itemStack.setItemMeta(itemMeta);
                plugin.guis.put(10, "Perm");
                plugin.guisInvert.put("Perm", 10);
                plugin.menuGui.add(itemStack);
                List<CashItem> tempList = new ArrayList<>();
                for (int j = 0; j < 36; j++) {
                    if (j == 10) {
                        ItemStack is = new ItemStack(Material.EXPERIENCE_BOTTLE);
                        ItemMeta iss = is.getItemMeta();
                        iss.setDisplayName("$725% Bônus de XP no McMMO!".replace('$', (char) 0x00A7));
                        iss.setLore(List.of("$aUpe mais rápido na sua especialidade! Não acumlativos!".replace('$', (char) 0x00A7),"$230 dias de duração!".replace('$', (char) 0x00A7), "", "$2Preço C$ 70".replace('$', (char) 0x00A7)));
                        is.setItemMeta(iss);
                        tempList.add(new CashItem(is, 70, List.of( "$8[$aE$9S$8] $7parabéns pela aquisição$8!".replace('$', (char) 0x00A7)), List.of("lp user %player_name% permission settemp mcmmo.perks.xp.customboost.all true 30d"), false));
                    } else {
                        tempList.add(new CashItem(getGlass(), 0, null, null, true));
                    }
                }
                plugin.othersGui.put(i, tempList);
            } else {
                plugin.menuGui.add(getGlass());
            }
        }
    }

    private void loadMapFromArchive(FileConfiguration cashGui, Map<Integer, List<CashItem>> othersGuiTemp, List<ItemStack> menuGuiTemp, Map<Integer, String> guisTemp, Map<String, Integer> guisTempInvert) {
        for (int i = 0; i < cashGui.getInt("menu.size"); i++) {
            if (cashGui.contains("menu." + i)) {
                ItemStack itemStack = new ItemStack(Material.valueOf(cashGui.getString("menu." + i + ".material")));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(cashGui.getString("menu." + i + ".name").replace('$', (char) 0x00A7));
                List<String> listando = cashGui.getStringList("menu." + i + ".lore");
                plugin.putColorOnList(listando);
                itemMeta.setLore(listando);
                itemStack.setItemMeta(itemMeta);
                menuGuiTemp.add(itemStack);
                String guiName = cashGui.getString("menu." + i + ".gui");
                guisTemp.put(i, guiName);
                guisTempInvert.put(guiName, i);
                List<CashItem> tempList = new ArrayList<>();
                for (int j = 0; j < cashGui.getInt("guis." + guiName + ".size"); j++) {
                    if (cashGui.contains("guis." + guiName + "." + j)) {
                        ItemStack guiItem = new ItemStack(Material.valueOf(cashGui.getString("guis." + guiName + "." + j + ".material")));
                        ItemMeta guiMeta = guiItem.getItemMeta();
                        guiMeta.setDisplayName(cashGui.getString("guis." + guiName + "." + j + ".name").replace('$', (char) 0x00A7));
                        List<String> listandoNovo = cashGui.getStringList("guis." + guiName + "." + j + ".lore");
                        plugin.putColorOnList(listandoNovo);
                        guiMeta.setLore(listandoNovo);
                        guiItem.setItemMeta(guiMeta);
                        List<String> commands = cashGui.getStringList("guis." + guiName + "." + j + ".commands");
                        List<String> msgs = cashGui.getStringList("guis." + guiName + "." + j + ".messages");
                        plugin.putColorOnList(msgs);
                        tempList.add(new CashItem(guiItem, cashGui.getInt("guis." + guiName + "." + j + ".cost"), msgs, commands, false));
                    } else {
                        tempList.add(new CashItem(getGlass(), 0, null, null, true));
                    }
                }
                othersGuiTemp.put(i, tempList);
            } else {
                menuGuiTemp.add(getGlass());
            }
        }
    }

    private ItemStack getGlass() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("$7Loja de $aCash$8.".replace('$', (char) 0x00A7));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
