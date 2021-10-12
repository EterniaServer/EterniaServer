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

    }

}
