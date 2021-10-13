package br.com.eterniaserver.eterniaserver.modules.experience;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import java.util.UUID;


final class Services {

    static class Experience {

        private final EterniaServer plugin;

        protected Experience(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        public void setDatabaseExp(final UUID uuid, final int amount) {
            final Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
            update.set.set("xp", amount);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
        }

        public int getXPForLevel(int lvl) {
            if (lvl > 0 && lvl < 16) return (lvl * lvl) + 6 * lvl;
            else if (lvl > 15 && lvl < 31) return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
            else if (lvl >= 31) return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
            else return 0;
        }

    }

}
