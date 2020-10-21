package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConstantsCfg {

    public final String serverPrefix;
    public final String baltopTag;
    public final String afkPlaceholder;
    public final String godPlaceholder;

    public final String gmSpectator;
    public final String gmSurvival;
    public final String gmCreative;
    public final String gmAdventure;

    public final String cnBlack;
    public final String cnDarkBlue;
    public final String cnDarkGreen;
    public final String cnDarkAqua;
    public final String cnDarkRed;
    public final String cnDarkPurple;
    public final String cnGold;
    public final String cnGray;
    public final String cnDarkGray;
    public final String cnBlue;
    public final String cnGreen;
    public final String cnAqua;
    public final String cnRed;
    public final String cnLightPurple;
    public final String cnYellow;
    public final String cnWhite;

    public final String chLocal;
    public final String chGlobal;
    public final String chStaff;

    public ConstantsCfg() {

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.CONFIG_FILE_PATH));
        FileConfiguration outConfig = new YamlConfiguration();

        this.serverPrefix = config.getString("server.prefix", "$8[$aE$9S$8]$7 ").replace('$', (char) 0x00A7);

        this.baltopTag = config.getString("server.baltop", "$8[$2Magnata$8]").replace('$', (char) 0x00A7);

        this.afkPlaceholder = config.getString("placeholders.afk", "$9 AFK").replace('$', (char) 0x00A7);
        this.godPlaceholder = config.getString("placeholders.godmode", "$9 GOD").replace('$', (char) 0x00A7);

        this.gmAdventure = config.getString("generics.gm.adventure", "aventura");
        this.gmCreative = config.getString("generics.gm.creative", "criativo");
        this.gmSpectator = config.getString("generics.gm.spectator", "espectador");
        this.gmSurvival = config.getString("generics.gm.survival", "sobrevivência");

        this.cnBlack = config.getString("chat.cn.black", "Preto");
        this.cnDarkBlue = config.getString("chat.cn.darkblue", "Azul Escuro");
        this.cnDarkGreen = config.getString("chat.cn.darkgreen", "Verde Escuro");
        this.cnDarkAqua = config.getString("chat.cn.darkaqua", "Ciano");
        this.cnDarkRed = config.getString("chat.cn.darkred", "Vermelho Escuro");
        this.cnDarkPurple = config.getString("chat.cn.darkpurple", "Roxo");
        this.cnGold = config.getString("chat.cn.gold", "Laranja");
        this.cnGray = config.getString("chat.cn.gray", "Cinza");
        this.cnDarkGray = config.getString("chat.cn.darkgray", "Cinza Escuro");
        this.cnBlue = config.getString("chat.cn.blue", "Azul");
        this.cnGreen = config.getString("chat.cn.green", "Verde");
        this.cnAqua = config.getString("chat.cn.aqua", "Azul Claro");
        this.cnRed = config.getString("chat.cn.red", "Vermelho");
        this.cnLightPurple = config.getString("chat.cn.lightpurple", "Rosa");
        this.cnYellow = config.getString("chat.cn.yellow", "Amarelo");
        this.cnWhite = config.getString("chat.cn.white", "Branco");
        this.chLocal = config.getString("chat.local", "Local");
        this.chGlobal = config.getString("chat.global", "Global");
        this.chStaff = config.getString("chat.staff", "Staff");

        outConfig.set("server.prefix", this.serverPrefix);
        outConfig.set("server.baltop", this.baltopTag);

        outConfig.set("placeholders.afk", this.afkPlaceholder);
        outConfig.set("placeholders.godmode", this.afkPlaceholder);

        outConfig.set("generics.gm.adventure", this.gmAdventure);
        outConfig.set("generics.gm.creative", this.gmCreative);
        outConfig.set("generics.gm.spectator", this.gmSpectator);
        outConfig.set("generics.gm.survival", this.gmSurvival);

        outConfig.set("chat.cn.black", this.cnBlack);
        outConfig.set("chat.cn.darkblue", this.cnDarkBlue);
        outConfig.set("chat.cn.darkgreen", this.cnDarkGreen);
        outConfig.set("chat.cn.darkaqua", this.cnDarkAqua);
        outConfig.set("chat.cn.darkred", this.cnDarkRed);
        outConfig.set("chat.cn.darkpurple", this.cnDarkPurple);
        outConfig.set("chat.cn.gold", this.cnGold);
        outConfig.set("chat.cn.gray", this.cnGray);
        outConfig.set("chat.cn.darkgray", this.cnDarkGray);
        outConfig.set("chat.cn.blue", this.cnBlue);
        outConfig.set("chat.cn.green", this.cnGreen);
        outConfig.set("chat.cn.aqua", this.cnAqua);
        outConfig.set("chat.cn.red", this.cnRed);
        outConfig.set("chat.cn.lightpurple", this.cnLightPurple);
        outConfig.set("chat.cn.yellow", this.cnYellow);
        outConfig.set("chat.cn.white", this.cnWhite);
        outConfig.set("chat.local", this.chLocal);
        outConfig.set("chat.global", this.chGlobal);
        outConfig.set("chat.staff", this.chStaff);

        try {
            outConfig.save(Constants.CONFIG_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }

    }

}
