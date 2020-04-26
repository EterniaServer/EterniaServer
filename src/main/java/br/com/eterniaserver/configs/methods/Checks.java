package br.com.eterniaserver.configs.methods;

import br.com.eterniaserver.configs.Vars;

public class Checks {

    public static int getXPForLevel(int lvl) {
        if (lvl > 0 && lvl < 16) {
            return (lvl * lvl) + 6 * lvl;
        } else if (lvl > 15 && lvl < 31) {
            return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
        } else if (lvl >= 31) {
            return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
        }
        return 0;
    }

    public static long getCooldown(String name) {
        if (!Vars.bed_cooldown.containsKey(name)) {
            return 0;
        } else {
            return Vars.bed_cooldown.get(name);
        }
    }

}
