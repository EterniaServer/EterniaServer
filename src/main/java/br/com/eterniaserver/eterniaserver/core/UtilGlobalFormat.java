package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholders;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UtilGlobalFormat {

	public void filter(User user, String message) {
		BaseComponent[] baseComponents = customPlaceholder(user.getPlayer(), EterniaServer.chat.globalFormat, message);
		Bukkit.spigot().broadcast(baseComponents);
	}

	private BaseComponent[] customPlaceholder(Player player, String format, String message) {
		Map<Integer, TextComponent> textComponentMap = new TreeMap<>();
		EterniaServer.chat.customPlaceholdersObjectsMap.forEach((placeholder, object) -> {
			if (format.contains("{" + placeholder + "}") && player.hasPermission(object.getPermission())) {
				textComponentMap.put(object.getPriority(), getText(player, object));
			}
		});

		String[] messageSplited = message.split(" ");

		for (int i = 0; i < messageSplited.length; i++) {
			if (i > 0) {
				messageSplited[i] = ChatColor.getLastColors(messageSplited[i - 1]) + messageSplited[i];
			}
		}

		BaseComponent[] baseComponents = new BaseComponent[textComponentMap.size() + messageSplited.length];

		AtomicInteger integer = new AtomicInteger(0);
		textComponentMap.forEach((id, component) -> baseComponents[integer.getAndIncrement()] = component);

		for (String actualMsg : messageSplited) {
			baseComponents[integer.getAndIncrement()] = getComponent(actualMsg, player);
		}

		return baseComponents;
	}

	private TextComponent getComponent(String actualMsg, Player player) {
		if (player.hasPermission(EterniaServer.constants.permChatMention) && actualMsg.contains(EterniaServer.constants.mentionPlaceholder) && Vars.playersName.containsKey(actualMsg)) {
			Player target = Bukkit.getPlayer(Vars.playersName.get(actualMsg));
			actualMsg = Vars.colors.get(3) + actualMsg + Vars.colors.get(15);
			if (target != null && target.isOnline()) {
				target.playNote(target.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
				target.sendTitle(EterniaServer.constants.chMentionTitle.replace("{0}", player.getName()).replace("{1}", player.getDisplayName()),
						EterniaServer.constants.chMentionSubtitle.replace("{0}", player.getName()).replace("{1}", player.getDisplayName()), 10, 40, 10);
			}
			return new TextComponent(actualMsg + " ");
		}

		if (player.hasPermission(EterniaServer.constants.permChatItem) && actualMsg.equals(EterniaServer.constants.showItemPlaceholder)) {
			ItemStack itemStack = player.getInventory().getItemInMainHand();
			if (!itemStack.getType().equals(Material.AIR)) {
				return sendItemInHand(actualMsg + " ", itemStack);
			}
		}
		if (!player.hasPermission(EterniaServer.constants.permChatColor)) {
			actualMsg = ChatColor.stripColor(actualMsg);
		}
		return new TextComponent(actualMsg + " ");
	}

	private	TextComponent sendItemInHand(String string, ItemStack itemStack) {
		if (APIServer.getVersion() >= 116) {
			HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, Bukkit.getItemFactory().hoverContentOf(itemStack));
			TextComponent component = new TextComponent(string.replace(EterniaServer.constants.showItemPlaceholder,
					EterniaServer.constants.chShowItemFormat.replace("{0}", String.valueOf(itemStack.getAmount())).replace("{1}", itemStack.getI18NDisplayName())));
			component.setHoverEvent(event);
			return component;
		}
		return new TextComponent(EterniaServer.constants.noSupport);
	}

	private TextComponent getText(Player player, CustomPlaceholders objects) {
		TextComponent textComponent = new TextComponent(APIServer.getColor(APIServer.setPlaceholders(player, objects.getValue())));
		if (!objects.getHoverText().equals("")) {
			List<TextComponent> textComponentList = new ArrayList<>();
			textComponentList.add(new TextComponent(APIServer.getColor(APIServer.setPlaceholders(player, objects.getHoverText()))));
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(textComponentList.toArray(new TextComponent[textComponentList.size() - 1]))));
		}

		if (!objects.getSuggestCmd().equals("")) {
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, APIServer.getColor(APIServer.setPlaceholders(player, objects.getSuggestCmd()))));
		}
		return textComponent;
	}

}
