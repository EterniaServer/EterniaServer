package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils;

import java.util.ArrayList;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Checks;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class TextMaker {

	public TextComponent text;
	private final ChatMessage message;
	private final Player p;
	private final EterniaServer plugin;
	private final Vars vars;
	private final Checks checks;

	public TextMaker(ChatMessage message, Player p, EterniaServer plugin) {
		this.p = p;
		this.message = message;
		this.plugin = plugin;
		this.vars = plugin.getVars();
		checks = plugin.getChecks();
	}

	public void convertMessageToComponents() {
		BaseComponent[] baseComp = new BaseComponent[message.size()];
		for(int i = 0; i < message.size(); i++) {
			ChatObject chatObject = message.getChatObjects().get(i);
			String msg = chatObject.message;
			msg = checks.setPlaceholders(p, msg);
			if(msg.contains("%message%")) {
				msg = msg.replace("%message%", message.messageSent);
			}
			TextComponent textComp = new TextComponent(TextComponent.fromLegacyText(msg));
			if(chatObject.getHover() != null) {
				ArrayList<TextComponent> tcs = new ArrayList<>();
				tcs.add(new TextComponent(checks.setPlaceholders(p, StringHelper.cc(chatObject.getHover()))));
				TextComponent[] bc = tcs.toArray(new TextComponent[tcs.size() - 1]);
				textComp.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, bc));
			}
			if(chatObject.getColor() != null) {
				textComp.setColor(chatObject.getColor().asBungee());
			}
			if(chatObject.getSuggest() != null) {
				textComp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, checks.setPlaceholders(p, StringHelper.cc(chatObject.getSuggest()))));
			}
			if(chatObject.getRun() != null) {
				textComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, checks.setPlaceholders(p, StringHelper.cc(chatObject.getRun()))));
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
		tcs.add(new TextComponent(StringHelper.cc(s)));
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
			msg = checks.setBothPlaceholders(p, to, msg);
			if(msg.contains("%message%"))
				msg = msg.replace("%message%", message.messageSent);
			TextComponent textComp = new TextComponent(TextComponent.fromLegacyText(msg));
			if(chatObject.getHover() != null) {
				ArrayList<TextComponent> tcs = new ArrayList<>();
				tcs.add(new TextComponent(checks.setBothPlaceholders(p, to, StringHelper.cc(chatObject.getHover()))));
				TextComponent[] bc = tcs.toArray(new TextComponent[tcs.size() - 1]);
				textComp.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, bc));
			}
			if(chatObject.getColor() != null) {
				textComp.setColor(chatObject.getColor().asBungee());
			}
			if(chatObject.getSuggest() != null) {
				textComp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, checks.setBothPlaceholders(p, to, StringHelper.cc(chatObject.getSuggest()))));
			}
			if(chatObject.getRun() != null) {
				textComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, checks.setBothPlaceholders(p, to, StringHelper.cc(chatObject.getRun()))));
			}
			if(chatObject.isText()) {
				setTextAttr(textComp, p);
			}
			baseComp[i] = textComp;
		}
		return new TextComponent(baseComp);
	}

	public String getConfigString(Player p, String extra) {
		return plugin.groupConfig.getString (vars.uufi.get(p.getName()).getName() + "." + extra);
	}

	public boolean getConfigBoolean(Player p, String extra) {
		return plugin.groupConfig.getBoolean(vars.uufi.get(p.getName()).getName() + "." + extra);
	}

	public String customPlaceholder(Player p, String s2) {
		String message = s2;
		for(CustomPlaceholder cp: vars.customPlaceholders) {
			String id = cp.getId();
			if(!message.contains("{" + id + "}")) continue;
			try {
				SubPlaceholder bestPlaceholder = null;
				for(SubPlaceholder subPlaceholder: cp.getPlaceholders()) {
					if(subPlaceholder.hasPerm(p)) {
						if(bestPlaceholder == null)
							bestPlaceholder = subPlaceholder;
						else {
							if(bestPlaceholder.getPriority() < subPlaceholder.getPriority())
								bestPlaceholder = subPlaceholder;
						}
					}
				}
				if(bestPlaceholder != null) {
					message = message.replace("{" + id + "}", bestPlaceholder.getValue());
				} else {
					message = message.replace("{" + id + "}", "");
				}
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		s2 = message;
		return checks.setPlaceholders(p, s2);
	}

}