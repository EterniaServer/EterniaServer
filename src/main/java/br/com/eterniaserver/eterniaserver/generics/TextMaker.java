package br.com.eterniaserver.eterniaserver.generics;

import java.util.ArrayList;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.ChatMessage;
import br.com.eterniaserver.eterniaserver.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.utils.StringHelper;
import br.com.eterniaserver.eterniaserver.utils.SubPlaceholder;

import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.HoverEvent.Action;

public class TextMaker extends StringHelper {

	protected TextComponent text;
	private final ChatMessage message;
	private final Player p;
	private final InternMethods internMethods;

	public TextMaker(ChatMessage message, Player p, EterniaServer plugin) {
		this.p = p;
		this.message = message;
		internMethods = plugin.getInternMethods();
	}

	public void convertMessageToComponents() {
		BaseComponent[] baseComp = new BaseComponent[message.size()];
		for(int i = 0; i < message.size(); i++) {
			ChatObject chatObject = message.getChatObjects().get(i);
			String msg = chatObject.message;
			msg = internMethods.setPlaceholders(p, msg);
			if(msg.contains("%message%")) {
				msg = msg.replace("%message%", message.getMessageSent());
			}
			TextComponent textComp = new TextComponent(TextComponent.fromLegacyText(msg));
			if(chatObject.getHover() != null) {
				ArrayList<TextComponent> tcs = new ArrayList<>();
				tcs.add(new TextComponent(internMethods.setPlaceholders(p, cc(chatObject.getHover()))));
				TextComponent[] bc = tcs.toArray(new TextComponent[tcs.size() - 1]);
				textComp.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, bc));
			}
			if(chatObject.getColor() != null) {
				textComp.setColor(chatObject.getColor().asBungee());
			}
			if(chatObject.getSuggest() != null) {
				textComp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, internMethods.setPlaceholders(p, cc(chatObject.getSuggest()))));
			}
			if(chatObject.getRun() != null) {
				textComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, internMethods.setPlaceholders(p, cc(chatObject.getRun()))));
			}
			if(chatObject.isText()) {
				setTextAttr(textComp, p);
			}
			baseComp[i] = textComp;
		}
		text = new TextComponent(baseComp);
	}

	private void setTextAttr(TextComponent text, Player p) {
		if (getConfigBoolean(p, "on_click.suggest_command")) {
			addClickSuggest(text, customPlaceholder(p, getConfigString(p, "on_click.suggested_command")));
		}
		if (getConfigBoolean(p, "on_click.run_command")) {
			addClickRun(text, customPlaceholder(p, getConfigString(p, "on_click.runned_command")));
		}
		if(getConfigBoolean(p, "on_hover.show_text")) {
			addHover(text, customPlaceholder(p, getConfigString(p, "on_hover.text_shown")));
		}
	}

	public void addHover(TextComponent text, String s) {
		if(s == null) return;
		ArrayList<TextComponent> tcs = new ArrayList<>();
		tcs.add(new TextComponent(cc(s)));
		TextComponent[] bc = tcs.toArray(new TextComponent[tcs.size() - 1]);
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, bc));
	}

	public void addClickSuggest(TextComponent text, String s) {
		if(s == null) return;
		text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, s));
	}

	public void addClickRun(TextComponent text, String s) {
		if(s == null) return;
		text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, s));
	}

	public TextComponent getText() {
		return text;
	}

	// Only used when relational placeholders is enabled. (If enabled, you have to compute placeholders for all players, not just once)
	public TextComponent getRelationalText(Player to) {
		BaseComponent[] baseComp = new BaseComponent[message.size()];
		for(int i = 0; i < message.size(); i++) {
			ChatObject chatObject = message.getChatObjects().get(i);
			String msg = chatObject.message;
			msg = internMethods.setBothPlaceholders(p, to, msg);
			if(msg.contains("%message%"))
				msg = msg.replace("%message%", message.getMessageSent());
			TextComponent textComp = new TextComponent(TextComponent.fromLegacyText(msg));
			if(chatObject.getHover() != null) {
				ArrayList<TextComponent> tcs = new ArrayList<>();
				tcs.add(new TextComponent(internMethods.setBothPlaceholders(p, to, cc(chatObject.getHover()))));
				TextComponent[] bc = tcs.toArray(new TextComponent[tcs.size() - 1]);
				textComp.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, bc));
			}
			if(chatObject.getColor() != null) {
				textComp.setColor(chatObject.getColor().asBungee());
			}
			if(chatObject.getSuggest() != null) {
				textComp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, internMethods.setBothPlaceholders(p, to, cc(chatObject.getSuggest()))));
			}
			if(chatObject.getRun() != null) {
				textComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, internMethods.setBothPlaceholders(p, to, cc(chatObject.getRun()))));
			}
			if(chatObject.isText()) {
				setTextAttr(textComp, p);
			}
			baseComp[i] = textComp;
		}
		return new TextComponent(baseComp);
	}

	public String getConfigString(Player p, String extra) {
		return EterniaServer.groupConfig.getString (Vars.uufi.get(p.getName()).getName() + "." + extra);
	}

	public boolean getConfigBoolean(Player p, String extra) {
		return EterniaServer.groupConfig.getBoolean(Vars.uufi.get(p.getName()).getName() + "." + extra);
	}

	public String customPlaceholder(Player p, String s2) {
		String stringMessage = s2;
		for(CustomPlaceholder cp: Vars.customPlaceholders) {
			String id = cp.getId();
			if(!stringMessage.contains("{" + id + "}")) continue;
			SubPlaceholder bestPlaceholder = internMethods.getSubPlaceholder(p, cp);
			if(bestPlaceholder != null) {
				stringMessage = stringMessage.replace("{" + id + "}", bestPlaceholder.getValue());
			} else {
				stringMessage = stringMessage.replace("{" + id + "}", "");
			}
		}
		s2 = stringMessage;
		return internMethods.setPlaceholders(p, s2);
	}

}