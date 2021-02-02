package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.PlayerRelated;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.objects.User;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

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

public class ChatFormatter {

	private static final Map<String, TextComponent> staticComponents = new HashMap<>();

	public void filter(final User user, final String message, ChannelObject channelObject, final Set<Player> players) {
		if (channelObject == null) {
			channelObject = EterniaServer.getChannelsMap().get(EterniaServer.getString(Strings.DEFAULT_CHANNEL).hashCode());
		}

		if (!user.hasPermission(channelObject.getPerm())) {
			user.sendMessage(Messages.SERVER_NO_PERM);
			return;
		}

		BaseComponent[] baseComponents = customPlaceholder(user.getPlayer(), channelObject.getFormat(), channelObject.getChannelColor(), message);

		if (!channelObject.isHasRange()) {
			for (Player player : players) {
				if (player.hasPermission(channelObject.getPerm())) {
					player.spigot().sendMessage(user.getUUID(), baseComponents);
				}
			}
			players.clear();
			return;
		}

		int pes = 0;
		for (Player p : players) {
			if ((user.getPlayer().getWorld() == p.getWorld() && p.getLocation().distanceSquared(user.getPlayer().getLocation()) <= Math.pow(channelObject.getRange(), 2)) || channelObject.getRange() <= 0) {
				pes += 1;
				p.spigot().sendMessage(user.getUUID(), baseComponents);
			} else if (p.hasPermission(EterniaServer.getString(Strings.PERM_SPY)) && PlayerRelated.isSpying(p.getUniqueId())) {
				p.sendMessage(user.getUUID(), ServerRelated.getColor(EterniaServer.getString(Strings.CONS_SPY_LOCAL)
						.replace("{0}", user.getName())
						.replace("{1}", user.getDisplayName())
						.replace("{2}", message)));
			}
		}

		if (pes <= 1) {
			user.sendMessage(Messages.CHAT_NO_ONE_NEAR);
		}

		players.clear();
	}

	private BaseComponent[] customPlaceholder(final Player player, final String format, final String channelColor, final String message) {

		final Map<Integer, TextComponent> textComponentMap = new TreeMap<>();

		for (final Map.Entry<String, CustomPlaceholder> entry : EterniaServer.getCustomPlaceholders().entrySet()) {
			if (format.contains("{" + entry.getKey() + "}") && player.hasPermission(entry.getValue().getPermission())) {
				textComponentMap.put(entry.getValue().getPriority(), getJsonTagText(player, entry.getValue()));
			}
		}

	 	final List<TextComponent> textComponentList = new ArrayList<>();
		final StringBuilder stringBuilder = new StringBuilder(channelColor);

		for (final String string : message.split(" ")) {

			final TextComponent textComponent = getComponent(ChatColor.stripColor(string), player);

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

		final BaseComponent[] baseComponents = new BaseComponent[textComponentMap.size() + textComponentList.size()];
		int i = 0;

		for (TextComponent textComponent : textComponentMap.values()) {
			baseComponents[i++] = textComponent;
		}

		for (BaseComponent textComponent : textComponentList) {
			baseComponents[i++] = textComponent;
		}

		return baseComponents;
	}

	private TextComponent getComponent(final String string, final Player player) {

		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_MENTION)) && string.contains(EterniaServer.getString(Strings.MENTION_PLACEHOLDER)) && PlayerRelated.hasNameOnline(string)) {
			final Player target = Bukkit.getPlayer(PlayerRelated.getUUIDFromMention(string));

			if (target != null && target.isOnline()) {
				target.playNote(target.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
				target.sendTitle(
						EterniaServer.getString(Strings.CONS_MENTION_TITLE)
								.replace("{0}", player.getName())
								.replace("{1}", player.getDisplayName()),
						EterniaServer.getString(Strings.CONS_MENTION_SUBTITLE)
								.replace("{0}", player.getName())
								.replace("{1}", player.getDisplayName()), 10, 40, 10);
			}

			return new TextComponent("§3" + string + " ");
		}

		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_ITEM)) && string.equals(EterniaServer.getString(Strings.SHOW_ITEM_PLACEHOLDER))) {
			final ItemStack itemStack = player.getInventory().getItemInMainHand();
			if (!itemStack.getType().equals(Material.AIR)) {
				return getItemComponentText(string + " ", itemStack);
			}
		}

		return null;
	}

	private TextComponent getText(final String string, final Player player) {

		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_COLOR))) {
			return new TextComponent(TextComponent.fromLegacyText(ServerRelated.translateHex(string)));
		}

		return new TextComponent(TextComponent.fromLegacyText(string));

	}

	private	TextComponent getItemComponentText(final String string, final ItemStack itemStack) {

		if (ServerRelated.getVersion() < 116) {
			return new TextComponent(EterniaServer.getString(Strings.NOT_SUPPORTED));
		}

		final TextComponent textComponent = new TextComponent(string
				.replace(EterniaServer.getString(Strings.SHOW_ITEM_PLACEHOLDER),
						EterniaServer.getString(Strings.CONS_SHOW_ITEM)
								.replace("{0}", String.valueOf(itemStack.getAmount()))
								.replace("{1}", itemStack.getType().toString())));

		textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, Bukkit.getItemFactory().hoverContentOf(itemStack)));

		return textComponent;

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

		final TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(ServerRelated.setPlaceholders(player, object.getValue())));

		if (!object.getHoverText().equals("")) {
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(ServerRelated.setPlaceholders(player, object.getHoverText())))));
		}

		if (!object.getSuggestCmd().equals("")) {
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ServerRelated.setPlaceholders(player, object.getSuggestCmd())));
		}

		return textComponent;

	}

}
