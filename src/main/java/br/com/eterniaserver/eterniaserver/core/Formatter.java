package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.objects.User;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class Formatter {

	private final EterniaServer plugin;

	private final Map<String, TextComponent> staticComponents = new HashMap<>();

	public Formatter(final EterniaServer plugin) {
		this.plugin = plugin;
	}

	public void filter(final User user, final String message, ChannelObject channelObject) {
		if (channelObject == null) {
			channelObject = plugin.getChannelsMap().get(plugin.getString(Strings.DEFAULT_CHANNEL).hashCode());
		}

		if (!user.hasPermission(channelObject.getPerm())) {
			plugin.sendMessage(user.getPlayer(), Messages.SERVER_NO_PERM);
			return;
		}

		Component[] baseComponents = customPlaceholder(user.getPlayer(), channelObject.getFormat(), channelObject.getChannelColor(), message);
		Component component = Component.empty();
		for (Component entry : baseComponents) {
			component = component.append(entry);
		}
		if (!channelObject.isHasRange()) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission(channelObject.getPerm())) {
					player.sendMessage(Identity.identity(user.getUUID()), component);
				}
			}
			return;
		}

		int pes = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if ((user.getPlayer().getWorld() == p.getWorld() && p.getLocation().distanceSquared(user.getPlayer().getLocation()) <= Math.pow(channelObject.getRange(), 2)) || channelObject.getRange() <= 0) {
				pes += 1;
				p.sendMessage(Identity.identity(user.getUUID()), component);
			} else if (p.hasPermission(plugin.getString(Strings.PERM_SPY)) && EterniaServer.getUserAPI().isSpying(p.getUniqueId())) {
				p.sendMessage(user.getUUID(), plugin.getColor(plugin.getString(Strings.CONS_SPY_LOCAL)
						.replace("{0}", user.getName())
						.replace("{1}", user.getDisplayName())
						.replace("{2}", message)));
			}
		}

		if (pes <= 1) {
			plugin.sendMessage(user.getPlayer(), Messages.CHAT_NO_ONE_NEAR);
		}
	}

	private Component[] customPlaceholder(final Player player, final String format, final String channelColor, final String message) {

		final Map<Integer, TextComponent> textComponentMap = new TreeMap<>();

		for (final Map.Entry<String, CustomPlaceholder> entry : plugin.getCustomPlaceholders().entrySet()) {
			if (format.contains("{" + entry.getKey() + "}") && player.hasPermission(entry.getValue().getPermission())) {
				textComponentMap.put(entry.getValue().getPriority(), getJsonTagText(player, entry.getValue()));
			}
		}

	 	final List<TextComponent> textComponentList = new ArrayList<>();
		final StringBuilder stringBuilder = new StringBuilder(channelColor);

		for (final String string : message.split(" ")) {

			final TextComponent textComponent = getComponent(PlainComponentSerializer.plain().serialize(LegacyComponentSerializer.legacySection().deserialize(string)), player);

			if (textComponent != null) {
				textComponentList.add(getText(stringBuilder.toString(), player));
				textComponentList.add(textComponent);
				stringBuilder.setLength(0);
			} else {
				stringBuilder.append(string).append(" ");
			}

		}

		if (stringBuilder.length() != 0) {
			textComponentList.add(getText(stringBuilder.toString(), player));
		}

		final Component[] baseComponents = new Component[textComponentMap.size() + textComponentList.size()];
		int i = 0;

		for (TextComponent textComponent : textComponentMap.values()) {
			baseComponents[i++] = textComponent;
		}

		for (Component textComponent : textComponentList) {
			baseComponents[i++] = textComponent;
		}

		return baseComponents;
	}

	private TextComponent getComponent(final String string, final Player player) {

		if (player.hasPermission(plugin.getString(Strings.PERM_CHAT_MENTION)) && EterniaServer.getUserAPI().hasNameOnline(string)) {
			final Player target = Bukkit.getPlayer(EterniaServer.getUserAPI().getUUIDFromMention(string));

			if (target != null && target.isOnline()) {
				target.playNote(target.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
				target.sendTitle(
						plugin.getString(Strings.CONS_MENTION_TITLE)
								.replace("{0}", player.getName())
								.replace("{1}", player.getDisplayName()),
						plugin.getString(Strings.CONS_MENTION_SUBTITLE)
								.replace("{0}", player.getName())
								.replace("{1}", player.getDisplayName()), 10, 40, 10);
			}
			return Component.text(string + " ").color(TextColor.color(3));
		}

		if (player.hasPermission(plugin.getString(Strings.PERM_CHAT_ITEM)) && string.equals(plugin.getString(Strings.SHOW_ITEM_PLACEHOLDER))) {
			final ItemStack itemStack = player.getInventory().getItemInMainHand();
			if (!itemStack.getType().equals(Material.AIR)) {
				return getItemComponentText(string + " ", itemStack);
			}
		}

		return null;
	}

	private TextComponent getText(final String string, final Player player) {

		if (player.hasPermission(plugin.getString(Strings.PERM_CHAT_COLOR))) {
			return LegacyComponentSerializer.legacySection().deserialize(plugin.translateHex(string));
		}

		return LegacyComponentSerializer.legacySection().deserialize(string);

	}

	private	TextComponent getItemComponentText(final String string, final ItemStack itemStack) {
		final TextComponent textComponent = Component.text(string
				.replace(plugin.getString(Strings.SHOW_ITEM_PLACEHOLDER),
						plugin.getString(Strings.CONS_SHOW_ITEM)
								.replace("{0}", String.valueOf(itemStack.getAmount()))
								.replace("{1}", itemStack.getType().toString())));

		return textComponent.hoverEvent(Bukkit.getItemFactory().asHoverEvent(itemStack, UnaryOperator.identity())).color(TextColor.color(3));

	}

	private TextComponent getJsonTagText(final Player player, final CustomPlaceholder object) {
		if (object.getIsStatic()) {
			if (!staticComponents.containsKey(object.getValue())) {
				final TextComponent textComponent = loadComponent(player, object);
				staticComponents.put(object.getValue(), textComponent);
				return textComponent;
			}
			return staticComponents.get(object.getValue());
		}
		return loadComponent(player, object);

	}

	private TextComponent loadComponent(final Player player, final CustomPlaceholder object) {

		TextComponent textComponent = LegacyComponentSerializer.legacySection().deserialize(plugin.translateHex(plugin.setPlaceholders(player, object.getValue())));

		if (!object.getHoverText().equals("")) {
			textComponent = textComponent.hoverEvent(HoverEvent.showText(LegacyComponentSerializer.legacySection().deserialize(plugin.translateHex(plugin.setPlaceholders(player, object.getHoverText())))));
		}

		if (!object.getSuggestCmd().equals("")) {
			textComponent = textComponent.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.translateHex(plugin.setPlaceholders(player, object.getSuggestCmd()))));
		}

		return textComponent;

	}

}
