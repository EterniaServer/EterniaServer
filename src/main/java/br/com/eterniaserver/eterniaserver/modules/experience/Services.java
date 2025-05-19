package br.com.eterniaserver.eterniaserver.modules.experience;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.experience.Entities.ExpBalance;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Experience {

        private final EterniaServer plugin;

        protected Experience(EterniaServer plugin) {
            this.plugin = plugin;
        }

        protected CompletableFuture<ExpBalance> getBalance(final UUID uuid) {
            return CompletableFuture.supplyAsync(() -> {
                ExpBalance expBalance = EterniaLib.getDatabase().get(ExpBalance.class, uuid);

                if (expBalance.getUuid() == null) {
                    expBalance.setUuid(uuid);
                    expBalance.setBalance(0);
                    EterniaLib.getDatabase().insert(ExpBalance.class, expBalance);
                    return expBalance;
                }

                return expBalance;
            });
        }

        protected void updateBalance(ExpBalance expBalance) {
            Runnable updateRunnable = () -> EterniaLib.getDatabase().update(ExpBalance.class, expBalance);
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, updateRunnable);
        }

        protected int getXPForLevel(int lvl) {
            if (lvl >= 0 && lvl < 17) {
                return (lvl * lvl) + 6 * lvl;
            }
            else if (lvl >= 17 && lvl < 32) {
                return (int) ((2.5 * (lvl * lvl)) - (40.5 * lvl) + 360);
            }
            else if (lvl >= 32) {
                return (int) ((4.5 * (lvl * lvl)) - (162.5 * lvl) + 2220);
            }
            else {
                return 0;
            }
        }

    }

}
